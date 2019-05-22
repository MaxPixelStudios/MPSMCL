package cn.maxpixel.pmcl;

import cn.maxpixel.pmcl.launcher.Component;
import cn.maxpixel.pmcl.launcher.Info;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application {
	Thread gc = new Thread(() -> {
		while(true) {
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {System.exit(0);}
			System.gc();
		}
	}, "GC");
	@Override
	public void start(Stage primaryStage) throws Exception {
		gc.start();
		BorderPane pane = new BorderPane();
		Component.addMainComponent(pane);
		Scene scene = new Scene(pane, 650, 720);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("icon/window_icon_64.png"));;
		primaryStage.setTitle(Info.NAME + " version " + Info.VERSION);
		primaryStage.show();
		primaryStage.setOnCloseRequest((event) -> {
			gc.interrupt();
		});
	}
	public static void main(String[] args) {
		launch(args);
	}
}