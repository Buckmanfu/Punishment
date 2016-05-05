package net.dkcraft.punishment.commands.freeze;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Freeze implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public FreezeMethods freeze;

	public Freeze(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.freeze = this.plugin.freeze;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (args.length == 2) {

				Player target = Bukkit.getPlayer(args[0]);

				String timeString = args[1];

				if (freeze.canBeFrozen(sender, target, args[0])) {
					if (methods.isValidTimeString(timeString)) {

						long time = methods.parse(timeString);
						String timeDurationString = methods.getDurationString(time);

						if (freeze.isInVehicle(target)) {
							target.getVehicle().eject();
						}

						freeze.freeze(target, time);

						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_SENDER.toString().replace("%target%", target.getName())));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_TARGET.toString().replace("%sender%", sender.getName()).replace("%time%", timeDurationString)));

						methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);

					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/freeze <player> <time s,m,h,d,w>")));
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/freeze <player> <time s,m,h,d,w>")));
			}
		}
		return true;
	}
}
