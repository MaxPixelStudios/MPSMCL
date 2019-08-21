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

package cn.maxpixel.mpsmcl.task;

import cn.maxpixel.mpsmcl.util.ArrayUtil;
import org.apache.logging.log4j.LogManager;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Schedule {
	private static int count = 0;
	private static ArrayList<Task> tasks;
	private String name;
	private Schedule(String name) {
		LogManager.getLogger("Task System/Schedule").debug("Schedule initializing");
		tasks = new ArrayList<>();
		this.name = name;
	}
	public static Schedule getSchedule() {
		StringBuffer sb = new StringBuffer(40);
		try {
			ArrayUtil.forEach(MessageDigest.getInstance("SHA-1").digest(Long.toString(new Random().nextLong()).getBytes(Charset.forName("UTF-8"))), (t) -> {
				if(Byte.toUnsignedInt(t) < 16) sb.append("0");
				sb.append(Integer.toHexString(Byte.toUnsignedInt(t)));
			});
		} catch (NoSuchAlgorithmException e) {}
		return new Schedule(sb.toString());
	}
	public Schedule then(Task t) {
		tasks.add(++count, Objects.requireNonNull(t));
		LogManager.getLogger("Task System/Schedule").debug("Added task");
		return this;
	}
	public Thread toThread() {
		return new Thread("Schedule " + name) {
			public void run() {
				LogManager.getLogger("Task System/Schedule").debug("Started running tasks");
				tasks.forEach(Task::execute);
			}
		};
	}

}