package us.veilhcf.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.veilhcf.hub.Main;
import us.veilhcf.hub.utils.ColorUtils;

public class FlyCommand implements CommandExecutor {
    Main main;

    public FlyCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("fly") && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "No");
            return true;
        } else{
        	Player p = (Player)sender;
        	
        	if(!p.hasPermission("lobby.fly")){
        		    if (p.getAllowFlight()){
            		  p.setAllowFlight(false);
            	      p.setFlying(false);
                      p.sendMessage(new ColorUtils().translateFromString("&8(&bVeil Hub&8) &9Your flight has been disabled."));
            	    }
            	    else{
            	    	p.setAllowFlight(true);
                         p.sendMessage(new ColorUtils().translateFromString("&8(&bVeil Hub&8) &9Your flight has been enabled."));
            	    }
        	}else{
        		p.sendMessage(ChatColor.RED + "You do not have permission to do that.");
        	}
        	
        	
        	
            return true;
        }
    }
}