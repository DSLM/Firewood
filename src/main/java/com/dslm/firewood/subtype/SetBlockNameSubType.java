package com.dslm.firewood.subtype;

import com.dslm.firewood.recipe.SubRecipeBase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SetBlockNameSubType extends FireEffectSubType
{
    protected ArrayList<ArrayList<String>> order = new ArrayList<>();
    protected ArrayList<Integer> checkOrder = new ArrayList<>();
    
    protected ArrayList<SetBlockNameFakeRecipe> blockMap = new ArrayList<>();
    
    
    public SetBlockNameSubType()
    {
    
    }
    
    public SetBlockNameSubType(FireEffectSubType fireEffectSubType, ArrayList<ArrayList<String>> order, ArrayList<Integer> checkOrder)
    {
        copyFrom(fireEffectSubType);
        this.order = order;
        this.checkOrder = checkOrder;
        
        
        getAllBlocks();
    }
    
    public SetBlockNameSubType(FireEffectSubType copy, JsonElement orderJson, JsonElement checkJson)
    {
        super(copy);
        if(orderJson instanceof JsonArray jsonArray)
        {
            order.add(new ArrayList<>());
            jsonArray.forEach(jsonElement1 -> {
                if(jsonElement1 instanceof JsonArray jsonArray1)
                {
                    AtomicInteger i = new AtomicInteger();
                    jsonArray1.forEach(jsonElement2 -> {
                        if(order.size() < i.get() + 1)
                        {
                            order.add(new ArrayList<>());
                        }
                        while(order.get(i.get()).size() < order.get(0).size() - 1)
                        {
                            order.get(i.get()).add("");
                        }
                        order.get(i.get()).add(jsonElement2.getAsString());
                        i.getAndIncrement();
                    });
                }
                else
                {
                    order.get(0).add(jsonElement1.getAsString());
                }
            });
        }
        
        if(checkJson instanceof JsonArray jsonArray)
        {
            jsonArray.forEach(jsonElement -> {
                checkOrder.add(jsonElement.getAsInt());
            });
        }
        
        
        getAllBlocks();
    }
    
    public SetBlockNameSubType(FriendlyByteBuf buf)
    {
        this();
        copyFrom((SetBlockNameSubType) fromNetwork(buf));
        
        
        getAllBlocks();
        
    }
    
    public static class Util<T>
    {
        public T getNext(ArrayList<T> nowOrder, int now)
        {
            return nowOrder.get((now + 1) % nowOrder.size());
        }
    }
    
    public void getAllBlocks()
    {
        if(order.size() == 0)
            return;
        
        blockMap = new ArrayList<>();
        Util<String> stringUtil = new Util<>();
        
        for(int i = 0; i < order.get(0).size(); i++)
        {
            int finalI = i;
            blockMap.add(new SetBlockNameFakeRecipe(id, subType, order.stream().map(strings -> strings.get(finalI)).toList()));
        }
        
        ForgeRegistries.BLOCKS.getKeys().forEach(resourceLocation -> {
            lineOrder:
            for(int i = 0; i < order.get(0).size(); i++)
            {
                String replacedString = resourceLocation.toString();
                for(int j = 0; j < order.size(); j++)
                {
                    String nowCheck = order.get(j).get(i);
                    String nowStr = stringUtil.getNext(order.get(j), i);
                    if(nowCheck.equals("") || nowStr.equals(""))
                    {
                        continue;
                    }
                    if(!replacedString.contains(nowCheck))
                    {
                        continue lineOrder;
                    }
                    replacedString = replacedString.replace(nowCheck, nowStr);
                }
                
                ResourceLocation newID = new ResourceLocation(replacedString);
                
                if(resourceLocation.toString().equals(replacedString) || !ForgeRegistries.BLOCKS.containsKey(newID))
                {
                    continue;
                }
                
                blockMap.get(i).put(resourceLocation.toString(), ForgeRegistries.BLOCKS.getValue(newID));
            }
            
            Util<SetBlockNameFakeRecipe> recipeUtil = new Util<>();
            for(int i = 0; i < blockMap.size(); i++)
            {
                blockMap.get(i).setNext(recipeUtil.getNext(blockMap, i));
            }
        });
    }
    
    public void copyFrom(SetBlockNameSubType copy)
    {
        super.copyFrom(copy);
        this.order = copy.order;
        this.checkOrder = copy.checkOrder;
    }
    
    public ArrayList<ArrayList<String>> getOrder()
    {
        return order;
    }
    
    public void setOrder(ArrayList<ArrayList<String>> order)
    {
        this.order = order;
    }
    
    public ArrayList<Integer> getCheckOrder()
    {
        return checkOrder;
    }
    
    public void setCheckOrder(ArrayList<Integer> checkOrder)
    {
        this.checkOrder = checkOrder;
    }
    
    public ArrayList<SetBlockNameFakeRecipe> getBlockMap()
    {
        return blockMap;
    }
    
    public void setBlockMap(ArrayList<SetBlockNameFakeRecipe> blockMap)
    {
        this.blockMap = blockMap;
    }
    
    @Override
    public FireEffectSubTypeBase fromNetwork(FriendlyByteBuf buf)
    {
        FireEffectSubType fireEffectSubType = (FireEffectSubType) super.fromNetwork(buf);
        
        
        ArrayList<ArrayList<String>> tempOrder = new ArrayList<>();
        int size = buf.readInt();
        
        for(int i = 0; i < size; i++)
        {
            ArrayList<String> temp1 = new ArrayList<>();
            int size1 = buf.readInt();
            
            for(int j = 0; j < size1; j++)
            {
                temp1.add(buf.readUtf());
            }
            
            tempOrder.add(temp1);
        }
        
        
        ArrayList<Integer> tempCheckOrder = new ArrayList<>();
        int size2 = buf.readInt();
        
        for(int i = 0; i < size2; i++)
        {
            tempCheckOrder.add(buf.readInt());
        }
        
        return new SetBlockNameSubType(fireEffectSubType, tempOrder, tempCheckOrder);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, FireEffectSubTypeBase recipe)
    {
        super.toNetwork(buf, recipe);
    
    
        buf.writeInt(order.size());
    
        for(ArrayList<String> line : order)
        {
            buf.writeInt(line.size());
        
            for(String string : line)
            {
                buf.writeUtf(string);
            }
        }
    
    
        buf.writeInt(checkOrder.size());
    
        for(Integer line : checkOrder)
        {
            buf.writeInt(line);
        }
    }
    
    public static class SetBlockNameFakeRecipe extends HashMap<String, Block> implements SubRecipeBase
    {
        protected String type;
        protected String subType;
        protected List<String> orderKey;
        protected SetBlockNameFakeRecipe next = this;
        
        public SetBlockNameFakeRecipe(String type, String subType, List<String> orderKey)
        {
            this.type = type;
            this.subType = subType;
            this.orderKey = orderKey;
        }
        
        public String getType()
        {
            return type;
        }
        
        public void setType(String type)
        {
            this.type = type;
        }
        
        public String getSubType()
        {
            return subType;
        }
        
        public void setSubType(String subType)
        {
            this.subType = subType;
        }
        
        public List<String> getOrderKey()
        {
            return orderKey;
        }
        
        public void setOrderKey(List<String> orderKey)
        {
            this.orderKey = orderKey;
        }
        
        public SetBlockNameFakeRecipe getNext()
        {
            return next;
        }
        
        public void setNext(SetBlockNameFakeRecipe next)
        {
            this.next = next;
        }
        
        public List<ItemStack> getJEIInputs()
        {
            return this.keySet().stream()
                    .map(s -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s)))
                    .map(Block::asItem)
                    .map(ItemStack::new)
                    .toList();
        }
        
        public List<ItemStack> getJEIOutputs()
        {
            return this.values().stream()
                    .map(Block::asItem)
                    .map(ItemStack::new)
                    .toList();
        }
    }
}
