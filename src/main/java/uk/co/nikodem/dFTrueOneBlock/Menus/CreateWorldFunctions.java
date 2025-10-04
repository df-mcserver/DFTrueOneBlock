package uk.co.nikodem.dFTrueOneBlock.Menus;

import org.bukkit.inventory.ItemStack;
import uk.co.nikodem.dFTrueOneBlock.Data.Gamemode;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockSession;
import uk.co.nikodem.dFTrueOneBlock.Data.WorldIcon;

import java.util.ArrayList;
import java.util.List;

import static uk.co.nikodem.dFTrueOneBlock.Menus.GuiHelper.CustomItem;

public class CreateWorldFunctions {
    public static ItemStack createSessionItem(SkyblockSession session, int index) {
        return CustomItem(session.getIcon().getMaterial(), session.getName(), index);
    }

    public static ItemStack createGamemodeItem(Gamemode gm, int index) {
        List<String> lores = new ArrayList<>();
        lores.add(gm.getDescription());
        return CustomItem(gm.getIcon(), gm.getName(), lores, index);
    }

    public static ItemStack createGamemodeItem(Gamemode gm) {
        int index = 0;
        for (int i = 0; i < Gamemode.gamemodes.length; i++) {
            if (Gamemode.gamemodes[i] == gm) {
                index = i;
                break;
            }
        }
        return createGamemodeItem(gm, index);
    }

    public static ItemStack createGamemodeItem() {
        int index = 0;
        for (int i = 0; i < Gamemode.gamemodes.length; i++) {
            if (Gamemode.gamemodes[i] == Gamemode.standard) {
                index = i;
                break;
            }
        }
        return createGamemodeItem(Gamemode.standard, index);
    }

    public static Gamemode getNextGamemode(int index) {
        return Gamemode.gamemodes[index];
    }

    public static Gamemode getNextGamemode(ItemStack item) {
        return getNextGamemode(item.getItemMeta().getCustomModelData());
    }

    public static ItemStack createWorldIcon(WorldIcon icon, int index) {
        return CustomItem(icon.getMaterial(), null, index);
    }

    public static ItemStack createWorldIcon(WorldIcon icon) {
        int index = 0;
        for (int i = 0; i < WorldIcon.worldicons.length; i++) {
            if (WorldIcon.worldicons[i] == icon) {
                index = i;
                break;
            }
        }
        return createWorldIcon(icon, index);
    }

    public static ItemStack createWorldIcon() {
        int index = 0;
        for (int i = 0; i < WorldIcon.worldicons.length; i++) {
            if (WorldIcon.worldicons[i] == WorldIcon.grassBlock) {
                index = i;
                break;
            }
        }
        return createWorldIcon(WorldIcon.grassBlock, index);
    }

    public static WorldIcon getNextWorldIcon(int index) {
        return WorldIcon.worldicons[index];
    }

    public static WorldIcon getNextWorldIcon(ItemStack item) {
        return getNextWorldIcon(item.getItemMeta().getCustomModelData());
    }

    public static String makeRandomWorldName() {
        String word1 = "";
        String word2 = "";

        String[] words1 = {
                "Beautiful",
                "Extravagant",
                "Vibrant",
                "Terrifying",
                "Rudimentary",
                "Classic",
                "Hellish",
                "Destructive",
                "Wretched",
                "Disgusting",
                "5-star",
                "1-star",
                "Terrible",
                "Grand",
                "Awful",
                "Isolated",
                "New",
                "Polarising",
                "Destroyed",
                "Revived",
        };

        String[] words2 = {
                "Grove",
                "Resort",
                "Island",
                "Sector",
                "World",
                "Realm",
                "Division",
                "Habitat",
                "Domain"
        };
        int random1 = (int)(Math.random()*(words1.length-1) +1);
        int random2 = (int)(Math.random()*(words2.length-1) +1);

        String result = "";

        if ((int)(Math.random()*(4096)) == 420) {
            // 1 / 4096 chance
            result = "skibidi grove";
        } else {
            word1 = words1[random1];
            word2 = words2[random2];
            result = word1+" "+word2;
        }

        return result;
    }
}
