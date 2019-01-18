package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Board {
    private Square[][] board = new Square[8][8];//the 8x8 board
    private Player whitePlayer;//the white player
    private Player blackPlayer;//the black player
    private Square selectedSquare = null;//currently selected square
    private String result = "on going";//the result of the game
    private boolean whiteTurn = true;//if it is white's turn to play
    private int x, y;//the x and y positions of the board

    /**
     * Board()
     * <p>
     * Description: constructor
     */
    public Board() {

        this.x = (Constants.FRAME_WIDTH - Constants.BOARD_SIZE) / 2;
        this.y = 10;

        reset();

        for (Square[] row : board) {
            for (Square current : row) {
                System.out.print(current.getPosition() + "  ");
            }
            System.out.println();
        }
    }

    /**
     * display(Graphics g)
     * <p>
     * Description: displays the whole board onto the graphics
     *
     * @param g the graphics where everything is going to be displayed
     */
    public void display(Graphics g) {

        if (whiteTurn) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col].display(g, col * Constants.SQUARE_SIZE + this.x, row * Constants.SQUARE_SIZE + this.y);
                }
            }
        } else {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col].display(g, col * Constants.SQUARE_SIZE + this.x, (board.length - row - 1) * Constants.SQUARE_SIZE + this.y);
                }
            }
        }

    }

    /**
     * @param pos
     * @return
     */
    public Square findSquare(String pos) {
        return board[board.length - Integer.parseInt(pos.charAt(1) + "")][(((int) pos.charAt(0)) - 97)];
    }

    /**
     * @param event
     */
    public void clicked(MouseEvent event) {
        if (result.equals("on going")) {
            Square clickedSquare = this.getClickedSquare(event);
            if (clickedSquare != null) {//if player has clicked a square
                //if there isn't a square that has been selected by the player
                if (clickedSquare.getThePieceCanMove() == null && selectedSquare != clickedSquare) {

                    if (selectedSquare != null ) {
                        selectedSquare.clicked(false);
                        selectedSquare = null;
                    }

                    selectedSquare = clickedSquare;//select the square
                    Piece clickedPiece = selectedSquare.getPiece();
                    if (clickedPiece != null) {
                        selectedSquare.clicked(whiteTurn == (clickedPiece.getPlayerColor() == Piece.Side.white));
                    } else {
                        selectedSquare.clicked(false);
                    }

                }
                //if the player clicked on the selected square
                else if (selectedSquare == clickedSquare) {

                    selectedSquare.clicked(false);
                    selectedSquare = null;

                }
                //if the piece on the selected square can move to the clicked square
                else if (clickedSquare.getThePieceCanMove() != null && selectedSquare != null) {

                    Piece piece = selectedSquare.getPiece();
                    selectedSquare.clicked(false);
                    selectedSquare = null;
                    move(piece, clickedSquare);
                }
            }
        }
    }

    /**
     * @param event
     * @return
     */
    public Square getClickedSquare(MouseEvent event) {

        int mouseX = event.getX();
        int mouseY = event.getY();

        boolean xInBounds = (mouseX >= this.x && mouseX <= this.x + Constants.BOARD_SIZE);
        boolean yInBounds = (mouseY >= this.y && mouseY <= this.y + Constants.BOARD_SIZE);
        Square s = null;
        if (xInBounds && yInBounds) {
            int row = (whiteTurn) ? ((mouseY - this.y) / Constants.SQUARE_SIZE) : (board.length - 1 - ((mouseY - this.y) / Constants.SQUARE_SIZE));
            int col = (mouseX - this.x) / Constants.SQUARE_SIZE;
            s = board[row][col];
        }
        return s;
    }

    private void move(Piece piece, Square clickedSquare) {
        boolean isCheck;
        if (whiteTurn) {
            whitePlayer.move(piece, clickedSquare);
        } else {
            blackPlayer.move(piece, clickedSquare);
        }

        moveSetup();
        whiteTurn = !whiteTurn;

        if (whiteTurn) {
            whitePlayer.occupy();
            blackPlayer.occupy();
            whitePlayer.checkKingMovements();
            blackPlayer.checkKingMovements();

            isCheck = whitePlayer.isKingUnderThreat();
            if (isCheck) {
                ArrayList<Constants.Rescuer> rescues = whitePlayer.getPossibleEscapes();
                moveSetup(false);
                if (!rescues.isEmpty()) {
                    for (Constants.Rescuer rescuer : rescues) {
                        rescuer.getPiece().possibleMovementSquares.add(rescuer.getSquare());
                        rescuer.getSquare().addPieceThatCanMove(rescuer.getPiece());
                    }
                } else {
                    result = "black wins";
                }
            } else if(!whitePlayer.hasMove()){
                result = "stalemate";
            }
        } else {
            whitePlayer.occupy();
            blackPlayer.occupy();
            whitePlayer.checkKingMovements();
            blackPlayer.checkKingMovements();

            isCheck = blackPlayer.isKingUnderThreat();
            if (isCheck) {
                ArrayList<Constants.Rescuer> rescues = blackPlayer.getPossibleEscapes();
                moveSetup(false);
                if (!rescues.isEmpty()) {
                    for (Constants.Rescuer rescuer : rescues) {
                        rescuer.getPiece().possibleMovementSquares.add(rescuer.getSquare());
                        rescuer.getSquare().addPieceThatCanMove(rescuer.getPiece());
                    }
                } else {
                    result = "white wins";
                }
            } else if(!blackPlayer.hasMove()){
                result = "stalemate";
            }
            System.out.print("");
        }
    }

    public void moveSetup() {
        moveSetup(true);
    }

    public void moveSetup(boolean fullyReset) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].reset(fullyReset);
            }
        }
        whitePlayer.reset();
        blackPlayer.reset();
    }

    public Square[][] getBoard() {
        return board;
    }

    public void reset() {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                boolean isDark = j % 2 != i % 2;
                String pos = (char) (j + 97) + "" + (board.length - i);//ascii
                board[i][j] = new Square(pos, new int[]{i, j}, isDark);
            }
        }

        whitePlayer = new Player(Piece.Side.white, this);
        blackPlayer = new Player(Piece.Side.black, this);

        whitePlayer.checkKingMovements();
        blackPlayer.checkKingMovements();
        selectedSquare = null;
        result = "on going";
        whiteTurn = true;
    }
}
