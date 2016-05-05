package net.dkcraft.punishment.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.commands.ban.methods.BanMethods;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.util.lang.Lang;

public class Methods {

	public Main plugin;
	public Config config;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	private ChatColor AQUA = ChatColor.AQUA;
	private ChatColor GRAY = ChatColor.GRAY;
	private ChatColor RED = ChatColor.RED;

	public Methods(Main plugin) {
		this.plugin = plugin;
		this.config = this.plugin.config;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.banmysql = this.plugin.banmysql;
	}

	public boolean isLoggingEnabled() {
		return config.getLoggingEnabled();
	}

	public boolean isDebugEnabled() {
		return config.getDebugEnabled();
	}

	public boolean isUpdatesEnabled() {
		return config.getUpdatesEnabled();
	}

	public boolean isMetricsEnabled() {
		return config.getMetricsEnabled();
	}

	public String getDatabaseProvider() {
		return config.getDatabaseProvider();
	}

	public String getLogFormat() {
		SimpleDateFormat format = new SimpleDateFormat(config.getDateFormat());
		return "[" + format.format(System.currentTimeMillis()) + "] [Punishment v:" + plugin.getDescription().getVersion() + "] ";
	}

	public void log(String message) {
		if (isLoggingEnabled() == true) {
			plugin.punishmentLog.add(getLogFormat() + message);
			plugin.punishmentLog.save();
		}
	}

	public void debug(String message) {
		if (isDebugEnabled() == true) {
			plugin.punishmentLog.add(getLogFormat() + message);
			plugin.punishmentLog.save();
		}
	}

	public void loadConfig() {
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
	}

	public void createLog() {
		if (isLoggingEnabled()) {
			plugin.punishmentLog = new ListStore(new File(plugin.getDataFolder().getAbsolutePath(), "punishment.log"));
			plugin.punishmentLog.load();
		}
	}

	public void checkForUpdates() {
		if (isUpdatesEnabled()) {
			double latest = Double.parseDouble(requestHttp("https://raw.githubusercontent.com/xDeeKay/Punishment/master/latest"));
			plugin.updateLatest = latest;
			if (latest > Double.parseDouble(plugin.getDescription().getVersion())) {
				plugin.updateAvailable = true;
				plugin.log.info("[Punishment] An update for Punishment is available! Latest: " + latest);
				plugin.log.info("[Punishment] " + plugin.getDescription().getWebsite());
				log("An update for Punishment is available! Latest: " + latest);
				log(plugin.getDescription().getWebsite());
			}
		}
	}

