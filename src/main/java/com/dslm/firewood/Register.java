package com.dslm.firewood;

import com.dslm.firewood.block.LanternBlock;
import com.dslm.firewood.block.SpiritualCampfireBlock;
import com.dslm.firewood.block.SpiritualFireBlock;
import com.dslm.firewood.block.entity.LanternBlockEntity;
import com.dslm.firewood.block.entity.SpiritualCampfireBlockEntity;
import com.dslm.firewood.block.entity.SpiritualFireBlockEntity;
import com.dslm.firewood.compat.top.TOPPlugin;
import com.dslm.firewood.entity.RemnantSoulEntity;
import com.dslm.firewood.item.*;
import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import com.dslm.firewood.mobEffect.FiredFlesh;
import com.dslm.firewood.mobEffect.FiredSpirit;
import com.dslm.firewood.network.NetworkHandler;
import com.dslm.firewood.recipe.*;
import com.dslm.firewood.recipe.serializer.BlockToBlockRecipeSerializer;
import com.dslm.firewood.recipe.serializer.PotionTinderRecipeSerializer;
import com.dslm.firewood.recipe.serializer.TeleportTinderRecipeSerializer;
import com.dslm.firewood.recipe.serializer.TinderRecipeSerializer;
import com.dslm.firewood.recipe.type.TinderRecipeType;
import com.dslm.firewood.recipe.type.TransmuteBlockRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dslm.firewood.util.StaticValue.MOD_ID;


