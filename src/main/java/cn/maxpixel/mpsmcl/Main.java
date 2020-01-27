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
import cn.maxpixel.mpsmcl.util.ArrayUtil;
import cn.maxpixel.mpsmcl.util.Configuration;
import cn.maxpixel.mpsmcl.util.Language;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.io.*;

public class Main {
	public static Configuration configuration;
	public static Language lang;
	public static Launcher launcher;
	static {
		System.setProperty("log4j2.skipJansi", "false");
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
			LogManager.getLogger("Main/Fatal Error Report").fatal("MaxPixel Studio's Minecraft Launcher has stopped because of a fatal error occur");
			LogManager.getLogger("Main/Fatal Error Report").fatal("Error thread: " + t.getName());
			LogManager.getLogger("Main/Fatal Error Report").fatal("Stack trace:");
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer, true));
			writer.flush();
			ArrayUtil.forEach(writer.toString().split("\n"), LogManager.getLogger("Main/Fatal Error Report")::fatal);
			LogManager.getLogger("Main/Fatal Error Report").fatal("Please report this error to GitHub issue page: https://github.com/MaxPixelStudios/MPSMCL/issues");
			LogManager.getLogger("Main/Fatal Error Report").fatal("If this error caused by you modified the program yourself, please don't report this.");
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
			JOptionPane.showMessageDialog(null, "MaxPixel Studio's Minecraft Launcher has stopped because of a fatal error occur" +
					"\nStacktrace: \n" + e + "\nPlease report this error to GitHub issue page: https://github.com/MaxPixelStudios/MPSMCL/issues\n" +
					"If this error caused by you modified the program yourself, please don't report.", "MPSMCL FATAL ERROR REPORT",
					JOptionPane.ERROR_MESSAGE);
		});
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			File temp = new File(System.getProperty("user.home") + "/AppData/Roaming/.mpsmcl/resource");
			if(temp.listFiles() != null) ArrayUtil.forEach(temp.listFiles(), File::delete);
			temp.delete();
			LogManager.getLogger("App Launcher").debug("Temp file deleted");
			LogManager.getLogger("App Launcher").traceExit();
		}, "Shutdown Thread"));
	}
	public static void main(String[] args) {
		LogManager.getLogger("Main").info("--------------------");
		LogManager.getLogger("Main").info("MaxPixel Studios' Minecraft Launcher Starting...");
		LogManager.getLogger("Main").info("Launcher Version: " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""));
		LogManager.getLogger("Main").info("OS: " + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		LogManager.getLogger("Main").info("--------------------");
		configuration = Configuration.loadConfiguration();
		LogManager.getLogger("Main").trace("Loaded configuration");
		lang = new Language();
		LogManager.getLogger("Main").trace("Loaded language");
		LogManager.getLogger("Main").debug("Running start task");
		Schedule.getSchedule().add(() -> {
			LogManager.getLogger("Main").debug("Checking for updates...");

		}).runThread();
		launcher = new Launcher();
		launcher.run();
	}
}