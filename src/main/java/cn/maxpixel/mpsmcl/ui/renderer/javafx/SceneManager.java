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

package cn.maxpixel.mpsmcl.ui.renderer.javafx;

import cn.maxpixel.mpsmcl.ui.renderer.javafx.scene.Sceneable;
import cn.maxpixel.mpsmcl.ui.renderer.javafx.scene.material.HomeScene;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cn.maxpixel.mpsmcl.LoggingConstants.SCENE_MANAGER;

public class SceneManager {
	private static boolean isWindowOpen;
	private static Stage stage;
	private static final Logger LOGGER = LogManager.getLogger(SCENE_MANAGER);
	public static void create(Stage primaryStage) {
		if(!isWindowOpen) {
			LOGGER.debug("Creating Scene Manager");
			isWindowOpen = true;
			stage = primaryStage;
			initScene();
			return;
		}
		LOGGER.fatal("Error when creating SceneManager");
		throw new IllegalStateException("SceneManager already created");
	}
	private static void initScene() {
		setScene(new HomeScene());
	}
	public static void close() {
		if(isWindowOpen) {
			LOGGER.debug("Closing Scene Manager");
			isWindowOpen = false;
			stage = null;
			return;
		}
		LOGGER.fatal("Error when closing SceneManager");
		throw new IllegalStateException("SceneManager already closed");
	}
	public static void setScene(Sceneable scene) {
		setScene(scene.getScene());
	}
	public static void setScene(Scene scene) {
		if(isWindowOpen) {
			LOGGER.debug("Setting Scene");
			stage.setScene(scene);
			return;
		}
		LOGGER.fatal("Error when set scene for stage");
		throw new IllegalStateException("SceneManager already closed");
	}
	public static Stage getStage() {
		return stage;
	}
}