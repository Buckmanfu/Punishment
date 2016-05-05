package net.dkcraft.punishment.commands.jail;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class DelJail implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public JailMethods jail;

	public DelJail(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.jail = this.plugin.jail;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("deljail") || cmd.getName().equalsIgnoreCase("removejail")) {
			if (args.length == 1) {

				String jailName = args[0].toLowerCase();

				if (jail.jailExists(jailName)) {

					jail.delJail(jailName);

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_DEL.toString().replace("%jail%", jailName)));

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_EXISTS_FALSE.toString().replace("%jail%", jailName)));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/deljail <jail>")));
			}
		}
		return true;
	}
}
