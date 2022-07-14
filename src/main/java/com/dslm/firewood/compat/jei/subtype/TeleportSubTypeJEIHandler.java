package com.dslm.firewood.compat.jei.subtype;

import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.TeleportSubType;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;

import static com.dslm.firewood.datagen.LanguageUtil.JEI_SUB_INFO;
import static com.dslm.firewood.util.StaticValue.BLACK_FONT_COLOR;

public class TeleportSubTypeJEIHandler extends FireEffectSubTypeJEIHandler
{
    private static final TeleportSubTypeJEIHandler INSTANCE = new TeleportSubTypeJEIHandler();
    
    public static TeleportSubTypeJEIHandler getInstance()
    {
        return INSTANCE;
    }
    
    @Override
    public int draw(FireEffectSubTypeBase subType, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY, int width, int height)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = super.draw(subType, recipeSlotsView, stack, mouseX, mouseY, width, height), textW = 0;
        
        if(!(subType instanceof TeleportSubType teleportSubType))
        {
            return textH;
        }
        
        TranslatableComponent dimFromBlacklistLine = new TranslatableComponent(JEI_SUB_INFO + "dim_from_blacklist." + teleportSubType.isDimFromBlacklist());
        minecraft.font.draw(stack, dimFromBlacklistLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent dimFromListLine = new TranslatableComponent(JEI_SUB_INFO + "dim_from_list");
        String dimFromList = teleportSubType.getDimFromList().toString();
        dimFromListLine.append(dimFromList);
        minecraft.font.draw(stack, dimFromListLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent dimToBlacklistLine = new TranslatableComponent(JEI_SUB_INFO + "dim_to_blacklist." + teleportSubType.isDimToBlacklist());
        minecraft.font.draw(stack, dimToBlacklistLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        TranslatableComponent dimToListLine = new TranslatableComponent(JEI_SUB_INFO + "dim_to_list");
        String dimToList = teleportSubType.getDimToList().toString();
        dimToListLine.append(dimToList);
        minecraft.font.draw(stack, dimToListLine, textW, textH, BLACK_FONT_COLOR);
        textH += minecraft.font.lineHeight;
        
        return textH;
    }
}
