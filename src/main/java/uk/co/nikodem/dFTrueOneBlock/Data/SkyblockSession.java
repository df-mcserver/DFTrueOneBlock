package uk.co.nikodem.dFTrueOneBlock.Data;

public class SkyblockSession {
    private final String owner;
    private final String worldname;
    private final String worldrealid;
    private final WorldIcon icon;

    public SkyblockSession(SkyblockPlayer owner, SkyblockWorld world) {
        this.owner = owner.getUniqueId();
        this.worldname = world.getName();
        this.worldrealid = world.getRealId();
        this.icon = world.getWorldIcon();
    }

    public String getOwner() {
        return this.owner;
    }

    public String getName() {
        return this.worldname;
    }

    public String getWorldrealid() {
        return this.worldrealid;
    }

    public WorldIcon getIcon() {
        return this.icon;
    }
}
