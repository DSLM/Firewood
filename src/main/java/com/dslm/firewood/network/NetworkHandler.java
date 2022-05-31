package com.dslm.firewood.network;

import com.dslm.firewood.util.StaticValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler
{
    private static final String PROTOCOL_VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(StaticValue.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    public static void init()
    {
        int chId = 0;
        CHANNEL.registerMessage(0, FireEffectSubTypeMessage.class, FireEffectSubTypeMessage::encode, FireEffectSubTypeMessage::decode, FireEffectSubTypeMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
