package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Board.java
 * <p>
 * Description: The class that represents the board and everything on it with their functions, such as pieces and their movements,
 * and players and their possible threat neutralizing
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
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

        reset();//good way to initialize the uninitialized variables
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
            //display the board in a way that row #1 and #2 are at the bottom
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col].display(g, col * Constants.SQUARE_SIZE + this.x, row * Constants.SQUARE_SIZE + this.y);
                }
            }
        } else {
            //display the board in a way that row #7 and #8 are at the bottom
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    board[row][col].display(g, col * Constants.SQUARE_SIZE + this.x, (board.length - row - 1) * Constants.SQUARE_SIZE + this.y);
                }
            }
        }
    }

    /**
     * findSquare(String pos)
     * <p>
     * Description: finds a square on the board from its address:
     * board[total number of rows - row number][letter];
     * Example: a8; letter is 'a' and the row number is 8
     *
     * @param pos the address of the square that is needed to be found
     * @return the square that is on that address
     */
    public Square findSquare(String pos) {
        return board[board.length - Integer.parseInt(pos.charAt(1) + "")][(((int) pos.charAt(0)) - 97)];
    }

    /**
     * clicked(MouseEvent event)
     * <p>
     * Description: called when the user clicks on the board. The user either can select or deselect a square.
     *
     * @param event the event that gives the positions of the mouse
     */
    public void clicked(MouseEvent event) {
        if (result.equals("on going")) {//if the game is not over yet

            Square clickedSquare = this.getClickedSquare(event);
            if (clickedSquare != null) {//if player has clicked a square
                if (clickedSquare.getThePieceCanMove() == null && selectedSquare != clickedSquare) {
                    //if there is a square that has already been selected, deselect it
                    if (selectedSquare != null) {
                        selectedSquare.clicked(false);
                        selectedSquare = null;
                    }

                    selectedSquare = clickedSquare;//select the clicked square
                    Piece clickedPiece = selectedSquare.getPiece();

                    //shows the movements of the pieces that is on the clicked square if its that piece's player's turn
                    if (clickedPiece != null) {
                        selectedSquare.clicked(whiteTurn == (clickedPiece.getPlayerColor() == Piece.Side.white));
                    } else {
                        selectedSquare.clicked(false);
                    }

                }
                //if the player clicked on the selected square, deselect
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
     * getClickedSquare(MouseEvent event)
     * <p>
     * Description: finds the square that the player has clicked on
     *
     * @param event the event that contains the x and the y position of the mouse
     * @return the square that has been clicked on
     */
    public Square getClickedSquare(MouseEvent event) {

        int mouseX = event.getX();
        int mouseY = event.getY();

        boolean xInBounds = (mouseX >= this.x && mouseX <= this.x + Constants.BOARD_SIZE);
        boolean yInBounds = (mouseY >= this.y && mouseY <= this.y + Constants.BOARD_SIZE);
        Square clickedSquare = null;
        if (xInBounds && yInBounds) {

            //the turn check is necessary because of the different display configs
            int row = (whiteTurn) ? ((mouseY - this.y) / Constants.SQUARE_SIZE) : (board.length - 1 - ((mouseY - this.y) / Constants.SQUARE_SIZE));
            int col = (mouseX - this.x) / Constants.SQUARE_SIZE;
            clickedSquare = board[row][col];
        }
        return clickedSquare;
    }

    /**
     * move(Piece piece, Square clickedSquare)
     * <p>
     * Description: moves the selected piece onto the clicked square and does all the occupations for
     * each square.
     *
     * @param piece         the selected piece that is going to be moved
     * @param clickedSquare the square in which the selected piece is going to be moved
     */
    private void move(Piece piece, Square clickedSquare) {
        boolean isCheck;//used to determine if it is a check

        if (whiteTurn) {
            whitePlayer.move(piece, clickedSquare);
        } else {
            blackPlayer.move(piece, clickedSquare);
        }

        moveSetup();//refresh all the threats and occupations
        whiteTurn = !whiteTurn;

        if (whiteTurn) {
            //reoccupy
            whitePlayer.occupy();
            blackPlayer.occupy();
            whitePlayer.checkKingMovements();
            blackPlayer.checkKingMovements();

            isCheck = whitePlayer.isKingUnderThreat();

            if (isCheck) {
                //find possible ways to escape the check
                ArrayList<Constants.Rescuer> rescues = whitePlayer.getPossibleEscapes();

                //empty the possible movement squares for each of the pieces
                moveSetup(false);

                if (!rescues.isEmpty()) {
                    //add all the possible escapes onto the possible movement squares of the rescuer pieces
                    for (Constants.Rescuer rescuer : rescues) {
                        rescuer.getPiece().possibleMovementSquares.add(rescuer.getSquare());
                        rescuer.getSquare().addPieceThatCanMove(rescuer.getPiece());
                    }
                } else {
                    result = "black wins";
                }
            } else if (!whitePlayer.hasMove()) {
                result = "stalemate";
            }
        } else {
            //same drill, but with the black pieces
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
            } else if (!blackPlayer.hasMove()) {
                result = "stalemate";
            }
        }
    }

    /**
     * moveSetup()
     * <p>
     * Description: fully resets the whole board to do the reoccupation
     */
    public void moveSetup() {
        moveSetup(true);
    }

    /**
     * moveSetup(boolean fullyReset)
     * <p>
     * Description: resets the whole board to do the reoccupation
     *
     * @param fullyReset if it is required to reset the path as a threat and other important factors
     */
    public void moveSetup(boolean fullyReset) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].reset(fullyReset);
            }
        }
        whitePlayer.reset();
        blackPlayer.reset();
    }

    /**
     * reset()
     * <p>
     * Description: used to reset the whole game, in other words, starting a new game on the same board
     */
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

    /**
     * GETTERS AND SETTERS
     **/

    public String getResult() {
        return result;
    }

    public Square[][] getBoard() {
        return board;
    }
}
