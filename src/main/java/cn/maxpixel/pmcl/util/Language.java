package cn.maxpixel.pmcl.util;

import java.util.Locale;
import java.util.Properties;

public class Language {
	private static Properties pro = new Properties();
	
	public Language() {
		String lang = Locale.getDefault().toString();
		if(lang.contains("zh_CN"))
			new Language("zh_cn");
		else if(lang.contains("zh_TW"))
			new Language("zh_tw");
		else if(lang.contains("en_US"))
			new Language("en_us");
		else 
			new Language("en_us");
	}
	
	public Language(String instance) {
			try {
				pro.load(getClass().getResourceAsStream("/lang/" + instance + ".properties"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public String get(String name) {
		String result = pro.getProperty(name);
		if(result == null) 
			throw new IllegalArgumentException("The name \"" + name + "\" not found");
		else 
			return result;
	}
}