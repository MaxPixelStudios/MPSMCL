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
package cn.maxpixel.mpsmcl.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.*;

public class FileUtil {
	public static boolean checkExists(String fileName) {
		LogManager.getLogger("File Utilities/File Exists Check").info("checking " + fileName);
		return new File(fileName).exists();
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource) {
		return createNewFileFromStream(fileName, resource, false);
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource, boolean override) {
		if((!checkExists(fileName)) || override) {
			LogManager.getLogger("File Utilities/Create New File").trace("File not exists or override is true, creating file...");
			try {
				LogManager.getLogger("File Utilities/Create New File").trace("Writing file...");
				try(FileOutputStream target = new FileOutputStream(fileName)) {
					for (int i = resource.read(); i != -1; i = resource.read()) target.write(i);
					LogManager.getLogger("File Utilities/Create New File").trace("IO stream closing...");
					resource.close();
				}
				LogManager.getLogger("File Utilities/Create New File").debug("IO stream closed");
				LogManager.getLogger("File Utilities/Create New File").info(fileName + " successful created");
				return true;
			} catch (IOException e) {
				LogManager.getLogger("File Utilities/Exception caught").error("IO error occurs");
				LogManager.getLogger("File Utilities/Exception caught").catching(e);
				return false;
			}
		} else {
			LogManager.getLogger("File Utilities/Create New File").warn("The " + fileName + " is already exists");
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