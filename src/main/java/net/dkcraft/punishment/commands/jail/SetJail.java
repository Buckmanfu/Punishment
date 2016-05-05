package net.dkcraft.punishment.commands.jail;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class SetJail implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public JailMethods jail;

	public SetJail(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.jail = this.plugin.jail;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setjail") || cmd.getName().equalsIgnoreCase("createjail")) {
			if (args.length == 1) {

				Player player = (Player) sender;
				Location location = player.getLocation();

				String jailName = args[0].toLowerCase();

				if (!jail.jailExists(jailName)) {

					jail.setJail(jailName, location);

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_SET.toString().replace("%jail%", jailName)));

					methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);

				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_EXISTS_TRUE.toString().replace("%jail%", jailName)));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/setjail <jail>")));
			}
		}
		return true;
	}
}
