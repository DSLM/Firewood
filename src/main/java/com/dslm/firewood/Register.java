package com.dslm.firewood;

import com.dslm.firewood.block.SpiritualCampfireBlock;
import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.compat.top.TOPPlugin;
import com.dslm.firewood.event.ForgeBusClientHandler;
import com.dslm.firewood.item.DebugItem;
import com.dslm.firewood.item.DyingEmberItem;
import com.dslm.firewood.item.TinderItem;
import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import com.dslm.firewood.mobEffect.FiredFlesh;
import com.dslm.firewood.mobEffect.FiredSpirit;
import com.dslm.firewood.recipe.GroundTinderRecipe;
import com.dslm.firewood.recipe.PotionTinderRecipe;
import com.dslm.firewood.recipe.TeleportTinderRecipe;
import com.dslm.firewood.recipe.TinderRecipe;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
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
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class Register
{// TODO: 2022/5/23 生命不够是否增加营火？方块-方块，方块-物品，方块-实体，实体-物品
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
    
    public static final RegistryObject<Item> SPIRITUAL_CAMPFIRE_ITEM =
            ITEMS.register("spiritual_campfire_item",
                    () -> new BlockItem(SPIRITUAL_CAMPFIRE_BLOCK.get(), new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> DYING_EMBER_ITEM =
            ITEMS.register("dying_ember_item", () -> new DyingEmberItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> TINDER_ITEM =
            ITEMS.register("tinder_item", () -> new TinderItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> DEBUG_ITEM =
            ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    
    public static final RegistryObject<MobEffect> FIRED_SPIRIT = MOB_EFFECTS.register("fired_spirit",
            () -> new FiredSpirit(MobEffectCategory.BENEFICIAL, 0xf47025));
    public static final RegistryObject<MobEffect> FIRED_FLESH = MOB_EFFECTS.register("fired_flesh",
            () -> new FiredFlesh(MobEffectCategory.BENEFICIAL, 0xf47025));
    
    public static final RegistryObject<RecipeSerializer<TinderRecipe>> TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register(TinderRecipe.Type.ID, TinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<TinderRecipe>> TELEPORT_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register(TeleportTinderRecipe.Type.ID, TeleportTinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<TinderRecipe>> POTION_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_potion_tinder", PotionTinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<TinderRecipe>> GROUND_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("crafting_ground_tinder", GroundTinderRecipe.Serializer::new);
    
    public static final RegistryObject<MenuType<SpiritualCampfireBlockMenu>> SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER =
            CONTAINERS.register("spiritual_campfire_block_container", () ->
                    IForgeMenuType.create((windowId, inv, data) -> new SpiritualCampfireBlockMenu(windowId, data.readBlockPos(), inv, inv.player)));
    
    
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
