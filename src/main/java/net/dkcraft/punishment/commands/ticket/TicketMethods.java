package net.dkcraft.punishment.commands.ticket;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.lang.Lang;

public class TicketMethods {

	public Main plugin;

	public Scoreboard scoreboard;

	ChatColor RED = ChatColor.RED;
	ChatColor GREEN = ChatColor.GREEN;

	public TicketMethods(Main plugin) {
		this.plugin = plugin;
	}

	// Setup scoreboard
	public void setupScoreboard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();

		Objective tickets = scoreboard.registerNewObjective("tickets", "dummy");
		tickets.setDisplayName("tickets");
		tickets.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	// Get Scoreboard
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	// List tickets
	public void listTickets(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_LIST_AMOUNT.toString().replace("%number%", "" + getTicketsSize())));
		int count = 1;
		for (Entry<String, String> entry : plugin.tickets.entrySet()) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					Lang.TICKET_LIST.toString().replace("%number%", "" + count++).replace("%target%", entry.getKey()).replace("%ticket%", entry.getValue())));
		}
	}

	// Create ticket
	public void createTicket(CommandSender sender, String message) {
		plugin.tickets.put(sender.getName(), message);
		getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(RED + sender.getName()).setScore(1);
	}

	// Send queue message
	public void sendQueueMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_CREATE.toString()));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_QUEUE.toString().replace("%number%", "" + getTicketsSize())));
	}

	// Claim ticket
	public void claimTicket(CommandSender sender) {
		plugin.claimedTicket.add(sender.getName());
		getScoreboard().resetScores(RED + sender.getName());
		getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(GREEN + sender.getName()).setScore(0);
	}

	// If ticket is claimed
	public boolean isClaimed(CommandSender sender) {
		return plugin.claimedTicket.contains(sender.getName());
	}

	// Delete ticket
	public void deleteTicket(CommandSender sender) {
		plugin.tickets.remove(sender.getName());
		getScoreboard().resetScores(RED + sender.getName());
		getScoreboard().resetScores(GREEN + sender.getName());
	}

	// View ticket
	public void viewTicket(CommandSender sender, Player target) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_VIEW.toString().replace("%target%", target.getName()).replace("%ticket%", plugin.tickets.get(sender.getName()))));
	}

	// Teleport ticket
	public void teleportTicket(CommandSender sender, Player target) {
		((Player) sender).teleport(target.getLocation());
	}

	// Get ticket amount
	public int getTicketsSize() {
		return plugin.tickets.size();
	}

	// If tickets is empty
	public boolean isEmpty() {
		return plugin.tickets.isEmpty();
	}

	// If player has ticket
	public boolean hasTicket(CommandSender sender) {
		return plugin.tickets.containsKey(sender.getName());
	}

	// Notify online staff
	public void notifyStaff(String message, CommandSender sender) {
		Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', Lang.TICKET_NOTIFY.toString().replace("%target%", sender.getName()).replace("%ticket%", message)), "punishment.ticket.staff");
	}
}
