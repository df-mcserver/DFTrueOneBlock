package uk.co.nikodem.dFTrueOneBlock.Data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class SaveData {
    private final DFTrueOneBlock plugin;

    private File playerDataFile;
    private FileConfiguration playerData;
    private File worldDataFile;
    private FileConfiguration worldData;

    public SaveData(DFTrueOneBlock plugin) {
        this.plugin = plugin;

        initialisePlayerData();
        initialiseWorldData();
    }

    @Nullable
    public FileConfiguration getPlayerData() {
        return this.playerData;
    }

    // playerdata functions

    public List<SkyblockPlayer> getSkyblockPlayers() {
        return (List<SkyblockPlayer>) getPlayerData().getList("players");
    }

    public void saveSkyblockPlayersToFile(List<SkyblockPlayer> skyblockPlayers) {
        assert getPlayerData() != null;
        for (int i = 0; i < skyblockPlayers.size(); i++) {
            SkyblockPlayer skyblockPlayer = skyblockPlayers.get(i);
            saveSkyblockPlayerToFile(skyblockPlayer);
        }
    }

    public void saveSkyblockPlayerToFile(SkyblockPlayer skyblockPlayer) {
        String path = "players."+skyblockPlayer.getUniqueId();
        assert getPlayerData() != null;
        getPlayerData().set(path+".uuid", skyblockPlayer.getUniqueId());
        if (skyblockPlayer.getWorld1() == null) {
            getPlayerData().set(path+".world1", null);
        } else {
            getPlayerData().set(path+".world1", skyblockPlayer.getWorld1());
        }
        if (skyblockPlayer.getWorld2() == null) {
            getPlayerData().set(path+".world2", null);
        } else {
            getPlayerData().set(path+".world2", skyblockPlayer.getWorld2());
        }
        if (skyblockPlayer.getWorld3() == null) {
            getPlayerData().set(path+".world3", null);
        } else {
            getPlayerData().set(path+".world3", skyblockPlayer.getWorld3());
        }
        getPlayerData().set(path+".version", SkyblockPlayer.dataVersion);
        savePlayerData();
    }

    public SkyblockPlayer loadSkyblockPlayerFromFile(String path) {
        String uuid = getPlayerData().getString(path+".uuid");
        SkyblockPlayer skyblockPlayer = new SkyblockPlayer(uuid);

        String world1realid = getPlayerData().getString(path+".world1");
        if (world1realid != null) skyblockPlayer.setWorld1(world1realid);

        String world2realid = getPlayerData().getString(path+".world2");
        if (world2realid != null) skyblockPlayer.setWorld2(world2realid);

        String world3realid = getPlayerData().getString(path+".world3");
        if (world3realid != null) skyblockPlayer.setWorld3(world3realid);

        return skyblockPlayer;
    }

    public void deletePlayerData(SkyblockPlayer skyblockPlayer) {
        String path = "players."+skyblockPlayer.getUniqueId();
        assert getPlayerData() != null;
        getPlayerData().set(path, null);
        savePlayerData();
    }

    public void removeWorldLink(SkyblockPlayer skyblockPlayer, String realid, int worldId) {
        String path = "players."+skyblockPlayer.getUniqueId();
        assert getPlayerData() != null;
        getPlayerData().set(path+".world"+String.valueOf(worldId), null);
        savePlayerData();
    }

    public void removeWorldLink(SkyblockPlayer skyblockPlayer, SkyblockWorld skyblockWorld) {
        removeWorldLink(skyblockPlayer, skyblockWorld.getRealId(), skyblockWorld.getWorldId());
    }

    // end of playerdata functions

    @Nullable
    public FileConfiguration getWorldData() {
        return this.worldData;
    }

    // worlddata functions

    public List<SkyblockWorld> getSkyblockWorlds() {
        return (List<SkyblockWorld>) getPlayerData().getList("skyblockworlds");
    }

    public void saveSkyblockWorldsToFile(List<SkyblockWorld> skyblockWorlds) {
        assert getPlayerData() != null;
        for (int i = 0; i < skyblockWorlds.size(); i++) {
            SkyblockWorld skyblockWorld = skyblockWorlds.get(i);
            saveSkyblockWorldToFile(skyblockWorld);
        }
    }

    public void saveSkyblockWorldToFile(SkyblockWorld skyblockWorld) {
        String path = "skyblockworlds."+skyblockWorld.getRealId();
        assert getWorldData() != null;
        getWorldData().set(path, null);
        getWorldData().set(path+".realid", skyblockWorld.getRealId());
        getWorldData().set(path+".worldid", skyblockWorld.getWorldId());
        getWorldData().set(path+".uuid", skyblockWorld.getUniqueId());
        getWorldData().set(path+".name", skyblockWorld.getName());
        getWorldData().set(path+".icon", skyblockWorld.getWorldIcon().getId());
        getWorldData().set(path+".completedtutorial", skyblockWorld.getTutorialCompletion());
        getWorldData().set(path+".completed", skyblockWorld.getWorldCompleted());
        getWorldData().set(path+".gamemode", skyblockWorld.getGamemode().getId());
        getWorldData().set(path+".owner", skyblockWorld.getOwner().getUniqueId());
        getWorldData().set(path+".version", SkyblockWorld.dataVersion);

        for (int i = 0; i < skyblockWorld.getMembers().size(); i++) {
            SkyblockWorldMember member = skyblockWorld.getMembers().get(i);
            String memberPath = path+".members."+member.getUniqueId();
            getWorldData().set(memberPath+".uuid", member.getUniqueId());
            getWorldData().set(memberPath+".permissionlevel", member.getPermissionLevel());

            getWorldData().set(memberPath+".joincount", member.getJoinCount());
            getWorldData().set(memberPath+".health", member.getHealth());
            getWorldData().set(memberPath+".hunger", member.getHunger());
            getWorldData().set(memberPath+".lvl", member.getLvl());
            getWorldData().set(memberPath+".exp", member.getExp());
            getWorldData().set(memberPath+".sneaking", member.getSneaking());

            saveItemStack(getWorldData(), memberPath+".cursorItem", member.getCursorItem());
            getWorldData().set(memberPath+".bed", member.getBed());
            saveInventory(getWorldData(), memberPath+".inv", member.getInv());
            getWorldData().set(memberPath+".version", SkyblockWorldMember.dataVersion);
//            getWorldData().set(memberPath+".inv", member.getInv());
        }
        saveWorldData();
    }

    @Nullable
    public SkyblockWorld loadSkyblockWorldFromFile(String path) {
        assert getWorldData() != null;
        String ownerUUID = getWorldData().getString(path+".owner");
        byte worldId = (byte) getWorldData().getInt(path+".worldid");
        byte gamemodeId = (byte) getWorldData().getInt(path+".gamemode");
        byte worldiconId = (byte) getWorldData().getInt(path+".icon");
        boolean completedtutorial = getWorldData().getBoolean(path+".completedtutorial");
        boolean completed = getWorldData().getBoolean(path+".completed");
        String worldName = getWorldData().getString(path+".name");

        SkyblockPlayer owner = plugin.skyblockData.getSkyblockPlayerFromUUID(ownerUUID);
        if (owner == null) return null;
        Gamemode gamemode = Gamemode.getGamemodeById(gamemodeId);
        if (gamemode == null) return null;

        WorldIcon worldIcon = WorldIcon.getWorldIconById(worldiconId);
        if (worldIcon == null) worldIcon = WorldIcon.grassBlock; // worldIcon didn't save / got corrupted lol

        SkyblockWorld skyblockWorld = new SkyblockWorld(owner, worldId, gamemode);

        skyblockWorld.setCompleted(completed);
        skyblockWorld.setCompletedTutorial(completedtutorial);
        skyblockWorld.setName(worldName);
        skyblockWorld.setWorldIcon(worldIcon);

        ConfigurationSection sec = getWorldData().getConfigurationSection(path+".members");
        if (sec != null) {
            for (String memberUUID : sec.getKeys(false)) {
                String memberPath = path+".members."+memberUUID;
                SkyblockWorldMember skyblockWorldMember = new SkyblockWorldMember(memberUUID);

                byte permissionLevel = (byte) getWorldData().getInt(memberPath+".permissionlevel");
                int joincount = getWorldData().getInt(memberPath+".joincount");

                double health = getWorldData().getDouble(memberPath+".health");
                int hunger = getWorldData().getInt(memberPath+".hunger");
                float exp = (float) getWorldData().getDouble(memberPath+".exp");
                int lvl = getWorldData().getInt(memberPath+".lvl");
                boolean sneaking = getWorldData().getBoolean(memberPath+".sneaking");
                Location bed = getWorldData().getLocation(memberPath+".bed");
                ItemStack cursorItem = createItemStackFromConfiguration(getWorldData(), memberPath+".cursorItem");
                ItemStack[] inv = createInventoryFromConfiguration(getWorldData(), memberPath+".inv");

                skyblockWorldMember.setJoinCount(joincount);
                skyblockWorldMember.setPermissionLevel(permissionLevel);

                skyblockWorldMember.setBed(bed);
                skyblockWorldMember.setCursorItem(cursorItem);
                skyblockWorldMember.setInv(inv);
                skyblockWorldMember.setHealth(health);
                skyblockWorldMember.setHunger(hunger);
                skyblockWorldMember.setExp(exp);
                skyblockWorldMember.setLvl(lvl);
                skyblockWorldMember.setSneaking(sneaking);

                skyblockWorld.registerMember(skyblockWorldMember);
            }
        }

        return skyblockWorld;
    }

    public void saveInventory(FileConfiguration fc, String path, ItemStack[] inv) {
        if (inv == null) return;
        if (inv.length < 1) return;
        for (int i = 0; i < inv.length; i++) {
            ItemStack itemToSave = Arrays.stream(inv).toList().get(i);
            if (itemToSave != null) saveItemStack(fc, path+"."+String.valueOf(i), itemToSave);
        }
    }

    public void saveItemStack(FileConfiguration fc, String path, ItemStack item) {
        if (item == null) return;
        fc.set(path+".type", item.getType().name());
        fc.set(path+".amount", item.getAmount());

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.hasDisplayName()) fc.set(path+".meta.displayname", meta.getDisplayName());
        else fc.set(path+".meta.displayname", null);
        if (meta.hasCustomModelData()) fc.set(path+".meta.custommodeldata", meta.getCustomModelData());
        else fc.set(path+".meta.custommodeldata", null);

        fc.set(path+".meta.damaged", null);
        Damageable damagableMeta = (Damageable) meta;
        if (damagableMeta.hasDamage()) {
            fc.set(path + ".meta.damaged", damagableMeta.getDamage());
        }

        fc.set(path+".meta.flags", null);
        for (int i = 0; i < meta.getItemFlags().size(); i++) {
            ItemFlag itemf = meta.getItemFlags().stream().toList().get(i);
            fc.set(path+".meta.flags."+itemf.name(), true);
        }

        StringBuilder lores = new StringBuilder();
        if (meta.hasLore()) {
            for (int i = 0; i < meta.getLore().size(); i++) {
                String loreString = meta.getLore().get(i);
                if (i == 0) lores = new StringBuilder(loreString);
                else lores.append("LORESPLIT").append(loreString);
//                fc.set(path + ".meta.lore." + String.valueOf(i), loreString);
            }
        }
        if (!lores.isEmpty()) fc.set(path+".meta.lore", lores.toString());
        else fc.set(path+".meta.lore", null);

        fc.set(path+".meta.enchants", null);
        if (meta.hasEnchants()) {
            for (int i = 0; i < meta.getEnchants().size(); i++) {
                Enchantment iteme = meta.getEnchants().keySet().stream().toList().get(i);
                fc.set(path + ".meta.enchants." + iteme.getKey().getKey(), meta.getEnchantLevel(iteme));
            }
        }

        fc.set(path+".meta.modifiers", null);
        if (meta.hasAttributeModifiers()) {
            for (int i = 0; i < meta.getAttributeModifiers().size(); i++) {
                Attribute itema = meta.getAttributeModifiers().keySet().stream().toList().get(i);
                fc.set(path + ".meta.modifiers." + itema.getKey().getKey(), true);
            }
        }
    }

    @Nullable
    public ItemStack[] createInventoryFromConfiguration(FileConfiguration fc, String path) {
        ItemStack[] inv = new ItemStack[41];
        ConfigurationSection sec = fc.getConfigurationSection(path);
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                Array.set(inv, Integer.parseInt(key), createItemStackFromConfiguration(fc, path+"."+key));
            }
        }
        return inv;
    }

    @Nullable
    public ItemStack createItemStackFromConfiguration(FileConfiguration fc, String path) {
        String materialName = fc.getString(path+".type");
        if (materialName == null) return null;
        Material itemType = Material.matchMaterial(materialName, false);
        if (itemType == null) return null;
        int itemAmount = fc.getInt(path+".amount");

        ItemStack item = new ItemStack(itemType, itemAmount);
        ItemMeta meta = item.getItemMeta();

        int damaged = fc.getInt(path+".meta.damaged");
        if (damaged > 0) {

            Damageable damageableMeta = (Damageable) meta;
            damageableMeta.setDamage(damaged);
        }

        int customModelData = fc.getInt(path+".meta.custommodeldata");
        if (customModelData > 0) meta.setCustomModelData(customModelData);

        String displayName = fc.getString(path+".meta.displayname");
        if (displayName != null) meta.setDisplayName(displayName);

        String loresString = fc.getString(path+".meta.lore");
        if (loresString != null) {
            List<String> lores = List.of(loresString.split("LORESPLIT"));
            if (!lores.isEmpty()) meta.setLore(lores);
        }

        ConfigurationSection sec = fc.getConfigurationSection(path+".meta.enchants");
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(key));
                int power = fc.getInt(path + ".meta.enchants." + key);
                meta.addEnchant(enchant, power, false);
            }
        }

        ConfigurationSection secflags = fc.getConfigurationSection(path+".meta.flags");
        if (secflags != null) {
            for (String key : secflags.getKeys(false)) {
                meta.addItemFlags(
                        ItemFlag.valueOf(key)
                );
            }
        }

        item.setItemMeta(meta);

        return item;
    }

    public void deleteWorldData(String realid) {
        String path = "skyblockworlds."+realid;
        assert getWorldData() != null;
        getWorldData().set(path, null);
        saveWorldData();
    }

    public void deleteWorldData(SkyblockWorld skyblockWorld) {
        deleteWorldData(skyblockWorld.getRealId());
    }

    // end of worlddata functions

    private void initialisePlayerData() {
        playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            playerDataFile.getParentFile().mkdirs();
            plugin.saveResource("playerdata.yml", false);
        }

        playerData = new YamlConfiguration();
        try {
            playerData.load(playerDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerData() {
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialiseWorldData() {
        worldDataFile = new File(plugin.getDataFolder(), "worlddata.yml");
        if (!worldDataFile.exists()) {
            worldDataFile.getParentFile().mkdirs();
            plugin.saveResource("worlddata.yml", false);
        }

        worldData = new YamlConfiguration();
        try {
            worldData.load(worldDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void saveWorldData() {
        try {
            worldData.save(worldDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
