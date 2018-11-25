package xyz.gnas.elif.app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import xyz.gnas.elif.app.common.ResourceManager;

import static xyz.gnas.elif.app.common.utility.DialogUtility.writeErrorLog;

public class FXMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> writeErrorLog(getClass(), "Uncaught "
                    + "exception", e));

            stage.setTitle("Elif");
            stage.getIcons().add(ResourceManager.getAppIcon());
            stage.setMaximized(true);
            FXMLLoader loader = new FXMLLoader(ResourceManager.getAppFXML());
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().addAll(ResourceManager.getCSSList());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            writeErrorLog(getClass(), "Could not start the application", e);
        }
    }
}