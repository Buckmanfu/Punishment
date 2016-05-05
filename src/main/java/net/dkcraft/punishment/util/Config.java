package net.dkcraft.punishment.util;

import net.dkcraft.punishment.Main;

public class Config {

	public Main plugin;

	public Config(Main plugin) {
		this.plugin = plugin;
	}

	private boolean loggingEnabled;
	private boolean debugEnabled;
	private boolean updatesEnabled;
	private boolean metricsEnabled;
	private String dateFormat;
	private String dbProvider;

	private String sqlitePath;

	private String mysqlUser;
	private String mysqlPort;
	private String mysqlPassword;
	private String mysqlHost;
	private String mysqlDatabase;

	public void loadConfig() {
		this.setLoggingEnabled(plugin.getConfig().getBoolean("settings.logging"));
		this.setDebugEnabled(plugin.getConfig().getBoolean("settings.debug"));
		this.setUpdatesEnabled(plugin.getConfig().getBoolean("settings.updates"));
		this.setMetricsEnabled(plugin.getConfig().getBoolean("settings.metrics"));
		this.setDateFormat(plugin.getConfig().getString("settings.date-format"));
		this.setDatabaseProvider(plugin.getConfig().getString("settings.db-provider"));

		this.setSqlitePath(plugin.getConfig().getString("sqlite.path"));

		this.setMysqlUser(plugin.getConfig().getString("mysql.user"));
		this.setMysqlPort(plugin.getConfig().getString("mysql.port"));
		this.setMysqlPassword(plugin.getConfig().getString("mysql.password"));
		this.setMysqlHost(plugin.getConfig().getString("mysql.host"));
		this.setMysqlDatabase(plugin.getConfig().getString("mysql.database"));
	}

	// Settings
	public void setLoggingEnabled(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	public boolean getLoggingEnabled() {
		return this.loggingEnabled;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public boolean getDebugEnabled() {
		return this.debugEnabled;
	}

	public void setUpdatesEnabled(boolean updatesEnabled) {
		this.updatesEnabled = updatesEnabled;
	}

	public boolean getUpdatesEnabled() {
		return this.updatesEnabled;
	}

	public void setMetricsEnabled(boolean metricsEnabled) {
		this.metricsEnabled = metricsEnabled;
	}

	public boolean getMetricsEnabled() {
		return this.metricsEnabled;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return this.dateFormat;
	}

	public void setDatabaseProvider(String dbProvider) {
		this.dbProvider = dbProvider;
	}

	public String getDatabaseProvider() {
		return this.dbProvider;
	}

	// SQLite
	public void setSqlitePath(String sqlitePath) {
		this.sqlitePath = sqlitePath;
	}

	public String getSqlitePath() {
		return this.sqlitePath;
	}

	// MySQL
	public void setMysqlUser(String mysqlUser) {
		this.mysqlUser = mysqlUser;
	}

	public String getMysqlUser() {
		return this.mysqlUser;
	}

	public void setMysqlPort(String mysqlPort) {
		this.mysqlPort = mysqlPort;
	}

	public String getMysqlPort() {
		return this.mysqlPort;
	}

	public void setMysqlPassword(String mysqlPassword) {
		this.mysqlPassword = mysqlPassword;
	}

	public String getMysqlPassword() {
		return this.mysqlPassword;
	}

	public void setMysqlHost(String mysqlHost) {
		this.mysqlHost = mysqlHost;
	}

	public String getMysqlHost() {
		return this.mysqlHost;
	}

	public void setMysqlDatabase(String mysqlDatabase) {
		this.mysqlDatabase = mysqlDatabase;
	}

	public String getMysqlDatabase() {
		return this.mysqlDatabase;
	}
}
