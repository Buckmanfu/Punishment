package net.dkcraft.punishment.commands.freeze;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class FreezeMethods {

	public Main plugin;
	public Methods methods;
	public BukkitTask task;

	public FreezeMethods(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	public boolean canBeFrozen(CommandSender sender, Player target, String targetName) {
		if (target == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ONLINE_FALSE.toString().replace("%target%", targetName)));
			return false;
		}

		if (target == sender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_SELF.toString()));
			return false;
		}

		if (isFrozen(target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_TRUE.toString().replace("%target%", target.getName())));
			return false;
		}

		if (isImmune(sender, target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_IMMUNE.toString().replace("%target%", target.getName())));
			return false;
		}
		return true;
	}

	public boolean isFrozen(Player target) {
		if (target != null && plugin.frozen.containsKey(target.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isImmune(CommandSender sender, Player target) {
		if (target.hasPermission("punishment.immune.freeze") && !(sender instanceof ConsoleCommandSender)) {
			return true;
		} else {
			return false;
		}
	}

	public void freeze(final Player target, long time) {

		long currentTime = methods.getCurrentTime();

		plugin.frozen.put(target.getName(), time);
		plugin.frozenStart.put(target.getName(), currentTime);

		this.task = new BukkitRunnable() {
			public void run() {
				unFreeze(target, "CONSOLE");
				methods.log(target.getName() + " was unfrozen automatically");
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_UNFREEZE_AUTO.toString()));
			}
		}.runTaskLater(plugin, time * 20);
	}

	public void unFreeze(Player target, String sender) {

		plugin.frozen.remove(target.getName());
		plugin.frozenStart.remove(target.getName());

		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}

	public boolean isInVehicle(Player target) {
		if (target.isInsideVehicle()) {
			return true;
		} else {
			return false;
		}
	}
}
