/*
 *     Copyright (C) 2019-2020  MaxPixel Studios
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

import cn.maxpixel.mpsmcl.game.Account;
import cn.maxpixel.mpsmcl.game.GameDirectory;
import cn.maxpixel.mpsmcl.util.IOUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonWriter;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.ArrayList;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class Configuration {
	public static Gson json = new GsonBuilder()
			.setPrettyPrinting()
			.serializeNulls()
			.excludeFieldsWithoutExposeAnnotation()
			.setDateFormat("yyyy/MM/dd HH:mm:ss")
			.enableComplexMapKeySerialization()
			.create();
	@SerializedName("language")
	@Expose()
	public String lang;
	@SerializedName("gameDirectories")
	@Expose
	public ArrayList<GameDirectory> gameDirs;
	@Expose
	public ArrayList<Account> accounts;
	@Expose
	public LauncherSettings launcherSettings;
	@Expose
	public String clientToken;
	public static Configuration loadConfiguration() {
		IOUtil.createNewFileFromStream("config.json", Configuration.class.getClassLoader().getResourceAsStream("default-config.json"));
		try(FileReader fr = new FileReader("config.json")) {
			LogManager.getLogger(CONFIGURATION + SLASH + LOAD).debug("Loading configuration");
			return json.fromJson(fr, Configuration.class);
		} catch (IOException e) {
			throw new RuntimeException("Error when loading configuration", e);
		}
	}
	public static void saveConfiguration(Configuration configuration) {
		LogManager.getLogger(CONFIGURATION + SLASH + SAVE).debug("Saving configuration");
		File file = new File("config.json");
		if(!file.exists()) {
			try {
				file.mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(FileWriter wr = new FileWriter(file);
		    JsonWriter writer = json.newJsonWriter(wr)) {
			json.toJson(configuration, Configuration.class, writer);
		} catch (IOException e) {
			LogManager.getLogger(CONFIGURATION + SLASH + SAVE).catching(e);
		}
	}
	public static void resetConfiguration() {
		LogManager.getLogger(CONFIGURATION + SLASH + RESET).debug("Resetting configuration");
		IOUtil.createNewFileFromStream("config.json", Configuration.class.getClassLoader().getResourceAsStream("/default-config.json"), true);
	}
}