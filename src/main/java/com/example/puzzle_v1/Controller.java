package com.example.puzzle_v1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Controller {
    @FXML
    private Button solveButton;
    @FXML
    private VBox vBox;
    @FXML
    private DraggableGridPane puzzleGrid;
    @FXML
    private DraggableGridPane puzzlePieces;

    private int rows = 4;
    private int columns = 4;

    private PuzzlePiece[][] table = new PuzzlePiece[rows][columns];

    private List<PuzzlePiece> pieces = new ArrayList<>();
    private final double maxSize = 100;




    @FXML
    public void onSolveButtonClick(ActionEvent actionEvent) {
        String filePath = "/images/source_photo2.jpg";
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(filePath)));

        PuzzlePiece view = new PuzzlePiece(image);

        reSize(view);

        puzzleGrid.add(view, 0, 0);
    }

    @FXML
    public void onStartButtonClick(ActionEvent actionEvent) {
        solveButton.setOpacity(1);




        //todo del try-catch
        try {
            Image check = new Image(getClass().getResourceAsStream("/puzzlePieces/img1.jpg"));
        }catch (NullPointerException e){
            cut(rows,columns);
        }

//        cut(rows,columns);

        puzzleGrid.setGridLinesVisible(true);

        puzzlePieces.setGridLinesVisible(true);
        int current = 0;

        //download img
        for (int i = 0; i < rows*columns; i++) {
                String path = "/puzzlePieces/img" + i + ".jpg";
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                PuzzlePiece view = new PuzzlePiece(image);
                reSize(view);
//                view.setOnDragDetected(event -> {
//                    view.startFullDrag();
//                    event.consume();
//                });
                pieces.add(view);
        }
        //set border
        definePieces();




        //set board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
//                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/white.jpg")));
                PuzzlePiece piece = new PuzzlePiece();
                copySize(piece, pieces.get(0).getFitHeight(), pieces.get(0).getFitWidth());
                piece.setOnDragDetected(event -> {
                    piece.startFullDrag();
                    event.consume();
                });
                puzzleGrid.add(piece, i,j);
                table[i][j] = piece;
            }
        }



        //set pieces choice
        for (int i = 0; i < (columns*rows)/2; i++) {
            for (int j = 0; j < 2; j++) {
                puzzlePieces.add(pieces.get(current), i,j);
                current++;
            }
        }

//        int x = GridPane.getColumnIndex(pieces.get(0));
//        int y = GridPane.getRowIndex(pieces.get(0));
//
//        int x1 = GridPane.getColumnIndex(table[1][1]);
//        int y1 = GridPane.getRowIndex(table[1][1]);

//        pieces.get(0).setOnDragDetected(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//
//            }
//        });
    }

    private void reSize(ImageView view){
        if(view.getImage().getHeight() > maxSize || view.getImage().getWidth() > maxSize){
            if (view.getImage().getHeight() > view.getImage().getWidth()){
                modify(view, maxSize, view.getImage().getHeight(), true);
            }
            else {
                modify(view, maxSize, view.getImage().getWidth(), false);
            }
        }
    }

    private void modify(ImageView imageView, double maxSize, double maxSide, boolean isHeight){
        if (isHeight){
            imageView.setFitHeight(maxSize);
            imageView.setFitWidth((imageView.getImage().getWidth()* maxSize)/ maxSide);
        }
        else {
            imageView.setFitHeight((imageView.getImage().getHeight()* maxSize)/ maxSide);
            imageView.setFitWidth(maxSize);
        }
    }

    public void cut(int rows, int columns){
        BufferedImage imgs[] = new BufferedImage[rows*columns];

        File file = new File("src/main/resources/images/source_photo2.jpg");

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

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
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

        for (int i = 0; i < rows*columns; i++)
        {
            File outputFile = new File("src/main/resources/puzzlePieces/img" + i + ".jpg");
            try {
                ImageIO.write(imgs[i], "jpg", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void copySize(PuzzlePiece img, double height, double width){
        img.setFitHeight(height);
        img.setFitWidth(width);
    }

    public void definePieces(){
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                PuzzlePiece current = pieces.get(counter);

//                if(i == 0 && j==0){
//                    current.setUpSide(PieceBorder.FLAT);
//                    current.setLeftSide(PieceBorder.FLAT);
//
//                }
//                if (j == 3 && i == 0){
//
//                }

                if(j == 0){
                    current.setLeftSide(PieceBorder.FLAT);
                    current.setRightSide(PieceBorder.OUT_SIDE);
                }

                if (j == 1){
                    current.setLeftSide(PieceBorder.IN_SIDE);
                    current.setRightSide(PieceBorder.OUT_SIDE);
                }

                if (j == 2){
                    current.setLeftSide(PieceBorder.IN_SIDE);
                    current.setRightSide(PieceBorder.IN_SIDE);
                }

                if (j == 3){
                    current.setLeftSide(PieceBorder.OUT_SIDE);
                    current.setRightSide(PieceBorder.FLAT);
                }

               if(i == 0){
                   current.setUpSide(PieceBorder.FLAT);
                   current.setDownSide(PieceBorder.OUT_SIDE);
               }

                if(i == 1){
                    current.setUpSide(PieceBorder.IN_SIDE);
                    current.setDownSide(PieceBorder.IN_SIDE);
                }

                if(i == 2){
                    current.setUpSide(PieceBorder.OUT_SIDE);
                    current.setDownSide(PieceBorder.OUT_SIDE);
                }

                if(i == 3){
                    current.setUpSide(PieceBorder.OUT_SIDE);
                    current.setDownSide(PieceBorder.FLAT);
                }

                counter++;
            }
        }
    }




}