public class Register
{// TODO: 2022/5/23 方块-物品，方块-实体，实体-物品，火焰材质重叠要改state，所有主要分类（无法强制实现？），多形状范围，允许自定义药水效果，重构药水效果，物品提灯材质记得改，推拉实体，营火界面合成+JEI，营火详情栏，右键灯笼
    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPES =
            DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);
    
    public static CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab(MOD_ID)
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
    
    
    public static final RegistryObject<EntityType<RemnantSoulEntity>> REMNANT_SOUL_ENTITY =
            ENTITIES.register("remnant_soul_entity",
                    () -> EntityType.Builder.of(RemnantSoulEntity::new, MobCategory.AMBIENT)
                            .sized(0.1f, 0.1f)
                            .clientTrackingRange(8)
                            .setShouldReceiveVelocityUpdates(false)
                            .build("remnant_soul_entity"));
    
    
    public static final RegistryObject<Block> SPIRITUAL_FIRE_BLOCK =
            BLOCKS.register("spiritual_fire_block",
                    () -> new SpiritualFireBlock(
                            BlockBehaviour.Properties.of(Material.FIRE).noCollission().strength(-1f).explosionResistance(3600000f).lightLevel((s) -> 15).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> SPIRITUAL_CAMPFIRE_BLOCK =
            BLOCKS.register("spiritual_campfire_block",
                    () -> new SpiritualCampfireBlock(
                            BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2F).sound(SoundType.WOOD).lightLevel((s) -> s.getValue(BlockStateProperties.LIT) ? 15 : 0).noOcclusion()));
    public static final RegistryObject<Block> LANTERN_BLOCK =
            BLOCKS.register("lantern_block",
                    () -> new LanternBlock(
                            BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel((s) -> s.getValue(BlockStateProperties.LIT) ? 15 : 0)));
    
    
    public static final RegistryObject<BlockEntityType<SpiritualFireBlockEntity>> SPIRITUAL_FIRE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("spiritual_fire_block_entity", () ->
                    BlockEntityType.Builder.of(SpiritualFireBlockEntity::new, SPIRITUAL_FIRE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpiritualCampfireBlockEntity>> SPIRITUAL_CAMPFIRE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("spiritual_campfire_block_entity", () ->
                    BlockEntityType.Builder.of(SpiritualCampfireBlockEntity::new, SPIRITUAL_CAMPFIRE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<LanternBlockEntity>> LANTERN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("lantern_block_entity", () ->
                    BlockEntityType.Builder.of(LanternBlockEntity::new, LANTERN_BLOCK.get()).build(null));
    
    
    public static final RegistryObject<Item> SPIRITUAL_CAMPFIRE_ITEM =
            ITEMS.register("spiritual_campfire_item",
                    () -> new BlockItem(SPIRITUAL_CAMPFIRE_BLOCK.get(), new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> DYING_EMBER_ITEM =
            ITEMS.register("dying_ember_item", () -> new DyingEmberItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> TINDER_ITEM =
            ITEMS.register("tinder_item", () -> new TinderItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> LANTERN_ITEM =
            ITEMS.register("lantern_item",
                    () -> new LanternItem(LANTERN_BLOCK.get(), new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> DEBUG_ITEM =
            ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> REMNANT_SOUL_ITEM =
            ITEMS.register("remnant_soul_item", () -> new RemnantSoulItem(new Item.Properties().tab(CREATIVE_MODE_TAB)));
    public static final RegistryObject<Item> REMNANT_SOUL_EGG_ITEM =
            ITEMS.register("remnant_soul_egg_item",
                    () -> new ForgeSpawnEggItem(REMNANT_SOUL_ENTITY, 0x333333, 0x888888,
                            new Item.Properties().tab(CREATIVE_MODE_TAB)));
    
    
    public static final RegistryObject<MobEffect> FIRED_SPIRIT = MOB_EFFECTS.register("fired_spirit",
            () -> new FiredSpirit(MobEffectCategory.BENEFICIAL, 0xf47025));
    public static final RegistryObject<MobEffect> FIRED_FLESH = MOB_EFFECTS.register("fired_flesh",
            () -> new FiredFlesh(MobEffectCategory.BENEFICIAL, 0xf47025));
    
    
    public static final RegistryObject<RecipeType<TinderRecipe>> TINDER_RECIPE_TYPE =
            RECIPES.register("tinder_recipe", TinderRecipeType::new);
    public static final RegistryObject<RecipeType<TinderRecipe>> TELEPORT_TINDER_RECIPE_TYPE =
            RECIPES.register("teleport_tinder_recipe", TinderRecipeType::new);
    public static final RegistryObject<RecipeType<TinderRecipe>> POTION_TINDER_RECIPE_TYPE =
            RECIPES.register("potion_tinder_recipe", TinderRecipeType::new);
    public static final RegistryObject<RecipeType<TinderRecipe>> GROUND_TINDER_RECIPE_TYPE =
            RECIPES.register("ground_tinder_recipe", TinderRecipeType::new);
    
    public static final RegistryObject<TinderRecipeSerializer<TinderRecipe>> TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("tinder_recipe", () -> new TinderRecipeSerializer<>(TinderRecipe.class));
    public static final RegistryObject<RecipeSerializer<TeleportTinderRecipe>> TELEPORT_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("teleport_tinder_recipe", () -> new TeleportTinderRecipeSerializer(TeleportTinderRecipe.class));
    public static final RegistryObject<RecipeSerializer<PotionTinderRecipe>> POTION_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("potion_tinder_recipe", () -> new PotionTinderRecipeSerializer(PotionTinderRecipe.class));
    public static final RegistryObject<RecipeSerializer<GroundTinderRecipe>> GROUND_TINDER_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("ground_tinder_recipe", () -> new TinderRecipeSerializer<>(GroundTinderRecipe.class));
    
    
    public static final RegistryObject<RecipeType<BlockToBlockRecipe>> BLOCK_TO_BLOCK_RECIPE_TYPE =
            RECIPES.register("block_to_block", TransmuteBlockRecipeType::new);
    
    public static final RegistryObject<RecipeSerializer<BlockToBlockRecipe>> BLOCK_TO_BLOCK_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("block_to_block", () -> new BlockToBlockRecipeSerializer(BlockToBlockRecipe.class));
    
    
    public static final RegistryObject<MenuType<SpiritualCampfireBlockMenu>> SPIRITUAL_CAMPFIRE_BLOCK_CONTAINER =
            CONTAINERS.register("spiritual_campfire_block_container", () ->
                    IForgeMenuType.create((windowId, inv, data) -> new SpiritualCampfireBlockMenu(windowId, data.readBlockPos(), inv, inv.player)));
    
    
    public static void register(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MOB_EFFECTS.register(bus);
        RECIPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
        CONTAINERS.register(bus);
        ENTITIES.register(bus);
        TOPPlugin.register();
        NetworkHandler.init();
    }
}
