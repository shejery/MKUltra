package com.chaosbuffalo.mkultra.init;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Jacob on 4/18/2016.
 */
public class ModTileEntities {


    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityMKSpawner.class,
                new ResourceLocation(MKUltra.MODID, "mk_spawner_tile"));
    }
}


