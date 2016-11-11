package com.morphanone.clientizen.network;

import com.morphanone.clientizen.Clientizen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public abstract class AbstractPluginChannel {

    private final String channelName;
    private final FMLEventChannel channel;

    public AbstractPluginChannel(String channelName) {
        this.channelName = channelName;
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(channelName);
        channel.register(this);
    }

    @SubscribeEvent
    public void onCustomPacket(ClientCustomPacketEvent event) {
        Clientizen.instance.outputInfo("Custom packet: " + event.getPacket().channel());
        if (event.getPacket().channel().equals(channelName)) {
            receivePacket(new DataDeserializer(event.getPacket().payload()));
        }
    }

    public void sendPacket(DataSerializer dataSerializer) {
        channel.sendToServer(new FMLProxyPacket(dataSerializer.toPacketBuffer(), channelName));
    }

    public abstract void receivePacket(DataDeserializer deserializer);
}
