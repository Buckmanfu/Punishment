package net.dkcraft.punishment.commands.ban.methods;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Config;
import net.dkcraft.punishment.util.Methods;

public class BanMethodsSQLite {

	public Main plugin;
	public Methods methods;
	public Config config;
	public BanMethods banmethods;

	public Connection connection;
	ResultSet result = null;

	public BanMethodsSQLite(Main plugin) {
		this.plugin = plugin;
		this.config = this.plugin.config;
		this.methods = this.plugin.methods;
		this.banmethods = this.plugin.banmethods;
	}

	public void createTables() {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;

		String queryPlayerBans = "CREATE TABLE IF NOT EXISTS pun_player_bans (id INTEGER PRIMARY KEY," 
				+ "target_uuid TEXT(50)," 
				+ "target_name TEXT(50)," 
				+ "sender_uuid TEXT(50),"
				+ "sender_name TEXT(50)," 
				+ "ban_date INTEGER," 
				+ "ban_length INTEGER," 
				+ "ban_reason TEXT(255)," 
				+ "active BOOLEAN," 
				+ "permanent BOOLEAN," 
				+ "unban_uuid TEXT(50),"
				+ "unban_name TEXT(50)," 
				+ "unban_date INTEGER," 
				+ "unban_reason TEXT(255));";

		String queryIPBans = "CREATE TABLE IF NOT EXISTS pun_ip_bans (id INTEGER PRIMARY KEY," 
				+ "ip_address TEXT(50)," 
				+ "sender_uuid TEXT(50)," 
				+ "sender_name TEXT(50)," 
				+ "ban_date INTEGER,"
				+ "ban_length INTEGER," 
				+ "ban_reason TEXT(255)," 
				+ "active BOOLEAN," 
				+ "permanent BOOLEAN," 
				+ "unban_uuid TEXT(50)," 
				+ "unban_name TEXT(50)," 
				+ "unban_date INTEGER,"
				+ "unban_reason TEXT(255));";

		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());
			connection.setAutoCommit(false);

			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet resultSet;

			resultSet = metadata.getTables(null, null, "pun_player_bans", null);
			if (!resultSet.next()) {
				statement = connection.prepareStatement(queryPlayerBans);
				statement.executeUpdate();
			}

			resultSet = metadata.getTables(null, null, "pun_ip_bans", null);
			if (!resultSet.next()) {
				statement = connection.prepareStatement(queryIPBans);
				statement.executeUpdate();
			}

			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		plugin.log.info("[Punishment] Created SQLite tables successfully.");
	}

	public void setPlayerBanInfo(String targetUUID) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		String query = "SELECT * FROM pun_player_bans WHERE target_uuid = ? ORDER BY ban_date DESC";
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query);
			statement.setString(1, targetUUID);

			result = statement.executeQuery();

			ArrayList<BanInfo> bans = new ArrayList<BanInfo>();

			while (result.next()) {

				String targetName = result.getString(3);
				String senderName = result.getString(5);
				long banDate = result.getLong(6);
				long banLength = result.getLong(7);
				String banReason = result.getString(8);
				boolean active = result.getBoolean(9);
				boolean permanent = result.getBoolean(10);

				bans.add(new BanInfo(targetName, senderName, banDate, banLength, banReason, active, permanent));
			}

			plugin.playerBans.put(targetUUID, bans);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setGlobalBanInfo(int amount) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		String query = "SELECT * FROM pun_player_bans ORDER BY ban_date DESC LIMIT ?";
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query);
			statement.setInt(1, amount);

			result = statement.executeQuery();

			int i = 0;
			while (result.next()) {

				String targetName = result.getString(3);
				String senderName = result.getString(5);
				long banDate = result.getLong(6);
				long banLength = result.getLong(7);
				String banReason = result.getString(8);
				boolean active = result.getBoolean(9);
				boolean permanent = result.getBoolean(10);

				plugin.globalBans.put(i, new BanInfo(targetName, senderName, banDate, banLength, banReason, active, permanent));

				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void banPlayer(String targetUUID, String targetName, String senderUUID, String senderName, long banDate, long banLength, String banReason, boolean permanent) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		String query = "INSERT INTO pun_player_bans(target_uuid, target_name, sender_uuid, sender_name, ban_date, ban_length, ban_reason, active, permanent) " + "VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query);
			statement.setString(1, targetUUID);
			statement.setString(2, targetName);
			statement.setString(3, senderUUID);
			statement.setString(4, senderName);
			statement.setLong(5, banDate);
			statement.setLong(6, banLength);
			statement.setString(7, banReason);
			statement.setBoolean(8, true);
			statement.setBoolean(9, permanent);
			statement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void unbanPlayer(String unbanUUID, String unbanName, long unbanDate, String unbanReason, String targetUUID) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection connection = null;
		String query = "UPDATE pun_player_bans " + "SET active = ?, unban_uuid = ?, unban_name = ?, unban_date = ?, unban_reason = ? " + "WHERE target_uuid = ? " + "AND active = ?";
		PreparedStatement statement = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query);
			statement.setBoolean(1, false);
			statement.setString(2, unbanUUID);
			statement.setString(3, unbanName);
			statement.setLong(4, unbanDate);
			statement.setString(5, unbanReason);
			statement.setString(6, targetUUID);
			statement.setBoolean(7, true);
			statement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void importBans() {

		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
			public void run() {

				plugin.log.info("[Punishment] Attempting to import bans...");

				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				Connection connection = null;
				String query = "INSERT INTO pun_player_bans(target_uuid, target_name, sender_uuid, sender_name, ban_date, ban_length, ban_reason, active, permanent) " + "VALUES(?,?,?,?,?,?,?,?,?)";
				PreparedStatement statement = null;

				try {

					connection = DriverManager.getConnection("jdbc:sqlite:" + config.getSqlitePath());

					statement = connection.prepareStatement(query);

					ArrayList<BanInfo> banList = banmethods.getListProductFromTextFile(plugin.getDataFolder().getAbsolutePath() + "/bannedplayers.txt");

					for (int i = 0; i < banList.size(); i++) {

						String targetName = banList.get(i).getTargetName();
						String targetUUID = Bukkit.getOfflinePlayer(targetName).getUniqueId().toString();
						String senderName = "CONSOLE";
						String senderUUID = Bukkit.getOfflinePlayer(senderName).getUniqueId().toString();
						long banDate = methods.getCurrentTime();
						long banLength = 0;
						String banReason = "Ban import.";
						boolean permanent = true;

						statement.setString(1, targetUUID);
						statement.setString(2, targetName);
						statement.setString(3, senderUUID);
						statement.setString(4, senderName);
						statement.setLong(5, banDate);
						statement.setLong(6, banLength);
						statement.setString(7, banReason);
						statement.setBoolean(8, true);
						statement.setBoolean(9, permanent);

						statement.executeUpdate();

						plugin.log.info("[Punishment] Successfully imported user: " + targetName);
					}

					plugin.log.info("[Punishment] Import complete.");

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
