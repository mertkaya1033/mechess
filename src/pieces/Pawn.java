package pieces;

import main.*;

import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean firstMove;

    /**
     * @param position
     * @param player
     */
    public Pawn(String position, Player player) {
        super(position, player, Type.PAWN);
        this.firstMove = true;
    }

    /**
     * @param b
     */
    @Override
    public void occupy(Board b) {
        boolean indexCheck;

        int[] index = currentSquare.getIndex();
        possibleMovementSquares = new ArrayList<>();
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
                } else{
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
                } else{
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
                } else{
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
                } else{
                    possibleMovementSquares.add(board[index[0] + 1][index[1] + 1]);
                    board[index[0] + 1][index[1] + 1].addPieceThatCanMove(this);
                }
            }
        }

        if(isPinned){
            setPinned(true);
        }
    }

    /**
     * @param pos
     */
    @Override
    public void move(Square pos) {
        super.move(pos);
        if (this.currentSquare == pos)
            this.firstMove = false;
    }

}
