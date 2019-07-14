package cn.maxpixel.mpsmcl.util;

import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	public static boolean checkExists(String fileName) {
		LogManager.getLogger("File Utilities/File Exists Check").info("checking " + fileName);
		return new File(fileName).exists();
	}
	public static boolean createNewFileFromStream(String fileName, InputStream resource) {
		if(!checkExists(fileName)) {
			LogManager.getLogger("File Utilities/Create New File").trace("File not exists, creating...");
			try {
				FileOutputStream target = new FileOutputStream(fileName);
				LogManager.getLogger("File Utilities/Create New File").trace("Writing file...");
				int i;
				while((i = resource.read()) != -1) {
					target.write(i);
					target.flush();
				}
				LogManager.getLogger("File Utilities/Create New File").trace("IO stream closing...");
				target.close();
				resource.close();
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
}