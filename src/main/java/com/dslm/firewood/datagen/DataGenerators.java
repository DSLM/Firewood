package com.dslm.firewood.datagen;

import com.dslm.firewood.Firewood;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Firewood.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        if(event.includeServer())
        {
            generator.addProvider(new RecipeProvider(generator));
            //generator.addProvider(new TutLootTables(generator));
            //TutBlockTags blockTags = new TutBlockTags(generator, event.getExistingFileHelper());
            //generator.addProvider(blockTags);
            //generator.addProvider(new TutItemTags(generator, blockTags, event.getExistingFileHelper()));
        }
        if(event.includeClient())
        {
            //generator.addProvider(new BlockModelProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModelProvider(generator, event.getExistingFileHelper()));
            //generator.addProvider(new BlockModelProvider(generator, event.getExistingFileHelper()));
            LanguageUtil.buildLanguage(generator);
        }
    }
}