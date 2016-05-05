package net.dkcraft.punishment.commands.mute;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class MuteMethods {

	public Main plugin;
	public Methods methods;
	public BukkitTask task;

	public MuteMethods(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	public boolean canBeMuted(CommandSender sender, Player target, String playerName) {
		if (target == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ONLINE_FALSE.toString().replace("%target%", playerName)));
			return false;
		}

		if (target == sender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_SELF.toString()));
			return false;
		}

		if (isMuted(target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_TRUE.toString().replace("%target%", target.getName())));
			return false;
		}

		if (isImmune(sender, target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_IMMUNE.toString().replace("%target%", target.getName())));
			return false;
		}
		return true;
	}

	public boolean isMuted(Player target) {
		if (target != null && plugin.muted.containsKey(target.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isImmune(CommandSender sender, Player target) {
		if (target.hasPermission("punishment.immune.mute") && !(sender instanceof ConsoleCommandSender)) {
			return true;
		} else {
			return false;
		}
	}

	public void mute(final Player target, long time) {

		long currentTime = methods.getCurrentTime();

		plugin.muted.put(target.getName(), time);
		plugin.mutedStart.put(target.getName(), currentTime);

		this.task = new BukkitRunnable() {
			public void run() {
				unMute(target, "CONSOLE");
				methods.log(target.getName() + " was unmuted automatically");
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_UNMUTE_AUTO.toString()));
			}
		}.runTaskLater(plugin, time * 20);
	}

	public void unMute(Player target, String sender) {

		plugin.muted.remove(target.getName());
		plugin.mutedStart.remove(target.getName());

		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}
}
