package com.example.puzzle_v1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Controller {
    @FXML
    private Button startButton;
    @FXML
    private Button solveButton;
    @FXML
    private Button restartButton;
    @FXML
    private HBox hBox;
    @FXML
    private DraggableGridPane puzzleGrid;
    @FXML
    private DraggableGridPane puzzlePieces;

    private final int rows = 4;
    private final int columns = 4;

    List<PuzzlePiece> piecesToRemove = new ArrayList<>();

    private List<PuzzlePiece> pieces = new ArrayList<>();
    private final double maxSize = 100;  //max px pieces size


    @FXML
    public void onSolveButtonClick(ActionEvent actionEvent) {
        onRestartButtonClick(actionEvent);

        placeCornerPieces();
        pieces.removeAll(piecesToRemove);
        piecesToRemove.clear();

        placeBorderPieces();
        pieces.removeAll(piecesToRemove);
        piecesToRemove.clear();

        placeCenterPieces();
        pieces.removeAll(piecesToRemove);
        piecesToRemove.clear();
    }

    private void placeCornerPieces() {
        for (PuzzlePiece piece : pieces) {
            if (piece.getLeftSide().equals(PieceBorder.FLAT) &&
                    piece.getUpSide().equals(PieceBorder.FLAT)) {
//                puzzleGrid.getChildren().remove( 0, 0);
                puzzleGrid.add(piece, 0, 0);
                piecesToRemove.add(piece);
            } else if (piece.getRightSide().equals(PieceBorder.FLAT) &&
                    piece.getDownSide().equals(PieceBorder.FLAT)) {
//                puzzleGrid.getChildren().remove( 3,3);
                puzzleGrid.add(piece, 3, 3);
                piecesToRemove.add(piece);

            } else if (piece.getLeftSide().equals(PieceBorder.FLAT) &&
                    piece.getDownSide().equals(PieceBorder.FLAT)) {
//                puzzleGrid.getChildren().remove( 0, 3);
                puzzleGrid.add(piece, 0, 3);
                piecesToRemove.add(piece);
            } else if (piece.getRightSide().equals(PieceBorder.FLAT) &&
                    piece.getUpSide().equals(PieceBorder.FLAT)) {
//                puzzleGrid.getChildren().remove( 3, 0);
                puzzleGrid.add(piece, 3, 0);
                piecesToRemove.add(piece);
            }
        }

    }

    public void placeBorderPieces() {
        int col;
        int row;
        for (PuzzlePiece piece : pieces) {
            col = GridPane.getColumnIndex(piece);
            row = GridPane.getRowIndex(piece);
            if (isBorder(piece)) {
                if (piece.getUpSide().equals(PieceBorder.FLAT)) {
                    if (piece.getRightSide().equals(PieceBorder.OUT_SIDE)) {
                        puzzleGrid.add(piece, 1, 0);
                        piecesToRemove.add(piece);
                    } else {
                        puzzleGrid.add(piece, 2, 0);
                        piecesToRemove.add(piece);
                    }
                } else if (piece.getDownSide().equals(PieceBorder.FLAT)) {
                    if (piece.getRightSide().equals(PieceBorder.OUT_SIDE)) {
                        puzzleGrid.add(piece, 1, 3);
                        piecesToRemove.add(piece);
                    } else {
                        puzzleGrid.add(piece, 2, 3);
                        piecesToRemove.add(piece);
                    }
                } else if (piece.getLeftSide().equals(PieceBorder.FLAT)) {
                    if (piece.getUpSide().equals(PieceBorder.OUT_SIDE)) {
                        puzzleGrid.add(piece, 0, 2);
                        piecesToRemove.add(piece);
                    } else {
                        puzzleGrid.add(piece, 0, 1);
                        piecesToRemove.add(piece);
                    }
                } else if (piece.getRightSide().equals(PieceBorder.FLAT)) {
                    if (piece.getUpSide().equals(PieceBorder.OUT_SIDE)) {
                        puzzleGrid.add(piece, 3, 2);
                        piecesToRemove.add(piece);
                    } else {
                        puzzleGrid.add(piece, 3, 1);
                        piecesToRemove.add(piece);
                    }
                }


            }
        }
    }

    public void placeCenterPieces() {
        for (PuzzlePiece piece : pieces) {

            if (piece.getUpSide().equals(PieceBorder.IN_SIDE)) {
                if (piece.getRightSide().equals(PieceBorder.OUT_SIDE)) {
                    puzzleGrid.add(piece, 1, 1);
                    piecesToRemove.add(piece);
                } else {
                    puzzleGrid.add(piece, 2, 1);
                    piecesToRemove.add(piece);
                }
            } else {
                if (piece.getRightSide().equals(PieceBorder.OUT_SIDE)) {
                    puzzleGrid.add(piece, 1, 2);
                    piecesToRemove.add(piece);
                } else {
                    puzzleGrid.add(piece, 2, 2);
                    piecesToRemove.add(piece);
                }
            }

        }
    }

    public boolean isBorder(PuzzlePiece piece) {
        return piece.getLeftSide().equals(PieceBorder.FLAT) ||
                piece.getRightSide().equals(PieceBorder.FLAT) ||
                piece.getUpSide().equals(PieceBorder.FLAT) ||
                piece.getDownSide().equals(PieceBorder.FLAT);
    }


    public boolean leftNeighborExists(PuzzlePiece piece) {
        return GridPane.getColumnIndex(piece) != 0;
    }

    public boolean rightNeighborExists(PuzzlePiece piece) {
        return GridPane.getColumnIndex(piece) != 3;
    }

    public boolean upNeighborExists(PuzzlePiece piece) {
        return GridPane.getRowIndex(piece) != 0;
    }

    public boolean downNeighborExists(PuzzlePiece piece) {
        return GridPane.getRowIndex(piece) != 3;
    }


    @FXML
    public void onStartButtonClick(ActionEvent actionEvent) {
        solveButton.setOpacity(1);
        restartButton.setOpacity(1);
        startButton.setOpacity(0);

        try {
            Image check = new Image(getClass().getResourceAsStream("/puzzlePieces/img1.jpg"));
        } catch (NullPointerException e) {
            cut(rows, columns);
        }

        puzzleGrid.setGridLinesVisible(true);
        puzzlePieces.setGridLinesVisible(true);

        //download img
        loadPieces();

        //set board
        setBoard();

    }

    private void loadPieces() {
        int current = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String path = "/puzzlePieces/img" + current + ".jpg";
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                PuzzlePiece view = new PuzzlePiece(image, puzzlePieces, i, j);
                reSize(view);
                definePiece(view, i, j);
                pieces.add(view);
                puzzlePieces.add(view, i, j);
                current++;
            }
        }
    }

    private void setBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/white.jpg")));
                PuzzlePiece piece = new PuzzlePiece(image, puzzleGrid, i, j);
                copySize(piece, pieces.get(0).getFitHeight(), pieces.get(0).getFitWidth());
                piece.setOnDragDetected(event -> {
                    piece.startFullDrag();
                    event.consume();
                });
                puzzleGrid.add(piece, i, j);
            }
        }
    }

    private void reSize(ImageView view) {
        if (view.getImage().getHeight() > maxSize || view.getImage().getWidth() > maxSize) {
            if (view.getImage().getHeight() > view.getImage().getWidth()) {
                modify(view, maxSize, view.getImage().getHeight(), true);
            } else {
                modify(view, maxSize, view.getImage().getWidth(), false);
            }
        }
    }

    private void modify(ImageView imageView, double maxSize, double maxSide, boolean isHeight) {
        if (isHeight) {
            imageView.setFitHeight(maxSize);
            imageView.setFitWidth((imageView.getImage().getWidth() * maxSize) / maxSide);
        } else {
            imageView.setFitHeight((imageView.getImage().getHeight() * maxSize) / maxSide);
            imageView.setFitWidth(maxSize);
        }
    }

    public void cut(int rows, int columns) {
        BufferedImage imgs[] = new BufferedImage[rows * columns];

        File file = new File("src/main/resources/images/source_photo.jpg");

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int subimage_Width = image.getWidth() / columns;
        int subimage_Height = image.getHeight() / rows;

        int current_img = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                imgs[current_img] = new BufferedImage(subimage_Width, subimage_Height, image.getType());
                Graphics2D img_creator = imgs[current_img].createGraphics();

                int src_first_x = subimage_Width * j;
                int src_first_y = subimage_Height * i;

                int dst_corner_x = subimage_Width * j + subimage_Width;
                int dst_corner_y = subimage_Height * i + subimage_Height;

                img_creator.drawImage(image, 0, 0, subimage_Width, subimage_Height, src_first_x, src_first_y, dst_corner_x, dst_corner_y, null);
                current_img++;
            }
        }

        for (int i = 0; i < rows * columns; i++) {
            File outputFile = new File("src/main/resources/puzzlePieces/img" + i + ".jpg");
            try {
                ImageIO.write(imgs[i], "jpg", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void copySize(PuzzlePiece img, double height, double width) {
        img.setFitHeight(height);
        img.setFitWidth(width);
    }

    public void definePiece(PuzzlePiece current, int i, int j) {

        if (j == 0) {
            current.setLeftSide(PieceBorder.FLAT);
            current.setRightSide(PieceBorder.OUT_SIDE);
        }

        if (j == 1) {
            current.setLeftSide(PieceBorder.IN_SIDE);
            current.setRightSide(PieceBorder.OUT_SIDE);
        }

        if (j == 2) {
            current.setLeftSide(PieceBorder.IN_SIDE);
            current.setRightSide(PieceBorder.IN_SIDE);
        }

        if (j == 3) {
            current.setLeftSide(PieceBorder.OUT_SIDE);
            current.setRightSide(PieceBorder.FLAT);
        }

        if (i == 0) {
            current.setUpSide(PieceBorder.FLAT);
            current.setDownSide(PieceBorder.OUT_SIDE);
        }

        if (i == 1) {
            current.setUpSide(PieceBorder.IN_SIDE);
            current.setDownSide(PieceBorder.IN_SIDE);
        }

        if (i == 2) {
            current.setUpSide(PieceBorder.OUT_SIDE);
            current.setDownSide(PieceBorder.OUT_SIDE);
        }

        if (i == 3) {
            current.setUpSide(PieceBorder.OUT_SIDE);
            current.setDownSide(PieceBorder.FLAT);
        }
    }


    public void onRestartButtonClick(ActionEvent actionEvent) {
        puzzleGrid.getChildren().clear();
        pieces.clear();
        puzzlePieces.getChildren().clear();

        onStartButtonClick(actionEvent);
    }
}