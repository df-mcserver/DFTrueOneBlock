package uk.co.nikodem.dFTrueOneBlock.Menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;
import uk.co.nikodem.dFTrueOneBlock.Data.*;

import static uk.co.nikodem.dFTrueOneBlock.Menus.CreateWorldFunctions.*;
import static uk.co.nikodem.dFTrueOneBlock.Menus.GuiHelper.*;

public class GuiHandler implements Listener {
    private final DFTrueOneBlock plugin;

    public GuiHandler(DFTrueOneBlock plugin) {
        this.plugin = plugin;
    }

    public void PostViewChanges(InventoryView view) {
        Player plr = (Player) view.getPlayer();
        String id = view.getOriginalTitle();

        if (id.equals("mm-mm")) {
            view.setTitle("Main Menu");
            view.getTopInventory().setItem(InventoryLocation(4, 2), CustomItem(Material.GRASS_BLOCK, "&3World saves"));
            view.getTopInventory().setItem(InventoryLocation(6, 2), CustomItem(Material.BEACON, "&3Join Friends"));
            CreateGuideInInventory(view, "Main Menu", "You can either join your own skyblock worlds, or join someone elses!", "Select \"World saves\" to join one of your worlds", "Select \"Join Friends\" to join another player's worlds.");
        } else if (id.equals("mm-worlds")) {
            view.setTitle("Worlds");
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Back"));

            SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);

            boolean WorldSpawned = false;

            if (skyblockPlayer.getWorld1() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld1(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(3, 1), CustomItem(Material.STRUCTURE_VOID, "&3Click to create!"));
            } else {
                String worldrealid = skyblockPlayer.getWorld1();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(3, 1), CustomWorldItem(world));
                view.getTopInventory().setItem(InventoryLocation(3, 2), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Edit"));
                WorldSpawned = true;
            }

            if (skyblockPlayer.getWorld2() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld2(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.STRUCTURE_VOID, "&3Click to create!"));
            } else {
                String worldrealid = skyblockPlayer.getWorld2();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(5, 1), CustomWorldItem(world));
                view.getTopInventory().setItem(InventoryLocation(5, 2), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Edit"));
                WorldSpawned = true;
            }

            if (skyblockPlayer.getWorld3() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld3(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(7, 1), CustomItem(Material.STRUCTURE_VOID, "&3Click to create!"));
            } else {
                String worldrealid = skyblockPlayer.getWorld3();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(7, 1), CustomWorldItem(world));
                view.getTopInventory().setItem(InventoryLocation(7, 2), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Edit"));
                WorldSpawned = true;
            }

            if (WorldSpawned) {
                view.getTopInventory().setItem(InventoryLocation(9, 5), CustomItem(Material.ARROW, "&3Move worlds"));
            }

