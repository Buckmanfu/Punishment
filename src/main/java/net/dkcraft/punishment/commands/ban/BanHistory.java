package net.dkcraft.punishment.commands.ban;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.commands.ban.methods.BanMethods;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class BanHistory implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	public BanHistory(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("banhistory")) {
			if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {
				if (args.length == 1) {

					String targetNameArgs = args[0];
					String targetUUID = Bukkit.getOfflinePlayer(targetNameArgs).getUniqueId().toString();

					if (methods.getDatabaseProvider().equals("sqlite")) {
						bansqlite.setPlayerBanInfo(targetUUID);
						if (!banmethods.hasPlayerBeenBanned(targetUUID)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_HISTORY_FALSE.toString().replace("%target%", targetNameArgs)));
							return true;
						}
					}

					if (methods.getDatabaseProvider().equals("mysql")) {
						banmysql.setPlayerBanInfo(targetUUID);
						if (!banmethods.hasPlayerBeenBanned(targetUUID)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_HISTORY_FALSE.toString().replace("%target%", targetNameArgs)));
							return true;
						}
					}

					int amount = plugin.playerBans.get(targetUUID).size();

					for (int i = 0; i < amount; i++) {

						String targetName = plugin.playerBans.get(targetUUID).get(i).getTargetName();
						String senderName = plugin.playerBans.get(targetUUID).get(i).getSenderName();
						long banDate = plugin.playerBans.get(targetUUID).get(i).getBanDate();
						long banLength = plugin.playerBans.get(targetUUID).get(i).getBanLength();
						String banReason = plugin.playerBans.get(targetUUID).get(i).getBanReason();
						boolean active = plugin.playerBans.get(targetUUID).get(i).getActive();
						boolean permanent = plugin.playerBans.get(targetUUID).get(i).getPermanent();
						long unbanDate = banDate + banLength;

						banmethods.sendBanInfo(sender, senderName, targetName, banDate, banLength, banReason, active, permanent, unbanDate);
					}

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/banhistory <player>")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must have at least 1 database type enabled to run this command. Please check your Punishment configuration and try again.");
			}
		}
		return true;
	}
}
