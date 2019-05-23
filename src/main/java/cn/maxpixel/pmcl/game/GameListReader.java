package cn.maxpixel.pmcl.game;

import java.io.File;
import java.util.LinkedList;

public class GameListReader extends File {
	private static final long serialVersionUID = -6733708357800793745L;
	public GameListReader(String pathname) {
		super(pathname);
	}
	
	
	public class GameList<V> extends LinkedList<V>{
		private static final long serialVersionUID = 7479360082116623721L;
		
	}
}