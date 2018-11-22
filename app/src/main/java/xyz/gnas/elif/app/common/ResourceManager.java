package xyz.gnas.elif.app.common;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import xyz.gnas.elif.app.FXMain;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Gnas
 * @Description Manage resources, including lazy initialisation
 * @Date Oct 10, 2018
 */
public class ResourceManager {
    private static Image appIcon;
    private static Image renameSingleIcon;

    private static Media notificationSound;

    private static List<String> cssList;

    private static URL appFXML;
    private static URL operationFXML;
    private static URL explorerFXML;
    private static URL singleRenameFXML;

    public static Image getAppIcon() {
        if (appIcon == null) {
            appIcon = getIcon("app.png");
        }

        return appIcon;
    }

    private static Image getIcon(String iconName) {
        return new Image(getClassLoader().getResourceAsStream("icons/" + iconName));
    }

    private static ClassLoader getClassLoader() {
        return FXMain.class.getClassLoader();
    }

    public static Image getRenameSingleIcon() {
        if (renameSingleIcon == null) {
            renameSingleIcon = getIcon("rename_single.png");
        }

        return renameSingleIcon;
    }

    public static Media getNotificationSound() {
        if (notificationSound == null) {
            notificationSound = new Media(getResourceString("notification.mp3"));
        }

        return notificationSound;
    }

    private static String getResourceString(String resource) {
        return getResourceURL(resource).toExternalForm();
    }

    public static List<String> getCSSList() {
        if (cssList == null) {
            cssList = new LinkedList<String>();
            cssList.add(getCSS("app"));
            cssList.add(getCSS("theme"));
        }

        return cssList;
    }

    private static String getCSS(String cssName) {
        return getResourceString("css/" + cssName + ".css");
    }

    private static URL getResourceURL(String resource) {
        return getClassLoader().getResource(resource);
    }

    public static URL getAppFXML() {
        if (appFXML == null) {
            appFXML = getFXML("App");
        }

        return appFXML;
    }

    private static URL getFXML(String fxml) {
        return getResourceURL("fxml/" + fxml + ".fxml");
    }

    public static URL getExplorerFXML() {
        if (explorerFXML == null) {
            explorerFXML = getFXML("explorer/Explorer");
        }

        return explorerFXML;
    }

    public static URL getOperationFXML() {
        if (operationFXML == null) {
            operationFXML = getFXML("operation/Operation");
        }

        return operationFXML;
    }

    public static URL getSingleRenameFXML() {
        if (singleRenameFXML == null) {
            singleRenameFXML = getDialogFXML("SingleRename");
        }

        return singleRenameFXML;
    }

    private static URL getDialogFXML(String fxml) {
        return getFXML("dialog/" + fxml);
    }
}
