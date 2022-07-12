package com.dslm.firewood.util;

public record Color(int Alpha, int Red, int Green, int Blue)
{
    public static Color intToColor(int argb)
    {
        return new Color(
                (argb & 0xff000000) >> 24,
                (argb & 0x00ff0000) >> 16,
                (argb & 0x0000ff00) >> 8,
                (argb & 0x000000ff));
    }
    
    public static int colorToInt(int a, int r, int g, int b)
    {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }
}
