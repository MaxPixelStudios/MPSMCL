package cn.maxpixel.pmcl.launcher;

import com.jfoenix.controls.JFXButton;

import cn.maxpixel.pmcl.Launcher;
import cn.maxpixel.pmcl.util.Language;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class Component {
	public static Language lang = new Language();
	private MainPane panes = new MainPane();
	public void addMainComponent(BorderPane all) {
		Component c = new Component();
		all.setTop(c.initComponent(new BorderPane()));
		all.setCenter(panes.homePane(new Pane()));
	}
	private BorderPane initComponent(BorderPane top) {
		top.setStyle("-fx-background-color: #00BBAA;");

		JFXButton settings = new JFXButton("      \n\n\n" + lang.get("component.settings"));
		settings.setStyle(
				  "-fx-background-image: url(icon/settings_icon_white_18dp.png);"
				+ "-fx-background-repeat: no-repeat;"
				+ "-fx-background-position: center;");
		settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Launcher.pane.setCenter(panes.settingPane(new Pane()));
			}
		});
		top.setRight(settings);

		FlowPane top_center = new FlowPane();
		top_center.setAlignment(Pos.CENTER_LEFT);
		JFXButton home = new JFXButton(lang.get("component.mainpane"));
		home.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Launcher.pane.setCenter(panes.homePane(new Pane()));
			}
		});
		top_center.getChildren().add(home);
		JFXButton dirManege = new JFXButton(lang.get("component.directory.manage"));
		dirManege.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Launcher.pane.setCenter(panes.gameDirMPane(new Pane()));
			}
		});
		top_center.getChildren().add(dirManege);
		top.setCenter(top_center);

		return top;
	}
}