package net.dkcraft.punishment.commands.ban;

import org.apache.commons.lang.StringUtils;
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

public class Unban implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	public Unban(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {
				if (args.length >= 2) {

					String unbanName = sender.getName();
					String unbanUUID = Bukkit.getOfflinePlayer(unbanName).getUniqueId().toString();

					String targetName = args[0];
					String targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId().toString();

					long unbanDate = methods.getCurrentTime();

					String unbanReason = StringUtils.join(args, ' ', 1, args.length);

					if (methods.getDatabaseProvider().equals("sqlite")) {
						bansqlite.setPlayerBanInfo(targetUUID);
						if (banmethods.isPlayerBanned(targetUUID)) {
							bansqlite.unbanPlayer(unbanUUID, unbanName, unbanDate, unbanReason, targetUUID);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_ACTIVE_FALSE.toString().replace("%target%", targetName)));
							return true;
						}
					}

					if (methods.getDatabaseProvider().equals("mysql")) {
						banmysql.setPlayerBanInfo(targetUUID);
						if (banmethods.isPlayerBanned(targetUUID)) {
							banmysql.unbanPlayer(unbanUUID, unbanName, unbanDate, unbanReason, targetUUID);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_ACTIVE_FALSE.toString().replace("%target%", targetName)));
							return true;
						}
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_UNBAN_SENDER.toString().replace("%target%", targetName)));

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + unbanReason);

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/unban <player> <reason>")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must have at least 1 database type enabled to run this command. Please check your Punishment configuration and try again.");
			}
		}
		return true;
	}
}
