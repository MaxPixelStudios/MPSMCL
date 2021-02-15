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

package cn.maxpixel.mpsmcl.task;

import cn.maxpixel.mpsmcl.AsyncThreadException;
import cn.maxpixel.mpsmcl.util.ArrayUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class Schedule {
	private static final Logger SCHEDULE_LOGGER = LogManager.getLogger(TASK_SYSTEM + SLASH + SCHEDULE);
	private int count;
	private AtomicInteger index;
	private ArrayList<Task> tasks;
	private String name;
	private Schedule(String name) {
		SCHEDULE_LOGGER.trace("Schedule initializing");
		tasks = new ArrayList<>();
		this.name = name;
		this.count = 0;
		this.index = new AtomicInteger();
	}
	public static Schedule getSchedule() {
		StringBuilder sb = new StringBuilder(40);
		try {
			ArrayUtil.forEach(MessageDigest.getInstance("SHA-1").digest(Long.toString(new Random().nextLong()).getBytes(StandardCharsets.UTF_16BE)), t -> {
				if(Byte.toUnsignedInt(t) < 16) sb.append("0");
				sb.append(Integer.toHexString(Byte.toUnsignedInt(t)));
			});
		} catch (NoSuchAlgorithmException ignored) {}
		return new Schedule(sb.toString());
	}
	public Schedule add(Task t) {
		tasks.add(count++, Objects.requireNonNull(t));
		SCHEDULE_LOGGER.trace("Added task");
		return this;
	}
	public Thread getThread() {
		return new Thread("Schedule " + name) {
			public void run() {
				SCHEDULE_LOGGER.debug("Started running tasks");
				tasks.forEach(Task::execute);
			}
		};
	}
	public void runThread() {
		getThread().start();
	}
	public Thread[] getAsynchronousThread(int tcount) {
		if(count < tcount) throw new AsyncThreadException("Asynchronous thread count more than task count");
		else if(tcount < 2) throw new AsyncThreadException("Asynchronous thread count could not less than 2");
		else {
			int threadNumber = 0;
			ThreadGroup asyncThreads = new ThreadGroup("Asynchronous Task Threads");
			ArrayList<Thread> threads = new ArrayList<>();
			int tpt = count / tcount; // task(s) per thread
			if((count - tpt * tcount) > 0) threads.add(new Thread(asyncThreads, "Schedule " + name + "_Asynchronous Thread" + threadNumber++) {
				@Override
				public void run() {
					int i;
					do {
						i = index.getAndIncrement();
						tasks.get(i).execute();
					} while(i < (count - tpt * tcount));
				}
			});
			do threads.add(new Thread(asyncThreads, "Schedule " + name + "_Asynchronous Thread" + threadNumber++) {
				@Override
				public void run() {
					for(int i = 0; i < tpt; ++i) tasks.get(index.getAndIncrement()).execute();
				}
			}); while(threadNumber <= tcount);

			return threads.toArray(new Thread[0]);
		}
	}
	public void runAsynchronousThread(int tcount) {
		ArrayUtil.forEach(getAsynchronousThread(tcount), Thread::start);
	}
}