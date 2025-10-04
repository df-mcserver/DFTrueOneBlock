package uk.co.nikodem.dFTrueOneBlock.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockPlayer;

public class Create implements CommandExecutor {
    private final DFTrueOneBlock plugin;

    public Create(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player plr) {
            SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);
            if (skyblockPlayer.getWorld1() == null) {
                plugin.mm.OpenMenu(plr, "create-main-world1");
            } else if (skyblockPlayer.getWorld2() == null) {
                plugin.mm.OpenMenu(plr, "create-main-world2");
            } else if (skyblockPlayer.getWorld3() == null) {
                plugin.mm.OpenMenu(plr, "create-main-world3");
            } else {
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2There are no more available worlds! Delete one to make a new one!"));
            }
        } else {
            commandSender.sendMessage("You are not a player!");
        }
        return true;
    }
}