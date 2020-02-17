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

package cn.maxpixel.mpsmcl.ui.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.awt.*;

public abstract class Window {
	static {
		if(GLFW.glfwInit()) {
			GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			width = vidMode.width() / 2;
			height = vidMode.height() / 2;
			x = vidMode.width() / 4;
			y = vidMode.height() / 4;
			GLFW.glfwTerminate();
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			width = (int) (screenSize.getWidth() / 2);
			height = (int) (screenSize.getHeight() / 2);
			x = (int) (screenSize.getWidth() / 4);
			y = (int) (screenSize.getHeight() / 4);
		}
	}
	public static volatile boolean running = false;
	public static int width, height;
	public static int x, y;
	public abstract void init() throws Exception;
	public abstract void run() throws Exception;
	protected abstract void close() throws Exception;
}