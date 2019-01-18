package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

/**
 * Knight.java
 * <p>
 * Description: The class that represents the type of piece called Knight, which can move like an L shape, 1 square in one
 * direction and 2 squares perpendicular to that move.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Knight extends Piece {

    /**
     * Knight(String position, Player player)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece
     * @param player   the player that posses this piece
     */
    public Knight(String position, Player player) {
        super(position, player, Type.KNIGHT);
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
        possibleMovementSquares = new ArrayList<>();
        addThreats(board.getBoard(), new int[]{1, -1, 2, -2});//1 and 2
        if (isPinned) {
            setPinned(true);
        }
    }

    /**
     * addThreats(Square[][] board, int[] range)
     * <p>
     * Description: adds the possible movements.
     *
     * @param board the board
     * @param range the direction of the path
     */
    public void addThreats(Square[][] board, int[] range) {
        boolean indexCheck;
        boolean possibleThreat;
        int[] index = currentSquare.getIndex();
        //use permutation to find possible movement squares
        for (int i : range) {
            for (int j : range) {
                if (Math.abs(i) != Math.abs(j)) {

                    indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                            index[1] + j >= 0 && index[1] + j < board[index[0]].length;
                    if (indexCheck) {
                        possibleThreat = !board[index[0] + i][index[1] + j].isPieceNull() &&
                                board[index[0] + i][index[1] + j].getPiece().getPlayerColor() != playerColor;

                        if (board[index[0] + i][index[1] + j].isPieceNull() || possibleThreat) {
                            //if there is not a piece on the square or it can capture that square, add it to the possible movements
                            possibleMovementSquares.add(board[index[0] + i][index[1] + j]);
                            board[index[0] + i][index[1] + j].addPieceThatCanMove(this);
                        } else {
                            board[index[0] + i][index[1] + j].getPiece().protect();
                        }
                    }
                }
            }
        }
    }
}
