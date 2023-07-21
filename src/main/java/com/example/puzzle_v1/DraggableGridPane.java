package com.example.puzzle_v1;

import javafx.scene.Node;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class DraggableGridPane extends GridPane {
    public DraggableGridPane() {
        super();
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof PuzzlePiece) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
//            if (dragboard.hasContent(DataFormat.IMAGE)) {
                int targetColIndex = GridPane.getColumnIndex((Node) event.getGestureTarget());
                int targetRowIndex = GridPane.getRowIndex((Node) event.getGestureTarget());

                Node draggedNode = (Node) dragboard.getContent(DataFormat.IMAGE);
                int originalColIndex = GridPane.getColumnIndex(draggedNode);
                int originalRowIndex = GridPane.getRowIndex(draggedNode);

                // Swap positions of the dragged node with the target node
                this.getChildren().remove(draggedNode);
                this.add(draggedNode, targetColIndex, targetRowIndex);
                this.add((Node) event.getGestureSource(), originalColIndex, originalRowIndex);

                success = true;
//            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
