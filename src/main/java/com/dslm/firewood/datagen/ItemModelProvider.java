package com.dslm.firewood.datagen;

import com.dslm.firewood.Register;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider
{
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, StaticValue.MOD_ID, existingFileHelper);
    }
    
    @Override
    protected void registerModels()
    {
        singleTexture(Register.SPIRITUAL_CAMPFIRE_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/spiritual_campfire_item"));
    
        singleTexture(Register.DYING_EMBER_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/dying_ember_item"));
    
        singleTexture(Register.TINDER_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("item/tinder_item_base"));
        singleTexture(Register.TINDER_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer1", modLoc("item/tinder_item_fire"));
    
        singleTexture(Register.DEBUG_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/generated"),
                "layer0", modLoc("firewood"));
    
        withExistingParent(Register.REMNANT_SOUL_EGG_ITEM.get().getRegistryName().getPath(),
                mcLoc("item/template_spawn_egg"));
    }
}