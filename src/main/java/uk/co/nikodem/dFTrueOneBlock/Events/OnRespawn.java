package uk.co.nikodem.dFTrueOneBlock.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockWorld;

public class OnRespawn implements Listener {
    private final DFTrueOneBlock plugin;

    public OnRespawn(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerRespawn(PlayerRespawnEvent event) {
        plugin.DiscoverRecipes(event.getPlayer());
        SkyblockWorld skyblockWorld = plugin.skyblockData.getSkyblockWorldPlayerIsIn(event.getPlayer());
        if (skyblockWorld != null) {
            skyblockWorld.OnPlayerRespawn(event);
        }
    }
}
