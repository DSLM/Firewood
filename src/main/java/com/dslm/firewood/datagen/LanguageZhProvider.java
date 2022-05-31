package com.dslm.firewood.datagen;

import com.dslm.firewood.util.StaticValue;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import static com.dslm.firewood.datagen.LanguageUtil.*;

public class LanguageZhProvider extends LanguageProvider
{
    
    public LanguageZhProvider(DataGenerator gen, String locale)
    {
        super(gen, StaticValue.MOD_ID, locale);
    }
    
    @Override
    protected void addTranslations()
    {
        for(String i : StringKey_zh.keySet())
        {
            add(i, StringKey_zh.get(i));
        }
        for(Item i : ItemKey_zh.keySet())
        {
            add(i, ItemKey_zh.get(i));
        }
        for(Block i : BlockKey_zh.keySet())
        {
            add(i, BlockKey_zh.get(i));
        }
        for(MobEffect i : MobEffectKey_zh.keySet())
        {
            add(i, MobEffectKey_zh.get(i));
        }
    }
}