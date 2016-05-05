package net.dkcraft.punishment.commands.jail;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.dkcraft.punishment.Main;
import net.dkcraft.punishment.util.Methods;
import net.dkcraft.punishment.util.lang.Lang;

public class JailMethods {

	public Main plugin;
	public Methods methods;
	public BukkitTask task;

	File jailFile;
	FileConfiguration jailConfig;

	public JailMethods(Main plugin) {
		this.plugin = plugin;
		this.methods = this.plugin.methods;
	}

	public boolean canBeJailed(CommandSender sender, Player target, String targetName) {
		if (target == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_ONLINE_FALSE.toString().replace("%target%", targetName)));
			return false;
		}

		if (target == sender) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_SELF.toString()));
			return false;
		}

		if (isJailed(target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_TRUE.toString().replace("%target%", target.getName())));
			return false;
		}

		if (isImmune(sender, target)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_IMMUNE.toString().replace("%target%", target.getName())));
			return false;
		}
		return true;
	}

	public boolean isJailed(Player target) {
		if (target != null && plugin.jailed.containsKey(target.getName())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isImmune(CommandSender sender, Player target) {
		if (target.hasPermission("punishment.immune.jail") && !(sender instanceof ConsoleCommandSender)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean jailExists(String jail) {
		return plugin.jails.containsKey(jail);
	}

	public Location getJailLocation(String jailName) {

		World world = Bukkit.getWorld(plugin.jails.get(jailName).getJailWorld().toString());
		double x = plugin.jails.get(jailName).getJailX();
		double y = plugin.jails.get(jailName).getJailY();
		double z = plugin.jails.get(jailName).getJailZ();
		float yaw = (float) plugin.jails.get(jailName).getJailYaw();
		float pitch = (float) plugin.jails.get(jailName).getJailPitch();
		Location jailLocation = new Location(world, x, y, z, yaw, pitch);

		return jailLocation;
	}

	public void jail(final Player target, final Location playerLocation, final Location jailLocation, long time) {

		long currentTime = methods.getCurrentTime();

		plugin.jailed.put(target.getName(), time);
		plugin.jailedStart.put(target.getName(), currentTime);
		plugin.jailLocation.put(target.getName(), playerLocation);

		target.teleport(jailLocation);

		this.task = new BukkitRunnable() {
			public void run() {
				unJail(target, playerLocation);
				methods.log(target.getName() + " was unjailed automatically");
				target.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.JAIL_UNJAIL_AUTO.toString()));
			}
		}.runTaskLater(plugin, time * 20);
	}

	public void unJail(Player target, Location playerLocation) {

		plugin.jailed.remove(target.getName());
		plugin.jailedStart.remove(target.getName());
		target.teleport(playerLocation);
		plugin.jailLocation.remove(target.getName());

		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}

	public void pauseJail(Player target) {

		plugin.jailed.remove(target.getName());
		plugin.jailedStart.remove(target.getName());

		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}

	public void createConfig() {
		jailFile = new File(plugin.getDataFolder().getAbsolutePath(), "jails.yml");
		if (!jailFile.exists()) {
			try {
				jailFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		jailConfig = YamlConfiguration.loadConfiguration(jailFile);
		if (!jailConfig.contains("jails")) {
			jailConfig.createSection("jails");
			try {
				jailConfig.save(jailFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadConfig() {

		if (jailFile.exists()) {

			if (jailConfig.isSet("jails")) {

				Set<String> jails = jailConfig.getConfigurationSection("jails").getKeys(false);

				if (!jails.isEmpty()) {
					for (String jailInfo : jails) {

						String jailName = jailInfo;
						String jailWorld = jailConfig.getString("jails." + jailName + ".world");
						double jailX = jailConfig.getDouble("jails." + jailName + ".x");
						double jailY = jailConfig.getDouble("jails." + jailName + ".y");
						double jailZ = jailConfig.getDouble("jails." + jailName + ".z");
						double jailYaw = jailConfig.getDouble("jails." + jailName + ".yaw");
						double jailPitch = jailConfig.getDouble("jails." + jailName + ".pitch");

						plugin.jails.put(jailName, new JailInfo(jailName, jailWorld, jailX, jailY, jailZ, jailYaw, jailPitch));
					}
				}
			}
		}
	}

	public void setJail(String jailName, Location location) {

		String jailWorld = location.getWorld().getName();
		double jailX = location.getX();
		double jailY = location.getY();
		double jailZ = location.getZ();
		double jailYaw = location.getYaw();
		double jailPitch = location.getPitch();

		jailConfig.set("jails." + jailName, "");
		jailConfig.set("jails." + jailName + ".world", jailWorld);
		jailConfig.set("jails." + jailName + ".x", jailX);
		jailConfig.set("jails." + jailName + ".y", jailY);
		jailConfig.set("jails." + jailName + ".z", jailZ);
		jailConfig.set("jails." + jailName + ".yaw", jailYaw);
		jailConfig.set("jails." + jailName + ".pitch", jailPitch);

		try {
			jailConfig.save(jailFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		plugin.jails.put(jailName, new JailInfo(jailName, jailWorld, jailX, jailY, jailZ, jailYaw, jailPitch));
	}

	public void delJail(String jailName) {

		jailConfig.set("jails." + jailName, null);

		try {
			jailConfig.save(jailFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		plugin.jails.remove(jailName);
	}
}
