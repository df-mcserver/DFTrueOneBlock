package uk.co.nikodem.dFTrueOneBlock.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.World;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockWorld;

public class OnGenerateChunks implements Listener {

    private final DFTrueOneBlock plugin;

    public OnGenerateChunks(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }
//
//    @EventHandler
//    public void OnGenerateChunks(ChunkPopulateEvent e) {
//        World world = e.getWorld();
//        String name = world.getName();
//
//        SkyblockWorld skyblockWorld = plugin.skyblockData.getSkyblockWorldFromRealID(name);
//        if (skyblockWorld != null) {
//            // is skyblock world
//            e
//        }
//    }
}
