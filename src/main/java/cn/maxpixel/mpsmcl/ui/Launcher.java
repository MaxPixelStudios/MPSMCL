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
import cn.maxpixel.mpsmcl.util.FileUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Launcher {
	private static long window;
	private static Callback GLdebugProc;
	private static GLFWErrorCallback errorCallback;

	public static void main(String[] args) {
		LogManager.getLogger("App Launcher").info("Launching Application");
		new File(System.getProperty("user.home") + "/AppData/Roaming/.mpsmcl/resource").mkdirs();
		FileUtil.createNewFileFromStream(System.getProperty("user.home") + "/AppData/Roaming/.mpsmcl/resource/icon.png", Launcher.class.getResourceAsStream("/icon/icon.png"));
		try {
			init();
			loop();
			destroy();
		}catch (Exception e) {
			LogManager.getLogger("App Launcher").catching(Level.ERROR, e);
		}
	}
	private static void init() throws Exception {
		LogManager.getLogger("App Launcher/Initialize").debug("Started initialize");
		errorCallback = GLFWErrorCallback.createThrow().set();
		LogManager.getLogger("App Launcher/Initialize").info("Created GLFW error callback throw");
		if(!glfwInit()) throw new InitializeException("GLFW initialize failed", new RuntimeException("Initialize error"));
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
		LogManager.getLogger("App Launcher/Initialize").trace("Window hints has been set");
		window = glfwCreateWindow(650, 720,
				Info.NAME + " version " + Info.VERSION + (Info.IS_TEST_VERSION ? "-" + Info.TEST_PHASE + Info.TEST_VERSION : ""), NULL, NULL);
		LogManager.getLogger("App Launcher/Initialize").trace("Created window");
		glfwSetWindowIcon(window, GLFWImage.malloc(1).put(0, load_image(System.getProperty("user.home") + "/AppData/Roaming/.mpsmcl/resource/icon.png")));
		LogManager.getLogger("App Launcher/Initialize").trace("Window icon has been set");
		if(window == NULL) {
			glfwTerminate();
			throw new InitializeException("Window created failed", new RuntimeException("Failed to create GLFW window"));
		}
		try(MemoryStack stack = stackPush()) {
			IntBuffer wWidth = stack.callocInt(1);
			IntBuffer wHeight = stack.callocInt(1);
			glfwGetWindowSize(window, wWidth, wHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window,
					(vidMode.width() - wWidth.get()) / 2,
					(vidMode.height() - wHeight.get()) / 2);
			LogManager.getLogger("App Launcher/Initialize").trace("Window position has been set");
		}
		glfwMakeContextCurrent(window);
		LogManager.getLogger("App Launcher/Initialize").trace("Make window context current");
		glfwSwapInterval(1);
		LogManager.getLogger("App Launcher/Initialize").debug("V-Sync enabled");
		GL.createCapabilities();
		LogManager.getLogger("App Launcher/Initialize").trace("Capabilities created");
		GLdebugProc = GLUtil.setupDebugMessageCallback();
		LogManager.getLogger("App Launcher/Initialize").info("OpenGL debug message callback created");
		glfwSetFramebufferSizeCallback(window, (window1, width, height) -> glViewport(0, 0, width, height));
		LogManager.getLogger("App Launcher/Initialize").trace("GLFW frame buffer size callback has been set");
		float rgba[] = Main.configuration.getLauncherSettings().getBackgroundColor();
		glClearColor(rgba[0], rgba[1], rgba[2],rgba[3]);
	}
	private static void loop() {
		while(!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}
	private static void destroy() {
		if(GLdebugProc != null) GLdebugProc.free();
		Callbacks.glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		LogManager.getLogger("App Launcher/Close").debug("Window destroyed");
		glfwTerminate();
		LogManager.getLogger("App Launcher/Close").trace("Terminated GLFW");
		errorCallback.free();
		LogManager.getLogger("App Launcher/Close").trace("Callbacks released");
		LogManager.getLogger("App Launcher").info("--------------------");
		LogManager.getLogger("App Launcher").info("Exiting MaxPixel Studio's Minecraft Launcher");
		LogManager.getLogger("App Launcher").info("--------------------");
	}
	private static GLFWImage load_image(String path) {
		try(MemoryStack stack = stackPush()) {
			GLFWImage icon = GLFWImage.mallocStack(stack);
			IntBuffer x = stack.callocInt(1);
			IntBuffer y = stack.callocInt(1);
			ByteBuffer image = stbi_load(path, x, y, stack.callocInt(1), 4);
			icon.set(x.get(), y.get(), image);
			return icon;
		}
	}
}