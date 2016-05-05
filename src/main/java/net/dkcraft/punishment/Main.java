package net.dkcraft.punishment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariDataSource;

import net.dkcraft.punishment.commands.Punishment;
import net.dkcraft.punishment.commands.ban.Ban;
import net.dkcraft.punishment.commands.ban.BanCheck;
import net.dkcraft.punishment.commands.ban.BanHistory;
import net.dkcraft.punishment.commands.ban.BanImport;
import net.dkcraft.punishment.commands.ban.BanListener;
import net.dkcraft.punishment.commands.ban.BanRecent;
import net.dkcraft.punishment.commands.ban.Unban;
import net.dkcraft.punishment.commands.ban.methods.BanInfo;
import net.dkcraft.punishment.commands.ban.methods.BanMethods;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsMySQL;
import net.dkcraft.punishment.commands.ban.methods.BanMethodsSQLite;
import net.dkcraft.punishment.commands.freeze.Freeze;
import net.dkcraft.punishment.commands.freeze.FreezeListener;
import net.dkcraft.punishment.commands.freeze.FreezeMethods;
import net.dkcraft.punishment.commands.freeze.Unfreeze;
import net.dkcraft.punishment.commands.jail.DelJail;
import net.dkcraft.punishment.commands.jail.Jail;
import net.dkcraft.punishment.commands.jail.JailInfo;
import net.dkcraft.punishment.commands.jail.JailListener;
import net.dkcraft.punishment.commands.jail.JailMethods;
import net.dkcraft.punishment.commands.jail.SetJail;
import net.dkcraft.punishment.commands.jail.Unjail;
import net.dkcraft.punishment.commands.kick.Kick;
import net.dkcraft.punishment.commands.kick.KickMethods;
import net.dkcraft.punishment.commands.mute.Mute;
import net.dkcraft.punishment.commands.mute.MuteListener;
import net.dkcraft.punishment.commands.mute.MuteMethods;
import net.dkcraft.punishment.commands.mute.Unmute;
import net.dkcraft.punishment.commands.note.Note;
import net.dkcraft.punishment.commands.note.NoteMethods;
import net.dkcraft.punishment.commands.ticket.Helpop;
import net.dkcraft.punishment.commands.ticket.Ticket;
import net.dkcraft.punishment.commands.ticket.TicketListener;
import net.dkcraft.punishment.commands.ticket.TicketMethods;
import net.dkcraft.punishment.commands.warn.Warn;
import net.dkcraft.punishment.commands.warn.WarnMethods;
import net.dkcraft.punishment.listeners.PlayerFileListener;
import net.dkcraft.punishment.listeners.UpdateNotifyListener;
import net.dkcraft.punishment.util.Config;
import net.dkcraft.punishment.util.ListStore;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.LangMethods;

public class Main extends JavaPlugin {

	public Main instance;

	public Config config;
	public ListStore punishmentLog;
	public Methods methods;
	public LangMethods lang;

	public BanMethods banmethods;
	public BanMethodsMySQL banmysql;
	public BanMethodsSQLite bansqlite;
	public FreezeMethods freeze;
	public JailMethods jail;
	public KickMethods kick;
	public MuteMethods mute;
	public NoteMethods note;
	public TicketMethods ticket;
	public WarnMethods warn;
	
	public HikariDataSource ds;

	public HashMap<Integer, BanInfo> globalBans = new HashMap<Integer, BanInfo>();
	public HashMap<String, ArrayList<BanInfo>> playerBans = new HashMap<String, ArrayList<BanInfo>>();

	public HashMap<String, Long> frozen = new HashMap<String, Long>();
	public HashMap<String, Long> frozenStart = new HashMap<String, Long>();
	public HashMap<String, Long> frozenRemaining = new HashMap<String, Long>();

	public HashMap<String, JailInfo> jails = new HashMap<String, JailInfo>();
	public HashMap<String, Location> jailLocation = new HashMap<String, Location>();
	public HashMap<String, Long> jailed = new HashMap<String, Long>();
	public HashMap<String, Long> jailedStart = new HashMap<String, Long>();
	public HashMap<String, Long> jailedRemaining = new HashMap<String, Long>();

	public HashMap<String, Long> muted = new HashMap<String, Long>();
	public HashMap<String, Long> mutedStart = new HashMap<String, Long>();
	public HashMap<String, Long> mutedRemaining = new HashMap<String, Long>();

	public HashMap<String, String> tickets = new HashMap<String, String>();
	public ArrayList<String> claimedTicket = new ArrayList<String>();

	public Boolean updateAvailable = false;
	public double updateLatest;

	public Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {

		this.instance = this;

		config = new Config(this);
		methods = new Methods(this);
		lang = new LangMethods(this);

		banmethods = new BanMethods(this);
		banmysql = new BanMethodsMySQL(this);
		bansqlite = new BanMethodsSQLite(this);
		freeze = new FreezeMethods(this);
		jail = new JailMethods(this);
		kick = new KickMethods(this);
		mute = new MuteMethods(this);
		note = new NoteMethods(this);
		ticket = new TicketMethods(this);
		warn = new WarnMethods(this);

		String pluginFolder = this.getDataFolder().getAbsolutePath();
		File playersFolder = new File(pluginFolder, "players");
		playersFolder.mkdirs();
		(new File(pluginFolder)).mkdirs();

		this.getCommand("ban").setExecutor(new Ban(this));
		this.getCommand("bancheck").setExecutor(new BanCheck(this));
		this.getCommand("banhistory").setExecutor(new BanHistory(this));
		this.getCommand("banimport").setExecutor(new BanImport(this));
		this.getCommand("banrecent").setExecutor(new BanRecent(this));
		this.getCommand("unban").setExecutor(new Unban(this));
		this.getCommand("freeze").setExecutor(new Freeze(this));
		this.getCommand("unfreeze").setExecutor(new Unfreeze(this));
		this.getCommand("deljail").setExecutor(new DelJail(this));
		this.getCommand("jail").setExecutor(new Jail(this));
		this.getCommand("setjail").setExecutor(new SetJail(this));
		this.getCommand("unjail").setExecutor(new Unjail(this));
		this.getCommand("kick").setExecutor(new Kick(this));
		this.getCommand("mute").setExecutor(new Mute(this));
		this.getCommand("unmute").setExecutor(new Unmute(this));
		this.getCommand("note").setExecutor(new Note(this));
		this.getCommand("helpop").setExecutor(new Helpop(this));
		this.getCommand("ticket").setExecutor(new Ticket(this));
		this.getCommand("warn").setExecutor(new Warn(this));
		this.getCommand("punishment").setExecutor(new Punishment(this));

		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerFileListener(this), this);
		pm.registerEvents(new UpdateNotifyListener(this), this);
		pm.registerEvents(new BanListener(this), this);
		pm.registerEvents(new FreezeListener(this), this);
		pm.registerEvents(new JailListener(this), this);
		pm.registerEvents(new MuteListener(this), this);
		pm.registerEvents(new TicketListener(this), this);

		methods.loadConfig();
		config.loadConfig();
		methods.createLog();

		methods.log("Starting Punishment");
		methods.debug("Debug enabled");

		methods.checkForUpdates();
		methods.loadMetrics();
		methods.loadDatabase();
		lang.loadLang();
		jail.createConfig();
		jail.loadConfig();
		ticket.setupScoreboard();
	}

	public void onDisable() {
		
		File file = new File(getDataFolder().getAbsolutePath(), "punishment.log");
		if (file.exists()) {
			punishmentLog.save();
		}
		
		methods.log("Stopping Punishment");
	}
}
