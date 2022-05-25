package com.dslm.firewood.tooltip;

import net.minecraft.network.chat.TranslatableComponent;

public class MiddleComponent extends TranslatableComponent
{
    private float damage = 0f;
    private float minHealth = 0f;
    
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
    
    public float getMinHealth()
    {
        return minHealth;
    }
    
    public void setMinHealth(float minHealth)
    {
        this.minHealth = minHealth;
    }
}
