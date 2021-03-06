package xyz.gnas.elif.app.controllers.explorer;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import xyz.gnas.elif.app.common.Configurations;
import xyz.gnas.elif.app.common.utility.ImageUtility;
import xyz.gnas.elif.app.common.utility.runner.RunnerUtility;
import xyz.gnas.elif.app.controllers.explorer.ExplorerTableCellCallback.Column;
import xyz.gnas.elif.app.models.explorer.ExplorerItemModel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

class ExplorerTableCell extends TableCell<ExplorerItemModel, ExplorerItemModel> {
    private Column column;

    public ExplorerTableCell(Column column) {
        this.column = column;
    }

    @Override
    protected void updateItem(ExplorerItemModel item, boolean empty) {
        RunnerUtility.executeVoidrunner(getClass(), "Error when displaying item", () -> {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                display(item);
            }
        });
    }

    private void display(ExplorerItemModel item) {
        switch (column) {
            case Name:
                setIcon(item);
                setText(item.getName());
                break;

            case Extension:
                setText(item.getExtension());
                break;

            case Size:
                DecimalFormat format = new DecimalFormat("#,###");
                setText(format.format(item.getSize()));
                break;

            case Date:
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                setText(dateFormat.format(item.getDate().getTime()));
                break;

            default:
                break;
        }
    }

    private void setIcon(ExplorerItemModel item) {
        // show icon depending on file or folder
        File file = item.getFile();

        if (file.isDirectory()) {
            MaterialIconView mivFolder = new MaterialIconView(MaterialIcon.FOLDER_OPEN, Configurations.ICON_SIZE);
            setGraphic(mivFolder);
        } else {
            ImageView imv = new ImageView(ImageUtility.getFileIcon(file, true));
            setGraphic(imv);
        }
    }
}