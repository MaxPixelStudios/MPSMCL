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
package cn.maxpixel.mpsmcl.ui;

import cn.maxpixel.mpsmcl.Main;
import cn.maxpixel.mpsmcl.configuration.LauncherSettings;
import cn.maxpixel.mpsmcl.ui.renderer.Window;
import cn.maxpixel.mpsmcl.ui.renderer.javafx.JFXWindow;
import cn.maxpixel.mpsmcl.ui.renderer.opengl.OGLWindow;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import static cn.maxpixel.mpsmcl.LoggingConstants.APP_LAUNCHER;

public class Launcher {

	public static Window window;

	public void run() {
		LogManager.getLogger(APP_LAUNCHER).info("Launching Application");
		Window.running = true;
		window = Main.configuration.getLauncherSettings().getRenderer() == LauncherSettings.Renderer.OPENGL ? new OGLWindow() : new JFXWindow();
		LogManager.getLogger(APP_LAUNCHER).info("Using Window Renderer: {} Renderer", window instanceof OGLWindow ? "OpenGL" : "JavaFX");
		try {
			window.init();
			window.run();
			exit();
		} catch (Exception e) {
			LogManager.getLogger(APP_LAUNCHER).catching(Level.ERROR, e);
		}
	}
	private void exit() {
		LogManager.getLogger(APP_LAUNCHER).info("--------------------");
		LogManager.getLogger(APP_LAUNCHER).info("Exiting MaxPixel Studio's Minecraft Launcher");
		LogManager.getLogger(APP_LAUNCHER).info("--------------------");
	}

	public Window getWindow() {
		return window;
	}
}