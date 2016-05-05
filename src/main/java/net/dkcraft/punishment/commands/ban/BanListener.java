package net.dkcraft.punishment.commands.ban;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.commands.ban.methods.BanMethods;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.util.Config;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class BanListener implements Listener {

	public Main plugin;
	public Methods methods;
	public Config config;
	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;

	long banLength;
	long unbanDateLong;

	public BanListener(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.config = this.plugin.config;
		this.banmethods = this.plugin.banmethods;
		this.banmysql = this.plugin.banmysql;
		this.bansqlite = this.plugin.bansqlite;
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {

		if (methods.getDatabaseProvider().equals("sqlite") || methods.getDatabaseProvider().equals("mysql")) {

			Player target = event.getPlayer();
			String targetUUID = target.getUniqueId().toString();

			long currentTime = methods.getCurrentTime();

			if (methods.getDatabaseProvider().equals("sqlite")) {
				bansqlite.setPlayerBanInfo(targetUUID);
				if (!banmethods.isPlayerBanned(targetUUID)) {
					return;
				}
			}

			if (methods.getDatabaseProvider().equals("mysql")) {
				banmysql.setPlayerBanInfo(targetUUID);
				if (!banmethods.isPlayerBanned(targetUUID)) {
					return;
				}
			}

			Long banDate = plugin.playerBans.get(targetUUID).get(0).getBanDate();
			banLength = plugin.playerBans.get(targetUUID).get(0).getBanLength();
			unbanDateLong = banDate + banLength;

			if (currentTime >= unbanDateLong) {
				
				if (methods.getDatabaseProvider().equals("sqlite")) {
					bansqlite.unbanPlayer(null, null, unbanDateLong, null, targetUUID);
				}
				
				if (methods.getDatabaseProvider().equals("mysql")) {
					banmysql.unbanPlayer(null, null, unbanDateLong, null, targetUUID);
				}
				
			} else {
				String senderName = plugin.playerBans.get(targetUUID).get(0).getSenderName();
				String banReason = plugin.playerBans.get(targetUUID).get(0).getBanReason();

				String banTime = methods.getDurationString(banLength);
				String unbanDate = methods.getUnbanDate(unbanDateLong);

				event.setKickMessage(ChatColor.translateAlternateColorCodes('&',
						Lang.BAN_TARGET.toString().replace("%sender%", senderName).replace("%time%", banTime).replace("%date%", unbanDate).replace("%message%", banReason)));
				event.setResult(Result.KICK_BANNED);
			}
		}
	}
}
