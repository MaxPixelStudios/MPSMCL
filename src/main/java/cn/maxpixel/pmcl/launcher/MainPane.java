package cn.maxpixel.pmcl.launcher;

import javafx.scene.layout.Pane;

public final class MainPane {
	Pane homePane(Pane p) {
		init(p);
		
		return p;
	}
	Pane gameDirMPane(Pane p) {
		init(p);
		
		return p;
	}
	Pane settingPane(Pane p) {
		init(p);
		
		return p;
	}
	private void init(Pane p) {
		p.setStyle("-fx-background-color: #2FAEC9");
	}
}