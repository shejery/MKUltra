package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.item.ItemStack;

public class ItemChoice {
    public final ItemStack item;
    public final double weight;
    public final int minLevel;
    public final float dropChance;

    public ItemChoice(ItemStack item, double weight, int minLevel, float dropChance){
        this.item = item;
        this.weight = weight;
        this.minLevel = minLevel;
        this.dropChance = dropChance;
    }
    public ItemChoice(ItemStack item, double weight, int minLevel){
        this(item, weight, minLevel, .05f);
    }
}
