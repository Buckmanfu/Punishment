package net.dkcraft.punishment.commands.ban;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.commands.ban.methods.BanMethods;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Ban implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	long banLength;
	long unbanDateLong;
	boolean permanent;

	public Ban(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ban")) {
			if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {
				if (args.length >= 3) {

					String targetName = args[0];
					Player target = Bukkit.getPlayer(targetName);
					String targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId().toString();

					String senderName = sender.getName();
					String senderUUID = Bukkit.getOfflinePlayer(senderName).getUniqueId().toString();

					long banDate = methods.getCurrentTime();

					String banLengthString = args[1];

					String banReason = StringUtils.join(args, ' ', 2, args.length);

					if (methods.isPermanent(banLengthString)) {
						banLength = 0;
						permanent = true;
					} else if (methods.isValidTimeString(banLengthString)) {
						banLength = methods.parse(banLengthString);
						permanent = false;
					}

					if (methods.getDatabaseProvider().equals("sqlite")) {
						bansqlite.setPlayerBanInfo(targetUUID);
						if (!banmethods.isPlayerBanned(targetUUID)) {
							bansqlite.banPlayer(targetUUID, targetName, senderUUID, senderName, banDate, banLength, banReason, permanent);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_ACTIVE_TRUE.toString().replace("%target%", targetName)));
							return true;
						}
					}

					if (methods.getDatabaseProvider().equals("mysql")) {
						banmysql.setPlayerBanInfo(targetUUID);
						if (!banmethods.isPlayerBanned(targetUUID)) {
							banmysql.banPlayer(targetUUID, targetName, senderUUID, senderName, banDate, banLength, banReason, permanent);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_ACTIVE_TRUE.toString().replace("%target%", targetName)));
							return true;
						}
					}

					unbanDateLong = banDate + banLength;

					String banTime = methods.getDurationString(banLength);
					String unbanDate = methods.getUnbanDate(unbanDateLong);

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_SENDER.toString().replace("%target%", targetName)));

					if (methods.isOnline(target)) {
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&',
								Lang.BAN_TARGET.toString().replace("%sender%", senderName).replace("%time%", banTime).replace("%date%", unbanDate).replace("%message%", banReason)));
					}

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1] + " " + banReason);

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ban <player> <time s,m,h,d,w> <message>")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must have at least 1 database type enabled to run this command. Please check your Punishment configuration and try again.");
			}
		}
		return true;
	}
}
