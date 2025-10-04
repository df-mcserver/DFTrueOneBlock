package uk.co.nikodem.dFTrueOneBlock.Data;

import javax.annotation.Nullable;

public class SkyblockPlayer {
    public static final double dataVersion = 1.0;
    private final String uuid;
    private String world1;
    private String world2;
    private String world3;

    public SkyblockPlayer(String uuid) {
        this.uuid = uuid;
    }

    // get() functions

    public String getUniqueId() {
        return this.uuid;
    }

    @Nullable
    public String getWorld1() {
        return this.world1;
    }

    @Nullable
    public String getWorld2() {
        return this.world2;
    }

    @Nullable
    public String getWorld3() {
        return this.world3;
    }

    // set() functions

    public void setWorld1(String world) {
        this.world1 = world;
    }

    public void setWorld1(SkyblockWorld world) {
        this.world1 = world.getRealId();
    }

    public void removeWorld1() {
        this.world1 = null;
    }

    public void setWorld2(String world) {
        this.world2 = world;
    }

    public void setWorld2(SkyblockWorld world) {
        this.world2 = world.getRealId();
    }

    public void removeWorld2() {
        this.world2 = null;
    }

    public void setWorld3(String world) {
        this.world3 = world;
    }

    public void setWorld3(SkyblockWorld world) {
        this.world3 = world.getRealId();
    }

    public void removeWorld3() {
        this.world3 = null;
    }

    public void deleteWorld1() {
        this.world1 = null;
    }

    public void deleteWorld2() {
        this.world2 = null;
    }

    public void deleteWorld3() {
        this.world3 = null;
    }
}
