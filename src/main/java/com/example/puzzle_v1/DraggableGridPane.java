package com.example.puzzle_v1;

import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class DraggableGridPane extends GridPane {
    public DraggableGridPane() {
        super();
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasContent(DataFormat.PLAIN_TEXT)) {
                PuzzlePiece draggedPiece = (PuzzlePiece) event.getGestureSource();

                int targetColIndex = GridPane.getColumnIndex(draggedPiece);
                int targetRowIndex = GridPane.getRowIndex(draggedPiece);

                Pane parent = (Pane) draggedPiece.getParent();
                if (parent != null) {
                    parent.getChildren().remove(draggedPiece);
                }

                this.add(draggedPiece, targetColIndex, targetRowIndex);

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

}
