package com.dslm.firewood;

import com.dslm.firewood.block.SpiritualCampfireBlock;
import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.blockEntity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.blockEntity.SpiritualFireBlockEntity;
import com.dslm.firewood.compat.TOPPlugin;
import com.dslm.firewood.container.SpiritualCampfireBlockContainer;
import com.dslm.firewood.event.ForgeBusClientHandler;
import com.dslm.firewood.item.DyingEmberItem;
import com.dslm.firewood.item.TinderItem;
import com.dslm.firewood.recipe.GroundTinderRecipe;
import com.dslm.firewood.recipe.PotionTinderRecipe;
import com.dslm.firewood.recipe.TeleportTinderRecipe;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class Register
{
    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Firewood.MOD_ID);
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Firewood.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Firewood.MOD_ID);
    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Firewood.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Firewood.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, Firewood.MOD_ID);
    
    public static CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab(Firewood.MOD_ID)
    {
        
        @Override
        public ItemStack makeIcon()
        {
            return Register.TINDER_ITEM.get().getDefaultInstance();
        }
    };
    
    public static final DamageSource SPIRITUAL_FIRE_DAMAGE =
            (new DamageSource("spiritualFire")).bypassArmor().bypassMagic().setMagic();
    public static final DamageSource FLESHY_FIRE_DAMAGE =
            (new DamageSource("fleshyFire")).bypassArmor().bypassMagic().setMagic();
    
    public static final RegistryObject<Block> SPIRITUAL_FIRE_BLOCK =
            BLOCKS.register("spiritual_fire_block",
                    () -> new SpiritualFireBlock(
                            BlockBehaviour.Properties.of(Material.FIRE).noCollission().strength(-1f).explosionResistance(3600000f).lightLevel((s) -> 15).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> SPIRITUAL_CAMPFIRE_BLOCK =
            BLOCKS.register("spiritual_campfire_block",
                    () -> new SpiritualCampfireBlock(
                            BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2F).sound(SoundType.WOOD).lightLevel((s) -> s.getValue(BlockStateProperties.LIT) ? 15 : 0).noOcclusion()));
    
    public static final RegistryObject<BlockEntityType<SpiritualCampfireBlockEntity>> SPIRITUAL_CAMPFIRE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("spiritual_campfire_block_entity", () ->
                    BlockEntityType.Builder.of(SpiritualCampfireBlockEntity::new, SPIRITUAL_CAMPFIRE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpiritualFireBlockEntity>> SPIRITUAL_FIRE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("spiritual_fire_block_entity", () ->
                    BlockEntityType.Builder.of(SpiritualFireBlockEntity::new, SPIRITUAL_FIRE_BLOCK.get()).build(null));
    
    public static final RegistryObject<Item> TINDER_ITEM =
            ITEMS.register("tinder_item", () -> new TinderItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> DYING_EMBER_ITEM =
            ITEMS.register("dying_ember_item", () -> new DyingEmberItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> SPIRITUAL_CAMPFIRE_ITEM =
            ITEMS.register("spiritual_campfire_item",
                    () -> new BlockItem(SPIRITUAL_CAMPFIRE_BLOCK.get(), new Item.Properties().tab(CREATIVE_MODE_TAB)));

//        public static final RegistryObject<MobEffect> FIRED_SPIRIT = MOB_EFFECTS.register("fired_spirit",
//            () -> new FiredSpirit(MobEffectCategory.BENEFICIAL, 0xf47025));
//    public static final RegistryObject<MobEffect> FIRED_FLESH = MOB_EFFECTS.register("fired_flesh",
//            () -> new FiredFlesh(MobEffectCategory.BENEFICIAL, 0xf47025));
    
    public static final RegistryObject<RecipeSerializer<PotionTinderRecipe>> POTION_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_potion_tinder", PotionTinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<TeleportTinderRecipe>> TELEPORT_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_teleport_tinder", TeleportTinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<GroundTinderRecipe>> GROUND_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_ground_tinder", GroundTinderRecipe.Serializer::new);
    
    public static final RegistryObject<MenuType<SpiritualCampfireBlockContainer>> SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER =
            CONTAINERS.register("spiritual_campfire_block_container", () ->
                    IForgeMenuType.create((windowId, inv, data) -> new SpiritualCampfireBlockContainer(windowId, data.readBlockPos(), inv, inv.player)));
    
    @SubscribeEvent
    public static void onRegistryInit(RegistryEvent.Register<?> event)
    {
    
    }
    
    @SubscribeEvent
    public void registerBlockColors(ColorHandlerEvent.Block event)
    {
    }
    
    public static void register(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        MOB_EFFECTS.register(bus);
        BLOCK_ENTITIES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        CONTAINERS.register(bus);
        TOPPlugin.register();
        ForgeBusClientHandler.register();
    }
}
