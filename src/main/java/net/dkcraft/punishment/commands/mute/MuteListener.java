package net.dkcraft.punishment.commands.mute;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class MuteListener implements Listener {

	public Main plugin;
	public Methods methods;
	public MuteMethods mute;

	public MuteListener(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.mute = this.plugin.mute;
	}

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (mute.isMuted(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_ACTION_SPEAK.toString()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.mutedRemaining.containsKey(player.getName())) {
			long remainingTime = plugin.mutedRemaining.get(player.getName());
			mute.mute(player, remainingTime);
			plugin.mutedRemaining.remove(player.getName());

		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (mute.isMuted(player)) {
			long startTime = plugin.mutedStart.get(player.getName());
			long timerDuration = plugin.muted.get(player.getName());
			long currentTime = methods.getCurrentTime();
			long remainingTime = timerDuration - (currentTime - startTime);
			plugin.mutedRemaining.put(player.getName(), remainingTime);
			mute.unMute(player, null);
		}
	}
}
