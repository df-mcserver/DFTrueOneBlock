package uk.co.nikodem.dFTrueOneBlock.Data;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class SkyblockWorldMember {
    public static final double dataVersion = 1.0;
    private final String uuid;
    private byte permissionLevel;
    // owner = 127
    // admin = 100
    // member = 1

    private int joinCount;
    private int hunger;
    private int lvl;
    private double health;
    private boolean sneaking;
    private float exp;
    private ItemStack cursorItem;
    private Location bed;
    private ItemStack[] inv;

    public SkyblockWorldMember(String uuid) {
        this.uuid = uuid;
        this.permissionLevel = 0;
        this.joinCount = 0;

        this.health = 20;
        this.hunger = 20;
        this.sneaking = false;
    }

    // player handling

    public void loadMemberToPlayer(Player plr) {
        plr.getInventory().setContents(inv);
        plr.setHealth(health);
        plr.setFoodLevel(hunger);
        plr.setExp(exp);
        plr.setLevel(lvl);
        plr.setItemOnCursor(cursorItem);
        plr.setBedSpawnLocation(bed);
        plr.setSneaking(sneaking);
    }

    public void savePlayerToMember(Player plr) {
        inv = plr.getInventory().getContents();
        health = plr.getHealth();
        hunger = plr.getFoodLevel();
        lvl = plr.getLevel();
        exp = plr.getExp();
        cursorItem = plr.getItemOnCursor();
        bed = plr.getBedSpawnLocation();
        sneaking = plr.isSneaking();
    }

    // get() functions

    public byte getPermissionLevel() {
        return this.permissionLevel;
    }

    public int getJoinCount() {
        return this.joinCount;
    }

    public double getHealth() {
        return this.health;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getLvl() {
        return this.lvl;
    }

    public float getExp() {
        return this.exp;
    }

    public boolean getSneaking() {
        return this.sneaking;
    }

    public ItemStack getCursorItem() {
        return this.cursorItem;
    }

    public Location getBed() {
        return this.bed;
    }

    public ItemStack[] getInv() {
        return this.inv;
    }

    public boolean isOwner() {
        if (permissionLevel == 127) {
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return permissionLevel == 100;
    }

    public boolean hasAdminPrivilleges() {
        if (permissionLevel >= 100) {
            return true;
        }
        return false;
    }

    public String getUniqueId() {
        return this.uuid;
    }

    // set() functions

    public void setToAdmin() {
        this.permissionLevel = 100;
    }

    public void setToOwner() {
        this.permissionLevel = 127;
    }

    public void setToMember() {
        this.permissionLevel = 1;
    }

    public void incrementJoinCount() {
        this.joinCount += 1;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public void setPermissionLevel(byte permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public void setBed(Location bed) {
        this.bed = bed;
    }

    public void setInv(ItemStack[] inv) {
        this.inv = inv;
    }

    public void setCursorItem(ItemStack cursorItem) {
        this.cursorItem = cursorItem;
    }
}
