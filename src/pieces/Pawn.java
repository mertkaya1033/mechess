package pieces;

import main.*;

import java.util.ArrayList;

/**
 * Pawn.java
 * <p>
 * Description: The class that represents the type of piece called Pawn, which can move 1 move ahead, 2 moves if its
 * first move or can capture a piece (not regular moving) that is on the forward edge square of its current square.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Pawn extends Piece {

    /**
     * Pawn(String position, Player player)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece
     * @param player   the player that posses this piece
     */
    public Pawn(String position, Player player) {
        super(position, player, Type.PAWN);
    }

    /**
     * occupy(Board board)
     * <p>
     * Description: This abstract method allows this piece to find/occupy each square it
     * can legally move on the board.
     *
     * @param b the board
     */
    @Override
    public void occupy(Board b) {
        //please look at how other pieces move to understand this one
        boolean indexCheck;

        int[] index = currentSquare.getIndex();
        possibleMovementSquares = new ArrayList<>();//reset
        Square[][] board = b.getBoard();

        if (playerColor == Side.white) {
            //movement
            //one move ahead check
            indexCheck = index[0] - 1 >= 0;
            if (indexCheck && board[index[0] - 1][index[1]].isPieceNull()) {
                possibleMovementSquares.add(board[index[0] - 1][index[1]]);
                //two move ahead check
                indexCheck = index[0] - 2 >= 0;
                if (firstMove && indexCheck && board[index[0] - 2][index[1]].isPieceNull()) {
                    possibleMovementSquares.add(board[index[0] - 2][index[1]]);
                }
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] - 1 >= 0;

            if (indexCheck) {
                if (board[index[0] - 1][index[1] - 1].isPieceNull()) {
                    board[index[0] - 1][index[1] - 1].disallowKingMovement(playerColor);
                } else if (board[index[0] - 1][index[1] - 1].getPiece().getPlayerColor() == playerColor) {
                    board[index[0] - 1][index[1] - 1].getPiece().protect();
                } else {
                    possibleMovementSquares.add(board[index[0] - 1][index[1] - 1]);
                    board[index[0] - 1][index[1] - 1].addPieceThatCanMove(this);
                }
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] + 1 <= board.length - 1;
            if (indexCheck) {
                if (board[index[0] - 1][index[1] + 1].isPieceNull()) {
                    board[index[0] - 1][index[1] + 1].disallowKingMovement(playerColor);
                } else if (board[index[0] - 1][index[1] + 1].getPiece().getPlayerColor() == playerColor) {
                    board[index[0] - 1][index[1] + 1].getPiece().protect();
                } else {
                    possibleMovementSquares.add(board[index[0] - 1][index[1] + 1]);
                    board[index[0] - 1][index[1] + 1].addPieceThatCanMove(this);
                }
            }

        } else {
            //movement
            //one move ahead check
            indexCheck = index[0] + 1 <= board.length - 1;
            if (indexCheck && board[index[0] + 1][index[1]].isPieceNull()) {

                possibleMovementSquares.add(board[index[0] + 1][index[1]]);
                //two move ahead check
                indexCheck = index[0] + 2 <= board.length - 1;
                if (firstMove && indexCheck && board[index[0] + 2][index[1]].isPieceNull()) {
                    possibleMovementSquares.add(board[index[0] + 2][index[1]]);
                }
            }

            //capture
            indexCheck = index[0] + 1 <= board.length - 1 && index[1] - 1 >= 0;
            if (indexCheck) {
                if (board[index[0] + 1][index[1] - 1].isPieceNull()) {
                    board[index[0] + 1][index[1] - 1].disallowKingMovement(playerColor);
                } else if (board[index[0] + 1][index[1] - 1].getPiece().getPlayerColor() == playerColor) {
                    board[index[0] + 1][index[1] - 1].getPiece().protect();
                } else {
                    possibleMovementSquares.add(board[index[0] + 1][index[1] - 1]);
                    board[index[0] + 1][index[1] - 1].addPieceThatCanMove(this);
                }
            }

            //capture
            indexCheck = index[0] + 1 <= board.length - 1 && index[1] + 1 <= board.length - 1;

            if (indexCheck) {
                if (board[index[0] + 1][index[1] + 1].isPieceNull()) {
                    board[index[0] + 1][index[1] + 1].disallowKingMovement(playerColor);
                } else if (board[index[0] + 1][index[1] + 1].getPiece().getPlayerColor() == playerColor) {
                    board[index[0] + 1][index[1] + 1].getPiece().protect();
                } else {
                    possibleMovementSquares.add(board[index[0] + 1][index[1] + 1]);
                    board[index[0] + 1][index[1] + 1].addPieceThatCanMove(this);
                }
            }
        }

        if (isPinned) {
            setPinned(true);
        }
    }

    /**
     * move(Square pos)
     * <p>
     * Description: moves the piece onto given square
     *
     * @param pos the square that this piece is going to be moved
     */
    @Override
    public void move(Square pos) {
        super.move(pos);
        if (this.currentSquare == pos)
            this.firstMove = false;
    }

}
