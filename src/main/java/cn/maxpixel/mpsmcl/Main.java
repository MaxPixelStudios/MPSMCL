package cn.maxpixel.mpsmcl;

import cn.maxpixel.mpsmcl.ui.Launcher;
import cn.maxpixel.mpsmcl.util.Configuration;
import cn.maxpixel.mpsmcl.util.Language;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Main {
	public static BorderPane pane;
	public static Configuration configuration;
	public static Language lang;
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			LogManager.getLogger("Main/Fatal Error Report").catching(Level.FATAL, e);
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
			LogManager.getLogger("Main/Fatal Error Report").fatal("MaxPixel Studio's Minecraft Launcher has stopped because of a fatal error occur");
			LogManager.getLogger("Main/Fatal Error Report").fatal("Error thread: " + t.getName());
			LogManager.getLogger("Main/Fatal Error Report").fatal("Stack trace:");
			LogManager.getLogger("Main/Fatal Error Report").fatal(e.toString());
			for(StackTraceElement s : e.getStackTrace()) {
				LogManager.getLogger("Main/Fatal Error Report").fatal("\tat " + s.toString());
			}
			if(e.getCause() != null) {
				LogManager.getLogger("Main/Fatal Error Report").fatal("Caused by: " + e.getCause().toString());
				for(StackTraceElement s : e.getCause().getStackTrace()) {
					LogManager.getLogger("Main/Fatal Error Report").fatal("\tat " + s.toString());
				}
			}
			LogManager.getLogger("Main/Fatal Error Report").fatal("Please report this to GitHub issue page: https://github.com/maxpixelstudio/MPSMCL/issues");
			LogManager.getLogger("Main/Fatal Error Report").fatal("-----------------------------------------------------");
		});
		System.setProperty("log4j.configurationFile", "log.xml");
		System.setProperty("log4j.skipJansi", "false");
		LogManager.getLogger("Main").info("--------------------");
		LogManager.getLogger("Main").info("MaxPixel Studio's Minecraft Launcher Starting...");
		LogManager.getLogger("Main").info("Launcher Version: " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""));
		LogManager.getLogger("Main").info("OS: " + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
		LogManager.getLogger("Main").info("--------------------");
		pane = new BorderPane();
		configuration = Configuration.loadConfiguration();
		LogManager.getLogger("Main").trace("Loaded configuration");
		lang = new Language();
		Launcher.main(args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LogManager.getLogger("App Launcher").info("--------------------");
			LogManager.getLogger("App Launcher").info("Exiting MaxPixel Studio's Minecraft Launcher");
			LogManager.getLogger("App Launcher").info("--------------------");
			LogManager.getLogger("App Launcher").traceExit();
		}));
	}
}