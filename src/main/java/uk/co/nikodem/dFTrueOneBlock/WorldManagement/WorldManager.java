package uk.co.nikodem.dFTrueOneBlock.WorldManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockPlayer;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockWorld;

import javax.annotation.Nullable;

public class WorldManager {

    private static DFTrueOneBlock plugin;

    public WorldManager(DFTrueOneBlock plugin) {
        WorldManager.plugin = plugin;
    }

    @Nullable
    public World getWorld(SkyblockWorld world) {
        String realId = world.getRealId();
        return getWorld(realId);
    }

    @Nullable
    public World getWorld(String realId) {
        return Bukkit.getWorld(realId);
    }

    public World createWorld(SkyblockWorld skyblockWorld) {
        return Bukkit.createWorld(
                new WorldCreator(
                        skyblockWorld.getRealId()
                )
        );
    }

    public boolean sendPlayerToWorld(SkyblockWorld skyblockWorld, SkyblockPlayer skyblockPlayer) {
        World world = getWorld(skyblockWorld);
        Player plr = plugin.skyblockData.getPlayer(skyblockPlayer);

        if (plr == null) return false;
        if (world == null) {
            world = createWorld(skyblockWorld);
        }

        if (plugin.skyblockData.getSkyblockWorldPlayerIsIn(plr) != skyblockWorld) {
            Location teleport = new Location(
                    world,
                    0, 64, 0
            );

            plr.teleport(teleport);
            return true;
        } else {
            return false;
        }
    }
}
