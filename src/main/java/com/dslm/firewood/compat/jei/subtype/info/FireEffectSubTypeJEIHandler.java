package com.dslm.firewood.compat.jei.subtype.info;

import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.util.StaticValue;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;

import java.util.HexFormat;
import java.util.List;

import static com.dslm.firewood.datagen.LanguageUtil.JEI_SUB_INFO;
import static com.dslm.firewood.datagen.LanguageUtil.TINDER_TOOLTIP;
import static com.dslm.firewood.util.StaticValue.BLACK_FONT_COLOR;

public class FireEffectSubTypeJEIHandler
{
    private static final FireEffectSubTypeJEIHandler INSTANCE = new FireEffectSubTypeJEIHandler();
    
    public static FireEffectSubTypeJEIHandler getInstance()
    {
        return INSTANCE;
    }
    
    public void setRecipe(FireEffectSubTypeBase subType, IRecipeLayoutBuilder builder, IFocusGroup focuses, List<Item> tinderItems, int width, int height)
    {
        var tempList = subType.getSubItems(tinderItems);
        builder.addSlot(RecipeIngredientRole.CATALYST, width - 16, 0).addItemStacks(tempList);
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStacks(tempList);
    }
    
    public int draw(FireEffectSubTypeBase subType, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = 0, textW = 0;
    
        TranslatableComponent typeLine = new TranslatableComponent(JEI_SUB_INFO + "type",
                new TranslatableComponent(TINDER_TOOLTIP + "major_effect.%s".formatted(subType.getType())));
        minecraft.font.draw(stack, typeLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent subTypeLine = new TranslatableComponent(JEI_SUB_INFO + "sub_type",
                new TranslatableComponent(TINDER_TOOLTIP + "major_effect.%1$s.%2$s".formatted(subType.getType(), subType.getSubType())));
        minecraft.font.draw(stack, subTypeLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;

//        var descLines = getDescToolTips(subType.getType(), subType.getSubType(), BLACK_FONT_COLOR);
//        for(Component component : descLines)
//        {
//            minecraft.font.draw(stack, component, textW, textH, BLACK_FONT_COLOR);
//            textH += minecraft.font.lineHeight;
//        }
    
        TranslatableComponent colorLine = new TranslatableComponent(JEI_SUB_INFO + "color", HexFormat.of().toHexDigits(subType.getColor()).substring(2));
        minecraft.font.draw(stack, colorLine, textW, textH, BLACK_FONT_COLOR);
        float padding = (Minecraft.getInstance().font.width("█") - Minecraft.getInstance().font.width("⬛")) / 2.0f;
        minecraft.font.draw(stack,
                "█",
                textW + Minecraft.getInstance().font.width(colorLine),
                textH,
                StaticValue.reverseColor(subType.getColor()));
        minecraft.font.draw(stack,
                "⬛",
                textW + Minecraft.getInstance().font.width(colorLine) + padding,
                textH,
                subType.getColor());
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent damageLine = new TranslatableComponent(JEI_SUB_INFO + "damage", subType.getDamage());
        minecraft.font.draw(stack, damageLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent minHealthLine = new TranslatableComponent(JEI_SUB_INFO + "min_health", subType.getMinHealth());
        minecraft.font.draw(stack, minHealthLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent cooldownLine = new TranslatableComponent(JEI_SUB_INFO + "cooldown",
                subType.getCooldown() / 20.0,
                subType.getCooldown());
        minecraft.font.draw(stack, cooldownLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent processLine = new TranslatableComponent(JEI_SUB_INFO + "process", subType.getProcess());
        minecraft.font.draw(stack, processLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        TranslatableComponent chanceLine = new TranslatableComponent(JEI_SUB_INFO + "chance",
                subType.getChance() * 100.0);
        minecraft.font.draw(stack, chanceLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
    
        int[] range = subType.getRange();
        TranslatableComponent rangeLine;
        if(range[0] < 0 || range[1] < 0 || range[2] < 0)
        {
            rangeLine = new TranslatableComponent(JEI_SUB_INFO + "range_single");
        }
        else
        {
            rangeLine = new TranslatableComponent(JEI_SUB_INFO + "range", range[0], range[1], range[2]);
        }
        minecraft.font.draw(stack, rangeLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent targetLimitLine = new TranslatableComponent(JEI_SUB_INFO + "target_limit", subType.getTargetLimit());
        minecraft.font.draw(stack, targetLimitLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        return textH;
    }
}
