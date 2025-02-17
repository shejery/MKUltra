package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class ArmorClass {

    public static final ArmorClass HEAVY = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.heavy"));
    public static final ArmorClass MEDIUM = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.medium"));
    public static final ArmorClass LIGHT = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.light"));
    public static final ArmorClass ROBES = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.robes"));
    public static final ArmorClass NOT_CLASSIFIED = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.undefined"));

    public final ResourceLocation location;

    public static void clearArmorClasses(){
        HEAVY.materials.clear();
        MEDIUM.materials.clear();
        LIGHT.materials.clear();
        ROBES.materials.clear();
    }

    public static void registerDefaults() {
        LIGHT.inherit(ROBES);
        MEDIUM.inherit(LIGHT);
        HEAVY.inherit(MEDIUM);
    }

    private final Set<ItemArmor.ArmorMaterial> materials = Sets.newHashSet();
    private final List<ArmorClass> ancestors = Lists.newArrayList();

    public ArmorClass(ResourceLocation location) {
        this.location = location;
    }

    @SideOnly(Side.CLIENT)
    public String getName()
    {
        return I18n.format(String.format("%s.%s.name", location.getNamespace(), location.getPath()));
    }

    public ResourceLocation getLocation()
    {
        return location;
    }

    public boolean canWear(ItemArmor.ArmorMaterial material) {
        return materials.contains(material) ||
                ancestors.stream().anyMatch(a -> a.canWear(material));
    }

    public static ArmorClass getArmorClassForArmorMat(ItemArmor.ArmorMaterial material){
        if (ROBES.materials.contains(material)){
            return ROBES;
        } else if (LIGHT.materials.contains(material)){
            return LIGHT;
        } else if (MEDIUM.materials.contains(material)){
            return MEDIUM;
        } else if (HEAVY.materials.contains(material)){
            return HEAVY;
        } else {
            return NOT_CLASSIFIED;
        }
    }

    private ArmorClass inherit(ArmorClass armorClass) {
        ancestors.add(armorClass);
        return this;
    }

    public ArmorClass register(ItemArmor.ArmorMaterial material) {
        materials.add(material);
        return this;
    }
}
