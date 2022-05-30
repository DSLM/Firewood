package com.dslm.firewood.event;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.capProvider.PlayerSpiritualDamageProvider;
import com.dslm.firewood.recipe.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Firewood.MOD_ID)
public class ModBusHandler
{
    
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(PlayerSpiritualDamageProvider.PlayerSpiritualDamage.class);
    }
    
    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event)
    {
        Registry.register(Registry.RECIPE_TYPE, TinderRecipe.Type.ID, TinderRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, TeleportTinderRecipe.Type.ID, TeleportTinderRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, PotionTinderRecipe.Type.ID, PotionTinderRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, GroundTinderRecipe.Type.ID, GroundTinderRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, TransmuteBlockToBlockRecipe.Type.ID, TransmuteBlockToBlockRecipe.Type.INSTANCE);
    }
}
