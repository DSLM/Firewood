package com.dslm.firewood.fireeffecthelper.flesh.base;

import java.util.HashMap;

public class FireEffectKindHelper<T>
{
    public final String id;
    public final HashMap<String, T> helpers = new HashMap<>();
    
    public FireEffectKindHelper(String kind)
    {
        id = kind;
    }
    
    public void addHelper(String type, T helper)
    {
        helpers.put(type, helper);
    }
}
