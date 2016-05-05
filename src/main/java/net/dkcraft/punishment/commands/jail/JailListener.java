package net.dkcraft.punishment.commands.jail;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class JailListener implements Listener {

	public Main plugin;
	public Methods methods;
	public JailMethods jail;

	public JailListener(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.jail = this.plugin.jail;
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (jail.isJailed(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_ACTION_COMMAND.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (jail.isJailed(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_ACTION_PLACE.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (jail.isJailed(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_ACTION_BREAK.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (jail.isJailed(player)) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_ACTION_INTERACT.toString()));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.jailedRemaining.containsKey(player.getName())) {
			long remainingTime = plugin.jailedRemaining.get(player.getName());
			Location jailLocation = player.getLocation();
			Location playerLocation = plugin.jailLocation.get(player.getName());
			jail.jail(player, playerLocation, jailLocation, remainingTime);
			plugin.jailedRemaining.remove(player.getName());

		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (jail.isJailed(player)) {
			long startTime = plugin.jailedStart.get(player.getName());
			long timerDuration = plugin.jailed.get(player.getName());
			long currentTime = methods.getCurrentTime();
			long remainingTime = timerDuration - (currentTime - startTime);
			plugin.jailedRemaining.put(player.getName(), remainingTime);
			jail.pauseJail(player);
		}
	}
}
