package net.dkcraft.punishment.commands.freeze;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class FreezeListener implements Listener {

	public Main plugin;
	public Methods methods;
	public FreezeMethods freeze;

	public FreezeListener(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.freeze = this.plugin.freeze;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		if (freeze.isFrozen(player)) {
			player.teleport(from);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (freeze.isFrozen(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_ACTION_COMMAND.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (freeze.isFrozen(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_ACTION_PLACE.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (freeze.isFrozen(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_ACTION_BREAK.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (freeze.isFrozen(player)) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_ACTION_INTERACT.toString()));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.frozenRemaining.containsKey(player.getName())) {
			long remainingTime = plugin.frozenRemaining.get(player.getName());
			freeze.freeze(player, remainingTime);
			plugin.frozenRemaining.remove(player.getName());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (freeze.isFrozen(player)) {
			long startTime = plugin.frozenStart.get(player.getName());
			long timerDuration = plugin.frozen.get(player.getName());
			long currentTime = methods.getCurrentTime();
			long remainingTime = timerDuration - (currentTime - startTime);
			plugin.frozenRemaining.put(player.getName(), remainingTime);
			freeze.unFreeze(player, "CONSOLE");
		}
	}
}
