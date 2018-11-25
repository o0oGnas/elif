package xyz.gnas.elif.app.controllers.dialog;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FilenameUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import xyz.gnas.elif.app.common.utility.DialogUtility;
import xyz.gnas.elif.app.common.utility.ImageUtility;
import xyz.gnas.elif.app.common.utility.WindowEventUtility;
import xyz.gnas.elif.app.events.dialog.SimpleRenameEvent;
import xyz.gnas.elif.core.logic.FileLogic;

import java.io.File;

import static xyz.gnas.elif.app.common.utility.DialogUtility.showConfirmation;

public class SimpleRenameController {
    @FXML
    private MaterialIconView mivFolder;

    @FXML
    private ImageView imvFile;

    @FXML
    private Label lblFile;

    @FXML
    private TextField txtName;

    private File file;

    private void showError(Exception e, String message, boolean exit) {
        DialogUtility.showError(getClass(), e, message, exit);
    }

    private void writeInfoLog(String log) {
        DialogUtility.writeInfoLog(getClass(), log);
    }

    @Subscribe
    public void onRenameEvent(SimpleRenameEvent event) {
        try {
            file = event.getFile();
            mivFolder.setVisible(file.isDirectory());

            if (!file.isDirectory()) {
                imvFile.setImage(ImageUtility.getFileIcon(file, true));
            }

            lblFile.setText(file.getAbsolutePath());
            txtName.setText(file.getName());
        } catch (Exception e) {
            showError(e, "Error handling single rename event", false);
        }
    }

    @FXML
    private void initialize() {
        try {
            EventBus.getDefault().register(this);
            mivFolder.managedProperty().bind(mivFolder.visibleProperty());
            imvFile.managedProperty().bind(imvFile.visibleProperty());
            imvFile.visibleProperty().bind(mivFolder.visibleProperty().not());
            addHandlerToSceneAndWindow();
        } catch (Exception e) {
            showError(e, "Could not initialise single rename dialog", true);
        }
    }

    private void addHandlerToSceneAndWindow() {
        WindowEventUtility.bindWindowEventHandler(txtName, new WindowEventUtility.WindowEventHandler() {
            @Override
            public void handleShownEvent() {
                try {
                    // select only the name of the file by default
                    txtName.requestFocus();
                    String name = FilenameUtils.removeExtension(file.getName());
                    txtName.selectRange(0, name.length());
                } catch (Exception e) {
                    showError(e, "Error handling window shown event", false);
                }
            }

            @Override
            public void handleFocusedEvent() {
            }

            @Override
            public void handleCloseEvent(WindowEvent windowEvent) {
            }
        });
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        try {
            switch (keyEvent.getCode()) {
                case ENTER:
                    rename(null);
                    break;

                case ESCAPE:
                    cancel(null);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            showError(e, "Could not handle key event", false);
        }
    }

    @FXML
    private void rename(ActionEvent event) {
        try {
            String newName = txtName.getText() == null ? "" : txtName.getText();
            File target = new File(file.getParent() + "\\" + newName);
            boolean result = true;

            // do not ask for confirm if use old name
            if (target.exists() && !newName.equalsIgnoreCase(file.getName())) {
                result = showConfirmation("There is already a file or folder with the same name, do you want to " +
                        "overwrite it?");
            }

            if (result) {
                writeInfoLog("Raname file " + file.getAbsolutePath() + " to " + target.getAbsolutePath());
                FileLogic.rename(file, target);
                hideDialog();
            }
        } catch (Exception e) {
            showError(e, "Could not apply rename", false);
        }
    }

    private void hideDialog() {
        txtName.getScene().getWindow().hide();
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {
            hideDialog();
        } catch (Exception e) {
            showError(e, "Could not cancel rename", false);
        }
    }
}