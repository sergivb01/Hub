package us.veilhcf.hub.selector;

import net.minecraft.util.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.veilhcf.hub.Main;
import us.veilhcf.hub.utils.ItemStackBuilder;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ServerSelector implements Listener{
    static List<String> serverSelectorLore = new ArrayList<>();
    static List<String> hubLore = new ArrayList<>();
    static List<String> hubitemLore = new ArrayList<>();
    private Main main;

    public ServerSelector(Main main){
        this.main = main;
    }

    static{
        serverSelectorLore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to play a gamemode."));

        hubLore.add(ChatColor.translateAlternateColorCodes('&', "&7Right click to change hub/lobby."));
        hubitemLore.add(ChatColor.translateAlternateColorCodes('&', "&7Connect to server"));

    }

    public static ItemStack selector = ItemStackBuilder.get(Material.COMPASS, 1, (short)0, "&6Server Selector", serverSelectorLore);
    public static ItemStack hub = ItemStackBuilder.get(Material.NETHER_STAR, 1, (short)0, "&eHub Selector", hubLore);

    private static ItemStack hub1 = ItemStackBuilder.get(Material.ENDER_PEARL, 1, (short)0, "&6Hub &e#1", hubitemLore);
    private static ItemStack hub2 = ItemStackBuilder.get(Material.ENDER_PEARL, 2, (short)0, "&6Hub &e#2", hubitemLore);
    private static Inventory hubinv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&6Hub Selector"));

    public ServerSelector(){
        hubinv.setItem(0, hub1);
        hubinv.setItem(1, hub2);
    }


    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        if ((!(e.getWhoClicked() instanceof Player)) || (e.getCurrentItem() == null)) {
            return;
        }
        
        if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
            e.setCancelled(false);
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(item.getType().equals(Material.AIR) && ChatColor.stripColor(e.getClickedInventory().getTitle()).contains("Selector")){
            e.setCancelled(true);
            return;
        }

        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("UHC")) {
            toServer(p, "uhc");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("LMS")) {
            toServer(p, "lms");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Veil")) {
            toServer(p, "veilz");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Factions") && !ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Lite")) {
            toServer(p, "hcf");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Kits")) {
            toServer(p, "kits");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Lite")) {
            toServer(p, "lite");
            p.closeInventory();
        }

        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Lite")) {
            toServer(p, "veilz");
            p.closeInventory();
        }

        else if (item.isSimilar(hub1)) {
            toServer(p, "lobby1");
            p.closeInventory();
        }

        else if (item.isSimilar(hub2)) {
            toServer(p, "lobby2");
            p.closeInventory();
        }
        e.setCancelled(true);

    }

    private void toServer(final Player p, final String server) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eConnecting to &a&l" + server + "&e, please wait."));
            out.writeUTF("Connect");
            out.writeUTF(server);
        }
        catch (Exception e) {
            //p.sendMessage("&cError while sending you to &e" + server + "&c. (" + e.getMessage() + ")");
        	p.sendMessage(ChatColor.RED + "Error while sending you to " + ChatColor.YELLOW + server);
        }
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
    	Player p = e.getPlayer();


        if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(selector)) && ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))))){
            Inventory inv = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&6Server Selector"));

            ItemStack hcf = ItemStackBuilder.get(Material.INK_SACK, 1, (short)1, "&aHardcore Factions", Main.getInstance().getHcfLore());
            ItemStack kits = ItemStackBuilder.get(Material.INK_SACK, 1, (short)13, "&aHardcore Kits", Main.getInstance().getKitsLore());
            ItemStack lite = ItemStackBuilder.get(Material.INK_SACK, 1, (short)6, "&aHardcore Factions &e[Lite]", Main.getInstance().getLiteLore());
            ItemStack uhc = ItemStackBuilder.get(Material.INK_SACK, 1, (short)14, "&aUHC Games", Main.getInstance().getUhcLore());
            ItemStack veilz = ItemStackBuilder.get(Material.INK_SACK, 1, (short)7, "&aVeilZ", Main.getInstance().getveilzLore());
            ItemStack lms = ItemStackBuilder.get(Material.INK_SACK, 1, (short)9, "&aLMS", Main.getInstance().getLmsLore());


            inv.setItem(10, hcf);
            inv.setItem(13, kits);
            inv.setItem(16, lite);
            inv.setItem(28, uhc);
            inv.setItem(31, veilz);
            inv.setItem(34, lms);

            p.openInventory(inv);
            e.setCancelled(true);
        }
        else if ((!e.getAction().equals(Action.PHYSICAL)) && (e.getItem() != null) && (e.getItem().isSimilar(hub)) && ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))))){
            p.openInventory(hubinv);
            e.setCancelled(true);
        }

    }
}
