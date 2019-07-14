package cn.maxpixel.mpsmcl.game;

import com.google.gson.annotations.Expose;

public class GameDirectory {
	@Expose
	private String path;
	public static String getDefaultPath() {
		return System.getProperty("user.home") + "\\AppData\\Roaming\\.mpsmcl";
	}
	public String getPath() {
		return path;
	}
}