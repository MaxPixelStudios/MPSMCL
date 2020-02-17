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

import cn.maxpixel.mpsmcl.ui.renderer.Window;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class JFXWindow extends Window {
	private JFXApp window = new JFXApp();
	@Override
	public void init() throws Exception {
		LogManager.getLogger(JAVAFX_WINDOW_RENDERER + SLASH + INITIALIZE).info("Started initialize");
	}

	@Override
	public void run() throws Exception {
		Application.launch(window.getClass());
		while(running) {}
		close();
	}

	@Override
	protected void close() throws Exception {
		Window.running = false;
		LogManager.getLogger(JAVAFX_WINDOW_RENDERER).debug("Closed window");
	}
}
