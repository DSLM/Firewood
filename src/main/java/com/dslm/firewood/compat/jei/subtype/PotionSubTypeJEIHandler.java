package com.dslm.firewood.compat.jei.subtype;

import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.PotionSubType;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;

import static com.dslm.firewood.datagen.LanguageUtil.JEI_SUB_INFO;
import static com.dslm.firewood.util.StaticValue.BLACK_FONT_COLOR;

public class PotionSubTypeJEIHandler extends FireEffectSubTypeJEIHandler
{
    private static final PotionSubTypeJEIHandler INSTANCE = new PotionSubTypeJEIHandler();
    
    public static PotionSubTypeJEIHandler getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    public int draw(FireEffectSubTypeBase subType, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = super.draw(subType, recipeSlotsView, stack, mouseX, mouseY, width, height), textW = 0;
        
        if(!(subType instanceof PotionSubType potionSubType))
        {
            return textH;
        }
        
        TranslatableComponent colorMixedLine = new TranslatableComponent(JEI_SUB_INFO + "color_mixed", 100 - potionSubType.getColorMixed(), potionSubType.getColorMixed());
        minecraft.font.draw(stack, colorMixedLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent effectMultiLine = new TranslatableComponent(JEI_SUB_INFO + "effect_multi", potionSubType.getEffectMulti());
        minecraft.font.draw(stack, effectMultiLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent toEnemyLine = new TranslatableComponent(JEI_SUB_INFO + "to_enemy." + potionSubType.isToEnemy());
        minecraft.font.draw(stack, toEnemyLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        return textH;
    }
}
