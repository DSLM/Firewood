package com.dslm.firewood.subType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;

public class TeleportSubType extends FireEffectSubType
{
    protected boolean dimFromBlacklist = true;
    protected ArrayList<String> dimFromList = new ArrayList<>();
    protected boolean dimToBlacklist = true;
    protected ArrayList<String> dimToList = new ArrayList<>();
    
    public TeleportSubType()
    {
    
    }
    
    public TeleportSubType(FireEffectSubType fireEffectSubType,
                           boolean dimFromBlacklist,
                           ArrayList<String> dimFromList,
                           boolean dimToBlacklist,
                           ArrayList<String> dimToList)
    {
        super(fireEffectSubType);
        this.dimFromBlacklist = dimFromBlacklist;
        this.dimFromList = dimFromList;
        this.dimToBlacklist = dimToBlacklist;
        this.dimToList = dimToList;
    }
    
    public TeleportSubType(FireEffectSubType copy,
                           JsonElement dimFromBlacklistJsonElement,
                           JsonElement dimFromListJsonElement,
                           JsonElement dimToBlacklistJsonElement,
                           JsonElement dimToListJsonElement)
    {
        super(copy);
        if(dimFromBlacklistJsonElement != null && !dimFromBlacklistJsonElement.isJsonNull())
        {
            dimFromBlacklist = dimFromBlacklistJsonElement.getAsBoolean();
        }
        if(dimFromListJsonElement instanceof JsonArray dimFromListJsonArray)
        {
            dimFromListJsonArray.forEach(jsonElement -> dimFromList.add(jsonElement.getAsString()));
        }
        if(dimToBlacklistJsonElement != null && !dimToBlacklistJsonElement.isJsonNull())
        {
            dimToBlacklist = dimToBlacklistJsonElement.getAsBoolean();
        }
        if(dimToListJsonElement instanceof JsonArray dimToListJsonArray)
        {
            dimToListJsonArray.forEach(jsonElement -> dimToList.add(jsonElement.getAsString()));
        }
    }
    
    public TeleportSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((TeleportSubType) fromNetwork(buf));
    }
    
    public void copyFrom(TeleportSubType copy)
    {
        super.copyFrom(copy);
        this.dimFromBlacklist = copy.dimFromBlacklist;
        this.dimFromList = copy.dimFromList;
        this.dimToBlacklist = copy.dimToBlacklist;
        this.dimToList = copy.dimToList;
    }
    
    public boolean allowFromDim(String dim)
    {
        return dimFromBlacklist != dimFromList.contains(dim);
    }
    
    public boolean allowToDim(String dim)
    {
        return dimToBlacklist != dimToList.contains(dim);
    }
    
    public boolean isDimFromBlacklist()
    {
        return dimFromBlacklist;
    }
    
    public void setDimFromBlacklist(boolean dimFromBlacklist)
    {
        this.dimFromBlacklist = dimFromBlacklist;
    }
    
    public ArrayList<String> getDimFromList()
    {
        return dimFromList;
    }
    
    public void setDimFromList(ArrayList<String> dimFromList)
    {
        this.dimFromList = dimFromList;
    }
    
    public boolean isDimToBlacklist()
    {
        return dimToBlacklist;
    }
    
    public void setDimToBlacklist(boolean dimToBlacklist)
    {
        this.dimToBlacklist = dimToBlacklist;
    }
    
    public ArrayList<String> getDimToList()
    {
        return dimToList;
    }
    
    public void setDimToList(ArrayList<String> dimToList)
    {
        this.dimToList = dimToList;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.fromNetwork(buf);
        
        boolean dimFromBlacklist1 = buf.readBoolean();
        ArrayList<String> dimFromList1 = new ArrayList<>();
        int size1 = buf.readInt();
        for(int j = 0; j < size1; j++)
        {
            dimFromList1.add(buf.readUtf());
        }
        
        boolean dimToBlacklist1 = buf.readBoolean();
        ArrayList<String> dimToList1 = new ArrayList<>();
        int size2 = buf.readInt();
        for(int j = 0; j < size2; j++)
        {
            dimToList1.add(buf.readUtf());
        }
        
        return new TeleportSubType(fireEffectSubType, dimFromBlacklist1, dimFromList1, dimToBlacklist1, dimToList1);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        super.toNetwork(buf, recipe);
        
        buf.writeBoolean(dimFromBlacklist);
        
        buf.writeInt(dimFromList.size());
        
        for(String string : dimFromList)
        {
            buf.writeUtf(string);
        }
        
        buf.writeBoolean(dimToBlacklist);
        
        buf.writeInt(dimToList.size());
        
        for(String string : dimToList)
        {
            buf.writeUtf(string);
        }
    }
}
