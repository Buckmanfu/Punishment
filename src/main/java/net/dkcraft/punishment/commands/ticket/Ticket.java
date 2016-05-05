package net.dkcraft.punishment.commands.ticket;

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

public class Ticket implements CommandExecutor {

	public Main plugin;
	public Methods methods;
	public TicketMethods ticket;

	public Ticket(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.ticket = this.plugin.ticket;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ticket")) {

			if (args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket list/create/claim/delete/view/teleport")));
				return true;
			}

			if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
				if (sender.hasPermission("punishment.ticket.list")) {
					if (args.length == 1) {
						if (!ticket.isEmpty()) {
							ticket.listTickets(sender);
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0]);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_LIST_NULL.toString()));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket list")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
				if (sender.hasPermission("punishment.ticket.create")) {
					if (args.length >= 2) {
						if (!ticket.hasTicket(sender)) {
							String message = StringUtils.join(args, ' ', 1, args.length);
							ticket.createTicket(sender, message);
							ticket.notifyStaff(message, sender);
							ticket.sendQueueMessage(sender);
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + message);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_TRUE.toString()));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket create <message>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("cl")) {
				if (sender.hasPermission("punishment.ticket.claim")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null && ticket.hasTicket(target)) {
							if (!ticket.isClaimed(target)) {
								ticket.claimTicket(target);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
										Lang.TICKET_CLAIM_SENDER.toString().replace("%target%", target.getName()).replace("%ticket%", plugin.tickets.get(target.getName()))));
								methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_CLAIMED_TRUE.toString().replace("%target%", target.getName())));
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_FALSE.toString().replace("%target%", args[1])));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket claim <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d")) {
				if (sender.hasPermission("punishment.ticket.delete")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null && ticket.hasTicket(target)) {
							ticket.deleteTicket(target);
							if (ticket.isClaimed(target)) {
								plugin.claimedTicket.remove(target.getName());
							}
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_DELETE.toString().replace("%target%", target.getName())));
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_FALSE.toString().replace("%target%", args[1])));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket delete <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
				if (sender.hasPermission("punishment.ticket.view")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null && ticket.hasTicket(target)) {
							ticket.viewTicket(sender, target);
							methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_FALSE.toString().replace("%target%", args[1])));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket view <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}

			} else if (args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("tp")) {
				if (sender.hasPermission("punishment.ticket.teleport")) {
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null && ticket.hasTicket(target)) {
							if (!ticket.isClaimed(target)) {
								ticket.teleportTicket(sender, target);
								ticket.claimTicket(target);
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
										Lang.TICKET_CLAIM_SENDER.toString().replace("%target%", target.getName()).replace("%ticket%", plugin.tickets.get(target.getName()))));
								methods.log(sender.getName() + " issued command: /" + cmd.getName() + " " + args[0] + " " + args[1]);
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_CLAIMED_TRUE.toString().replace("%target%", target.getName())));
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_EXISTS_FALSE.toString().replace("%target%", args[1])));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket teleport <player>")));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.NO_PERMISSIONS.toString()));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.INCORRECT_COMMAND_USAGE.toString().replace("%usage%", "/ticket list/create/claim/delete/view/teleport")));
			}
		}
		return true;
	}
}
