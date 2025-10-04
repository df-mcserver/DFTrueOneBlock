package uk.co.nikodem.dFTrueOneBlock.Data;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkyblockWorld {
    public static final double dataVersion = 1.0;
    private final String uuid;
    private final List<SkyblockWorldMember> members = new ArrayList<>();
    private final SkyblockWorldMember owner;
    private final Gamemode gamemode;

    private boolean completedTutorial;
    private boolean completed;

    private String name;
    private WorldIcon worldIcon;

    private byte worldId;

    public SkyblockWorld(@NotNull SkyblockPlayer plr, byte worldid, Gamemode gamemode) {
        this.uuid = plr.getUniqueId();
        this.worldId = worldid;
        this.gamemode = gamemode;
        this.completed = false;
        this.worldIcon = WorldIcon.grassBlock;
        this.completedTutorial = true;

        if (gamemode == Gamemode.hardcore || gamemode == Gamemode.test) {
            this.completedTutorial = false;
        }

        SkyblockWorldMember owner = new SkyblockWorldMember(plr.getUniqueId());
        owner.setToOwner();
        this.owner = owner;
    }

    // player handling

    public void OnPlayerJoin(PlayerJoinEvent event) {
        Player plr = event.getPlayer();
        SkyblockWorldMember member = getMember(String.valueOf(plr.getUniqueId()));
        if (member == null) {
            plr.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&2You aren't a member of this world!"));
            return;
        }
        plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2Welcome to Skyblock!\n&3There's not much to do here, find something out!"));
        member.loadMemberToPlayer(plr);
        member.incrementJoinCount();
    }

    public void OnPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS));
    }

    public void MakePlayerJoin(Player plr) {

    }

    // get() functions

    public String getRealId() {
        return this.uuid+"--"+String.valueOf(this.worldId);
    }

    public String getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        if (this.name == null) return "New World";
        return this.name;
    }

    public byte getWorldId() {
        return this.worldId;
    }

    public Gamemode getGamemode() {
        return this.gamemode;
    }

    public SkyblockWorldMember getOwner() {
        return this.owner;
    }

    public boolean getTutorialCompletion() {
        return this.completedTutorial;
    }

    public boolean getWorldCompleted() {
        return this.completed;
    }

    public List<SkyblockWorldMember> getMembers() {
        return this.members;
    }

    public WorldIcon getWorldIcon() {
        if (this.worldIcon == null) return WorldIcon.grassBlock;
        return this.worldIcon;
    }

    // other getting functions

    public boolean PlayerIsInSkyblockWorld(Player plr) {
        World wlrd = plr.getWorld();
        return wlrd.getName().equals(getRealId());
    }

    @Nullable
    public SkyblockWorldMember getMember(String uuid) {
        if (getMembers().isEmpty()) return null;
        SkyblockWorldMember result = null;
        for (int i = 0; i < getMembers().size(); i++) {
            SkyblockWorldMember member = getMembers().get(i);
            if (Objects.equals(member.getUniqueId(), uuid)) {
                result = member;
                break;
            }
        }
        return result;
    }

    public boolean memberExists(String uuid) {
        if (getMembers().isEmpty()) return false;
        boolean result = false;
        for (int i = 0; i < getMembers().size(); i++) {
            SkyblockWorldMember member = getMembers().get(i);
            if (Objects.equals(member.getUniqueId(), uuid)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean memberIsOwner(SkyblockWorldMember member) {
        return Objects.equals(member.getUniqueId(), owner.getUniqueId());
    }

    public boolean memberIsOwner(String uuid) {
        SkyblockWorldMember member = getMember(uuid);
        assert member != null;
        return memberIsOwner(member);
    }

    public boolean memberExists(SkyblockWorldMember member) {
        return memberExists(member.getUniqueId());
    }

    // player set() functions

    public byte playerSetName(@NotNull SkyblockWorldMember member, String name) {
        if (!member.isOwner()) {
            return -1; // error -1, player is not owner
        }
        if (name.length() > 30) {
            return -2; // error -2, name is too long
        }
        setName(name);
        return 0; // success
    }

    public byte playerSetIcon(@NotNull SkyblockWorldMember member, WorldIcon icon) {
        if (!member.isOwner()) {
            return -1; // error -1, player is not owner
        }
        setWorldIcon(icon);
        return 0; // success
    }

    public byte playerAddMember(@NotNull SkyblockWorldMember member, String newMemberUUID) {
        if (Objects.equals(member.getUniqueId(), newMemberUUID)) {
            return -1; // error -1, you cannot add yourself
        }
        if (!member.hasAdminPrivilleges()) {
            return -2; // error -2, player is not admin or higher
        }
        if (memberExists(newMemberUUID)) {
            return -3; // error -3, player already exists
        }
        if (memberIsOwner(newMemberUUID)) {
            return -3; // error -3, player already exists
        }
        if (!completedTutorial) {
            return -4; // error -4, tutorial must be completed
        }
        registerMember(createNewMember(newMemberUUID));
        return 0; // success
    }

    public byte playerRemoveMember(@NotNull SkyblockWorldMember member) {
        if (!member.hasAdminPrivilleges()) {
            return -1; // error -1, player is not admin or higher
        }
        if (!completedTutorial) {
            return -2; // error -2, tutorial must be completed
        }
        removeMember(member);
        return 0; // success
    }

    // set() functions

    public void setName(String name) {
        this.name = name;
    }

    public void setWorldIcon(WorldIcon icon) {
        this.worldIcon = icon;
    }

    public void registerMember(@NotNull SkyblockWorldMember member) {
        this.members.add(member);
    }

    public void removeMember(@NotNull SkyblockWorldMember member) {
        this.members.remove(member);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCompletedTutorial(boolean completed) {
        this.completedTutorial = completed;
    }

    public void setWorldId(byte worldId) {
        this.worldId = worldId;
    }

    // other setting functions

    public SkyblockWorldMember createNewMember(String uuid) {
        SkyblockWorldMember member = new SkyblockWorldMember(uuid);
        member.setToMember();

        return member;
    }

    // saving functions
}
