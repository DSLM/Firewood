package com.dslm.firewood.network;

import com.dslm.firewood.subType.FireEffectSubTypeBase;
import com.dslm.firewood.subType.FireEffectSubTypeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.dslm.firewood.subType.FireEffectSubTypeManager.DEFAULT_TYPE_BUILDER;
import static com.dslm.firewood.subType.FireEffectSubTypeManager.typeBuilders;

public class FireEffectSubTypeMessage
{
    public HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap;
    
    public FireEffectSubTypeMessage(HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap)
    {
        this.effectsMap = effectsMap;
    }
    
    public HashMap<String, HashMap<String, FireEffectSubTypeBase>> getEffectsMap()
    {
        return effectsMap;
    }
    
    public void setEffectsMap(HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap)
    {
        this.effectsMap = effectsMap;
    }
    
    public static void encode(FireEffectSubTypeMessage message, FriendlyByteBuf buf)
    {
        HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap = message.getEffectsMap();
        
        buf.writeInt(effectsMap.size());
    
        for(Map.Entry<String, HashMap<String, FireEffectSubTypeBase>> one : effectsMap.entrySet())
        {
            buf.writeUtf(one.getKey());
            buf.writeInt(one.getValue().size());
            for(Map.Entry<String, FireEffectSubTypeBase> two : one.getValue().entrySet())
            {
                buf.writeUtf(two.getKey());
                two.getValue().toNetwork(buf, two.getValue());
            }
        }
    }
    
    public static FireEffectSubTypeMessage decode(FriendlyByteBuf buf)
    {
    
        HashMap<String, HashMap<String, FireEffectSubTypeBase>> effectsMap = new HashMap<>();
        
        int firstLen = buf.readInt();
        
        for(int i = 0; i < firstLen; i++)
        {
            String mainKey = buf.readUtf();
            int secondLen = buf.readInt();
            HashMap<String, FireEffectSubTypeBase> subMap = new HashMap<>();
            for(int j = 0; j < secondLen; j++)
            {
                subMap.put(buf.readUtf(), typeBuilders.getOrDefault(mainKey, DEFAULT_TYPE_BUILDER).getNewData(buf));
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
