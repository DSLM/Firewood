package com.dslm.firewood.tooltip;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class MiddleComponent extends TranslatableComponent
{
    private float damage = 0f;
    
    public MiddleComponent(String pKey)
    {
        super(pKey);
    }
    
    public MiddleComponent(String pKey, Object... pArgs)
    {
        super(pKey, pArgs);
    }
    
    public float getDamage()
    {
        return damage;
    }
    
    public void setDamage(float damage)
    {
        this.damage = damage;
    }
}
