package com.morphanone.clientizen;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.Denizen2Implementation;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;
import com.morphanone.clientizen.commands.gui.OverlayImageCommand;
import com.morphanone.clientizen.commands.gui.OverlayTextCommand;
import com.morphanone.clientizen.forgecommands.ClientExCommand;
import com.morphanone.clientizen.gui.OverlayGuiHandler;
import com.morphanone.clientizen.network.ClientizenPluginChannel;
import com.morphanone.clientizen.scripts.ServerScriptManager;
import com.morphanone.clientizen.util.Schedulable;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod(
        modid = Clientizen.MOD_ID,
        name = Clientizen.MOD_NAME,
        version = Clientizen.MOD_VERSION,
        clientSideOnly = true
)
public class Clientizen extends Denizen2Implementation {

    public static final String MOD_ID = "clientizen";

    public static final String MOD_NAME = "Clientizen";

    public static final String MOD_VERSION = "%MOD_VERSION%";

    public static final String PLUGIN_CHANNEL = "Clientizen";

    @Instance(MOD_ID)
    public static Clientizen instance;

    public Logger logger;

    public File configFolder;

    public File configFile;

    public YAMLConfiguration config;

    public File scriptsFolder;

    public File addonsFolder;

    public File imagesFolder;

    public ClientizenPluginChannel channel;

    public OverlayGuiHandler overlayGuiHandler;

    private final List<Schedulable> scheduledTasks = new ArrayList<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Initial setup
        logger = event.getModLog();
        configFolder = new File(event.getModConfigurationDirectory(), MOD_ID);
        configFolder.mkdirs();
        configFile = new File(configFolder, "config.yml");
        scriptsFolder = new File(configFolder, "scripts");
        scriptsFolder.mkdirs();
        addonsFolder = new File(configFolder, "addons");
        addonsFolder.mkdirs();
        imagesFolder = new File(configFolder, "images");
        imagesFolder.mkdirs();
        // Load config (save default if it doesn't exist)
        saveDefaultConfig();
        loadConfig();
        // Network
        channel = new ClientizenPluginChannel();
        // GUI
        overlayGuiHandler = new OverlayGuiHandler();
        MinecraftForge.EVENT_BUS.register(overlayGuiHandler);
        // Set Denizen2 implementation
        Denizen2Core.init(this);
        // Commands : Overlay GUI
        Denizen2Core.register(new OverlayImageCommand());
        Denizen2Core.register(new OverlayTextCommand());
        // Load Denizen2
        Denizen2Core.start();
        // Client tick
        MinecraftForge.EVENT_BUS.register(this);
        // Register '/cex' command
        ClientCommandHandler.instance.registerCommand(new ClientExCommand());
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            double delta = event.renderTickTime / 20;
            Denizen2Core.tick(delta);
            Iterator<Schedulable> iter = scheduledTasks.iterator();
            while (iter.hasNext()) {
                Schedulable schedulable = iter.next();
                if (schedulable.run(delta)) {
                    iter.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        Clientizen.instance.outputInfo("Connected to server!");
        schedule(new Schedulable() {
            @Override
            public void run() {
                Clientizen.instance.outputInfo("Sending READY!");
                channel.sendReady();
            }
        }, 0.1);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Clientizen.instance.outputInfo("Disconnected from server!");
        ServerScriptManager.clearScripts();
    }

    public void schedule(Schedulable schedulable, double delayInSeconds) {
        schedulable.remainingDelay = delayInSeconds;
        scheduledTasks.add(schedulable);
    }

    private void saveDefaultConfig() {
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("default_config.yml");
                 PrintWriter writer = new PrintWriter(configFile)) {
                writer.write(CoreUtilities.streamToString(is));
            }
            catch (IOException e) {
                outputException(e);
            }
        }
    }

    private void loadConfig() {
        try {
            config = YAMLConfiguration.load(CoreUtilities.streamToString(new FileInputStream(configFile)));
        }
        catch (IOException e) {
            outputException(e);
        }
    }

    @Override
    public void reload() {
        Clientizen.instance.outputInfo("Reloading!");
        ServerScriptManager.injectScripts();
    }

    @Override
    public void outputException(Exception e) {
        logger.error("+> Internal exception! Trace follows: ");
        if (e instanceof ErrorInducedException) {
            outputError(e.getMessage());
        }
        else {
            trace(e);
        }
    }

    public void trace(Throwable e) {
        if (e == null) {
            return;
        }
        logger.trace("   " + e.getClass().getCanonicalName() + ": " + e.getMessage());
        for (StackTraceElement ste : e.getStackTrace()) {
            logger.trace("     " + ste.toString());
        }
        if (e.getCause() != e) {
            trace(e.getCause());
        }
    }

    @Override
    public void outputGood(String s) {
        logger.debug("+> [Good] " + s);
    }

    @Override
    public void outputInfo(String s) {
        logger.info("+> [Info] " + s);
    }

    @Override
    public void outputInvalid(CommandQueue queue, CommandEntry entry) {
        queue.handleError(entry, "Invalid/unknown command: " + entry.originalLine + "... -> " + entry.arguments);
    }

    @Override
    public void outputError(String s) {
        logger.error("+> [Error] " + s);
    }

    @Override
    public boolean generalDebug() {
        return Settings.debugGeneral();
    }

    @Override
    public File getScriptsFolder() {
        return scriptsFolder;
    }

    @Override
    public File getAddonsFolder() {
        return addonsFolder;
    }

    @Override
    public String getImplementationName() {
        return null;
    }

    @Override
    public String getImplementationVersion() {
        return MOD_NAME;
    }

    @Override
    public boolean enforceLocale() {
        return Settings.enforceLocale();
    }
}
