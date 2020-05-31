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

import cn.maxpixel.mpsmcl.InitializeException;
import cn.maxpixel.mpsmcl.Main;
import cn.maxpixel.mpsmcl.configuration.RuntimeConfigurations;
import cn.maxpixel.mpsmcl.ui.renderer.Window;
import cn.maxpixel.mpsmcl.util.ArrayUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class RenderEnvironment {
	private static final Logger OPENGL_WINDOW_RENDERER_LOGGER = LogManager.getLogger();
	private static final Logger INIT_LOGGER = LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + INITIALIZE);
	private static class Log4J2PrintStream extends OutputStream {
		private static final Logger LOGGER = LogManager.getLogger("LWJGL Debug Stream");
		private static class SingletonCharSequence implements CharSequence {
			private final char c;
			public SingletonCharSequence(char c) {
				this.c = c;
			}
			@Override
			public int length() {
				return 1;
			}
			@Override
			public char charAt(int index) {
				if(index != 0) throw new IndexOutOfBoundsException();
				return c;
			}
			@Override
			public CharSequence subSequence(int start, int end) {
				if(start != 0 || end != 0) throw new IndexOutOfBoundsException();
				return new SingletonCharSequence(c);
			}
		}
		@Override
		public void write(int b) {
			LOGGER.debug(new SingletonCharSequence((char) b));
		}
		@Override
		public void write(byte[] b, int off, int len) {
			LOGGER.debug(new String(b, off, len));
		}
	}
	private static void configLwjglDebugStreamAndSetErrorCallback() {
		Configuration.DEBUG_STREAM.set(new PrintStream(new Log4J2PrintStream()));
		INIT_LOGGER.trace("LWJGL debug stream created");

		final Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens((field, value) -> 0x10000 < value && value < 0x20000, null, GLFW.class);
		final Logger OPENGL_WINDOW_ERROR_LOGGER = LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + EXCEPTION_CAUGHT);
		glfwSetErrorCallback((error, descriptionAddress) -> {
			String description = memUTF8Safe(descriptionAddress);
			OPENGL_WINDOW_ERROR_LOGGER.fatal("--------------------");
			OPENGL_WINDOW_ERROR_LOGGER.fatal("A GLFW error occurred: {}", ERROR_CODES.get(error));
			OPENGL_WINDOW_ERROR_LOGGER.fatal("Error code: {}", String.format("0x%X", error));
			OPENGL_WINDOW_ERROR_LOGGER.fatal("Error description: {}", description);
			OPENGL_WINDOW_ERROR_LOGGER.fatal("Stacktrace: {}", () -> {
				StringBuilder builder = new StringBuilder();
				ArrayUtil.forEach(Thread.currentThread().getStackTrace(), builder.append('\t').append('\t')::append);
				return builder;
			});
			OPENGL_WINDOW_ERROR_LOGGER.fatal("--------------------");
		});
		INIT_LOGGER.trace("Created GLFW error callback");
	}

	static void initGLFW() {
		configLwjglDebugStreamAndSetErrorCallback();
		if(!glfwInit()) throw new InitializeException("GLFW initialize failed", new RuntimeException("Initialize error"));
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		INIT_LOGGER.trace("Window hints configured complete");
	}

	static void configureWindow(long window) {
		glfwSetWindowIcon(window, loadImages("icon/icon.png"));
		INIT_LOGGER.trace("Window icon configured complete");
		try(MemoryStack stack = stackPush()) {
			IntBuffer wWidth = stack.callocInt(1);
			IntBuffer wHeight = stack.callocInt(1);
			glfwGetWindowSize(window, wWidth, wHeight);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window,
					(vidMode.width() - wWidth.get(0)) / 2,
					(vidMode.height() - wHeight.get(0)) / 2);
			INIT_LOGGER.trace("Window position configured complete");
			glfwShowWindow(window);
			INIT_LOGGER.trace("Made window visible");
		}
		glfwMakeContextCurrent(window);
		INIT_LOGGER.trace("Made OpenGL context to current window");
		RuntimeConfigurations.changeSwapInterval(1);
		INIT_LOGGER.info("V-Sync enabled");
		GL.createCapabilities();
		INIT_LOGGER.trace("OpenGL capabilities created");
		GLUtil.setupDebugMessageCallback();
		glfwSetFramebufferSizeCallback(window, (window1, width, height) -> glViewport(0, 0, width, height));
		glfwSetWindowCloseCallback(window, win -> Window.running = false);
		INIT_LOGGER.trace("Window callbacks configured complete");
		float[] rgba = Main.configuration.launcherSettings.backgroundColor;
		RuntimeConfigurations.changeBackgroundColor(rgba[0], rgba[1], rgba[2],rgba[3]);
		INIT_LOGGER.trace("Initialize completed");
	}
	private static ByteBuffer readFromResources(String name) {
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).debug("Loading resource: {}", name);
		try(InputStream in = RenderEnvironment.class.getClassLoader().getResourceAsStream(name);
		    ReadableByteChannel ch = Channels.newChannel(in)) {
			ByteBuffer bb = memAlloc(in.available());
			ch.read(bb);
			bb.flip();
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).trace("Loaded resource: {}", name);
			return bb;
		} catch (Exception e) {
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).error("Couldn't load resource " + name + ", Caused by: ", e);
			throw new IllegalArgumentException("Resource load failed", e);
		}
	}
	private static GLFWImage loadImage(String path) {
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).trace("Loading Image");
		try(MemoryStack stack = stackPush()) {
			IntBuffer x = stack.callocInt(1);
			IntBuffer y = stack.callocInt(1);
			ByteBuffer buffer = readFromResources(path);
			ByteBuffer image = stbi_load_from_memory(buffer, x, y, stack.callocInt(1), 0);
			memFree(buffer);
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).debug("Loaded Image");
			return GLFWImage.callocStack(stack).set(x.get(0), y.get(0), image);
		} catch (Exception e) {
			LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).error("Couldn't load the image!", e);
			throw new RuntimeException(e);
		}
	}
	private static GLFWImage.Buffer loadImages(String... paths) {
		LogManager.getLogger(OPENGL_WINDOW_RENDERER + SLASH + LOAD_IMAGE).trace("Loading Images");
		try(MemoryStack stack = stackPush()) {
			GLFWImage.Buffer icons = GLFWImage.callocStack(paths.length, stack);
			ArrayUtil.forEach(paths, path -> icons.put(loadImage(path)));
			return icons.flip();
		}
	}
}