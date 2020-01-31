/*
 *     Copyright (C) 2019  MaxPixel Studios
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cn.maxpixel.mpsmcl.configuration;

import cn.maxpixel.mpsmcl.game.GameDirectory;
import cn.maxpixel.mpsmcl.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Configuration {
	public static Gson json = new GsonBuilder()
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
	private ArrayList<GameDirectory> gameDirs;
	@Expose
	@SerializedName("launcherSettings")
	private LauncherSettings settings;
	public static Configuration loadConfiguration() {
		FileReader fr = null;
		try {
			FileUtil.createNewFileFromStream("config.json", Configuration.class.getResourceAsStream("/default-config.json"));
			fr = new FileReader("config.json");
			LogManager.getLogger("Launcher Configuration/Load Configuration").trace("Loading configuration");
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
		return gameDirs;
	}
	public LauncherSettings getLauncherSettings() {
		return settings;
	}
	public static void resetConfiguration() {
		LogManager.getLogger("Launcher Configuration/Reset Configuration").debug("Resetting configuration");
		FileUtil.createNewFileFromStream("config.json", Configuration.class.getResourceAsStream("/default-config.json"), true);
	}
}