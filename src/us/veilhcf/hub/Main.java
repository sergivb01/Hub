package us.veilhcf.hub;

import me.sergivb01.giraffe.Giraffe;
import me.sergivb01.giraffe.listeners.LobbyTabListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.veilhcf.hub.commands.AuthorCommand;
import us.veilhcf.hub.commands.BuildCommand;
import us.veilhcf.hub.commands.FlyCommand;
import us.veilhcf.hub.commands.SpawnCommand;
import us.veilhcf.hub.scoreboard.ScoreboardHandler;
import us.veilhcf.hub.selector.PlayerEvents;
import us.veilhcf.hub.selector.ServerSelector;
import us.veilhcf.hub.utils.ColorUtils;
import us.veilhcf.hub.utils.ServerStatusAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {
    private static Main instance;
    private ServerStatusAPI serverStatusAPI;
    private HashMap<String, String> globalStatus = new HashMap<>();

    private List<String> hcfLore = new ArrayList<>();
    private List<String> kitsLore = new ArrayList<>();
    private List<String> liteLore = new ArrayList<>();
    private List<String> uhcLore = new ArrayList<>();
    private List<String> veilzLore = new ArrayList<>();
    private List<String> lmsLore = new ArrayList<>();

    private final Map<String, Integer> players = new HashMap<>();
    private ScoreboardHandler scoreboardHandler;

    public void onEnable() {
        final long timeMillis = System.currentTimeMillis();
        Main.instance = this;

        this.serverStatusAPI = new ServerStatusAPI();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "RedisBungee");
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        this.getConsoleCommandSender().sendMessage(new ColorUtils().translateFromString("&7[Lobby] Plugin loaded in " + (System.currentTimeMillis() - timeMillis) + "ms."));
        this.getConsoleCommandSender().sendMessage("");
        this.getConsoleCommandSender().sendMessage(new ColorUtils().translateFromString("&lPlugin has been loaded and connected to the database"));
        this.getConsoleCommandSender().sendMessage("");

        this.getCommand("author").setExecutor(new AuthorCommand());
        this.getCommand("fly").setExecutor(new FlyCommand(this));
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("builder").setExecutor(new BuildCommand(this));

        this.scoreboardHandler = new ScoreboardHandler(this);

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerSelector(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> this.globalStatus = this.serverStatusAPI.getStatus("localhost", 25565, 300), 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateStatus, 20L, 3 * 20L);
    }

    public void onDisable() {
        scoreboardHandler.clearBoards();
        Main.instance = null;
        this.serverStatusAPI = null;
    }

    private void updateStatus() {
        this.hcfLore.clear();
        this.kitsLore.clear();
        this.liteLore.clear();
        this.lmsLore.clear();
        this.uhcLore.clear();
        this.veilzLore.clear();

        LobbyTabListener lobbyTabListener = Giraffe.getInstance().getLobbyTabListener();
        Map<String, String> hcf = lobbyTabListener.getHcfServer();
        Map<String, String> kits = lobbyTabListener.getKitsServer();
        Map<String, String> lite = lobbyTabListener.getLiteServer();
        Map<String, String> veilz = lobbyTabListener.getVeilzServer();


        this.hcfLore.add(ChatColor.translateAlternateColorCodes('&', hcf.get("up").equals("true") ? ("&e" + hcf.get("online") + "&a/&e" + hcf.get("max")) : "Offline"));
        //this.hcfLore.add(ChatColor.translateAlternateColorCodes('&', this.hcfStatus.get("online").equals("true") ? (this.hcfStatus.get("motd")) : "&7Restarting/Down"));
        this.hcfLore.add("");
        this.hcfLore.add(ChatColor.translateAlternateColorCodes('&', "&7The classic version of HCFactions,"));
        this.hcfLore.add(ChatColor.translateAlternateColorCodes('&', "&7Where it all began."));
        this.hcfLore.add("");
        this.hcfLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.hcfLore.add("");

        this.kitsLore.add(ChatColor.translateAlternateColorCodes('&', kits.get("up").equals("true") ? ("&e" + kits.get("online") + "&a/&e" + kits.get("max")) : "Offline"));
        //this.kitsLore.add(ChatColor.translateAlternateColorCodes('&', this.kitsStatus.get("online").equals("true") ? (this.kitsStatus.get("motd")) : "&7Restarting/Down"));
        this.kitsLore.add("");
        this.kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7The PvP packed version of HCFactions,"));
        this.kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&7choose a kit and PvP its that simple."));
        this.kitsLore.add("");
        this.kitsLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.kitsLore.add("");

        this.liteLore.add(ChatColor.translateAlternateColorCodes('&', lite.get("up").equals("true") ? ("&e" + lite.get("online") + "&a/&e" + lite.get("max")) : "Offline"));
        //this.liteLore.add(ChatColor.translateAlternateColorCodes('&', this.liteStatus.get("online").equals("true") ? (this.liteStatus.get("motd")) : "&7Restarting/Down"));
        this.liteLore.add("");
        this.liteLore.add(ChatColor.translateAlternateColorCodes('&', "&7The compact version of HCFactions,"));
        this.liteLore.add(ChatColor.translateAlternateColorCodes('&', "&7perfect for players who prefer smaller factions."));
        this.liteLore.add("");
        this.liteLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.liteLore.add("");

        //this.uhcLore.add(ChatColor.translateAlternateColorCodes('&', this.uhcStatus.get("online").equals("true") ? ("&e" + this.uhcStatus.get("onlineplayers") + "&a/&e" + this.uhcStatus.get("maxplayers")) : "            &4&l\u2717 Offline \u2717           "));
        //this.uhcLore.add(ChatColor.translateAlternateColorCodes('&', "&aStatus: " + (this.uhcStatus.get("online").equals("true") ? (this.uhcStatus.get("motd")) : "&7Restarting/Down")));
        this.uhcLore.add("");
        this.uhcLore.add(ChatColor.translateAlternateColorCodes('&', "&7Gather materials to make weapons and armour."));
        this.uhcLore.add(ChatColor.translateAlternateColorCodes('&', "&7Then find and fight people, remember golden apples!"));
        this.uhcLore.add("");
        this.uhcLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.uhcLore.add("");

        //this.veilzLore.add(ChatColor.translateAlternateColorCodes('&', this.veilzStatus.get("online").equals("true") ? ("&e" + this.veilzStatus.get("onlineplayers") + "&a/&e" + this.veilzStatus.get("maxplayers")) : "            &4&l\u2717 Offline \u2717           "));
        this.veilzLore.add(ChatColor.translateAlternateColorCodes('&', veilz.get("up").equals("true") ? ("&e" + veilz.get("online") + "&a/&e" + veilz.get("max")) : "Offline"));
        this.veilzLore.add("");
        this.veilzLore.add(ChatColor.translateAlternateColorCodes('&', "&7The newest addition to Veil,"));
        this.veilzLore.add(ChatColor.translateAlternateColorCodes('&', "&7something."));
        this.veilzLore.add("");
        this.veilzLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.veilzLore.add("");

        this.lmsLore.add("");
        this.lmsLore.add(ChatColor.translateAlternateColorCodes('&', "&7Challenge someone to a 1v1,"));
        this.lmsLore.add(ChatColor.translateAlternateColorCodes('&', "&7staying on until you die. How long can you last?"));
        this.lmsLore.add("");
        this.lmsLore.add(ChatColor.translateAlternateColorCodes('&', "&a[Click to join]"));
        this.lmsLore.add("");
    }

    public static Main getInstance() {
        return Main.instance;
    }

    private ConsoleCommandSender getConsoleCommandSender() {
        return Bukkit.getServer().getConsoleSender();
    }

    public List<String> getHcfLore() {
        return this.hcfLore;
    }

    public List<String> getUhcLore() {
        return this.uhcLore;
    }
    public List<String> getLmsLore() {
        return this.lmsLore;
    }

    public List<String> getKitsLore() {
        return this.kitsLore;
    }

    public List<String> getLiteLore() {
        return this.liteLore;
    }

    public List<String> getveilzLore(){
        return this.veilzLore;
    }

    public HashMap<String, String> getGlobalStatus() {
        return this.globalStatus;
    }


}
