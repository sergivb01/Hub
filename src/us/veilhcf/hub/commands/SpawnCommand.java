package us.veilhcf.hub.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SpawnCommand implements CommandExecutor  {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("spawn")) {
             if(!(sender instanceof Player)){
                 sender.sendMessage(ChatColor.RED + "You may not do that.");
                 return false;
             }

            Player player = (Player) sender;
            player.teleport(player.getWorld().getSpawnLocation().add(0.5, 0.5, 0.5));
            player.sendMessage(ChatColor.GREEN + "You have been teleported to the spawn.");
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.25F, 1.25F);
        }
        return true;
    }
}
