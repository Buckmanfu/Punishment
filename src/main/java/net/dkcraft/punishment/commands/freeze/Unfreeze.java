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

public class Unfreeze implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public FreezeMethods freeze;

	public Unfreeze(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.freeze = this.plugin.freeze;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unfreeze")) {
			if (args.length == 1) {

				Player target = Bukkit.getPlayer(args[0]);

				if (!freeze.isFrozen(target)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_FALSE.toString().replace("%target%", args[0])));
					return true;
				}

				freeze.unFreeze(target, sender.getName());

				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_UNFREEZE_SENDER.toString().replace("%target%", target.getName())));
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.FREEZE_UNFREEZE_TARGET.toString().replace("%sender%", sender.getName())));

				methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/unfreeze <player>")));
			}
		}
		return true;
	}
}
