package uk.co.nikodem.dFTrueOneBlock.Events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockPlayer;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockWorld;

public class OnJoin implements Listener {
    private final DFTrueOneBlock plugin;

    public OnJoin(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        Player plr = event.getPlayer();

        RemovePlayerMetadata(plr);

        plugin.DiscoverRecipes(plr);

        SkyblockWorld skyblockWorld = plugin.skyblockData.getSkyblockWorldPlayerIsIn(plr);
        if (skyblockWorld != null) {
            skyblockWorld.OnPlayerJoin(event);
        } else {
            World world = plr.getWorld();
            Location spawnPoint = world.getSpawnLocation();
            spawnPoint.add(0.5, 0, 0.5);
            plr.teleport(spawnPoint);
        }

        String uuid = String.valueOf(plr.getUniqueId());

        SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(uuid);
        if (skyblockPlayer == null) {
            if (plugin.saveData.getPlayerData() == null) return;
            if (plugin.saveData.getPlayerData().get("players."+uuid) != null) {
                skyblockPlayer = plugin.saveData.loadSkyblockPlayerFromFile("players."+uuid);
                plugin.skyblockData.skyblockPlayers.add(skyblockPlayer);
            }
        }
    }

    public void RemovePlayerMetadata(Player plr) {
        plr.removeMetadata("customNameChatType", plugin);
        plr.removeMetadata("customNameChatResponse", plugin);
        plr.removeMetadata("editMenuSelectedWI", plugin);
        plr.removeMetadata("iconSelectorSelected", plugin);
        plr.removeMetadata("uiStartCode", plugin);
        plr.removeMetadata("createMenuSelectedGM", plugin);
        plr.removeMetadata("gamemodeSelectorSelected", plugin);
    }
}
