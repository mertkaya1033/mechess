package pieces;

import main.*;

import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean firstMove;

    public Pawn(String position, Player player) {
        super(position, player, Type.PAWN);
        this.firstMove = true;
    }

    @Override
    public void occupy(Board b) {
        boolean indexCheck;
        boolean isSameColor;

        threads = new ArrayList<>();
        Square[][] board = b.getBoard();

        if (this.getColor() == CPlayer.white) {
            //movement
            //one move ahead check
            indexCheck = index[0] - 1 >= 0;
            if (indexCheck && board[index[0] - 1][index[1]].isPieceNull()) {
                threads.add(board[index[0] - 1][index[1]]);
                board[index[0] - 1][index[1]].addThreat(this);
                //two move ahead check
                indexCheck = index[0] - 2 >= 0;
                if (firstMove && indexCheck && board[index[0] - 2][index[1]].isPieceNull()) {
                    threads.add(board[index[0] - 2][index[1]]);
                    board[index[0] - 2][index[1]].addThreat(this);
                }
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] - 1 >= 0;
            if (indexCheck && !board[index[0] - 1][index[1] - 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] - 1].getPiece().getPlayer() == this.player;
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                threads.add(board[index[0] - 1][index[1] - 1]);
                board[index[0] - 1][index[1] - 1].addThreat(this);
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] + 1 <= board.length - 1;
            if (indexCheck && !board[index[0] - 1][index[1] + 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] + 1].getPiece().getPlayer() == this.player;
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                threads.add(board[index[0] - 1][index[1] + 1]);
                board[index[0] - 1][index[1] + 1].addThreat(this);
            }

        } else {
            //movement
            //one move ahead check
            indexCheck = index[0] + 1 <= board.length - 1;
            if (indexCheck && board[index[0] + 1][index[1]].isPieceNull()) {

                threads.add(board[index[0] + 1][index[1]]);
                board[index[0] + 1][index[1]].addThreat(this);
                //two move ahead check
                indexCheck = index[0] + 2 <= board.length - 1;
                if (firstMove && indexCheck && board[index[0] + 2][index[1]].isPieceNull()) {
                    threads.add(board[index[0] + 2][index[1]]);
                    board[index[0] + 2][index[1]].addThreat(this);
                }
            }

            //capture
            indexCheck = index[0] + 1 <= board.length - 1 && index[1] - 1 >= 0;
            if (indexCheck && !board[index[0] + 1][index[1] - 1].isPieceNull()) {
                isSameColor = board[index[0] + 1][index[1] - 1].getPiece().getPlayer() == this.player;
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                threads.add(board[index[0] + 1][index[1] - 1]);
                board[index[0] + 1][index[1] - 1].addThreat(this);
            }

            //capture
            indexCheck = index[0] + 1 <= board.length - 1 && index[1] + 1 <= board.length - 1;
            if (indexCheck && !board[index[0] + 1][index[1] + 1].isPieceNull()) {
                isSameColor = board[index[0] + 1][index[1] + 1].getPiece().getPlayer() == this.player;
            } else {
                isSameColor = true;
            }
            if (!isSameColor) {
                threads.add(board[index[0] + 1][index[1] + 1]);
                board[index[0] + 1][index[1] + 1].addThreat(this);
            }

        }
    }
    @Override
    public void move(Square pos){
        super.move(pos);
        if(this.currentSquare == pos)
            this.firstMove = false;
    }

}
