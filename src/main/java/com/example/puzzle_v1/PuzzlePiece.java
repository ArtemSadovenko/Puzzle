package com.example.puzzle_v1;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

import java.util.concurrent.atomic.AtomicReference;


public class PuzzlePiece extends ImageView {
    private PieceBorder upSide;
    private PieceBorder downSide;
    private PieceBorder rightSide;
    private PieceBorder leftSide;

    private double mouseX;
    private double mouseY;

    public PuzzlePiece(){
        super();
        initDragAndDrop();
    }

    public PuzzlePiece(Image image) {
        // Call the superclass constructor with the provided image
        super(image);
        upSide = PieceBorder.UNDEFINED;
        downSide = PieceBorder.UNDEFINED;
        rightSide = PieceBorder.UNDEFINED;
        leftSide = PieceBorder.UNDEFINED;
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        this.setOnDragDetected(event -> {
            startFullDrag();
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
//            ClipboardContent content = new ClipboardContent();
//            content.put(DataFormat.IMAGE, this.getImage());
//            dragboard.setContent(content);

            event.consume();
        });

        this.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;
            this.setTranslateX(this.getTranslateX() + deltaX);
            this.setTranslateY(this.getTranslateY() + deltaY);
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        this.setOnDragOver(event -> {
            if (event.getGestureSource() != this && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasImage()) {
                // Update the position of the dragged piece based on its new location
                int targetColIndex = GridPane.getColumnIndex(this);
                int targetRowIndex = GridPane.getRowIndex(this);
                GridPane.setColumnIndex((Node) dragboard.getContent(DataFormat.IMAGE), targetColIndex);
                GridPane.setRowIndex((Node) dragboard.getContent(DataFormat.IMAGE), targetRowIndex);

                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }


    // Method to swap PuzzlePiece positions
    private void swapPieces(PuzzlePiece otherPiece) {
        int thisCol = GridPane.getColumnIndex(this);
        int thisRow = GridPane.getRowIndex(this);
        int otherCol = GridPane.getColumnIndex(otherPiece);
        int otherRow = GridPane.getRowIndex(otherPiece);

        GridPane.setColumnIndex(this, otherCol);
        GridPane.setRowIndex(this, otherRow);
        GridPane.setColumnIndex(otherPiece, thisCol);
        GridPane.setRowIndex(otherPiece, thisRow);
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


}
