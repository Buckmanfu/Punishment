package net.dkcraft.punishment.commands.jail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Jail implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public JailMethods jail;

	public Jail(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.jail = this.plugin.jail;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("jail")) {
			if (args.length == 3) {

				Player target = Bukkit.getPlayer(args[0]);
				String jailName = args[1].toLowerCase();

				String timeString = args[2];

				if (jail.canBeJailed(sender, target, args[0])) {
					if (jail.jailExists(jailName)) {
						if (methods.isValidTimeString(timeString)) {

							long time = methods.parse(timeString);
							String timeDurationString = methods.getDurationString(time);

							Location playerLocation = target.getLocation();

							Location jailLocation = jail.getJailLocation(jailName);

							jail.jail(target, playerLocation, jailLocation, time);

							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_SENDER.toString().replace("%target%", target.getName())));
							target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_TARGET.toString().replace("%sender%", sender.getName()).replace("%time%", timeDurationString)));

							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1] + " " + args[2]);

						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/jail <player> <jail> <time s,m,h,d,w>")));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_EXISTS_FALSE.toString().replace("%jail%", jailName)));
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/jail <player> <jail> <time s,m,h,d,w>")));
			}
		}
		return true;
	}
}
