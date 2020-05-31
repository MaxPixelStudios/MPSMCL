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

import cn.maxpixel.mpsmcl.Info;
import cn.maxpixel.mpsmcl.ui.renderer.Window;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class JFXApp extends Application {
	@Override
	public void stop() throws Exception {
		Window.running = false;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(Info.NAME + " version " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""));
		primaryStage.setHeight(Window.height);
		primaryStage.setWidth(Window.width);
		primaryStage.setX(Window.x);
		primaryStage.setY(Window.y);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon/icon.png"))));
		SceneManager.create(primaryStage);
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.show();
	}
}
