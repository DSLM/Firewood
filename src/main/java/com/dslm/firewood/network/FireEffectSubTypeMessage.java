package com.dslm.firewood.network;

import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectSubType;
import com.dslm.firewood.recipe.FireEffectSubTypeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FireEffectSubTypeMessage
{
    public HashMap<String, HashMap<String, FireEffectSubType>> effectsMap;
    
    public FireEffectSubTypeMessage(HashMap<String, HashMap<String, FireEffectSubType>> effectsMap)
    {
        this.effectsMap = effectsMap;
    }
    
    public HashMap<String, HashMap<String, FireEffectSubType>> getEffectsMap()
    {
        return effectsMap;
    }
    
    public void setEffectsMap(HashMap<String, HashMap<String, FireEffectSubType>> effectsMap)
    {
        this.effectsMap = effectsMap;
    }
    
    public static void encode(FireEffectSubTypeMessage message, FriendlyByteBuf buf)
    {
        HashMap<String, HashMap<String, FireEffectSubType>> effectsMap = message.getEffectsMap();
        
        buf.writeInt(effectsMap.size());
        
        for(Map.Entry<String, HashMap<String, FireEffectSubType>> one : effectsMap.entrySet())
        {
            buf.writeUtf(one.getKey());
            buf.writeInt(one.getValue().size());
            for(Map.Entry<String, FireEffectSubType> two : one.getValue().entrySet())
            {
                buf.writeUtf(two.getKey());
                FireEffectSubType.toNetwork(buf, two.getValue());
            }
        }
    }
    
    public static FireEffectSubTypeMessage decode(FriendlyByteBuf buf)
    {
        
        HashMap<String, HashMap<String, FireEffectSubType>> effectsMap = new HashMap<>();
        
        int firstLen = buf.readInt();
        
        for(int i = 0; i < firstLen; i++)
        {
            String mainKey = buf.readUtf();
            int secondLen = buf.readInt();
            HashMap<String, FireEffectSubType> subMap = new HashMap<>();
            for(int j = 0; j < secondLen; j++)
            {
                subMap.put(buf.readUtf(), FireEffectSubType.fromNetwork(buf));
            }
            effectsMap.put(mainKey, subMap);
        }
        
        return new FireEffectSubTypeMessage(effectsMap);
    }
    
    public static void handle(FireEffectSubTypeMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        if(context.getDirection().getReceptionSide().isClient())
        {
            context.enqueueWork(() -> FireEffectSubTypeManager.setEffectsMap(message.getEffectsMap()));
        }
        context.setPacketHandled(true);
    }
}
