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

package cn.maxpixel.mpsmcl.configuration;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11C;

public class RuntimeConfigurations {
	public static void changeBackgroundColor(float red, float green, float blue, float alpha) {
		GL11C.glClearColor(red, green, blue, alpha);
	}
	public static void changeSwapInterval(int interval) {
		if(interval < 0 || interval > 2) throw new IllegalArgumentException();
		GLFW.glfwSwapInterval(interval);
	}
}