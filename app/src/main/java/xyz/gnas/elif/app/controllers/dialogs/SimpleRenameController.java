package xyz.gnas.elif.app.controllers.dialogs;

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
import xyz.gnas.elif.app.common.utility.LogUtility;
import xyz.gnas.elif.app.common.utility.runner.RunnerUtility;
import xyz.gnas.elif.app.common.utility.runner.VoidRunner;
import xyz.gnas.elif.app.common.utility.window_event.WindowEventHandler;
import xyz.gnas.elif.app.common.utility.window_event.WindowEventUtility;
import xyz.gnas.elif.app.events.dialogs.DialogEvent.DialogType;
import xyz.gnas.elif.app.events.dialogs.SingleFileDialogEvent;
import xyz.gnas.elif.core.logic.FileLogic;

import java.io.File;

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

    private void executeRunner(String errorMessage, VoidRunner runner) {
        RunnerUtility.executeVoidrunner(getClass(), errorMessage, runner);
    }

    private void writeInfoLog(String log) {
        LogUtility.writeInfoLog(getClass(), log);
    }

    @Subscribe
    public void onSingleFileDialogEvent(SingleFileDialogEvent event) {
        executeRunner("Error when handling simple rename event", () -> {
            if (event.getType() == DialogType.SimpleRename) {
                file = event.getFile();
                mivFolder.setVisible(file.isDirectory());

                if (!file.isDirectory()) {
                    imvFile.setImage(ImageUtility.getFileIcon(file, true));
                }

                lblFile.setText(file.getAbsolutePath());
                txtName.setText(file.getName());
            }
        });
    }

    @FXML
    private void initialize() {
        executeRunner("Could not initialise simple rename dialogs", () -> {
            EventBus.getDefault().register(this);
            mivFolder.managedProperty().bind(mivFolder.visibleProperty());
            imvFile.managedProperty().bind(imvFile.visibleProperty());
            imvFile.visibleProperty().bind(mivFolder.visibleProperty().not());
            addHandlerToSceneAndWindow();
        });
    }

    private void addHandlerToSceneAndWindow() {
        WindowEventUtility.bindWindowEventHandler(getClass(), txtName, new WindowEventHandler() {
            @Override
            public void handleShownEvent() {
                txtName.requestFocus();

                // select only the name of the file or the whole folder name
                txtName.selectRange(0, file.isDirectory() ? file.getName().length() :
                        FilenameUtils.removeExtension(file.getName()).length());
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
        executeRunner("Could not handle key event", () -> {
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
        });
    }

    @FXML
    private void rename(ActionEvent event) {
        executeRunner("Could not apply simple rename", () -> {
            String newName = txtName.getText() == null ? "" : txtName.getText();
            File target = new File(file.getParent() + "\\" + newName);
            boolean result = true;

            // do not ask for confirm if use old name
            if (target.exists() && !newName.equalsIgnoreCase(file.getName())) {
                result = DialogUtility.showConfirmation("There is already a file or folder with the same name, " +
                        "do you want to overwrite it?");
            }

            if (result) {
                writeInfoLog("SimpleRename file " + file.getAbsolutePath() + " to " + target.getAbsolutePath());
                FileLogic.rename(file, target);
                hideDialog();
            }
        });
    }

    private void hideDialog() {
        txtName.getScene().getWindow().hide();
    }

    @FXML
    private void cancel(ActionEvent event) {
        executeRunner("Could not cancel simple rename", this::hideDialog);
    }
}