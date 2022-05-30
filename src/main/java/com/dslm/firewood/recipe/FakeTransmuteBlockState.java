package com.dslm.firewood.recipe;

import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class FakeTransmuteBlockState
{
    private final Block block;
    private final CompoundTag properties;
    private final TagKey<Block> tagKey;
    private final Boolean isTag;
    
    
    public FakeTransmuteBlockState(Block block, CompoundTag properties)
    {
        this.block = block;
        this.properties = properties;
        this.tagKey = null;
        isTag = false;
    }
    
    public FakeTransmuteBlockState(TagKey<Block> tagKey)
    {
        this.block = null;
        this.properties = null;
        this.tagKey = tagKey;
        isTag = true;
    }
    
    public Block getBlock()
    {
        return block;
    }
    
    public CompoundTag getProperties()
    {
        return properties;
    }
    
    public TagKey<Block> getTagKey()
    {
        return tagKey;
    }
    
    public Boolean isTag()
    {
        return isTag;
    }
    
    public Boolean isBlockState()
    {
        return !isTag;
    }
    
    public static CompoundTag writeBlockState(FakeTransmuteBlockState state)
    {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putString("Name", ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString());
        if(state.getProperties().size() > 0)
        {
            compoundtag.put("Properties", state.getProperties());
        }
        
        return compoundtag;
    }
    
    public static FakeTransmuteBlockState readFakeBlockState(CompoundTag tag)
    {
        if(tag.contains("Tag"))
        {
            
            if(tag.contains("Name"))
            {
                throw new JsonParseException("An ingredient entry is either a tag or a block state, not both");
            }
            ResourceLocation resourcelocation = new ResourceLocation(tag.getString("Tag"));
            TagKey<Block> tagKey = TagKey.create(Registry.BLOCK_REGISTRY, resourcelocation);
            return new FakeTransmuteBlockState(tagKey);
        }
        
        if(!tag.contains("Name", 8))
        {
            return FakeTransmuteBlockState.AIR;
        }
        
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("Name")));
        CompoundTag properties = new CompoundTag();
        
        if(tag.contains("Properties", 10))
        {
            properties = tag.getCompound("Properties");
        }
        return new FakeTransmuteBlockState(block, properties);
    }
    
    public Boolean compareBlockState(BlockState realState)
    {
        if(isTag())
        {
            return realState.is(getTagKey());
        }
        
        if(!realState.is(getBlock()))
        {
            return false;
        }
        BlockState fakeState = NbtUtils.readBlockState(FakeTransmuteBlockState.writeBlockState(this));
        
        for(Property<?> property : realState.getProperties())
        {
            if(getProperties().contains(property.getName()) && !fakeState.getValue(property).equals(realState.getValue(property)))
            {
                return false;
            }
            
        }
        return true;
    }
    
    public static final FakeTransmuteBlockState AIR = new FakeTransmuteBlockState(Blocks.AIR, new CompoundTag());
}
