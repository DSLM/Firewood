package com.dslm.firewood.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

public class StaticValue
{
    public static final String MOD_ID = "firewood";
    
    public static final String MAJOR = "majorEffects";
    public static final String MINOR = "minorEffects";
    public static final int MAJOR_EFFECT_COLOR = 0x48c774;
    public static final int MINOR_EFFECT_COLOR = 0x48c774;
    
    public static final String TYPE = "type";
    public static final String SUB_TYPE = "subType";
    public static final String ING_BLOCK = "ing_block";
    public static final String TARGET_BLOCK = "target_block";
    public static final String COLOR = "color";
    public static final String DAMAGE = "damage";
    public static final String MIN_HEALTH = "minHealth";
    public static final String PROCESS = "process";
    public static final String CHANCE = "chance";
    public static final String RANGE = "range";
    
    public static final String ACTIVE_LANTERN = "activeLantern";
    
    public static final TagKey<Item> ITEM_TINDER_TAG = TagKey.create(Registry.ITEM_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final TagKey<Block> BLOCK_TINDER_TAG = TagKey.create(Registry.BLOCK_REGISTRY,
            new ResourceLocation("firewood", "tinder"));
    
    public static final ResourceLocation ICONS = new ResourceLocation(MOD_ID, "textures/gui/icons.png");
    
    public static final String[] COLORS = {
            "light_blue",
            "light_gray",
            "white",
            "orange",
            "magenta",
            "yellow",
            "lime",
            "pink",
            "gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"};
    
    public static final List<String> COLORS_ARRAY = Arrays.asList(COLORS);
    
    
    public static final ArrayList<String> DEFAULT_COLOR_ORDER = new ArrayList<>()
    {{
        add("green");
        add("lime");
        add("yellow");
        add("orange");
        add("red");
        add("pink");
        add("magenta");
        add("purple");
        add("blue");
        add("cyan");
        add("light_blue");
        add("white");
        add("light_gray");
        add("gray");
        add("black");
        add("brown");
    }};
    
    public static int colorInt(String s)
    {
        var s1 = s.toLowerCase().replace("0x", "");
        return HexFormat.fromHexDigits(s1);
    }
}
