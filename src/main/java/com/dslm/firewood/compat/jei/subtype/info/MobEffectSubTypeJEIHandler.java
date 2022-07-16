package com.dslm.firewood.compat.jei.subtype.info;

import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.MobEffectSubType;
import com.dslm.firewood.util.StaticValue;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;

import static com.dslm.firewood.datagen.LanguageUtil.JEI_SUB_INFO;
import static com.dslm.firewood.util.StaticValue.BLACK_FONT_COLOR;

public class MobEffectSubTypeJEIHandler extends FireEffectSubTypeJEIHandler
{
    private static final MobEffectSubTypeJEIHandler INSTANCE = new MobEffectSubTypeJEIHandler();
    
    public static MobEffectSubTypeJEIHandler getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    public int draw(FireEffectSubTypeBase subType, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = super.draw(subType, recipeSlotsView, stack, mouseX, mouseY, width, height), textW = 0;
        
        if(!(subType instanceof MobEffectSubType mobEffectSubType))
        {
            return textH;
        }
        
        TranslatableComponent effectsLine = new TranslatableComponent(JEI_SUB_INFO + "effects");
        minecraft.font.draw(stack, effectsLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        ArrayList<Component> effectsLines = new ArrayList<>();
        StaticValue.getPotionLines(mobEffectSubType.getEffects(), effectsLines, 1);
        for(Component component : effectsLines)
        {
            minecraft.font.draw(stack, component, textW, textH, BLACK_FONT_COLOR);
            textH += minecraft.font.lineHeight;
        }
        
        TranslatableComponent effectsToEnemyLine = new TranslatableComponent(JEI_SUB_INFO + "effects_to_enemy");
        minecraft.font.draw(stack, effectsToEnemyLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        ArrayList<Component> effectsToEnemyLines = new ArrayList<>();
        StaticValue.getPotionLines(mobEffectSubType.getEffectsToEnemy(), effectsToEnemyLines, 1);
        for(Component component : effectsToEnemyLines)
        {
            minecraft.font.draw(stack, component, textW, textH, BLACK_FONT_COLOR);
            textH += minecraft.font.lineHeight;
        }
        
        return textH;
    }
}
