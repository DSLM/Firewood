package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagProvider extends ItemTagsProvider
{
    public ItemTagProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, ExistingFileHelper helper)
    {
        super(generator, blockTagsProvider, StaticValue.MOD_ID, helper);
    }
    
    @Override
    protected void addTags()
    {
        tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("curios", "belt")))
                .add(Register.LANTERN_ITEM.get());
        tag(TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("curios", "charm")))
                .add(Register.LANTERN_ITEM.get());
    }
}