            CreateGuideInInventory(view, "Worlds", "You have 3 world slots for your skyblock worlds.", "A world can have different gamemodes, members, names etc.", "Click on a world to join it, or to create a new world in that slot.", "Shift click (or quick move) a world between slots.", "You can click the arrow in the bottom right corner to move a world between slots.");
        } else if (id.startsWith("create-main-world")) {
            String worldNumber = id.substring(id.length() - 1);
            view.setTitle("Creating world "+worldNumber);
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Cancel"));
            view.getTopInventory().setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Create"));

            view.getTopInventory().setItem(InventoryLocation(1, 3), CustomItem(Material.ARROW, "Previous world icon"));
            // icon handled later
            view.getTopInventory().setItem(InventoryLocation(3, 3), CustomItem(Material.ARROW, "Next world icon"));

            view.getTopInventory().setItem(InventoryLocation(7, 3), CustomItem(Material.ARROW, "Previous gamemode"));
            // gamemode handled later
            view.getTopInventory().setItem(InventoryLocation(9, 3), CustomItem(Material.ARROW, "Next gamemode"));

            List<String> worldNameLore = new ArrayList<>();
            worldNameLore.add("Click to randomise name!");

            if (plr.hasMetadata("customNameChatResponse")) {
                List<MetadataValue> values = plr.getMetadata("customNameChatResponse");
                String message = (!values.isEmpty()) ? values.get(0).asString() : null;
                view.getTopInventory().setItem(InventoryLocation(5, 0), CustomItem(Material.OAK_SIGN, message, worldNameLore));
                plr.removeMetadata("customNameChatResponse", plugin);
            } else {
                view.getTopInventory().setItem(InventoryLocation(5, 0), CustomItem(Material.OAK_SIGN, "New World", worldNameLore));
            }

            if (plr.hasMetadata("editMenuSelectedWI")) {
                List<MetadataValue> values2 = plr.getMetadata("editMenuSelectedWI");
                Integer idWorldIcon = (!values2.isEmpty()) ? values2.get(0).asInt() : null;
                plr.removeMetadata("editMenuSelectedWI", plugin);
                if (idWorldIcon != null) view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon(getNextWorldIcon(idWorldIcon)));
            } else {
                view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon());
            }

            if (plr.hasMetadata("createMenuSelectedGM")) {
                List<MetadataValue> values2 = plr.getMetadata("createMenuSelectedGM");
                Integer idGameMode = (!values2.isEmpty()) ? values2.get(0).asInt() : null;
                plr.removeMetadata("createMenuSelectedGM", plugin);
                if (idGameMode != null) view.getTopInventory().setItem(InventoryLocation(8, 3), createGamemodeItem(getNextGamemode(idGameMode)));
            } else {
                view.getTopInventory().setItem(InventoryLocation(8, 3), createGamemodeItem());
            }

            List<String> customNameLoreCreate = new ArrayList<>();
            customNameLoreCreate.add("You will have to input the world name in the chat.");
            view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&3Input a custom name", customNameLoreCreate));
            CreateGuideInInventory(view, "Creating world", "When creating a world, you can choose a world icon, name and gamemode.", "The world icon and name can be changed later, but the gamemode is pernament.", "You can click on the displayed world icon or gamemode to open a selection menu.");
        } else if (id.startsWith("delete-world")) {
            playWarningSound(plr);
            String worldNumberDW = id.substring(id.length() - 1);
            view.setTitle("Delete confirmation for world "+worldNumberDW);
            SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(plr.getUniqueId()+"--"+worldNumberDW);
            List<String> barrierLore = new ArrayList<>();
            List<String> svLore = new ArrayList<>();
            List<String> signLore = new ArrayList<>();
            barrierLore.add(ChatColor.translateAlternateColorCodes('&', "&4The world will be DELETED FOREVER. This is IRREVERSABLE."));
            svLore.add(ChatColor.translateAlternateColorCodes('&', "&2Nothing will happen, and your world is kept safe."));
            if (world != null) {
                signLore.add("Are you sure you want to DELETE world "+worldNumberDW+", named \""+world.getName()+"\"?");
                signLore.add("The world will be UNRECOVERABLE if you delete the world!");
                signLore.add("It is advised you check the world first to see if you want to delete it.");
            } else {
                signLore.add("world was not found lol - ignore");
            }
            view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.OAK_SIGN, "&4Deletion", signLore));
            view.getTopInventory().setItem(InventoryLocation(4, 4), CustomItem(Material.RED_DYE, "&4Delete", barrierLore));
            view.getTopInventory().setItem(InventoryLocation(6, 4), CustomItem(Material.BARRIER, "&2Cancel", svLore));
            CreateGuideInInventory(view, "Delete world","Are you sure you want to delete your world?", "If you press the red dye, the world will be pernamently deleted.", "If you press the barrier block, nothing will happen and your world will be safe.");
        } else if (id.startsWith("edit-world")) {
            String worldNumberEW = id.substring(id.length() - 1);

            SkyblockPlayer skyblockPlayerEW = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);
            SkyblockWorld skyblockWorldEW;

            String worldrealid = "";
            if (skyblockPlayerEW.getWorld1() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld1(), true) != null && Integer.parseInt(worldNumberEW) == 1) {
                // world 1
                worldrealid = skyblockPlayerEW.getWorld1();
            } else if (skyblockPlayerEW.getWorld2() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld2(), true) != null && Integer.parseInt(worldNumberEW) == 2) {
                // world 2
                worldrealid = skyblockPlayerEW.getWorld2();
            } else if (skyblockPlayerEW.getWorld3() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld3(), true) != null && Integer.parseInt(worldNumberEW) == 3) {
                // world 3
                worldrealid = skyblockPlayerEW.getWorld3();
            } else {
                view.close();
            }
            skyblockWorldEW = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);

            if (skyblockWorldEW == null) {
                view.close();
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4GUI has encountered an error! Tried to access world that isn't loaded properly!"));
                return;
            }

            Gamemode gm = skyblockWorldEW.getGamemode();

            view.setTitle("Editing world "+worldNumberEW);
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Cancel"));

            view.getTopInventory().setItem(InventoryLocation(1, 3), CustomItem(Material.ARROW, "Previous world icon"));
            // world icon done later
            view.getTopInventory().setItem(InventoryLocation(3, 3), CustomItem(Material.ARROW, "Next world icon"));

            view.getTopInventory().setItem(InventoryLocation(7, 3), CustomItem(Material.BARRIER, "&4Gamemode cannot be edited!"));
            view.getTopInventory().setItem(InventoryLocation(8, 3), createGamemodeItem(gm));
            view.getTopInventory().setItem(InventoryLocation(9, 3), CustomItem(Material.BARRIER, "&4Gamemode cannot be edited!"));

            view.getTopInventory().setItem(InventoryLocation(9, 5), CustomItem(Material.RED_DYE, "Delete world"));

            List<String> newWorldNameLore = new ArrayList<>();
            newWorldNameLore.add("Click to randomise name!");

            if (plr.hasMetadata("customNameChatResponse")) {
                List<MetadataValue> values = plr.getMetadata("customNameChatResponse");
                String message = (!values.isEmpty()) ? values.get(0).asString() : null;
                view.getTopInventory().setItem(InventoryLocation(5, 0), CustomItem(Material.OAK_SIGN, message, newWorldNameLore));
                plr.removeMetadata("customNameChatResponse", plugin);
                if (message != null) {
                    if (!Objects.equals(message, skyblockWorldEW.getName())) {
                        view.getTopInventory().setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Confirm edits"));
                    }
                } else {
                    view.getTopInventory().setItem(InventoryLocation(5, 0), CustomItem(Material.OAK_SIGN, skyblockWorldEW.getName(), newWorldNameLore));
                }
            } else {
                view.getTopInventory().setItem(InventoryLocation(5, 0), CustomItem(Material.OAK_SIGN, skyblockWorldEW.getName(), newWorldNameLore));
            }

            if (plr.hasMetadata("editMenuSelectedWI")) {
                List<MetadataValue> values2 = plr.getMetadata("editMenuSelectedWI");
                Integer idWorldIcon = (!values2.isEmpty()) ? values2.get(0).asInt() : null;
                plr.removeMetadata("editMenuSelectedWI", plugin);
                if (idWorldIcon == null) {
                    view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon(skyblockWorldEW.getWorldIcon()));
                } else if (idWorldIcon < WorldIcon.worldicons.length) {
                    WorldIcon wi = getNextWorldIcon(idWorldIcon);
                    view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon(wi));
                    if (wi != skyblockWorldEW.getWorldIcon()) {
                        view.getTopInventory().setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Confirm edits"));
                    }
                } else {
                    view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon(skyblockWorldEW.getWorldIcon()));
                }
            } else {
                view.getTopInventory().setItem(InventoryLocation(2, 3), createWorldIcon(skyblockWorldEW.getWorldIcon()));
            }

            List<String> customNameLore = new ArrayList<>();
            customNameLore.add("You will have to input the world name in the chat.");
            view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&3Input a custom name", customNameLore));
            CreateGuideInInventory(view, "Editing world", "When editing a world, you can change the name and world icon.", "The gamemode cannot be changed, as it is pernament.", "You can click on the displayed world icon or gamemode to open a selection menu.", "If you click on the red dye in the bottom right corner, you will be asked if you want to delete this world.");
        } else if (id.startsWith("icon-selector")) {
            int page = Integer.parseInt(id.substring(id.length() - 1));

            view.setTitle("Select a world icon");
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Back"));

            int maxPerPage = 9*5;
            int start = (page-1)*maxPerPage;

            for (int i = 0; i < maxPerPage; i++) {
                if (i+start <= WorldIcon.worldicons.length-1) {
                    WorldIcon wi = WorldIcon.worldicons[i+start];
                    view.getTopInventory().setItem(i, createWorldIcon(wi));
                } else {
                    view.setItem(i, null);
                }
            }

            if (page > 1) view.getTopInventory().setItem(InventoryLocation(4, 5), CustomItem(Material.ARROW, "Previous Page"));
            if ((maxPerPage * page) < WorldIcon.worldicons.length) view.getTopInventory().setItem(InventoryLocation(6, 5), CustomItem(Material.ARROW, "Next Page"));
            CreateGuideInInventory(view, "Icon selector", "You can choose between "+WorldIcon.worldicons.length+" icons.");
        } else if (id.startsWith("gamemode-selector")) {
            int pageGM = Integer.parseInt(id.substring(id.length() - 1));

            view.setTitle("Select a gamemode");
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Back"));

            int maxPerPageGM = 9*5;
            int startGM = (pageGM-1)* maxPerPageGM;

            for (int i = 0; i < maxPerPageGM; i++) {
                if (i+startGM <= Gamemode.gamemodes.length-1) {
                    Gamemode gm = Gamemode.gamemodes[i+startGM];
                    view.getTopInventory().setItem(i, createGamemodeItem(gm));
                } else {
                    view.setItem(i, null);
                }
            }

            if (pageGM > 1) view.getTopInventory().setItem(InventoryLocation(4, 5), CustomItem(Material.ARROW, "Previous Page"));
            if ((maxPerPageGM * pageGM) < Gamemode.gamemodes.length) view.getTopInventory().setItem(InventoryLocation(6, 5), CustomItem(Material.ARROW, "Next Page"));
            CreateGuideInInventory(view, "Gamemode selector", "You can choose between "+Gamemode.gamemodes.length+" gamemodes.");
        } else if (id.startsWith("mm-join")) {
            int pageMJ = Integer.parseInt(id.substring(id.length() - 1));

            view.setTitle("Join a session");
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Back"));

            int maxPerPageMJ = 9*5;
            int startMJ = (pageMJ-1)* maxPerPageMJ;

            for (int i = 0; i < maxPerPageMJ; i++) {
                if (i+startMJ <= plugin.skyblockData.skyblockSessions.size()-1) {
                    SkyblockSession session = plugin.skyblockData.skyblockSessions.get(i+startMJ);
                    view.getTopInventory().setItem(i, createSessionItem(session, i));
                } else {
                    view.setItem(i, null);
                }
            }

            if (plugin.skyblockData.skyblockSessions.isEmpty()) {
                List<String> loresInfo = new ArrayList<>();
                loresInfo.add("No sessions found!");
                ItemStack info = CustomItem(Material.OAK_SIGN, "&4Error", loresInfo);
                view.getTopInventory().setItem(InventoryLocation(5, 2), info);
            } else {
                if (pageMJ > 1) view.getTopInventory().setItem(InventoryLocation(4, 5), CustomItem(Material.ARROW, "Previous Page"));
                if ((maxPerPageMJ * pageMJ) < Gamemode.gamemodes.length) view.getTopInventory().setItem(InventoryLocation(6, 5), CustomItem(Material.ARROW, "Next Page"));
            }
            view.getTopInventory().setItem(InventoryLocation(9, 5), CustomItem(Material.NETHER_STAR, "&3Refresh"));
            CreateGuideInInventory(view, "Joining a session", "If someone has added you as a member to their skyblock world, you can join them here.", "If the host has enabled the feature, you can join the world if they're not online. By default, this is disabled.");
        } else if (id.startsWith("mm-moveworld")) {
            playWarningSound(plr);
            int movingWorld = Integer.parseInt(id.substring(id.length() - 1));
            view.setTitle("Moving world "+movingWorld);
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Cancel"));

            SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);

            if (skyblockPlayer.getWorld1() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld1(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(3, 1), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Move world here"));
            } else {
                String worldrealid = skyblockPlayer.getWorld1();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                if (movingWorld != 1) {
                    view.getTopInventory().setItem(InventoryLocation(3, 1), CustomWorldItem(world));
                } else {
                    view.getTopInventory().setItem(InventoryLocation(3, 1), CustomItem(Material.BARRIER, "&6Current world slot"));
                    view.getTopInventory().setItem(InventoryLocation(5, 3), CustomWorldItem(world));
                }
            }

            if (skyblockPlayer.getWorld2() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld2(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Move world here"));
            } else {
                String worldrealid = skyblockPlayer.getWorld2();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                if (movingWorld != 2) {
                    view.getTopInventory().setItem(InventoryLocation(5, 1), CustomWorldItem(world));
                } else {
                    view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.BARRIER, "&6Current world slot"));
                    view.getTopInventory().setItem(InventoryLocation(5, 3), CustomWorldItem(world));
                }
            }

            if (skyblockPlayer.getWorld3() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld3(), true) == null) {
                // blank space
                view.getTopInventory().setItem(InventoryLocation(7, 1), CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&6Move world here"));
            } else {
                String worldrealid = skyblockPlayer.getWorld3();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                if (movingWorld != 3) {
                    view.getTopInventory().setItem(InventoryLocation(7, 1), CustomWorldItem(world));
                } else {
                    view.getTopInventory().setItem(InventoryLocation(7, 1), CustomItem(Material.BARRIER, "&6Current world slot"));
                    view.getTopInventory().setItem(InventoryLocation(5, 3), CustomWorldItem(world));
                }
            }
            CreateGuideInInventory(view, "Moving world", "You can move the currently selected world from slot "+movingWorld+" to whichever you please.", "You can swap 2 worlds by clicking on that world.", "This doesn't affect anything but where the world is placed in this menu.");
        } else if (id.equals("select-to-move-world")) {
            view.setTitle("Select a world to move.");
            view.getTopInventory().setItem(InventoryLocation(5, 5), CustomItem(Material.BARRIER, "&4Cancel"));

            SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);

            if (skyblockPlayer.getWorld1() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld1(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(3, 1), CustomItem(Material.BARRIER, "&3Not a world"));
            } else {
                String worldrealid = skyblockPlayer.getWorld1();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(3, 1), CustomWorldItem(world));
            }

            if (skyblockPlayer.getWorld2() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld2(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(5, 1), CustomItem(Material.BARRIER, "&3Not a world"));
            } else {
                String worldrealid = skyblockPlayer.getWorld2();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(5, 1), CustomWorldItem(world));
            }

            if (skyblockPlayer.getWorld3() == null || plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayer.getWorld3(), true) == null) {
                view.getTopInventory().setItem(InventoryLocation(7, 1), CustomItem(Material.BARRIER, "&3Not a world"));
            } else {
                String worldrealid = skyblockPlayer.getWorld3();
                SkyblockWorld world = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                view.getTopInventory().setItem(InventoryLocation(7, 1), CustomWorldItem(world));
            }

            CreateGuideInInventory(view, "Selecting a world to move", "You can click on a world to move it to another slot.", "You can swap worlds around using this feature.", "You can shift click (or quick move) a world to instantly select that world next time!");
        } else {
            view.close();
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4GUI has encountered an error! Tried to access undefined menu "+view.getOriginalTitle()+"!"));
        }
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
        Player plr = (Player) event.getWhoClicked();
        InventoryView v = event.getView();
        String id = v.getOriginalTitle();
        int slot = event.getSlot();
        ItemStack itemInSlot = v.getItem(slot);
        if (itemInSlot == null) return;
        Material slotItem = itemInSlot.getType();

        if (id.equals("mm-mm")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(4, 2)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-worlds");
            } else if (slot == InventoryLocation(6, 2)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-join1");
            }
        } else if (id.equals("mm-worlds")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-mm");
            } else if (slot == InventoryLocation(3, 1)) {
                if (slotItem == Material.STRUCTURE_VOID) {
                    playClickSound(plr);
                    OpenMenu(plr, "create-main-world1");
                } else {
                    if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        // shift clicked
                        playClickSound(plr);
                        OpenMenu(plr, "mm-moveworld1");
                    } else {
                        //
                    }
                }
            } else if (slot == InventoryLocation(5, 1)) {
                if (slotItem == Material.STRUCTURE_VOID) {
                    playClickSound(plr);
                    OpenMenu(plr, "create-main-world2");
                } else {
                    if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        // shift clicked
                        playClickSound(plr);
                        OpenMenu(plr, "mm-moveworld2");
                    }
                }
            } else if (slot == InventoryLocation(7, 1)) {
                if (slotItem == Material.STRUCTURE_VOID) {
                    playClickSound(plr);
                    OpenMenu(plr, "create-main-world3");
                } else {
                    if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                        // shift clicked
                        playClickSound(plr);
                        OpenMenu(plr, "mm-moveworld3");
                    }
                }
            } else if (slot == InventoryLocation(3, 2)) {
                if (slotItem == Material.ORANGE_STAINED_GLASS_PANE) {
                    playClickSound(plr);
                    OpenMenu(plr, "edit-world1");
                }
            } else if (slot == InventoryLocation(5, 2)) {
                if (slotItem == Material.ORANGE_STAINED_GLASS_PANE) {
                    playClickSound(plr);
                    OpenMenu(plr, "edit-world2");
                }
            } else if (slot == InventoryLocation(7, 2)) {
                if (slotItem == Material.ORANGE_STAINED_GLASS_PANE) {
                    playClickSound(plr);
                    OpenMenu(plr, "edit-world3");
                }
            } else if (slot == InventoryLocation(9, 5)) {
                if (slotItem == Material.ARROW) {
                    playClickSound(plr);
                    OpenMenu(plr, "select-to-move-world");
                }
            }
        } else if (id.startsWith("create-main-world")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-worlds");
            } else if (slot == InventoryLocation(1, 3)) {
                playClickSound(plr);
                // previous world icon
                ItemStack item = v.getItem(InventoryLocation(2, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                if (!im.hasCustomModelData()) return;
                int index = im.getCustomModelData();
                index--;
                if (index < 0) index = WorldIcon.worldicons.length-1;
                WorldIcon icon = getNextWorldIcon(index);
                v.setItem(InventoryLocation(2, 3), createWorldIcon(icon, index));
            } else if (slot == InventoryLocation(2, 3)) {
                playClickSound(plr);
                String worldNumber = id.substring(id.length() - 1);
                ItemStack i = v.getItem(InventoryLocation(5, 0));
                if (i == null) return;
                ItemMeta im = i.getItemMeta();
                if (im == null) return;

                if (itemInSlot.getItemMeta() == null) return;
                if (!itemInSlot.getItemMeta().hasCustomModelData()) return;

                ItemStack gamemodeItem = v.getItem(InventoryLocation(8, 3));

                int wiid = itemInSlot.getItemMeta().getCustomModelData();
                if (gamemodeItem == null) return;
                if (gamemodeItem.getItemMeta() == null) return;
                int gmid = gamemodeItem.getItemMeta().getCustomModelData();

                plr.setMetadata("customNameChatResponse", new FixedMetadataValue(plugin, im.getDisplayName()));
                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                plr.setMetadata("createMenuSelectedGM", new FixedMetadataValue(plugin, gmid));
                plr.setMetadata("iconSelectorSelected", new FixedMetadataValue(plugin, worldNumber));
                plr.setMetadata("uiStartCode", new FixedMetadataValue(plugin, "create-main-world"));
                OpenMenu(plr, "icon-selector1");
            } else if (slot == InventoryLocation(3, 3) ) {
                playClickSound(plr);
                // next world icon
                ItemStack item = v.getItem(InventoryLocation(2, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                if (im.hasCustomModelData()) return;
                int index = im.getCustomModelData();
                index++;
                if (index > WorldIcon.worldicons.length-1) index = 0;
                WorldIcon icon = getNextWorldIcon(index);
                v.setItem(InventoryLocation(2, 3), createWorldIcon(icon, index));
            } else if (slot == InventoryLocation(7, 3)) {
                playClickSound(plr);
                // previous gamemode
                ItemStack item = v.getItem(InventoryLocation(8, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                int index = im.getCustomModelData();
                index--;
                if (index < 0) index = Gamemode.gamemodes.length-1;
                Gamemode gm = getNextGamemode(index);
                v.setItem(InventoryLocation(8, 3), createGamemodeItem(gm, index));
            } else if (slot == InventoryLocation(8 ,3)) {
                playClickSound(plr);
                String worldNumber = id.substring(id.length() - 1);
                ItemStack i = v.getItem(InventoryLocation(5, 0));
                if (i == null) return;
                ItemMeta im = i.getItemMeta();
                if (im == null) return;

                if (itemInSlot.getItemMeta() == null) return;
                if (!itemInSlot.getItemMeta().hasCustomModelData()) return;

                ItemStack gamemodeItem = v.getItem(InventoryLocation(8, 3));

                int wiid = itemInSlot.getItemMeta().getCustomModelData();
                if (gamemodeItem == null) return;
                if (gamemodeItem.getItemMeta() == null) return;
                int gmid = gamemodeItem.getItemMeta().getCustomModelData();


                plr.setMetadata("customNameChatResponse", new FixedMetadataValue(plugin, im.getDisplayName()));
                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                plr.setMetadata("createMenuSelectedGM", new FixedMetadataValue(plugin, gmid));
                plr.setMetadata("gamemodeSelectorSelected", new FixedMetadataValue(plugin, worldNumber));
                plr.setMetadata("uiStartCode", new FixedMetadataValue(plugin, "create-main-world"));
                OpenMenu(plr, "gamemode-selector1");
            } else if (slot == InventoryLocation(9, 3)) {
                playClickSound(plr);
                // next gamemode
                ItemStack item = v.getItem(InventoryLocation(8, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                int index = im.getCustomModelData();
                index++;
                if (index > Gamemode.gamemodes.length-1) index = 0;
                Gamemode gm = getNextGamemode(index);
                v.setItem(InventoryLocation(8, 3), createGamemodeItem(gm, index));
            } else if (slot == InventoryLocation(5, 0)) {
                playClickSound(plr);
                // randomise name
                String randomisedName = makeRandomWorldName();

                ItemMeta im = itemInSlot.getItemMeta();
                if (im == null) return;

                while (randomisedName.equals(im.getDisplayName())) {
                    // ensures the same name won't appear twice
                    // fixes "New World" being shown after randomising for the first time
                    randomisedName = makeRandomWorldName();
                }

                im.setDisplayName(randomisedName);
                itemInSlot.setItemMeta(im);
            } else if (slot == InventoryLocation(5, 3)) {
                playClickSound(plr);
                playSucceedSound(plr);
                // create world

                String worldNumber = id.substring(id.length() - 1);

                ItemStack nameItem = v.getItem(InventoryLocation(5, 0));
                if (nameItem == null) return;
                ItemMeta nameItemMeta = nameItem.getItemMeta();
                if (nameItemMeta == null) return;

                ItemStack gamemodeItem = v.getItem(InventoryLocation(8, 3));
                ItemStack worldIconItem = v.getItem(InventoryLocation(2, 3));

                if (gamemodeItem == null || worldIconItem == null) return;

                String worldName = nameItem.getItemMeta().getDisplayName();
                Gamemode gamemode = getNextGamemode(gamemodeItem);
                WorldIcon worldIcon = getNextWorldIcon(worldIconItem);

                String playerUUID = String.valueOf(plr.getUniqueId());

                SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(playerUUID);

                assert skyblockPlayer != null;
                assert gamemode != null;
                assert worldIcon != null;

                plugin.skyblockData.tryToCreateSkyblockWorld(skyblockPlayer, Byte.parseByte(worldNumber), worldName, gamemode, worldIcon);

                OpenMenu(plr, "mm-worlds");
            } else if (slot == InventoryLocation(5, 1)) {
                playClickSound(plr);
                playWarningSound(plr);
                // custom name
                String worldNumber = id.substring(id.length() - 1);
                ItemStack worldIconItem = v.getItem(InventoryLocation(2, 3));
                if (worldIconItem == null) return;
                if (worldIconItem.getItemMeta() == null) return;
                int wiid = worldIconItem.getItemMeta().getCustomModelData();
                ItemStack gamemodeItem = v.getItem(InventoryLocation(8, 3));
                if (gamemodeItem == null) return;
                if (gamemodeItem.getItemMeta() == null) return;
                int gmid = gamemodeItem.getItemMeta().getCustomModelData();
                plr.setMetadata("customNameChatType", new FixedMetadataValue(plugin, worldNumber));
                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                plr.setMetadata("createMenuSelectedGM", new FixedMetadataValue(plugin, gmid));
                plr.setMetadata("uiStartCode", new FixedMetadataValue(plugin, "create-main-world"));
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInput the new world name >>"));
                v.close();
            }
        } else if (id.startsWith("delete-world")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(4, 4)) {
                playClickSound(plr);
                // delete world
                // ask in chat
                String worldNumberDW = id.substring(id.length() - 1);
                SkyblockWorld skyblockWorld = plugin.skyblockData.getSkyblockWorldFromRealID(plr.getUniqueId()+"--"+worldNumberDW);
                if (skyblockWorld == null) {
                    plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4GUI has encountered an error! Tried to access world that does not exist!"));
                    return;
                }
                plr.setMetadata("deleteWorldRealId", new FixedMetadataValue(plugin, skyblockWorld.getRealId()));
                plr.closeInventory();
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3Are you sure you want to delete \""+skyblockWorld.getName()+"\"?\nType \"Delete "+skyblockWorld.getName()+"\" to confirm.\nType anything else to cancel."));
            } else if (slot == InventoryLocation(6, 4)) {
                playClickSound(plr);
                String worldNumberDW = id.substring(id.length() - 1);
                OpenMenu(plr, "edit-world"+worldNumberDW);
            }
        } else if (id.startsWith("edit-world")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-worlds");
            } else if (slot == InventoryLocation(1, 3)) {
                playClickSound(plr);
                // previous world icon
                ItemStack item = v.getItem(InventoryLocation(2, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                int index = im.getCustomModelData();
                index--;
                if (index < 0) index = WorldIcon.worldicons.length-1;
                WorldIcon icon = getNextWorldIcon(index);
                v.setItem(InventoryLocation(2, 3), createWorldIcon(icon, index));
                v.setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Confirm edits"));
            } else if (slot == InventoryLocation(2, 3)) {
                playClickSound(plr);
                String worldNumber = id.substring(id.length() - 1);
                ItemStack i = v.getItem(InventoryLocation(5, 0));
                if (i == null) return;
                ItemMeta im = i.getItemMeta();
                if (im == null) return;

                if (itemInSlot.getItemMeta() == null) return;
                int wiid = itemInSlot.getItemMeta().getCustomModelData();

                plr.setMetadata("customNameChatResponse", new FixedMetadataValue(plugin, im.getDisplayName()));
                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                plr.setMetadata("iconSelectorSelected", new FixedMetadataValue(plugin, worldNumber));
                plr.setMetadata("uiStartCode", new FixedMetadataValue(plugin, "edit-world"));
                OpenMenu(plr, "icon-selector1");
            } else if (slot == InventoryLocation(3, 3) ) {
                playClickSound(plr);
                // next world icon
                ItemStack item = v.getItem(InventoryLocation(2, 3));
                if (item == null) return;
                ItemMeta im = item.getItemMeta();
                if (im == null) return;
                int index = im.getCustomModelData();
                index++;
                if (index > WorldIcon.worldicons.length-1) index = 0;
                WorldIcon icon = getNextWorldIcon(index);
                v.setItem(InventoryLocation(2, 3), createWorldIcon(icon, index));
                v.setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Confirm edits"));
            } else if (slot == InventoryLocation(5, 0)) {
                playClickSound(plr);
                // randomise name
                String randomisedName = makeRandomWorldName();

                ItemMeta im = itemInSlot.getItemMeta();
                if (im == null) return;

                while (randomisedName.equals(im.getDisplayName())) {
                    // ensures the same name won't appear twice
                    // fixes "New World" being shown after randomising for the first time
                    randomisedName = makeRandomWorldName();
                }

                im.setDisplayName(randomisedName);
                itemInSlot.setItemMeta(im);
                v.setItem(InventoryLocation(5, 3), CustomItem(Material.LIME_DYE, "&2Confirm edits"));
            } else if (slot == InventoryLocation(5, 3)) {
                playClickSound(plr);
                playSucceedSound(plr);
                // save edits
                String worldNumber = id.substring(id.length() - 1);

                if (slotItem != Material.LIME_DYE) return;

                SkyblockPlayer skyblockPlayerEW = plugin.skyblockData.getSkyblockPlayerFromUUID(String.valueOf(plr.getUniqueId()), true);
                SkyblockWorld skyblockWorldEW = null;

                if (skyblockPlayerEW.getWorld1() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld1(), true) != null && Integer.parseInt(worldNumber) == 1) {
                    // world 1
                    String worldrealid = skyblockPlayerEW.getWorld1();
                    skyblockWorldEW = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                } else if (skyblockPlayerEW.getWorld2() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld2(), true) != null && Integer.parseInt(worldNumber) == 2) {
                    // world 2
                    String worldrealid = skyblockPlayerEW.getWorld2();
                    skyblockWorldEW = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                } else if (skyblockPlayerEW.getWorld3() != null && plugin.skyblockData.getSkyblockWorldFromRealID(skyblockPlayerEW.getWorld3(), true) != null && Integer.parseInt(worldNumber) == 3) {
                    // world 3
                    String worldrealid = skyblockPlayerEW.getWorld3();
                    skyblockWorldEW = plugin.skyblockData.getSkyblockWorldFromRealID(worldrealid);
                }

                if (skyblockWorldEW == null) {
                    v.close();
                    plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4GUI has encountered an error! Tried to access world that does not exist!"));
                    return;
                }

                ItemStack worldIconItem = v.getItem(InventoryLocation(2, 3));
                if (worldIconItem == null) return;

                ItemStack nameItem = v.getItem(InventoryLocation(5, 0));
                if (nameItem == null) return;
                if (nameItem.getItemMeta() == null) return;

                String worldName = nameItem.getItemMeta().getDisplayName();
                WorldIcon worldIcon = getNextWorldIcon(worldIconItem);

                skyblockWorldEW.setName(worldName);
                skyblockWorldEW.setWorldIcon(worldIcon);

                plugin.skyblockData.saveSkyblockWorld(skyblockWorldEW);
                OpenMenu(plr, "mm-worlds");
            } else if (slot == InventoryLocation(9, 5)) {
                playClickSound(plr);
                // delete
                String worldNumber = id.substring(id.length() - 1);
                OpenMenu(plr, "delete-world"+worldNumber);
            } else if (slot == InventoryLocation(5, 1)) {
                playClickSound(plr);
                playWarningSound(plr);
                // custom name
                String worldNumber = id.substring(id.length() - 1);
                ItemStack worldIconItem = v.getItem(InventoryLocation(2, 3));
                if (worldIconItem == null) return;
                if (worldIconItem.getItemMeta() == null) return;
                int wiid = worldIconItem.getItemMeta().getCustomModelData();
                plr.setMetadata("customNameChatType", new FixedMetadataValue(plugin, worldNumber));
                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                plr.setMetadata("uiStartCode", new FixedMetadataValue(plugin, "edit-world"));
                plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInput the new world name >>"));
                v.close();
            }
        } else if (id.startsWith("icon-selector")) {
            event.setCancelled(true);
            int worldNumber = Integer.parseInt(id.substring(id.length() - 1));
            if (slot == InventoryLocation(6, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "icon-selector"+(worldNumber + 1));
                return;
            } else if (slot == InventoryLocation(4, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "icon-selector"+(worldNumber - 1));
                return;
            } else if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                List<MetadataValue> values = plr.getMetadata("iconSelectorSelected");
                String wnum = (!values.isEmpty()) ? values.get(0).asString() : null;
                List<MetadataValue> values2 = plr.getMetadata("uiStartCode");
                String uiStartCode = (!values.isEmpty()) ? values2.get(0).asString() : null;
                plr.removeMetadata("iconSelectorSelected", plugin);
                plr.removeMetadata("uiStartCode", plugin);
                plugin.mm.OpenMenu(plr, uiStartCode+wnum);
                return;
            }
            if (slot < 9*5) {
                playClickSound(plr);
                if (itemInSlot.getItemMeta() == null) return;
                int wiid = itemInSlot.getItemMeta().getCustomModelData();

                plr.setMetadata("editMenuSelectedWI", new FixedMetadataValue(plugin, wiid));
                List<MetadataValue> values = plr.getMetadata("iconSelectorSelected");
                String wnum = (!values.isEmpty()) ? values.get(0).asString() : null;

                List<MetadataValue> values2 = plr.getMetadata("uiStartCode");
                String uiStartCode = (!values.isEmpty()) ? values2.get(0).asString() : null;

                plr.removeMetadata("iconSelectorSelected", plugin);
                plr.removeMetadata("uiStartCode", plugin);
                plugin.mm.OpenMenu(plr, uiStartCode+wnum);
            }
        } else if (id.startsWith("gamemode-selector")) {
            event.setCancelled(true);
            int worldNumberGM = Integer.parseInt(id.substring(id.length() - 1));
            if (slot == InventoryLocation(6, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "gamemode-selector"+(worldNumberGM + 1));
                return;
            } else if (slot == InventoryLocation(4, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "gamemode-selector"+(worldNumberGM - 1));
                return;
            } else if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                List<MetadataValue> values = plr.getMetadata("gamemodeSelectorSelected");
                String wnum = (!values.isEmpty()) ? values.get(0).asString() : null;
                List<MetadataValue> values2 = plr.getMetadata("uiStartCode");
                String uiStartCode = (!values.isEmpty()) ? values2.get(0).asString() : null;
                plr.removeMetadata("gamemodeSelectorSelected", plugin);
                plr.removeMetadata("uiStartCode", plugin);
                plugin.mm.OpenMenu(plr, uiStartCode+wnum);
                return;
            }
            if (slot < 9*5) {
                playClickSound(plr);
                if (itemInSlot.getItemMeta() == null) return;
                int gmid = itemInSlot.getItemMeta().getCustomModelData();

                plr.setMetadata("createMenuSelectedGM", new FixedMetadataValue(plugin, gmid));
                List<MetadataValue> values = plr.getMetadata("gamemodeSelectorSelected");
                String wnum = (!values.isEmpty()) ? values.get(0).asString() : null;

                List<MetadataValue> values2 = plr.getMetadata("uiStartCode");
                String uiStartCode = (!values.isEmpty()) ? values2.get(0).asString() : null;

                plr.removeMetadata("gamemodeSelectorSelected", plugin);
                plr.removeMetadata("uiStartCode", plugin);
                plugin.mm.OpenMenu(plr, uiStartCode+wnum);
            }
        } else if (id.startsWith("mm-join")) {
            event.setCancelled(true);
            // join menu
            int joinPage = Integer.parseInt(id.substring(id.length() - 1));
            if (slot == InventoryLocation(6, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "mm-join"+(joinPage + 1));
                return;
            } else if (slot == InventoryLocation(4, 5)) {
                if (slotItem != Material.ARROW) return;
                playClickSound(plr);
                OpenMenu(plr, "mm-join"+(joinPage - 1));
                return;
            } else if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                // return
                OpenMenu(plr);
            } else if (slot == InventoryLocation(9, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-join1");
            }
            if (slot < 9*5) {
                playClickSound(plr);
                // session clicked

            }
        } else if (id.startsWith("mm-moveworld")) {
            event.setCancelled(true);
            int worldNumber = Integer.parseInt(id.substring(id.length() - 1));
            if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-worlds");
            } else {
                SkyblockPlayer skyblockPlayer = plugin.skyblockData.getSkyblockPlayerFromUUID(plr.getUniqueId().toString());
                if (skyblockPlayer == null) return;

                byte worldSlot;
                if (slot == InventoryLocation(3, 1)) {
                    worldSlot = 1;
                } else if (slot == InventoryLocation(5, 1)) {
                    worldSlot = 2;
                } else if (slot == InventoryLocation(7, 1)) {
                    worldSlot = 3;
                } else if (slot == InventoryLocation(5, 3)) {
                    playClickSound(plr);
                    OpenMenu(plr, "mm-worlds");
                    return;
                } else {
                    return;
                }

                byte result;
                if (slotItem.equals(Material.ORANGE_STAINED_GLASS_PANE)) {
                    // free slot
                    result = plugin.skyblockData.moveSkyblockWorld(skyblockPlayer, (byte) worldNumber, worldSlot);
                } else if (slotItem.equals(Material.BARRIER)) {
                    return;
                } else {
                    // world slot
                    result = plugin.skyblockData.swapSkyblockWorlds(skyblockPlayer, (byte) worldNumber, worldSlot);
                }
                if (result > -1) {
                    playSucceedSound(plr);
                    OpenMenu(plr, "mm-worlds");
                } else {
                    playWarningSound(plr);
                    plr.closeInventory();
                    plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Failed to move world! Reason: World is not loaded properly!"));
                }
            }
        } else if (id.equals("select-to-move-world")) {
            event.setCancelled(true);
            if (slot == InventoryLocation(5, 5)) {
                playClickSound(plr);
                OpenMenu(plr, "mm-worlds");
            } else {
                if (slot == InventoryLocation(3, 1)) {
                    if (slotItem == Material.BARRIER) return;
                    // slot 1
                    OpenMenu(plr, "mm-moveworld1");
                } else if (slot == InventoryLocation(5, 1)) {
                    if (slotItem == Material.BARRIER) return;
                    // slot 2
                    OpenMenu(plr, "mm-moveworld2");
                }
                else if (slot == InventoryLocation(7, 1)) {
                    if (slotItem == Material.BARRIER) return;
                    // slot 3
                    OpenMenu(plr, "mm-moveworld3");
                }
            }
        }
    }

    public void OpenMenu(Player plr, String id) {
        if (plr.hasMetadata("customNameChatType")) {
            plr.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You must first input the new name of your world!"));
            return;
        }
        PostViewChanges(Objects.requireNonNull(plr.openInventory(CreateMenu(id))));
    }

    public void OpenMenu(Player plr) {
        OpenMenu(plr, "mm-mm");
    }
}
