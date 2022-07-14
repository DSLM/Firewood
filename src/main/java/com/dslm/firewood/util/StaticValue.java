package com.dslm.firewood.util;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.fml.ModList;

import java.util.HexFormat;

import static com.dslm.firewood.config.ColorConfig.HIGH_CONTRAST_MODE;

public class StaticValue
{
    public static final String MOD_ID = "firewood";
    
    public static final String MAJOR = "majorEffects";
    public static final String MINOR = "minorEffects";
    public static final int MAJOR_EFFECT_COLOR = 0x48c774;
    public static final int MINOR_EFFECT_COLOR = 0x48c774;
    public static final int TOTAL_COLOR = 0x48c774;
    public static final int VANILLA_FONT_COLOR = 0x404040;
    public static final int BLACK_FONT_COLOR = 0;
    
    public static final String TYPE = "type";
    public static final String SUB_TYPE = "subType";
    public static final String ING_BLOCK = "ing_block";
    public static final String TARGET_BLOCK = "target_block";
    public static final String COLOR = "color";
    public static final String DAMAGE = "damage";
    public static final String MIN_HEALTH = "min_health";
    public static final String PROCESS = "process";
    public static final String IN_CACHE = "in_cache";
    public static final String CACHE = "cache";
    public static final String CHANCE = "chance";
    public static final String RANGE = "range";
    public static final String TARGET_LIMIT = "target_limit";
    public static final String COOLDOWN = "cooldown";
    
    public static final String ACTIVE_LANTERN = "activeLantern";
    
    public static final TagKey<Item> ITEM_TINDER_TAG = TagKey.create(Registry.ITEM_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final TagKey<Block> BLOCK_TINDER_TAG = TagKey.create(Registry.BLOCK_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final ResourceLocation ICONS = new ResourceLocation(MOD_ID, "textures/gui/icons.png");
    
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    
    public static int colorInt(String s)
    {
        var s1 = s.toLowerCase().replace("0x", "");
        return HexFormat.fromHexDigits(s1);
    }
    
    public static final String TOP_MOD = "theoneprobe";
    public static final String CURIOS_MOD = "curios";
    public static final String PATCHOULI_MOD = "patchouli";
    public static final String SHIMMER = "shimmer";
    
    public static boolean checkMod(String modId)
    {
        return ModList.get().isLoaded(modId);
    }
    
    public static int reverseColor(int color)
    {
        Color temp1 = Color.intToColor(color);
        return Color.colorToInt(0, 255 - temp1.Red(), 255 - temp1.Green(), 255 - temp1.Blue());
    }
    
    public static TranslatableComponent colorfulText(TranslatableComponent text, int color)
    {
        return HIGH_CONTRAST_MODE != null && HIGH_CONTRAST_MODE.get() ? text : (TranslatableComponent) text.withStyle(style -> style.withColor(TextColor.fromRgb(color)));
    }
}