	public void loadMetrics() {
		if (isMetricsEnabled()) {
			try {
				Metrics metrics = new Metrics(plugin);
				metrics.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadDatabase() {

		if (getDatabaseProvider().equals("sqlite")) {
			plugin.log.info("[Punishment] Using SQLite as database provider.");
			File file = new File(config.getSqlitePath());
			if (!file.exists()) {
				bansqlite = new BanMethodsSQLite(plugin);
				bansqlite.createTables();
			}

		} else if (getDatabaseProvider().equals("mysql")) {
			plugin.log.info("[Punishment] Using MySQL as database provider.");
			banmysql = new BanMethodsMySQL(plugin);
			banmysql.openConnection();
			banmysql.createTables();
		} else {
			plugin.log.warning("[Punishment] Incorrect database provider defined in config.");
		}
	}

	public long getCurrentTime() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long currentTime = timestamp.getTime() / 1000;
		return currentTime;
	}

	public boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isOnline(Player target) {
		if (target != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPermanent(String banLengthString) {
		if (banLengthString.equals("permanent") || banLengthString.equals("perma") || banLengthString.equals("perm") || banLengthString.equals("p")) {
			return true;
		} else {
			return false;
		}
	}

	public String getUnbanDate(long banLength) {
		DateFormat dateFormat = new SimpleDateFormat(config.getDateFormat());
		String date = dateFormat.format(banLength * 1000);
		return date;
	}

	public String getDurationString(long duration) {

		long days = TimeUnit.SECONDS.toDays(duration);
		duration -= TimeUnit.DAYS.toSeconds(days);

		long hours = TimeUnit.SECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toSeconds(hours);

		long minutes = TimeUnit.SECONDS.toMinutes(duration);
		duration -= TimeUnit.MINUTES.toSeconds(minutes);

		long seconds = TimeUnit.SECONDS.toSeconds(duration);

		StringBuilder result = new StringBuilder();
		if (days != 0) {
			result.append(days + " day(s) ");
		}
		if (hours != 0) {
			result.append(hours + " hours(s) ");
		}
		if (minutes != 0) {
			result.append(minutes + " minutes(s) ");
		}
		if (seconds != 0) {
			result.append(seconds + " seconds(s)");
		}
		return result.toString();
	}

	public long parse(String input) {
		long result = 0;
		String number = "";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isDigit(c)) {
				number += c;
			} else if (Character.isLetter(c) && !number.isEmpty()) {
				result += convert(Integer.parseInt(number), c);
				number = "";
			}
		}
		return result;
	}

	public long convert(int value, char unit) {
		switch (unit) {
		case 'y':
			return value * 31536000;
		case 'M':
			return value * 2592000;
		case 'w':
			return value * 604800;
		case 'd':
			return value * 86400;
		case 'h':
			return value * 3600;
		case 'm':
			return value * 60;
		case 's':
			return value * 1;
		}
		return 0;
	}

	public boolean isValidTimeString(String banLengthString) {
		if (banLengthString.contains("s") || banLengthString.contains("m") || banLengthString.contains("h") || banLengthString.contains("d")
				|| banLengthString.contains("w") && banLengthString.matches(".*\\d.*")) {
			return true;
		} else {
			return false;
		}
	}

	public String requestHttp(String requestUrl) {
		String sourceLine = null;

		URL address = null;
		try {
			address = new URL(requestUrl);
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}

		InputStreamReader pageInput = null;
		try {
			pageInput = new InputStreamReader(address.openStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		BufferedReader source = new BufferedReader(pageInput);

		try {
			sourceLine = source.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return sourceLine;
	}

	@SuppressWarnings("deprecation")
	public boolean playerFileExists(CommandSender sender, String args) {

		OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args);
		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		if (playerFile.exists()) {
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_EXISTS_FALSE.toString().replace("%target%", target.getName())));
			return false;
		}
	}

	public void savePlayerFile(OfflinePlayer target) {

		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		FileConfiguration userconfig = null;
		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		try {
			userconfig.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void helpMenu(CommandSender sender) {

		sender.sendMessage(GRAY + "Available Punishment Commands");

		if (sender.hasPermission("punishment.punishment.help")) {
			sender.sendMessage(AQUA + " /punishment help: " + GRAY + "List your available Punishment commands.");
		}
		if (sender.hasPermission("punishment.punishment.info")) {
			sender.sendMessage(AQUA + " /punishment info: " + GRAY + "Display your Punishment info.");
		}
		if (sender.hasPermission("punishment.punishment.check")) {
			sender.sendMessage(AQUA + " /punishment check <player>: " + GRAY + "Display player Punishment info.");
		}
		if (sender.hasPermission("punishment.warn")) {
			sender.sendMessage(AQUA + " /warn <player> <message>: " + GRAY + "Warn a player.");
		}
		if (sender.hasPermission("punishment.mute")) {
			sender.sendMessage(AQUA + " /mute <player> <time>: " + GRAY + "Mute a player.");
		}
		if (sender.hasPermission("punishment.mute.unmute")) {
			sender.sendMessage(AQUA + " /unmute <player>: " + GRAY + "Unmute a player.");
		}
		if (sender.hasPermission("punishment.freeze")) {
			sender.sendMessage(AQUA + " /freeze <player> <time>: " + GRAY + "Freeze a player.");
		}
		if (sender.hasPermission("punishment.freeze.unfreeze")) {
			sender.sendMessage(AQUA + " /unfreeze <player>: " + GRAY + "Unfreeze a player.");
		}
		if (sender.hasPermission("punishment.jail")) {
			sender.sendMessage(AQUA + " /jail <player> <jail> <time>: " + GRAY + "Jail a player.");
		}
		if (sender.hasPermission("punishment.jail.unjail")) {
			sender.sendMessage(AQUA + " /unjail <player>: " + GRAY + "Unjail a player.");
		}
		if (sender.hasPermission("punishment.jail.set")) {
			sender.sendMessage(AQUA + " /setjail <jail>: " + GRAY + "Set a jail location.");
		}
		if (sender.hasPermission("punishment.jail.del")) {
			sender.sendMessage(AQUA + " /deljail <jail>: " + GRAY + "Delete a jail location.");
		}
		if (sender.hasPermission("punishment.kick")) {
			sender.sendMessage(AQUA + " /kick <player> <message>: " + GRAY + "Kick a player.");
		}
		if (sender.hasPermission("punishment.ban")) {
			sender.sendMessage(AQUA + " /ban <player> <time> <message>: " + GRAY + "Ban a player.");
		}
		if (sender.hasPermission("punishment.ban.unban")) {
			sender.sendMessage(AQUA + " /unban <player> <message>: " + GRAY + "Unban a player from the server.");
		}
		if (sender.hasPermission("punishment.ban.check")) {
			sender.sendMessage(AQUA + " /bancheck <player>: " + GRAY + "Check if a player is banned.");
		}
		if (sender.hasPermission("punishment.ban.history")) {
			sender.sendMessage(AQUA + " /banhistory <player>: " + GRAY + "Display a players ban history.");
		}
		if (sender.hasPermission("punishment.ban.recent")) {
			sender.sendMessage(AQUA + " /banrecent <amount>: " + GRAY + "Display recent server bans.");
		}
		if (sender.hasPermission("punishment.ban.import")) {
			sender.sendMessage(AQUA + " /banimport: " + GRAY + "Import bans from file list.");
		}
		if (sender.hasPermission("punishment.note.add")) {
			sender.sendMessage(AQUA + " /note add <player> <message>: " + GRAY + "Add a player note.");
		}
		if (sender.hasPermission("punishment.note.delete")) {
			sender.sendMessage(AQUA + " /note delete <player> <note>: " + GRAY + "Delete a player note.");
		}
		if (sender.hasPermission("punishment.note.view")) {
			sender.sendMessage(AQUA + " /note view <player>: " + GRAY + "View a player note.");
		}
		if (sender.hasPermission("punishment.ticket.create")) {
			sender.sendMessage(AQUA + " /ticket create <player>: " + GRAY + "Create a ticket.");
		}
		if (sender.hasPermission("punishment.ticket.delete")) {
			sender.sendMessage(AQUA + " /ticket delete <player>: " + GRAY + "Delete a ticket.");
		}
		if (sender.hasPermission("punishment.ticket.claim")) {
			sender.sendMessage(AQUA + " /ticket claim <player>: " + GRAY + "Claim a ticket.");
		}
		if (sender.hasPermission("punishment.ticket.teleport")) {
			sender.sendMessage(AQUA + " /ticket teleport <player>: " + GRAY + "Teleport to a ticket.");
		}
		if (sender.hasPermission("punishment.ticket.view")) {
			sender.sendMessage(AQUA + " /ticket view <player>: " + GRAY + "View a players ticket.");
		}
		if (sender.hasPermission("punishment.ticket.list")) {
			sender.sendMessage(AQUA + " /ticket list: " + GRAY + "List all tickets.");
		}
	}

	public void infoMenu(CommandSender sender) {
		sender.sendMessage(GRAY + "This server is running Punishment version " + AQUA + plugin.getDescription().getVersion());

		if (isUpdatesEnabled()) {
			if (plugin.updateAvailable) {
				sender.sendMessage(RED + "An update for Punishment is available! Latest: " + plugin.updateLatest);
				sender.sendMessage(RED + plugin.getDescription().getWebsite());
			}
		}

		sender.sendMessage(GRAY + "Created by " + AQUA + plugin.getDescription().getAuthors().get(0) + " bukkit.org/members/xdeekay.90614316");
	}

	@SuppressWarnings("deprecation")
	public void checkMenu(CommandSender sender, String target) {

		String targetUUID = Bukkit.getOfflinePlayer(target).getUniqueId().toString();

		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + targetUUID + ".yml");
		FileConfiguration userconfig = YamlConfiguration.loadConfiguration(playerFile);

		if (playerFile.exists()) {

			sender.sendMessage(GRAY + "Displaying Punishment info for " + AQUA + target);

			sender.sendMessage(AQUA + " UUID: " + GRAY + targetUUID);

			String IP = userconfig.getString("currentIP");
			int usernames = userconfig.getStringList("usernameHistory").size();
			int notes = userconfig.getStringList("notes").size();

			sender.sendMessage(AQUA + " IP: " + GRAY + IP);
			sender.sendMessage(AQUA + " Usernames: " + GRAY + usernames);
			sender.sendMessage(AQUA + " Notes: " + GRAY + notes);

			if (plugin.tickets.containsKey(target)) {
				String ticket = plugin.tickets.get(target);
				sender.sendMessage(AQUA + " Ticket: " + GRAY + ticket);
			} else {
				sender.sendMessage(AQUA + " Ticket: " + GRAY + "false");
			}

			if (plugin.muted.containsKey(target) || plugin.mutedRemaining.containsKey(target)) {
				sender.sendMessage(AQUA + " Muted: " + GRAY + "true");
			} else {
				sender.sendMessage(AQUA + " Muted: " + GRAY + "false");
			}

			if (plugin.frozen.containsKey(target) || plugin.frozenRemaining.containsKey(target)) {
				sender.sendMessage(AQUA + " Frozen: " + GRAY + "true");
			} else {
				sender.sendMessage(AQUA + " Frozen: " + GRAY + "false");
			}

			if (plugin.jailed.containsKey(target) || plugin.jailedRemaining.containsKey(target)) {
				sender.sendMessage(AQUA + " Jailed: " + GRAY + "true");
			} else {
				sender.sendMessage(AQUA + " Jailed: " + GRAY + "false");
			}

			if (getDatabaseProvider().equals("sqlite")) {

				bansqlite = new BanMethodsSQLite(plugin);
				banmethods = new BanMethods(plugin);

				bansqlite.setPlayerBanInfo(targetUUID);
				if (banmethods.isPlayerBanned(targetUUID)) {
					sender.sendMessage(AQUA + " Banned: " + GRAY + "true");
				} else {
					sender.sendMessage(AQUA + " Banned: " + GRAY + "false");
				}
			}

			if (getDatabaseProvider().equals("mysql")) {

				banmysql = new BanMethodsMySQL(plugin);
				banmethods = new BanMethods(plugin);

				banmysql.setPlayerBanInfo(targetUUID);
				if (banmethods.isPlayerBanned(targetUUID)) {
					sender.sendMessage(AQUA + " Banned: " + GRAY + "true");
				} else {
					sender.sendMessage(AQUA + " Banned: " + GRAY + "false");
				}
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_EXISTS_FALSE.toString().replace("%target%", target)));
		}
	}
}
