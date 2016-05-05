package net.dkcraft.punishment.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;

public class UpdateNotifyListener implements Listener {

	public Main plugin;
	public Methods methods;

	public UpdateNotifyListener(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (methods.isUpdatesEnabled()) {
			if (plugin.updateAvailable) {
				if (player.isOp()) {
					player.sendMessage(ChatColor.RED + "An update for Punishment is available! Latest: " + plugin.updateLatest);
					player.sendMessage(ChatColor.RED + plugin.getDescription().getWebsite());
				}
			}
		}
	}
}
