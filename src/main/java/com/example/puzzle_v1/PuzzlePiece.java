package com.example.puzzle_v1;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.concurrent.atomic.AtomicReference;


public class PuzzlePiece extends ImageView {
    private PieceBorder upSide;
    private PieceBorder downSide;
    private PieceBorder rightSide;
    private PieceBorder leftSide;

    private double mouseX;
    private double mouseY;

    private GridPane parentGridPane;

    public PuzzlePiece(){
        super();
        initDragAndDrop();
    }

    public PuzzlePiece(Image image) {
        super(image);
        upSide = PieceBorder.UNDEFINED;
        downSide = PieceBorder.UNDEFINED;
        rightSide = PieceBorder.UNDEFINED;
        leftSide = PieceBorder.UNDEFINED;
        initDragAndDrop();
    }



    public PuzzlePiece(Image image, GridPane parentGridPane, int columnIndex, int rowIndex) {
        super(image);
        this.parentGridPane = parentGridPane;
        GridPane.setColumnIndex(this, columnIndex);
        GridPane.setRowIndex(this, rowIndex);
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnDragDetected(event -> {
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(DataFormat.PLAIN_TEXT, "");
            dragboard.setContent(content);
            event.consume();
        });

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
                GridPane targetGridPane = (GridPane) this.getParent();
                PuzzlePiece draggedPiece = (PuzzlePiece) event.getGestureSource();

                int targetColIndex = GridPane.getColumnIndex(this);
                int targetRowIndex = GridPane.getRowIndex(this);

                Pane parent = (Pane) draggedPiece.getParent();
                if (parent != null) {
                    draggedPiece.setParentGridPane(parentGridPane);
                    parent.getChildren().remove(draggedPiece);
                }

                targetGridPane.add(draggedPiece, targetColIndex, targetRowIndex);

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });


    }



    public PieceBorder getUpSide() {
        return upSide;
    }

    public PieceBorder getDownSide() {
        return downSide;
    }

    public PieceBorder getRightSide() {
        return rightSide;
    }

    public PieceBorder getLeftSide() {
        return leftSide;
    }

    public void setUpSide(PieceBorder upSide) {
        this.upSide = upSide;
    }

    public void setDownSide(PieceBorder downSide) {
        this.downSide = downSide;
    }

    public void setRightSide(PieceBorder rightSide) {
        this.rightSide = rightSide;
    }

    public void setLeftSide(PieceBorder leftSide) {
        this.leftSide = leftSide;
    }


    public GridPane getParentGridPane() {
        return parentGridPane;
    }

    public void setParentGridPane(GridPane parentGridPane) {
        this.parentGridPane = parentGridPane;
    }
}
