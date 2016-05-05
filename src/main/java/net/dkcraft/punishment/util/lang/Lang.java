package net.dkcraft.punishment.util.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {

	INCORRECT_COMMAND_USAGE("incorrect-command-usage", "&cIncorrect command usage. Usage: %usage%"), 
	NO_PERMISSIONS("no-permissions", "&cYou don't have permission to do that."), 
	PLAYER_ONLINE_FALSE("player-online-false", "&c%target% is offline."), 
	PLAYER_EXISTS_FALSE("player-exists-false", "&c%target% does not exist."),

	BAN_SENDER("ban-sender", "&b%target% &7has been banned."), 
	BAN_TARGET("ban-target", "&cYou have been banned from the server by %sender% for %time%. Your ban will expire: %date%. Reason: %message%"), 
	BAN_UNBAN_SENDER("ban-unban-sender", "&b%target% &7has been unbanned."), 
	BAN_ACTIVE_TRUE("ban-active-true", "&c%target% is already banned."), 
	BAN_ACTIVE_FALSE("ban-active-false", "&c%target% is not banned."), 
	BAN_HISTORY_FALSE("ban-history-false", "&c%target% has no ban history."), 
	BAN_IMMUNE("ban-immune", "&b%target% &7is immune from being banned."), 
	BAN_SELF("ban-self", "&cYou can't ban yourself."), 
	BAN_CONNECT("ban-connect", "&cYou are currently banned from the server by %sender% for %time%. Your ban will expire: %date%. Reason: %message%"),
	BAN_INFO_HEAD("ban-info-head", "&b%target% &7banned by &b%sender% &7on &b%date%"), 
	BAN_INFO_ACTIVE("ban-info-active", " &bActive: &7%active%"), 
	BAN_INFO_LENGTH("ban-info-length", " &bLength: &7%length%"), 
	BAN_INFO_REASON("ban-info-reason", " &bReason: &7%reason%"), 
	BAN_INFO_EXPIRES("ban-info-expires", " &bExpires: &7%expires%"), 

	FREEZE_SENDER("freeze-sender", "&b%target% &7has been frozen."), 
	FREEZE_TARGET("freeze-target", "&cYou have been frozen by %sender% for %time%."), 
	FREEZE_UNFREEZE_SENDER("freeze-unfreeze-sender", "&b%target% &7has been unfrozen."), 
	FREEZE_UNFREEZE_TARGET("freeze-unfreeze-target", "&7You have been unfrozen by &b%sender%."), 
	FREEZE_UNFREEZE_AUTO("freeze-unfreeze-auto", "&7You have been unfrozen."), 
	FREEZE_TRUE("freeze-true", "&c%target% is already frozen."), 
	FREEZE_FALSE("freeze-false", "&c%target% is not frozen."), 
	FREEZE_IMMUNE("freeze-immune", "&c%target% is immune from being frozen."), 
	FREEZE_SELF("freeze-self", "&cYou can't freeze yourself."), 
	FREEZE_ACTION_BREAK("freeze-action-break", "&cYou can't break blocks while frozen."), 
	FREEZE_ACTION_PLACE("freeze-action-place", "&cYou can't place blocks while frozen."), 
	FREEZE_ACTION_INTERACT("freeze-action-interact", "&cYou can't interact with that while frozen."), 
	FREEZE_ACTION_COMMAND("freeze-action-command", "&cYou can't run commands while frozen."),

	JAIL_SENDER("jail-sender", "&b%target% &7has been jailed."), 
	JAIL_TARGET("jail-target", "&cYou have been jailed by %sender% for %time%."), 
	JAIL_UNJAIL_SENDER("jail-unjail-sender", "&b%target% &7has been unjailed."), 
	JAIL_UNJAIL_TARGET("jail-unjail-target", "&7You have been unjailed by &b%sender%."), 
	JAIL_UNJAIL_AUTO("jail-unjail-auto", "&7You have been unjailed."), 
	JAIL_TRUE("jail-true", "&c%target% is already jailed."), 
	JAIL_FALSE("jail-false", "&c%target% is not jailed."), 
	JAIL_SET("jail-set", "&7Set new jail location: &b%jail%"), 
	JAIL_DEL("jail-del", "&7Deleted jail location: &b%jail%"), 
	JAIL_EXISTS_TRUE("jail-exists-true", "&cA jail by the name of '%jail%' already exists."), 
	JAIL_EXISTS_FALSE("jail-exists-false", "&cA jail by the name of '%jail%' does not exist."), 
	JAIL_IMMUNE("jail-immune", "&c%target% is immune from being jailed."), 
	JAIL_SELF("jail-self", "&cYou can't jail yourself."), 
	JAIL_ACTION_BREAK("jail-action-break", "&cYou can't break blocks while jailed."), 
	JAIL_ACTION_PLACE("jail-action-place", "&cYou can't place blocks while jailed."), 
	JAIL_ACTION_INTERACT("jail-action-interact", "&cYou can't interact with that while jailed."), 
	JAIL_ACTION_COMMAND("jail-action-command", "&cYou can't run commands while jailed."),

	KICK_SENDER("kick-sender", "&b%target% &7has been kicked."), 
	KICK_TARGET("kick-target", "&cYou have been kicked from the server by %sender%. Reason: %message%"), 
	KICK_IMMUNE("kick-immune", "&c%target% is immune from being kicked."), 
	KICK_SELF("kick-self", "&cYou can't kick yourself."),

	MUTE_SENDER("muted-sender", "&b%target% &7has been muted."), 
	MUTE_TARGET("muted-target", "&cYou have been muted by %sender% for %time%."), 
	MUTE_UNMUTE_SENDER("mute-unmute-sender", "&b%target% &7has been unmuted."), 
	MUTE_UNMUTE_TARGET("mute-unmute-target", "&7You have been unmuted by &b%sender%."), 
	MUTE_UNMUTE_AUTO("mute-unmute-auto", "&7You have been unmuted."), 
	MUTE_TRUE("mute-true", "&c%target% is already muted."), 
	MUTE_FALSE("mute-false", "&c%target% is not muted."), 
	MUTE_IMMUNE("mute-immune", "&c%target% is immune from being muted."), 
	MUTE_SELF("mute-self", "&cYou can't mute yourself."), 
	MUTE_ACTION_SPEAK("mute-action-speak", "&cYou can't speak while muted."),

	NOTE_ADD("note-add", "&7Added note for &b%target%."), 
	NOTE_DELETE("note-delete", "&7Deleted note for &b%target%."), 
	NOTE_VIEW("note-view", "&7Viewing notes for &b%target%:"), 
	NOTE_LIST("note-list", "&b%number%. &7%note%"), 
	NOTE_EXISTS_FALSE("note-exists-false", "&cThe note for %target% you're trying to delete doesn't exist."),

	TICKET_CREATE("ticket-create", "&bTicket submitted. &7Please wait for a response."), 
	TICKET_QUEUE("ticket-queue", "&bQueue position: &7%number%"), 
	TICKET_NOTIFY("ticket-notify", "&b%target% &7has sumbitted a ticket: &b%ticket%"), 
	TICKET_CLAIM_SENDER("ticket-claim-sender", "&7Claimed &b%target%'s &7ticket: &b%ticket%"), 
	TICKET_CLAIM_TARGET("ticket-claim-target", "&7Your ticket has been claimed by %sender%, please wait."), 
	TICKET_DELETE("ticket-delete", "&7Deleted &b%target%'s &7ticket"), 
	TICKET_LIST_AMOUNT("ticket-list-amount", "&7There are &b%number% &7active tickets."), 
	TICKET_LIST("ticket-list", "&b%number%. %target%: %ticket%"), 
	TICKET_LIST_NULL("ticket-list-null", "&cThere are no tickets to display."), 
	TICKET_VIEW("ticket-view", "&b%target%'s &7current ticket: &b%ticket%"), 
	TICKET_CLAIMED_TRUE("ticket-claimed-true", "&c%target%'s ticket has already been claimed."), 
	TICKET_EXISTS_TRUE("ticket-exists-true", "&cYou have already submitted a ticket, please wait."), 
	TICKET_EXISTS_FALSE("ticket-exists-false", "&c%target% has no ticket."),

	WARN_SENDER("warn-sender", "&b%target% &7has been warned."), 
	WARN_TARGET("warn-target", "&cYou have been warned by %sender%: %message%"), 
	WARN_IMMUNE("warn-immune", "&c%target% is immune from being warned."), 
	WARN_SELF("warn-self", "&cYou can't warn yourself.");

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	Lang(String path, String start) {
		this.path = path;
		this.def = start;
	}

	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		if (this == INCORRECT_COMMAND_USAGE)
			return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}
}
