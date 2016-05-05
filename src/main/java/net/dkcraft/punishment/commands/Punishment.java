package net.dkcraft.punishment.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Punishment implements CommandExecutor {

	public Main plugin;
	public Methods methods;

	public Punishment(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("punishment") || cmd.getName().equalsIgnoreCase("pun")) {

			if (args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/punishment help/info/check")));
				return true;
			}

			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
				if (sender.hasPermission("punishment.punishment.help")) {
					if (args.length == 1) {
						methods.helpMenu(sender);
						methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/punishment help")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
				if (sender.hasPermission("punishment.punishment.info")) {
					if (args.length == 1) {
						methods.infoMenu(sender);
						methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/punishment info")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("c")) {
				if (sender.hasPermission("punishment.punishment.check")) {
					if (args.length == 2) {
						String target = args[1];
						methods.checkMenu(sender, target);
						methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/punishment check <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/punishment help/info/check")));
			}
		}
		return true;
	}
}
