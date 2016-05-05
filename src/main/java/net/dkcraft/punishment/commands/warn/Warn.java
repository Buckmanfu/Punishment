package net.dkcraft.punishment.commands.warn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Warn implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public WarnMethods warn;

	public Warn(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.warn = this.plugin.warn;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warn")) {
			if (args.length >= 2) {

				Player target = Bukkit.getPlayer(args[0]);
				String message = "";

				if (warn.playerChecks(sender, target, args[0])) {
					return true;
				}

				for (int i = 1; i < args.length; ++i) {
					message += args[i] + " ";
				}

				warn.warn(target, sender, message);

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.WARN_SENDER.toString().replace("%target%", target.getName())));

				methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + message);

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/warn <player> <reason>")));
			}
		}
		return true;
	}
}
