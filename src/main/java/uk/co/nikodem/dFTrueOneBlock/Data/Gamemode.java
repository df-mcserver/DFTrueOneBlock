package uk.co.nikodem.dFTrueOneBlock.Data;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import javax.annotation.Nullable;

public class Gamemode {
    public static final Gamemode deathprogress = new Gamemode((byte) -128, Material.SCULK_CATALYST, "&4Death progression", "&4Die to progress in the game.");
    public static final Gamemode hardcore = new Gamemode((byte) -127, Material.ENDER_EYE, "&4Hardcore", "&4One life.\nIf you die, your world is automatically deleted.");
    public static final Gamemode standard = new Gamemode((byte) 0, Material.GRASS_BLOCK, "&3Standard", "&2Normal skyblock");
    public static final Gamemode oneblock = new Gamemode((byte) 1, Material.BEDROCK, "&3One block", "&2Normal one-block skyblock");
    public static final Gamemode test = new Gamemode((byte) 69, Material.DIAMOND_BLOCK, "&2Creative", "&2Normal skyblock in creative mode");
    public static final Gamemode[] gamemodes = {deathprogress, hardcore, standard, oneblock, test};

    @Nullable
    public static Gamemode getGamemodeById(byte id) {
        for (Gamemode gamemode : gamemodes) {
            if (gamemode.getId() == id)
                return gamemode;
        }
        return null;
    }

    private final Byte id;
    private final Material icon;
    private final String name;
    private final String description;

    public Gamemode(Byte id, Material material, String name, String description) {
        this.id = id;
        this.icon = material;

        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.description = ChatColor.translateAlternateColorCodes('&', description);
    }

    public Byte getId() {
        return id;
    }

    public Material getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
