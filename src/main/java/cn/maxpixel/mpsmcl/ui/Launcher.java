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
package cn.maxpixel.mpsmcl.ui;

import cn.maxpixel.mpsmcl.Info;
import cn.maxpixel.mpsmcl.InitializeException;
import cn.maxpixel.mpsmcl.Main;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class Launcher {
	private long window;
	private Callback GLdebugProc;
	private GLFWErrorCallback errorCallback;
	public static GLCapabilities capabilities;

	public void run() {
		LogManager.getLogger("App Launcher").info("Launching Application");
		try {
			init();
			loop();
			destroy();
		} catch (Exception e) {
			LogManager.getLogger("App Launcher").catching(Level.ERROR, e);
		}
	}
	private void init() {
		LogManager.getLogger("App Launcher/Initialize").debug("Releasing files");
		new File(System.getProperty("user.home") + "/AppData/Roaming/.mpsmcl/resource").mkdirs();
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
		window = glfwCreateWindow(650, 720,
				Info.NAME + " version " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""), NULL, NULL);
		if(window == NULL) {
			glfwTerminate();
			throw new InitializeException("Window created failed", new RuntimeException("Failed to create GLFW window"));
		}
		LogManager.getLogger("App Launcher/Initialize").trace("Created window");
		glfwSetWindowIcon(window, GLFWImage.malloc(1).put(0, loadImage("icon/icon.png")));
		LogManager.getLogger("App Launcher/Initialize").trace("Window icon set");
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
	private void loop() {
		while(!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	private void destroy() {
		if(GLdebugProc != null) GLdebugProc.free();
		Callbacks.glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		LogManager.getLogger("App Launcher/Close").debug("Window destroyed");
		glfwTerminate();
		LogManager.getLogger("App Launcher/Close").trace("Terminated GLFW");
		errorCallback.free();
		LogManager.getLogger("App Launcher/Close").trace("Callbacks released");
		LogManager.getLogger(APP_LAUNCHER).info("--------------------");
		LogManager.getLogger(APP_LAUNCHER).info("Exiting MaxPixel Studio's Minecraft Launcher");
		LogManager.getLogger(APP_LAUNCHER).info("--------------------");
	}
	private static GLFWImage loadImage(String path) {
		LogManager.getLogger(APP_LAUNCHER + SLASH + LOAD_IMAGE).trace("Loading Image");
		try(MemoryStack stack = stackPush()) {
			GLFWImage icon = GLFWImage.mallocStack(stack);
			IntBuffer x = stack.callocInt(1);
			IntBuffer y = stack.callocInt(1);
			ByteBuffer image = stbi_load(new File(Launcher.class.getClassLoader().getResource(path).toURI()).getAbsolutePath(), x, y, stack.callocInt(1), 4);
			icon.set(x.get(), y.get(), image);
			LogManager.getLogger(APP_LAUNCHER + SLASH + LOAD_IMAGE).debug("Loaded Image");
			return icon;
		} catch (URISyntaxException e) {
			LogManager.getLogger(APP_LAUNCHER + SLASH + LOAD_IMAGE).error("Couldn't load the image!\nStacktrace: {}", e);
		}
		return null;
	}
	public long getWindow() {
		return window;
	}
}