package net.dkcraft.punishment.commands.kick;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.lang.Lang;

public class KickMethods {

	public Main plugin;

	public KickMethods(Main plugin) {
		this.plugin = plugin;
	}

	public boolean canBeKicked(CommandSender sender, Player target, String targetName) {
		if (target == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ONLINE_FALSE.toString().replace("%target%", targetName)));
			return false;
		}

		if (target == sender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KICK_SELF.toString()));
			return false;
		}

		if (isImmune(sender, target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KICK_IMMUNE.toString().replace("%target%", target.getName())));
			return false;
		}
		return true;
	}

	public boolean isImmune(CommandSender sender, Player target) {
		if (target.hasPermission("punishment.immune.kick") && !(sender instanceof ConsoleCommandSender)) {
			return true;
		} else {
			return false;
		}
	}

	public void kick(Player target, CommandSender sender, String message) {
		target.kickPlayer(ChatColor.translateAlternateColorCodes('&', Lang.KICK_TARGET.toString().replace("%sender%", sender.getName()).replace("%message%", message)));
	}
}
