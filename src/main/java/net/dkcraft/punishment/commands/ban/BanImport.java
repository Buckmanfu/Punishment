package net.dkcraft.punishment.commands.ban;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class BanImport implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	public BanImport(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("banimport")) {
			if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {
				if (args.length == 0) {
					
					File file = new File(plugin.getDataFolder().getAbsolutePath() + "/bannedplayers.txt");
					if (!file.exists()) {
					    sender.sendMessage(ChatColor.RED + "bannedplayers.txt does not exist.");
					    return true;
					}
					
					sender.sendMessage(ChatColor.GRAY + "Attempting to import bans. Check console for more info.");

					if (methods.getDatabaseProvider().equals("sqlite")) {
						bansqlite.importBans();
					}

					if (methods.getDatabaseProvider().equals("mysql")) {
						banmysql.importBans();
					}

					methods.log(sender.getName() + " issued command: /" + cmd.getName());

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/banimport")));
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You must have at least 1 database type enabled to run this command. Please check your Punishment configuration and try again.");
			}
		}
		return true;
	}
}
