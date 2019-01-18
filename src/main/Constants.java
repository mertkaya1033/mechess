package main;

/**
 * Constants.java
 * <p>
 * Description: This file contains all the required constants for the game. That includes
 * the artificial made classes to hold two different types of objects at the same time.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Constants {
    public static final int FRAME_WIDTH = 600;
    public static final int FRAME_HEIGHT = 700;
    public static final int BOARD_SIZE = (FRAME_WIDTH / 9) * 8;
    public static final int SQUARE_SIZE = BOARD_SIZE / 8;

    /**
     * Description: A class which holds a piece and a square at the same time. It is called
     * rescuer because it is used to hold which piece can neutralize the threat on the king and
     * the square that piece needs to move to neutralize the threat.
     */
    public static class Rescuer {
        private Piece piece;
        private Square square;

        public Rescuer(Piece piece, Square square) {
            this.piece = piece;
            this.square = square;
        }

        public Piece getPiece() {
            return piece;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
        }

        public Square getSquare() {
            return square;
        }

        public void setSquare(Square square) {
            this.square = square;
        }
    }
}
