package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(String position, Player player) {
        super(position, player, Type.KNIGHT);
    }

    @Override
    public void occupy(Board board) {
        possibleMovementSquares = new ArrayList<>();
        addThreats(board.getBoard(), new int[]{1, -1, 2, -2});
        if(isPinned){
            setPinned(true);
        }
    }

    public void addThreats(Square[][] board, int[] range) {
        boolean indexCheck;
        boolean possibleThreat;
        int[]index = currentSquare.getIndex();
        for (int i : range) {
            for (int j : range) {
                if (Math.abs(i) != Math.abs(j)) {

                    indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                            index[1] + j >= 0 && index[1] + j < board[index[0]].length;
                    if(indexCheck){
                        possibleThreat = !board[index[0] + i][index[1] + j].isPieceNull() &&
                                board[index[0] + i][index[1] + j].getPiece().getPlayerColor() != playerColor;

                        if (board[index[0] + i][index[1] + j].isPieceNull() || possibleThreat) {
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
