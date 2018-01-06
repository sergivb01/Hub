package us.veilhcf.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class AuthorCommand implements CommandExecutor  {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("author") || command.getName().equalsIgnoreCase("dev")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Plugin developed by &bMove2Linux&9. May not be distributed."));
        }
        return true;
    }
}
