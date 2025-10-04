package uk.co.nikodem.dFTrueOneBlock.Menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.nikodem.dFTrueOneBlock.Data.SkyblockWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiHelper {
    private static final int rows = 6;
    public static int InventoryLocation(int x, int y) {
        int pos = ((y*9)+x)-1;
        if (pos < 0) pos = 0;
        else if (pos >= (9*rows)) pos = (9*rows)-1;
        return pos;
    }

    public static void BlankOutInventory(Inventory inv) {
        ItemStack blank = createInventoryBlank();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, blank);
        }
    }

    public static ItemStack createInventoryBlank() {
        ItemStack freeSlot = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta freeSlotMeta = freeSlot.getItemMeta();
        assert freeSlotMeta != null;
        freeSlotMeta.setDisplayName(ChatColor.DARK_GRAY+"");
        freeSlotMeta.setCustomModelData(8008);
        freeSlot.setItemMeta(freeSlotMeta);
        return freeSlot;
    }

    public static Inventory CreateMenu(String id) {
        Inventory newInv = Bukkit.createInventory(null, 9 * rows, id);
        BlankOutInventory(newInv);
        return newInv;
    }

    public static ItemStack CustomItem(Material material, String name, int ModelID) {
        ItemStack i = new ItemStack(material, 1);
        ItemMeta im = i.getItemMeta();
        assert im != null;
        im.setCustomModelData(ModelID);
        if (name != null) im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack CustomItem(Material material, String name) {
        return CustomItem(material, name, 0);
    }

    public static ItemStack CustomItem(Material material, String name, List<String> lores, int ModelID) {
        ItemStack i = CustomItem(material, name, ModelID);
        ItemMeta im = i.getItemMeta();
        im.setLore(lores);
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack CustomItem(Material material, String name, List<String> lores) {
        return CustomItem(material, name, lores, 0);
    }

    @Nullable
    public static ItemStack CustomWorldItem(SkyblockWorld skyblockWorld) {
        if (skyblockWorld == null) return null;
        ItemStack i = new ItemStack(skyblockWorld.getWorldIcon().getMaterial(), 1);
        ItemMeta im = i.getItemMeta();
        assert im != null;
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3"+skyblockWorld.getName()));

        List<String> lores = new ArrayList<>();

        if (skyblockWorld.getWorldCompleted()) {
            im.addEnchant(Enchantment.LOYALTY, 100, true);
            lores.add(ChatColor.translateAlternateColorCodes('&', "&aWorld completed!"));
        } else if (!skyblockWorld.getTutorialCompletion()) {
            lores.add(ChatColor.translateAlternateColorCodes('&', "&7In tutorial"));
        }
        lores.add(ChatColor.translateAlternateColorCodes('&', skyblockWorld.getGamemode().getName()));

        lores.add(ChatColor.translateAlternateColorCodes('&', "&3Click to join!"));

        im.setLore(lores);
        i.setItemMeta(im);

        return i;
    }

    public static ItemStack GuideItem(String title, String... description) {
        List<String> lore = List.of(description);
        return CustomItem(Material.MAP, title, lore);
    }

    public static void CreateGuideInInventory(InventoryView view, String title, String... description) {
        view.getTopInventory().setItem(45, GuideItem(title, description));
    }

    public static void playClickSound(Player plr) {
        plr.playSound(
                plr,
                Sound.UI_BUTTON_CLICK,
                1F,
                1F
        );
    }

    public static void playSucceedSound(Player plr) {
        plr.playSound(
                plr,
                Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                1F,
                1F
        );
    }

    public static void playWarningSound(Player plr) {
        plr.playSound(
                plr,
                Sound.BLOCK_NOTE_BLOCK_BASS,
                1F,
                1F
        );
    }
}
