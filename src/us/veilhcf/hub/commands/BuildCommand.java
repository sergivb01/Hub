package us.veilhcf.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.veilhcf.hub.Main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class BuildCommand implements CommandExecutor {
    private Main main;

    public BuildCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("builder") && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You should not do that bro. Are u fucking trying to crash me? go and kys bitch!");
            return true;
        } else{
        	Player p = (Player)sender;
        	toServer(p);
            return true;
        }
    }

    private void toServer(final Player p) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou will be sent to &e" + "build server" + " &cin few moments... Please wait"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWe are sending you to &e" + "build server" + "&c..."));
            out.writeUTF("Connect");
            out.writeUTF("dev");
        }
        catch (Exception e) {
            p.sendMessage(ChatColor.RED + "Error while sending you to " + ChatColor.YELLOW + "build server.");
        }
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
    }

}