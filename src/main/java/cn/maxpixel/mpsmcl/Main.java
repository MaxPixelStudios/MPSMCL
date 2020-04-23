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
package cn.maxpixel.mpsmcl;

import cn.maxpixel.mpsmcl.task.Schedule;
import cn.maxpixel.mpsmcl.ui.Launcher;
import cn.maxpixel.mpsmcl.util.ArrayUtil;
import cn.maxpixel.mpsmcl.configuration.Configuration;
import cn.maxpixel.mpsmcl.util.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class Main {
	public static Configuration configuration;
	public static Language lang;
	public static Launcher launcher;
	private static final Logger LOGGER = LogManager.getLogger(MAIN);
	static {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			final Logger FATAL_ERROR_REPORT_LOGGER = LogManager.getLogger(MAIN + SLASH + FATAL_ERROR_REPORT);
			FATAL_ERROR_REPORT_LOGGER.fatal("-----------------------------------------------------");
			FATAL_ERROR_REPORT_LOGGER.fatal(Info.FULL_NAME + " has stopped because of a fatal error occur");
			FATAL_ERROR_REPORT_LOGGER.fatal("Error thread: " + t.getName());
			FATAL_ERROR_REPORT_LOGGER.fatal("Stack trace:");
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer, true));
			writer.flush();
			ArrayUtil.forEach(writer.toString().split("\n"), FATAL_ERROR_REPORT_LOGGER::fatal);
			FATAL_ERROR_REPORT_LOGGER.fatal("Please report this error to GitHub issue page: https://github.com/MaxPixelStudios/MPSMCL/issues");
			FATAL_ERROR_REPORT_LOGGER.fatal("If this error caused by you modified the program yourself, please don't report this.");
			FATAL_ERROR_REPORT_LOGGER.fatal("-----------------------------------------------------");
			JOptionPane.showMessageDialog(null, Info.FULL_NAME + " has stopped because of a fatal error occur" +
					"\nStacktrace: \n" + writer + "\nPlease report this error to GitHub issue page: https://github.com/MaxPixelStudios/MPSMCL/issues\n" +
					"If this error caused by you modified the program yourself, please don't report.", "MPSMCL FATAL ERROR REPORT",
					JOptionPane.ERROR_MESSAGE);
		});
		Runtime.getRuntime().addShutdownHook(Schedule.getSchedule().add(() -> LogManager.getLogger(APP_LAUNCHER).traceExit()).getThread());
	}
	public static void main(String[] args) {
		LOGGER.info("--------------------");
		LOGGER.info(Info.FULL_NAME + " Starting...");
		LOGGER.info("Launcher Version: " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""));
		LOGGER.info("OS: " + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		LOGGER.info("--------------------");
		configuration = Configuration.loadConfiguration();
		LOGGER.info("Loaded configuration");
		lang = new Language();
		LOGGER.trace("Loaded language");
		LOGGER.debug("Running start task");
//		Schedule.getSchedule().add(() -> {
//			LogManager.getLogger(MAIN).debug("Checking for updates...");
//
//		}).runThread();
		Runtime.getRuntime().addShutdownHook(Schedule.getSchedule().add(() -> Configuration.saveConfiguration(configuration)).getThread());
		launcher = new Launcher();
		launcher.run();
	}
}