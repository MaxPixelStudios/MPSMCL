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

package cn.maxpixel.mpsmcl.ui.renderer.opengl;

import cn.maxpixel.mpsmcl.Info;
import cn.maxpixel.mpsmcl.InitializeException;
import cn.maxpixel.mpsmcl.ui.renderer.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.system.Configuration;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OGLWindow extends Window {
	private long window;
	@Override
	public void init() throws Exception {
		final Logger INIT_LOGGER = LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + INITIALIZE);
		INIT_LOGGER.info("Started initialize");
		Configuration.MEMORY_ALLOCATOR.set("rpmalloc");
		INIT_LOGGER.trace("Set the default native memory allocator to rpmalloc");
		RenderEnvironment.initGLFW();
		window = glfwCreateWindow(width, height,
				Info.NAME + " version " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""), NULL, NULL);
		if(window == NULL) {
			glfwTerminate();
			throw new InitializeException("Failed to create window", new RuntimeException("Failed to create GLFW window"));
		}
		INIT_LOGGER.trace("Created window");
		RenderEnvironment.configureWindow(window);
	}

	@Override
	public void run() throws Exception {
		running();
		close();
	}

	private void running() {
		while(running) {


			glClear(GL_COLOR_BUFFER_BIT);
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	@Override
	protected void close() throws Exception {
		glfwDestroyWindow(window);
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).debug("Window destroyed");
		Callbacks.glfwFreeCallbacks(window);
		glfwSetErrorCallback(null).free();
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).trace("Callbacks released");
		glfwTerminate();
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).trace("Terminated GLFW");
	}
}