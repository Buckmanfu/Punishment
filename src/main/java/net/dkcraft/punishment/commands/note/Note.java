package net.dkcraft.punishment.commands.note;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Note implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public NoteMethods note;

	public Note(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.note = this.plugin.note;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("note")) {

			if (args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note add/delete/view")));
				return true;
			}

			if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
				if (sender.hasPermission("punishment.note.view")) {
					if (args.length == 2) {
						if (methods.playerFileExists(sender, args[1])) {
							OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[1]);
							note.viewNote(sender, target);
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note view <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d")) {
				if (sender.hasPermission("punishment.note.delete")) {
					if (args.length == 3) {
						if (methods.playerFileExists(sender, args[1])) {
							OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[1]);
							
							if (methods.isInt(args[2])) {
								if (note.noteExists(sender, target, args[2])) {
									note.deleteNote(sender, target, args[2]);
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NOTE_DELETE.toString().replace("%target%", target.getName())));
									methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1] + " " + args[2]);
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NOTE_EXISTS_FALSE.toString().replace("%target%", target.getName())));
								}
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note delete <player> <note>")));
							}
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note delete <player> <note>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
				if (sender.hasPermission("punishment.note.add")) {
					if (args.length >= 3) {
						if (methods.playerFileExists(sender, args[1])) {
							OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[1]);
							String message = StringUtils.join(args, ' ', 2, args.length);
							note.addNote(sender, target, message);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NOTE_ADD.toString().replace("%target%", target.getName())));
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1] + " " + message);
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note add <player> <note>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/note add/delete/view")));
			}
		}
		return true;
	}
}
