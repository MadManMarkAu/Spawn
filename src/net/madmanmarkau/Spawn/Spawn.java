package net.madmanmarkau.Spawn;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Spawn extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
    public PermissionHandler Permissions;
    public PluginDescriptionFile pdfFile;

	@Override
	public void onDisable() {
	    log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " unloaded");
	}

	@Override
	public void onEnable() {
		this.pdfFile = this.getDescription();

		setupPermissions();
		
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " loaded");
	}

	public void setupPermissions() {
		Plugin perm = this.getServer().getPluginManager().getPlugin("Permissions");
			
		if (this.Permissions == null) {
			if (perm!= null) {
				this.getServer().getPluginManager().enablePlugin(perm);
				this.Permissions = ((Permissions) perm).getHandler();
			}
			else {
				log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + "not enabled. Permissions not detected");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;

		if (cmd.getName().compareToIgnoreCase("spawn_warpspawn") == 0) {
			if (Permissions.has(player, "spawn.warp")) {
				Location spawn = player.getWorld().getSpawnLocation();

				spawn.setX(spawn.getBlockX() + 0.5);
				spawn.setY(spawn.getBlockY());
				spawn.setZ(spawn.getBlockZ() + 0.5);

				player.teleport(spawn);

				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
				return true;
			}
		} else if (cmd.getName().compareToIgnoreCase("spawn_setspawn") == 0) {
			if (Permissions.has(player, "spawn.set")) {
	    		Location spawn = player.getLocation();
	
				player.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
				
				player.sendMessage(ChatColor.RED + "Spawn location for [" + player.getWorld().getName() + "] set.");
				log.info("Player " + player.getName() + " set spawn location for [" + player.getWorld().getName() + "]");
				
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
				return true;
			}
		}
		return false;
    }
}
