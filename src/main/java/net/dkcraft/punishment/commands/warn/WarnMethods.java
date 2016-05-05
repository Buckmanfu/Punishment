package net.dkcraft.punishment.commands.warn;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.lang.Lang;

public class WarnMethods {

	public Main plugin;

	public WarnMethods(Main plugin) {
		this.plugin = plugin;
	}

	public boolean playerChecks(CommandSender sender, Player target, String targetName) {
		if (target == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ONLINE_FALSE.toString().replace("%target%", targetName)));
			return true;
		}

		if (target == sender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.WARN_SELF.toString()));
			return true;
		}

		if (isImmune(sender, target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.WARN_IMMUNE.toString().replace("%target%", target.getName())));
			return true;
		}
		return false;
	}

	public boolean isImmune(CommandSender sender, Player target) {
		if (target.hasPermission("punishment.immune.mute") && !(sender instanceof ConsoleCommandSender)) {
			return true;
		} else {
			return false;
		}
	}

	public void warn(Player target, CommandSender sender, String message) {
		target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.WARN_TARGET.toString().replace("%sender%", sender.getName()).replace("%message%", message)));
	}
}
