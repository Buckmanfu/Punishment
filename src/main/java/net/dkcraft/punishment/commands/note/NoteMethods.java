package net.dkcraft.punishment.commands.note;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.lang.Lang;

public class NoteMethods {

	public Main plugin;

	public NoteMethods(Main plugin) {
		this.plugin = plugin;
	}

	public void addNote(CommandSender sender, OfflinePlayer target, String message) {

		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		FileConfiguration userconfig = null;
		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		List<String> noteList = userconfig.getStringList("notes");

		noteList.add(message);
		userconfig.set("notes", noteList);

		try {
			userconfig.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean noteExists(CommandSender sender, OfflinePlayer target, String args) {

		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		FileConfiguration userconfig = null;
		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		List<String> noteList = userconfig.getStringList("notes");

		int noteListSize = noteList.size();
		int lineNumber = Integer.parseInt(args);

		if (lineNumber <= noteListSize && lineNumber > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteNote(CommandSender sender, OfflinePlayer target, String args) {

		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		FileConfiguration userconfig = null;
		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		List<String> noteList = userconfig.getStringList("notes");

		String line = noteList.get(Integer.parseInt(args) - 1);

		noteList.remove(line);
		userconfig.set("notes", noteList);

		try {
			userconfig.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void viewNote(CommandSender sender, OfflinePlayer target) {

		UUID uuid = target.getUniqueId();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");

		FileConfiguration userconfig = null;
		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		List<String> noteList = userconfig.getStringList("notes");

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NOTE_VIEW.toString().replace("%target%", target.getName())));

		int count = 1;
		for (String note : noteList) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NOTE_LIST.toString().replace("%number%", "" + count++)).replace("%note%", note));
		}
	}
}
