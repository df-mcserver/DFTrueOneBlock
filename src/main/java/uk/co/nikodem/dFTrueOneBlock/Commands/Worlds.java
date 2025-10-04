package uk.co.nikodem.dFTrueOneBlock.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;

public class Worlds implements CommandExecutor {
    private final DFTrueOneBlock plugin;

    public Worlds(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player plr) {
            plugin.mm.OpenMenu(plr, "mm-worlds");
        } else {
            commandSender.sendMessage("You are not a player!");
        }
        return true;
    }
}