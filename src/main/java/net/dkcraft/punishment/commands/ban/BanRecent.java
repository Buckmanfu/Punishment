package net.dkcraft.punishment.commands.ban;

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

public class BanRecent implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	public BanRecent(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("banrecent")) {
			if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {
				if (args.length == 1) {

					if (!methods.isInt(args[0])) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/banrecent <amount>")));
						return true;
					}
					
					int amount = Integer.parseInt(args[0]);

					if (methods.getDatabaseProvider().equals("sqlite")) {
						bansqlite.setGlobalBanInfo(amount);
					}

					if (methods.getDatabaseProvider().equals("mysql")) {
						banmysql.setGlobalBanInfo(amount);
					}
					
					int amountLimit = plugin.globalBans.size();
					
					for (int i = 0; i < amount; i++) {
						if (i <= amountLimit - 1) {
							
							String targetName = plugin.globalBans.get(i).getTargetName();
							String senderName = plugin.globalBans.get(i).getSenderName();
							long banDate = plugin.globalBans.get(i).getBanDate();
							long banLength = plugin.globalBans.get(i).getBanLength();
							String banReason = plugin.globalBans.get(i).getBanReason();
							boolean active = plugin.globalBans.get(i).getActive();
							boolean permanent = plugin.globalBans.get(i).getPermanent();
							long unbanDate = banDate + banLength;
							
							banmethods.sendBanInfo(sender, senderName, targetName, banDate, banLength, banReason, active, permanent, unbanDate);
						}
					}

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/banrecent <amount>")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must have at least 1 database type enabled to run this command. Please check your Punishment configuration and try again.");
			}
		}
		return true;
	}
}
