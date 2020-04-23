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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
	public static boolean checkExists(String fileName) {
		LogManager.getLogger("File Utilities/File Exists Check").info("checking " + fileName);
		return Files.exists(Paths.get(fileName));
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource) {
		return createNewFileFromStream(fileName, resource, false);
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource, boolean override) {
		final Logger CREATE_NEW_FILE = LogManager.getLogger("File Utilities/Create New File");
		if((!checkExists(fileName)) || override) {
			CREATE_NEW_FILE.trace("File not exists or override is true, creating file...");
			try {
				CREATE_NEW_FILE.trace("Writing file...");
				try(FileOutputStream target = new FileOutputStream(fileName);
					FileChannel channel = target.getChannel();
					InputStream resourceStream = resource;
					ReadableByteChannel resourceChannel = Channels.newChannel(resourceStream)) {
					channel.transferFrom(resourceChannel, 0, Long.MAX_VALUE);
					CREATE_NEW_FILE.trace("IO stream closing...");
				}
				CREATE_NEW_FILE.debug("IO stream closed");
				CREATE_NEW_FILE.info(fileName + " successful created");
				return true;
			} catch (IOException e) {
				LogManager.getLogger("File Utilities/Exception caught").error("IO error occurs");
				LogManager.getLogger("File Utilities/Exception caught").catching(e);
				return false;
			}
		} else {
			CREATE_NEW_FILE.warn("The " + fileName + " is already exists");
			return false;
		}
	}
	public static String readStringFromStream(InputStream stream) {
		StringBuilder buffer = new StringBuilder();
		try {
			LogManager.getLogger("File Utilities/Read File").info("Reading file");
			int i;
			while((i = stream.read()) != -1) {
				buffer.append((char)i);
			}
			LogManager.getLogger("File Utilities/Read File").debug("File read complete");
		} catch(IOException ioe) {
			LogManager.getLogger("File Utilities/Read File").warn("An error occurred when reading file");
			LogManager.getLogger("File Utilities/Read File").catching(Level.WARN, ioe);
		}
		return buffer.toString();
	}
}