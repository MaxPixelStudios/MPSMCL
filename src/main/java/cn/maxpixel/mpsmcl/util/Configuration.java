package cn.maxpixel.mpsmcl.util;

import cn.maxpixel.mpsmcl.game.GameDirectory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Configuration {
	private static Gson json = new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			.excludeFieldsWithoutExposeAnnotation()
			.setDateFormat("yyyy/MM/dd HH:mm:ss")
			.create();
	@SerializedName("language")
	@Expose()
	private String lang;
	@SerializedName("gameDirectories")
	@Expose
	private ArrayList<GameDirectory> gameDir;
	public static Configuration loadConfiguration() {
		FileReader fr = null;
		try {
			FileUtil.createNewFileFromStream("config.json", Configuration.class.getResourceAsStream("/default-config.json"));
			fr = new FileReader("config.json");
			LogManager.getLogger("Launcher Configuration/Load Configuration").debug("Creating configuration");
			return json.fromJson(fr, Configuration.class);
		} catch (FileNotFoundException e) {
			LogManager.getLogger("Launcher Configuration/Exception caught").fatal("Configuration file not found!");
			LogManager.getLogger("Launcher Configuration/Exception caught").catching(Level.FATAL, e);
			FileUtil.createNewFileFromStream("config.json", Configuration.class.getResourceAsStream("/default-config.json"));
			return json.fromJson(fr, Configuration.class);
		}
	}
	public String getLanguage() {
		return lang;
	}
	public ArrayList<GameDirectory> getGameDirectories() {
		return gameDir;
	}
}