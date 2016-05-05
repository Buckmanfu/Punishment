package net.dkcraft.punishment.util.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.YamlConfiguration;

import net.dkcraft.punishment.Main;

public class LangMethods {

	public Main plugin;

	public static YamlConfiguration LANG;
	public static File LANG_FILE;

	public LangMethods(Main plugin) {
		this.plugin = plugin;
	}

	public YamlConfiguration getLang() {
		return LANG;
	}

	public File getLangFile() {
		return LANG_FILE;
	}

	@SuppressWarnings("deprecation")
	public void loadLang() {
		File lang = new File(plugin.getDataFolder(), "lang.yml");
		if (!lang.exists()) {
			try {
				plugin.getDataFolder().mkdir();
				lang.createNewFile();
				InputStream defConfigStream = plugin.getResource("lang.yml");
				if (defConfigStream != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
					defConfig.save(lang);
					Lang.setFile(defConfig);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace(); // So they notice
				System.out.println("[Punishment] Couldn't create language file.");
				System.out.println("[Punushment] This is a fatal error. Now disabling");
				// plugin.setEnabled(false); // Without it loaded, we can't send
				// them messages
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for (Lang item : Lang.values()) {
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		Lang.setFile(conf);
		LANG = conf;
		LANG_FILE = lang;
		try {
			conf.save(getLangFile());
		} catch (IOException e) {
			System.out.println("Punishment: Failed to save lang.yml.");
			System.out.println("Punishment: Report this stack trace to xDeeKay.");
			e.printStackTrace();
		}
	}
}
