package net.dkcraft.punishment.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.dkcraft.punishment.Main;

public class PlayerFileListener implements Listener {

	public Main plugin;

	public PlayerFileListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String IP = player.getAddress().getHostString();
		File playerFile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + uuid.toString() + ".yml");
		FileConfiguration userconfig = null;

		if (!playerFile.exists()) {
			try {
				playerFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		userconfig = YamlConfiguration.loadConfiguration(playerFile);

		if (!userconfig.contains("currentUsername")) {
			userconfig.set("currentUsername", player.getName());
		} else if (userconfig.getString("currentUsername") != player.getName()) {
			userconfig.set("currentUsername", player.getName());
		}

		if (!userconfig.contains("usernameHistory")) {
			userconfig.set("usernameHistory", new ArrayList<String>());
		}

		List<String> usernameList = userconfig.getStringList("usernameHistory");

		if (!usernameList.contains(player.getName())) {
			usernameList.add(player.getName());
			userconfig.set("usernameHistory", usernameList);
		}

		if (!userconfig.contains("currentIP")) {
			userconfig.set("currentIP", IP);
		} else if (userconfig.getString("CurrentIP") != IP) {
			userconfig.set("currentIP", IP);
		}

		List<String> ipList = userconfig.getStringList("ipHistory");

		if (!ipList.contains(IP)) {
			ipList.add(IP);
			userconfig.set("ipHistory", ipList);
		}

		if (!userconfig.contains("notes")) {
			userconfig.set("notes", "");
		}

		try {
			userconfig.save(playerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
