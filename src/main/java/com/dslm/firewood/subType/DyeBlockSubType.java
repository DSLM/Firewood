package com.dslm.firewood.subType;

import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;

public class DyeBlockSubType extends FireEffectSubType
{
    public ArrayList<String> colorOrder = new ArrayList<>();
    
    public DyeBlockSubType()
    {
    
    }
    
    public DyeBlockSubType(FireEffectSubType fireEffectSubType, ArrayList<String> colorOrder)
    {
        copyFrom(fireEffectSubType);
        this.colorOrder = colorOrder;
    }
    
    public DyeBlockSubType(FireEffectSubType copy, JsonElement jsonElement)
    {
        super(copy);
        if(jsonElement instanceof JsonArray jsonArray)
        {
            jsonArray.forEach(jsonElement1 -> {
                String newColor = jsonElement1.getAsString();
                if(StaticValue.COLORS_ARRAY.contains(newColor))
                {
                    colorOrder.add(newColor);
                }
            });
        }
    }
    
    public DyeBlockSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((DyeBlockSubType) fromNetwork(buf));
    }
    
    public void copyFrom(DyeBlockSubType copy)
    {
        super.copyFrom(copy);
        this.colorOrder = copy.colorOrder;
    }
    
    public ArrayList<String> getColorOrder()
    {
        return colorOrder;
    }
    
    public void setColorOrder(ArrayList<String> colorOrder)
    {
        this.colorOrder = colorOrder;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.fromNetwork(buf);
        
        ArrayList<String> temp = new ArrayList<>();
        int size = buf.readInt();
        
        for(int i = 0; i < size; i++)
        {
            temp.add(buf.readUtf());
        }
        
        return new DyeBlockSubType(fireEffectSubType, temp);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        super.toNetwork(buf, recipe);
        
        buf.writeInt(colorOrder.size());
        
        for(String color : colorOrder)
        {
            buf.writeUtf(color);
        }
    }
}
