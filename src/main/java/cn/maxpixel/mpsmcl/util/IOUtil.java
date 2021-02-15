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

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static cn.maxpixel.mpsmcl.LoggingConstants.*;

public class IOUtil {
	public static boolean checkExists(String fileName) {
		LogManager.getLogger(FILE_UTILITIES + SLASH + FILE_EXISTS_CHECK).debug("checking " + fileName);
		return Files.exists(Paths.get(fileName));
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource) {
		return createNewFileFromStream(fileName, resource, false);
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource, boolean overwrite) {
		final Logger CREATE_NEW_FILE_LOGGER = LogManager.getLogger(FILE_UTILITIES + SLASH + CREATE_NEW_FILE);
		if(!checkExists(fileName) || overwrite) {
			CREATE_NEW_FILE_LOGGER.debug("File not exists or overwrite is true, creating file...");
			try {
				CREATE_NEW_FILE_LOGGER.trace("Writing file...");
				try(FileChannel channel = FileChannel.open(Paths.get(fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
				    InputStream resourceStream = resource;
				    ReadableByteChannel resourceChannel = Channels.newChannel(resourceStream)) {
					channel.transferFrom(resourceChannel, 0, Long.MAX_VALUE);
					CREATE_NEW_FILE_LOGGER.trace("IO stream closing...");
				}
				CREATE_NEW_FILE_LOGGER.trace("IO stream closed");
				CREATE_NEW_FILE_LOGGER.info(fileName + " successful created");
				return true;
			} catch (IOException e) {
				final Logger EXCEPTION_CAUGHT_LOGGER = LogManager.getLogger(FILE_UTILITIES + SLASH + EXCEPTION_CAUGHT);
				EXCEPTION_CAUGHT_LOGGER.error("IO error occurs", e);
				return false;
			}
		} else {
			CREATE_NEW_FILE_LOGGER.warn("The " + fileName + " is already exists");
			return false;
		}
	}
	public static String readStreamAsString(InputStream stream) {
		final Logger READ_FILE_LOGGER = LogManager.getLogger(FILE_UTILITIES + SLASH + READ_FILE);
		try {
			ByteArrayList list = new ByteArrayList();
			READ_FILE_LOGGER.info("Reading file");
			int len;
			byte[] bytes = new byte[8192];
			while((len = stream.read(bytes)) > 0) {
				list.addElements(list.size(), bytes, 0, len);
			}
			READ_FILE_LOGGER.debug("File read complete");
			return new String(list.toByteArray());
		} catch(IOException ioe) {
			READ_FILE_LOGGER.warn("An error occurred when reading file", ioe);
			return "";
		}
	}
}