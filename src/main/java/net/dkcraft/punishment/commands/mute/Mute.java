package net.dkcraft.punishment.commands.mute;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Mute implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public MuteMethods mute;

	public Mute(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.mute = this.plugin.mute;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (args.length == 2) {

				Player target = Bukkit.getPlayer(args[0]);

				String timeString = args[1];

				if (mute.canBeMuted(sender, target, args[0])) {
					if (methods.isValidTimeString(timeString)) {

						long time = methods.parse(timeString);
						String timeDurationString = methods.getDurationString(time);

						mute.mute(target, time);

						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_SENDER.toString().replace("%target%", target.getName())));
						target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.MUTE_TARGET.toString().replace("%sender%", sender.getName()).replace("%time%", timeDurationString)));

						methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);

					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/mute <player> <time s,m,h,d,w>")));
					}
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/mute <player> <time s,m,h,d,w>")));
			}
		}
		return true;
	}
}
