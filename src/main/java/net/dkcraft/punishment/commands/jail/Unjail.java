package net.dkcraft.punishment.commands.jail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Unjail implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public JailMethods jail;

	public Unjail(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.jail = this.plugin.jail;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unjail")) {
			if (args.length == 1) {

				Player target = Bukkit.getPlayer(args[0]);

				if (!jail.isJailed(target)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_FALSE.toString().replace("%target%", args[0])));
					return true;
				}

				Location playerLocation = plugin.jailLocation.get(target.getName());

				jail.unJail(target, playerLocation);

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_UNJAIL_SENDER.toString().replace("%target%", target.getName())));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_UNJAIL_TARGET.toString().replace("%sender%", sender.getName())));

				methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/unjail <player>")));
			}
		}
		return true;
	}
}
