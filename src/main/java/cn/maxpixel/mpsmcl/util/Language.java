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
package cn.maxpixel.mpsmcl.util;

import cn.maxpixel.mpsmcl.Main;
import cn.maxpixel.mpsmcl.UnknownLanguageException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class Language {
	private static final Properties langConfig = new Properties();
	private static final Logger LANGUAGE_SYSTEM_LOGGER = LogManager.getLogger(LANGUAGE_SYSTEM);
	public Language() {
		try {
			if(Main.configuration.lang.equals("default")) {
				final Locale lang = Locale.getDefault();
				LANGUAGE_SYSTEM_LOGGER.debug("Default language has been select, finding your language...");
				final ClassLoader loader = getClass().getClassLoader();
				if(lang.equals(Locale.SIMPLIFIED_CHINESE)) {
					LANGUAGE_SYSTEM_LOGGER.debug("Your language is Chinese(Simplified), loading language file...");
					langConfig.load(loader.getResourceAsStream("lang/zh_cn.properties"));
				} else if(lang.equals(Locale.TRADITIONAL_CHINESE)) {
					LANGUAGE_SYSTEM_LOGGER.debug("Your language is Chinese(Traditional), loading language file...");
					langConfig.load(loader.getResourceAsStream("lang/zh_tw.properties"));
				} else if(lang.equals(Locale.US)) {
					LANGUAGE_SYSTEM_LOGGER.debug("Your language is English(US), loading language file...");
					langConfig.load(loader.getResourceAsStream("lang/en_us.properties"));
				} else {
					LANGUAGE_SYSTEM_LOGGER.debug("Couldn't find your language, using default language English(US), loading language file...");
					langConfig.load(loader.getResourceAsStream("lang/en_us.properties"));
				}
				LANGUAGE_SYSTEM_LOGGER.trace("Language file has been loaded");
			} else {
				final String languageId = Main.configuration.lang.toLowerCase().replace('-', '_');
				try {
					LANGUAGE_SYSTEM_LOGGER.debug("Selected language id \"" + languageId + "\". Loading language file...");
					langConfig.load(getClass().getClassLoader().getResourceAsStream("lang/" + languageId + ".properties"));
					LANGUAGE_SYSTEM_LOGGER.trace("Language file has been loaded");
				} catch(NullPointerException npe) {
					LogManager.getLogger("Language System/Exception caught").catching(Level.FATAL, new UnknownLanguageException("Language id \"" +
							languageId+ "\" does not exist"));
				}
			}
		} catch(IOException ioe) {
			LogManager.getLogger(LANGUAGE_SYSTEM + SLASH + EXCEPTION_CAUGHT).catching(ioe);
		}
	}
	public String get(String langName) {
		LANGUAGE_SYSTEM_LOGGER.trace("Finding value of key \"{}\" ...", langName);
		try {
			if (langName == null) {
				LANGUAGE_SYSTEM_LOGGER.error("Language name cannot be null!");
			} else if (langConfig.getProperty(langName) == null) {
				LANGUAGE_SYSTEM_LOGGER.error("Cannot find language property " + langName);
			} else return langConfig.getProperty(langName);
		} catch (Exception e) {
			LogManager.getLogger(LANGUAGE_SYSTEM + SLASH + EXCEPTION_CAUGHT).catching(Level.WARN, e);
		}
		return "null";
	}
}