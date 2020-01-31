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

package cn.maxpixel.mpsmcl.ui.renderer.opengl;

import cn.maxpixel.mpsmcl.Info;
import cn.maxpixel.mpsmcl.InitializeException;
import cn.maxpixel.mpsmcl.Main;
import cn.maxpixel.mpsmcl.ui.renderer.Window;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OGLWindow extends Window {
	private Callback GLdebugProc;
	private GLFWErrorCallback errorCallback;
	public static GLCapabilities capabilities;
	private long window;
	@Override
	public void init() throws Exception {
		LogManager.getLogger("App Launcher/Initialize").info("Started initialize");
		errorCallback = GLFWErrorCallback.createThrow().set();
		LogManager.getLogger("App Launcher/Initialize").trace("Created GLFW error callback throw");
		if(!glfwInit()) throw new InitializeException("GLFW initialize failed", new RuntimeException("Initialize error"));
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		if(Platform.get() == Platform.MACOSX) glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		LogManager.getLogger("App Launcher/Initialize").trace("Window hints has been set");
		window = glfwCreateWindow(width, height,
				Info.NAME + " version " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""), NULL, NULL);
		if(window == NULL) {
			glfwTerminate();
			throw new InitializeException("Window created failed", new RuntimeException("Failed to create GLFW window"));
		}
		LogManager.getLogger("App Launcher/Initialize").trace("Created window");
		glfwSetWindowIcon(window, GLFWImage.malloc(1).put(0, loadImage("icon/icon.png")));
		LogManager.getLogger("App Launcher/Initialize").trace("Window icon set");
		glfwSetWindowCloseCallback(window, l -> Window.running = false);
		try(MemoryStack stack = stackPush()) {
			IntBuffer wWidth = stack.callocInt(1);
			IntBuffer wHeight = stack.callocInt(1);
			glfwGetWindowSize(window, wWidth, wHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window,
					(vidMode.width() - wWidth.get()) / 2,
					(vidMode.height() - wHeight.get()) / 2);
			LogManager.getLogger("App Launcher/Initialize").trace("Window position set");
			glfwShowWindow(window);
		}
		glfwMakeContextCurrent(window);
		LogManager.getLogger("App Launcher/Initialize").trace("Make window context current");
		glfwSwapInterval(1);
		LogManager.getLogger("App Launcher/Initialize").info("V-Sync enabled");
		capabilities = GL.createCapabilities();
		LogManager.getLogger("App Launcher/Initialize").trace("Capabilities created");
		GLdebugProc = GLUtil.setupDebugMessageCallback();
		LogManager.getLogger("App Launcher/Initialize").trace("OpenGL debug message callback created");
		glfwSetFramebufferSizeCallback(window, (window1, width, height) -> glViewport(0, 0, width, height));
		LogManager.getLogger("App Launcher/Initialize").trace("GLFW frame buffer size callback has been set");
		float[] rgba = Main.configuration.getLauncherSettings().getBackgroundColor();
		glClearColor(rgba[0], rgba[1], rgba[2],rgba[3]);
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
		if(GLdebugProc != null) GLdebugProc.free();
		Callbacks.glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).debug("Window destroyed");
		glfwTerminate();
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).trace("Terminated GLFW");
		errorCallback.free();
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + CLOSE).trace("Callbacks released");
	}

	private static GLFWImage loadImage(String path) {
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).trace("Loading Image");
		try(MemoryStack stack = stackPush()) {
			GLFWImage icon = GLFWImage.mallocStack(stack);
			IntBuffer x = stack.callocInt(1);
			IntBuffer y = stack.callocInt(1);
			ByteBuffer image = stbi_load(new File(OGLWindow.class.getClassLoader().getResource(path).toURI()).getAbsolutePath(), x, y, stack.callocInt(1), 4);
			icon.set(x.get(), y.get(), (ByteBuffer) image.flip());
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).debug("Loaded Image");
			return icon;
		} catch (URISyntaxException e) {
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).error("Couldn't load the image!\nStacktrace: {}", e);
		}
		return null;
	}
}