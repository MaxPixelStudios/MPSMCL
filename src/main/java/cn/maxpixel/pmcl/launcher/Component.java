package cn.maxpixel.pmcl.launcher;

import com.jfoenix.controls.JFXButton;

import cn.maxpixel.pmcl.util.Language;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class Component {
	public static BorderPane main = new BorderPane();
	private Language lang = new Language("en_us");
	public static void addMainComponent(BorderPane pane) {
		Component c = new Component();
		pane.setTop(c.topComponent(new BorderPane()));
		pane.setCenter(c.centerComponent(main));
	}
	private BorderPane topComponent(BorderPane top) {
		top.setStyle("-fx-background-color: #00BBAA;");

		JFXButton settings = new JFXButton("      \n\n\n" + lang.get("component.settings"));
		settings.setStyle("-fx-background-image: url(icon/settings_icon_white_18dp.png);"
				+ "-fx-background-repeat: no-repeat;"
				+ "-fx-background-position: center;");
		settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
			}
		});
		top.setRight(settings);

		FlowPane top_center = new FlowPane();
		top_center.setAlignment(Pos.CENTER_LEFT);
		JFXButton home = new JFXButton(lang.get("component.mainpane"));
		top_center.getChildren().add(home);
		JFXButton dirManege = new JFXButton(lang.get("component.directory.manage"));
		top_center.getChildren().add(dirManege);
		top.setCenter(top_center);

		return top;
	}
	private BorderPane centerComponent(BorderPane center) {
		
		return center;
	}
	public void setMain(BorderPane main) {
		
	}
}