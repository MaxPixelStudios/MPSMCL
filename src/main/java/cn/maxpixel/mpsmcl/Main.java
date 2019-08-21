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
package cn.maxpixel.mpsmcl;

import cn.maxpixel.mpsmcl.task.Schedule;
import cn.maxpixel.mpsmcl.ui.Launcher;
import cn.maxpixel.mpsmcl.util.Configuration;
import cn.maxpixel.mpsmcl.util.Language;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class Main {
	public static Configuration configuration;
	public static Language lang;
	static {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
			LogManager.getLogger("Main/Fatal Error Report").fatal("MaxPixel Studio's Minecraft Launcher has stopped because of a fatal error occur");
			LogManager.getLogger("Main/Fatal Error Report").fatal("Error thread: " + t.getName());
			LogManager.getLogger("Main/Fatal Error Report").fatal("Stack trace:");
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer, true));
			LogManager.getLogger("Main/Fatal Error Report").fatal(writer.toString());
			LogManager.getLogger("Main/Fatal Error Report").fatal("Please report this error to GitHub issue page: https://github.com/maxpixelstudio/MPSMCL/issues");
			LogManager.getLogger("Main/Fatal Error Report").fatal("If this error caused by you modified the program yourself, please don't report this.");
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() -> LogManager.getLogger("App Launcher").traceExit()));
	}
	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile", "log.xml");
		System.setProperty("log4j.skipJansi", "false");
		LogManager.getLogger("Main").info("--------------------");
		LogManager.getLogger("Main").info("MaxPixel Studios' Minecraft Launcher Starting...");
		LogManager.getLogger("Main").info("Launcher Version: " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""));
		LogManager.getLogger("Main").info("OS: " + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		LogManager.getLogger("Main").info("--------------------");
		configuration = Configuration.loadConfiguration();
		LogManager.getLogger("Main").trace("Loaded configuration");
		lang = new Language();
		LogManager.getLogger("Main").debug("Running start task");
		Schedule.getSchedule().then(() -> {
			LogManager.getLogger("Main").debug("Checking for updates...");

		}).then(() -> {

		}).toThread().start();
		Launcher.main(args);
	}
	public static void restart() {
		try {
			Runtime.getRuntime().exec("java -jar " + new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}