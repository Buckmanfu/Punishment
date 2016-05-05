package net.dkcraft.punishment.commands.ban.methods;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.zaxxer.hikari.HikariDataSource;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Config;
import net.dkcraft.punishment.util.Methods;

public class BanMethodsMySQL {

	public Main plugin;
	public Methods methods;
	public Config config;
	public BanMethods banmethods;
	
	ResultSet result = null;

	public BanMethodsMySQL(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
		this.config = this.plugin.config;
		this.banmethods = this.plugin.banmethods;
	}
	
	public void openConnection() {

		plugin.log.info("[Punishment] Opening MySQL connection...");

		String host = config.getMysqlHost();
		String port = config.getMysqlPort();
		String database = config.getMysqlDatabase();
		String user = config.getMysqlUser();
		String password = config.getMysqlPassword();

		plugin.ds = new HikariDataSource();
		plugin.ds.setMaximumPoolSize(10);
		plugin.ds.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		plugin.ds.addDataSourceProperty("serverName", host);
		plugin.ds.addDataSourceProperty("port", port);
		plugin.ds.addDataSourceProperty("databaseName", database);
		plugin.ds.addDataSourceProperty("user", user);
		plugin.ds.addDataSourceProperty("password", password);
	}

	@SuppressWarnings("resource")
	public synchronized void createTables() {

		Connection connection = null;

		String queryPlayerBans = "CREATE TABLE IF NOT EXISTS pun_player_bans (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," 
				+ "target_uuid VARCHAR(50)," 
				+ "target_name VARCHAR(50),"
				+ "sender_uuid VARCHAR(50)," 
				+ "sender_name VARCHAR(50)," 
				+ "ban_date LONG," 
				+ "ban_length LONG," 
				+ "ban_reason VARCHAR(255)," 
				+ "active BOOLEAN," 
				+ "permanent BOOLEAN,"
				+ "unban_uuid VARCHAR(50)," 
				+ "unban_name VARCHAR(50)," 
				+ "unban_date LONG," 
				+ "unban_reason VARCHAR(255));";

		String queryIPBans = "CREATE TABLE IF NOT EXISTS pun_ip_bans (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," 
				+ "ip_address VARCHAR(50)," 
				+ "sender_uuid VARCHAR(50)," 
				+ "sender_name VARCHAR(50),"
				+ "ban_date LONG," 
				+ "ban_length LONG," 
				+ "ban_reason VARCHAR(255)," 
				+ "active BOOLEAN," 
				+ "permanent BOOLEAN," 
				+ "unban_uuid VARCHAR(50)," 
				+ "unban_name VARCHAR(50),"
				+ "unban_date LONG," 
				+ "unban_reason VARCHAR(255));";

		PreparedStatement statement = null;

		try {
			connection = plugin.ds.getConnection();

			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet resultSet;

			resultSet = metadata.getTables(null, null, "pun_player_bans", null);
			if (!resultSet.next()) {
				statement = connection.prepareStatement(queryPlayerBans);
				statement.executeUpdate();
				plugin.log.info("[Punishment] Created MySQL tables successfully.");
			}

			resultSet = metadata.getTables(null, null, "pun_ip_bans", null);
			if (!resultSet.next()) {
				statement = connection.prepareStatement(queryIPBans);
				statement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setPlayerBanInfo(String targetUUID) {

		Connection connection = null;
		String query = "SELECT * FROM pun_player_bans WHERE target_uuid = ? ORDER BY ban_date DESC";
		PreparedStatement statement = null;

		try {
			connection = plugin.ds.getConnection();

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
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setGlobalBanInfo(int amount) {

		Connection connection = null;
		String query = "SELECT * FROM pun_player_bans ORDER BY ban_date DESC LIMIT ?";
		PreparedStatement statement = null;

		try {
			connection = plugin.ds.getConnection();

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
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void banPlayer(String targetUUID, String targetName, String senderUUID, String senderName, long banDate, long banLength, String banReason, boolean permanent) {

		Connection connection = null;
		String query = "INSERT INTO pun_player_bans(target_uuid, target_name, sender_uuid, sender_name, ban_date, ban_length, ban_reason, active, permanent) " + "VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement statement = null;

		try {
			connection = plugin.ds.getConnection();

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

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void unbanPlayer(String unbanUUID, String unbanName, long unbanDate, String unbanReason, String targetUUID) {

		Connection connection = null;
		String query = "UPDATE pun_player_bans " + "SET active = ?, unban_uuid = ?, unban_name = ?, unban_date = ?, unban_reason = ? " + "WHERE target_uuid = ? " + "AND active = ?";
		PreparedStatement statement = null;

		try {
			connection = plugin.ds.getConnection();

			statement = connection.prepareStatement(query);
			statement.setBoolean(1, false);
			statement.setString(2, unbanUUID);
			statement.setString(3, unbanName);
			statement.setLong(4, unbanDate);
			statement.setString(5, unbanReason);
			statement.setString(6, targetUUID);
			statement.setBoolean(7, true);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void importBans() {

		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
			public void run() {

				plugin.log.info("[Punishment] Attempting to import bans...");

				Connection connection = null;
				String query = "INSERT INTO pun_player_bans(target_uuid, target_name, sender_uuid, sender_name, ban_date, ban_length, ban_reason, active, permanent) " + "VALUES(?,?,?,?,?,?,?,?,?)";
				PreparedStatement statement = null;

				try {
					connection = plugin.ds.getConnection();

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
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (statement != null) {
						try {
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
