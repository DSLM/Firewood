package com.dslm.firewood.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class InfoButton extends Button
{
    public InfoButton(int X, int Y, int width, int height, Component message, Button.OnPress onPress, Button.OnTooltip onTooltip)
    {
        super(X, Y, width, height, message, onPress, onTooltip);
    }
}
