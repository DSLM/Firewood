package com.dslm.firewood.datagen;

import com.dslm.firewood.util.StaticValue;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagProvider extends BlockTagsProvider
{
    public BlockTagProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper)
    {
        super(pGenerator, StaticValue.MOD_ID, existingFileHelper);
    }
}
