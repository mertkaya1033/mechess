package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

/**
 * Bishop.java
 * <p>
 * Description: The class that represents the type of piece called Bishop, which can move diagonally on the board.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Bishop extends Piece {
    /**
     * Bishop(String position, Player player)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece
     * @param player   the player that posses this piece
     */
    public Bishop(String position, Player player) {
        super(position, player, Type.BISHOP);
    }

    /**
     * occupy(Board board)
     * <p>
     * Description: This abstract method allows this piece to find/occupy each square it
     * can legally move on the board.
     *
     * @param board the board
     */
    @Override
    public void occupy(Board board) {
        possibleMovementSquares = new ArrayList<>();//reset
        pathAsAThreat = new ArrayList<>();//reset
        pms = new ArrayList<>();//reset
        for (int i = 0; i < 4; i++) {
            pms.add(new ArrayList<>());
        }
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, true, 0);//up-right
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, false, 1);//up-left
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, true, 2);//down-right
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, false, 3);//down-left

        //to check if it threatens the opponent's king
        for (ArrayList<Square> c : pms) {
            if (!c.isEmpty() && !c.get(c.size() - 1).isPieceNull() && c.get(c.size() - 1).getPiece().getType() == Type.KING) {
                pathAsAThreat.addAll(c);
                break;
            }
        }
        if (isPinned) {
            setPinned(true);
        }
    }

    /**
     * addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType)
     * <p>
     * Description: adds the threats that are diagonal to this piece
     *
     * @param board        the board
     * @param currentIndex the index of the square that it currently is checking
     * @param goingRight   the direction of the check
     * @param goingUp      the direction of the check
     * @param pmsType      which pms is the path going to be saved
     */
    private void addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addDiagonalThreats(board, nextIndex, goingUp, goingRight, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkDiagonalPin(board, nextIndex, goingUp, goingRight));
                } else {
                    disallowDiagonalKingMovement(board, nextIndex, goingUp, goingRight);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    /**
     * checkDiagonalPin(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: checks if this piece pins another piece
     *
     * @param board        the board
     * @param currentIndex the index of the current square that it is currently checking
     * @param goingUp      the direction of the pin
     * @param goingRight   the direction of the pin
     * @return if it does pin the piece or not
     */
    private boolean checkDiagonalPin(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                return checkDiagonalPin(board, nextIndex, goingUp, goingRight);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * disallowDiagonalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: doesn't allow the opponent's king to move to a place where this piece still capture it
     *
     * @param board        the board
     * @param currentIndex the index of the current square that the method is currently on
     * @param goingUp      the direction of the threats
     * @param goingRight   the direction of the threats
     */
    private void disallowDiagonalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull() || (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor && board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING)) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowDiagonalKingMovement(board, nextIndex, goingUp, goingRight);

            }
        }
    }

    /**
     * findNextDiagonalIndex(int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: finds the index of the next square of a diagonal check, given its direction
     *
     * @param currentIndex the current index
     * @param goingUp      the direction of the check
     * @return the next index
     */
    private static int[] findNextDiagonalIndex(int[] currentIndex, boolean goingUp, boolean goingRight) {
        if (goingRight && goingUp) {
            return new int[]{currentIndex[0] - 1, currentIndex[1] + 1};//top right direction
        } else if (goingRight) {
            return new int[]{currentIndex[0] + 1, currentIndex[1] + 1};//bottom right direction
        } else if (goingUp) {
            return new int[]{currentIndex[0] - 1, currentIndex[1] - 1};//top left direction
        } else {
            return new int[]{currentIndex[0] + 1, currentIndex[1] - 1};//bottom left direction
        }
    }
}
