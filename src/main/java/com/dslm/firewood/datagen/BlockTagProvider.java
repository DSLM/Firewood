package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagProvider extends BlockTagsProvider
{
    public BlockTagProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper)
    {
        super(pGenerator, StaticValue.MOD_ID, existingFileHelper);
    }
    
    @Override
    protected void addTags()
    {
        tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("firewood", "tinder")))
                .add(Register.SPIRITUAL_FIRE_BLOCK.get());
    }
}
