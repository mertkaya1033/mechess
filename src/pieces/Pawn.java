package pieces;

import main.*;

import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean firstMove;

    public Pawn(String position, CPlayer color) {
        super(position, color, Type.PAWN);
        this.firstMove = true;
    }

    @Override
    public void occupy(Board b) {
        boolean indexCheck;
        boolean isSameColor;

        threads = new ArrayList<>();
        Square[][] board = b.getBoard();

        if (this.color == CPlayer.white) {
            //movement
            //one move ahead check
            indexCheck = index[0] - 1 >= 0;
            if (indexCheck && board[index[0] - 1][index[1]].isPieceNull()) {

                threads.add(board[index[0] - 1][index[1]]);
                board[index[0] - 1][index[1]].addThreat(this);
                //two move ahead check
                indexCheck = index[0] - 2 >= 0;
                System.out.println(indexCheck);
                if (firstMove && indexCheck && board[index[0] - 2][index[1]].isPieceNull()) {
                    threads.add(board[index[0] - 2][index[1]]);
                    board[index[0] - 2][index[1]].addThreat(this);
                }
            }

            //capture
            indexCheck = index[0] - 1 >= 0 && index[1] - 1 >= 0;
            if(indexCheck && !board[index[0] - 1][index[1] - 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] - 1].getPiece().getColor() == this.color;
            }else{
                isSameColor = true;
            }
            if (!isSameColor ) {
                threads.add(board[index[0] - 1][index[1] - 1]);
                board[index[0] - 1][index[1] - 1].addThreat(this);
            }

            indexCheck = index[0] - 1 >= 0 && index[1] + 1 <= board.length - 1;
            if(indexCheck && !board[index[0] - 1][index[1] + 1].isPieceNull()) {
                isSameColor = board[index[0] - 1][index[1] + 1].getPiece().getColor() == this.color;
            }else{
                isSameColor = true;
            }
            if (!isSameColor) {
                threads.add(board[index[0] - 1][index[1] + 1]);
                board[index[0] - 1][index[1] + 1].addThreat(this);
            }

        } else {
//            if (board.getBoard()[index[0]][index[1] + 1].isPieceNull()) {
//                //moving one square forward
//                //add index out of bounds check
//                threads.add(board.getBoard()[index[0]][index[1] + 1]);
//                board.getBoard()[index[0]][index[1] + 1].addThreat(this);
//
//                //moving two squares forward
//                //add index out of bounds check
//                if (firstMove && board.getBoard()[index[0]][index[1] + 2].isPieceNull()) {
//                    threads.add(board.getBoard()[index[0]][index[1] + 2]);
//                    board.getBoard()[index[0]][index[1] + 2].addThreat(this);
//                }
//            }
//            //moving capturing a piece
//            //add index out of bounds check
//            if (!board.getBoard()[index[0] + 1][index[1] + 1].isPieceNull()) {
//                threads.add(board.getBoard()[index[0] + 1][index[1] + 1]);
//                board.getBoard()[index[0] + 1][index[1] - 1].addThreat(this);
//            }
//
//            //moving capturing a piece
//            //add index out of bounds check
//            if (!board.getBoard()[index[0] - 1][index[1] + 1].isPieceNull()) {
//                threads.add(board.getBoard()[index[0] - 1][index[1] + 1]);
//                board.getBoard()[index[0] - 1][index[1] - 1].addThreat(this);
//            }

        }
    }

}
