package com.dslm.firewood.fireEffectHelper.flesh.data;

import java.util.Set;

public interface FireEffectNBTDataInterface
{
    String getType();
    
    String getSubType();
    
    String get(String s);
    
    String put(String key, String value);
    
    FireEffectNBTDataInterface copy();
    
    Set<String> keySet();
    
    int size();
}
