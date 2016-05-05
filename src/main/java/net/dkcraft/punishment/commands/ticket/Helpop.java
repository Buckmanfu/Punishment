package net.dkcraft.punishment.commands.ticket;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class Helpop implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public TicketMethods ticket;

	public Helpop(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.ticket = this.plugin.ticket;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("helpop")) {
			if (args.length > 0) {

				Player player = (Player) sender;
				String message = StringUtils.join(args, ' ', 0, args.length);

				if (!ticket.hasTicket(player)) {
					ticket.createTicket(player, message);
					ticket.notifyStaff(message, player);
					ticket.sendQueueMessage(player);
				} else if (ticket.isClaimed(player)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_CLAIM_TARGET.toString().replace("%sender%", sender.getName())));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_TRUE.toString()));
				}

				methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + message);

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/helpop <message>")));
			}
		}
		return true;
	}
}
