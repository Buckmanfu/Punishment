package net.dkcraft.punishment.commands.kick;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Kick implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public KickMethods kick;

	public Kick(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.kick = this.plugin.kick;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kick")) {
			if (args.length >= 2) {

				Player target = Bukkit.getPlayer(args[0]);
				String message = StringUtils.join(args, ' ', 1, args.length);

				if (kick.canBeKicked(sender, target, args[0])) {

					kick.kick(target, sender, message);

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.KICK_SENDER.toString().replace("%target%", target.getName())));

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + message);
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/kick <player> <reason>")));
			}
		}
		return true;
	}
}
