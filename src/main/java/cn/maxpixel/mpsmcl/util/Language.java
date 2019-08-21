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
package cn.maxpixel.mpsmcl.util;

import cn.maxpixel.mpsmcl.Main;
import cn.maxpixel.mpsmcl.UnknownLanguageException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class Language {
	private static Properties langConfig = new Properties();
	public Language() {
		try {
			if(Main.configuration.getLanguage().equalsIgnoreCase("default")) {
				String lang = Locale.getDefault().toString();
				LogManager.getLogger("Language System").trace("Default language has been select, finding your language");
				if(lang.contains("zh_CN")) {
					LogManager.getLogger("Language System").debug("Your language is Chinese(Simplified), loading language file...");
					langConfig.load(getClass().getResourceAsStream("/lang/zh_cn.properties"));
				} else if(lang.contains("zh_TW")) {
					LogManager.getLogger("Language System").debug("Your language is Chinese(Traditional), loading language file...");
					langConfig.load(getClass().getResourceAsStream("/lang/zh_tw.properties"));
				} else if(lang.contains("en_US")) {
					LogManager.getLogger("Language System").debug("Your language is English(US), loading language file...");
					langConfig.load(getClass().getResourceAsStream("/lang/en_us.properties"));
				} else {
					LogManager.getLogger("Language System").debug("Couldn't find your language, using default language English(US), loading language file...");
					langConfig.load(getClass().getResourceAsStream("/lang/en_us.properties"));
				}
				LogManager.getLogger("Language System").trace("Language file has been loaded");
			} else if(Main.configuration.getLanguage().equalsIgnoreCase("zh_cn") || Main.configuration.getLanguage().equalsIgnoreCase("zh_tw")
					|| Main.configuration.getLanguage().equalsIgnoreCase("en_us")) {
				LogManager.getLogger("Language System").debug("Selected language " + Main.configuration.getLanguage() + "loading language file...");
				langConfig.load(getClass().getResourceAsStream("/lang/" + Main.configuration.getLanguage() + ".properties"));
				LogManager.getLogger("Language System").trace("Language file has been loaded");
			} else {
				LogManager.getLogger("Language System/Exception caught").catching(Level.FATAL, new UnknownLanguageException("Language " +
						Main.configuration.getLanguage() + " is not exists"));
			}
		} catch(IOException ioe) {
			LogManager.getLogger("Language System/Exception caught").error("Error occurs");
			LogManager.getLogger("Language System/Exception caught").catching(ioe);
		}
	}
	public String get(String langName) {
		LogManager.getLogger("Language System").debug("Finding language properties value...");
		try {
			if (langName == null) {
				LogManager.getLogger("Language System").error("Language name cannot be null!");
				throw new NullPointerException("Language name cannot be null!");
			} else if (langConfig.getProperty(langName) == null) {
				LogManager.getLogger("Language System").error("Cannot find language name " + langName);
				throw new NullPointerException("Cannot find language name " + langName);
			} else return langConfig.getProperty(langName);
		} catch (Exception e) {
			LogManager.getLogger("Language System/Exception caught").catching(Level.WARN, e);
			return null;
		}
	}
}