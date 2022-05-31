package com.dslm.firewood.datagen;

import com.dslm.firewood.util.StaticValue;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.dslm.firewood.datagen.LanguageUtil.*;


public class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider
{
    
    public LanguageProvider(DataGenerator gen, String locale)
    {
        super(gen, StaticValue.MOD_ID, locale);
    }
    
    @Override
    protected void addTranslations()
    {
        for(String i : StringKey_en.keySet())
        {
            add(i, StringKey_en.get(i));
        }
        for(Item i : ItemKey_en.keySet())
        {
            add(i, ItemKey_en.get(i));
        }
        for(Block i : BlockKey_en.keySet())
        {
            add(i, BlockKey_en.get(i));
        }
        for(MobEffect i : MobEffectKey_en.keySet())
        {
            add(i, MobEffectKey_en.get(i));
        }
    }
}