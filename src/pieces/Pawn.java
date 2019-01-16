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
        boolean isSameColor;
        boolean isCheck = false;
        possibleMovementSquares = new ArrayList<>();
        Square[][] board = b.getBoard();

        if (this.getColor() == CPlayer.white) {
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
            if (indexCheck && !board[index[0] - 1][index[1] - 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] - 1].getPiece().getColor() == this.player.getColor();
            } else {
                isSameColor = true;
            }

            if (!isSameColor) {
                if (!isCheck && !board[index[0] - 1][index[1] - 1].isPieceNull() && board[index[0] - 1][index[1] - 1].getPiece().getType() == Type.KING) {
                    possibleMovementSquares = new ArrayList<>();
                    isCheck = true;
                }
                possibleMovementSquares.add(board[index[0] - 1][index[1] - 1]);
                board[index[0] - 1][index[1] - 1].addThreat(this);
            } else if (indexCheck) {
                board[index[0] - 1][index[1] - 1].addPossibleMovement(this);
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] + 1 <= board.length - 1;
            if (indexCheck && !board[index[0] - 1][index[1] + 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] + 1].getPiece().getColor() == this.player.getColor();
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                if (!isCheck && !board[index[0] - 1][index[1] + 1].isPieceNull() && board[index[0] - 1][index[1] + 1].getPiece().getType() == Type.KING) {
                    possibleMovementSquares = new ArrayList<>();
                    isCheck = true;
                }
                possibleMovementSquares.add(board[index[0] - 1][index[1] + 1]);
                board[index[0] - 1][index[1] + 1].addThreat(this);
            } else if (indexCheck) {
//                board[index[0] - 1][index[1] + 1].addThreat(this);
                board[index[0] - 1][index[1] + 1].addPossibleMovement(this);
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
            if (indexCheck && !board[index[0] + 1][index[1] - 1].isPieceNull()) {
                isSameColor = board[index[0] + 1][index[1] - 1].getPiece().getColor() == this.player.getColor();
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                if (!isCheck && !board[index[0] + 1][index[1] - 1].isPieceNull() && board[index[0] + 1][index[1] - 1].getPiece().getType() == Type.KING) {
                    possibleMovementSquares = new ArrayList<>();
                    isCheck = true;
                }
                possibleMovementSquares.add(board[index[0] + 1][index[1] - 1]);
                board[index[0] + 1][index[1] - 1].addThreat(this);
            } else if (indexCheck) {
//                board[index[0] + 1][index[1] - 1].addThreat(this);
                board[index[0] + 1][index[1] - 1].addPossibleMovement(this);
            }

            //capture
            indexCheck = index[0] + 1 <= board.length - 1 && index[1] + 1 <= board.length - 1;
            if (indexCheck && !board[index[0] + 1][index[1] + 1].isPieceNull()) {
                isSameColor = board[index[0] + 1][index[1] + 1].getPiece().getColor() == this.player.getColor();
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                if (!isCheck && !board[index[0] + 1][index[1] + 1].isPieceNull() && board[index[0] + 1][index[1] + 1].getPiece().getType() == Type.KING) {
                    possibleMovementSquares = new ArrayList<>();
                    isCheck = true;
                }
                possibleMovementSquares.add(board[index[0] + 1][index[1] + 1]);
                board[index[0] + 1][index[1] + 1].addThreat(this);
            } else if (indexCheck) {
//                board[index[0] + 1][index[1] + 1].addThreat(this);
                board[index[0] + 1][index[1] + 1].addPossibleMovement(this);
            }

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
