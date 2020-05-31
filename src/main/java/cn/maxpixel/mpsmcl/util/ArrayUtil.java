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
package cn.maxpixel.mpsmcl.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class ArrayUtil {
	public static <T> void forEach(T[] arr, Consumer<? super T> method) {
		if(arr != null) {
			Objects.requireNonNull(method);
			for(T o : arr) {
				method.accept(o);
			}
		}
	}
	public static void forEach(byte[] arr, Consumer<? super Byte> method) {
		for(byte b : arr) {
			method.accept(b);
		}
	}
	public static void forEach(short[] arr, Consumer<? super Short> method) {
		for(short s : arr) {
			method.accept(s);
		}
	}
	public static void forEach(int[] arr, IntConsumer method) {
		for(int i : arr) {
			method.accept(i);
		}
	}

}