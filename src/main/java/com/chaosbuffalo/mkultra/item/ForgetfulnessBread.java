package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Created by Jacob on 4/4/2016.
 */
public class ForgetfulnessBread extends ItemFood {

    public ForgetfulnessBread(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setAlwaysEdible();
    }


    public ForgetfulnessBread(int amount, boolean isWolfFood) {
        this(amount, 0.6F, isWolfFood);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10 * GameConstants.TICKS_PER_SECOND, 100));
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 10 * GameConstants.TICKS_PER_SECOND, 1));

            IPlayerData data = MKUPlayerData.get(player);
            if (data != null) {
                data.activateClass(MKURegistry.INVALID_CLASS);
            }
        }
    }
}