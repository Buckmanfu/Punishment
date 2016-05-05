package net.dkcraft.punishment.commands.ban.methods;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class BanMethods {

	public Main plugin;
	public Methods methods;

	public BanMethods(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	public boolean hasPlayerBeenBanned(String targetUUID) {
		if (plugin.playerBans.get(targetUUID).size() != 0) {
			return true;
		}
		return false;
	}

	public boolean isPlayerBanned(String targetUUID) {
		if (hasPlayerBeenBanned(targetUUID)) {
			return plugin.playerBans.get(targetUUID).get(0).getActive();
		}
		return false;
	}

	public boolean isPlayerPermBanned(String targetUUID) {
		if (isPlayerBanned(targetUUID)) {
			return plugin.playerBans.get(targetUUID).get(0).getPermanent();
		}
		return false;
	}
	
	public void sendBanInfo(CommandSender sender, String senderName, String targetName, long banDate, long banLength, String banReason, boolean active, boolean permanent, long unbanDate) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_HEAD.toString().replace("%target%", targetName).replace("%sender%", senderName).replace("%date%", methods.getUnbanDate(banDate))));

		if (active) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_ACTIVE.toString().replace("%active%", "True")));
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_ACTIVE.toString().replace("%active%", "False")));
		}
		
		if (permanent) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_LENGTH.toString().replace("%length%", "Permanent")));
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_LENGTH.toString().replace("%length%", methods.getDurationString(banLength))));
		}

		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_REASON.toString().replace("%reason%", banReason)));

		if (permanent) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_EXPIRES.toString().replace("%expires%", "Never")));
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.BAN_INFO_EXPIRES.toString().replace("%expires%", methods.getUnbanDate(unbanDate))));
		}
	}
	
	public ArrayList<BanInfo> getListProductFromTextFile(String filePath) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader bReader = null;
		ArrayList<BanInfo> listResult = new ArrayList<BanInfo>();
		try {
			fis = new FileInputStream(filePath);
			isr = new InputStreamReader(fis);
			bReader = new BufferedReader(isr);
			String line = null;

			while (true) {
				line = bReader.readLine();
				if (line == null) {
					break;
				} else {
					listResult.add(new BanInfo(line, null, 0, 0, null, false, false));
				}
			}

		} catch (Exception e) {
			System.out.println("Read file error");
			e.printStackTrace();
		} finally {
			try {
				bReader.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listResult;
	}
}
