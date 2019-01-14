package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(String position, Player player) {
        super(position, player, Type.BISHOP);
    }

    @Override
    public void occupy(Board board) {
        possibleMovementSquares = new ArrayList<>();
        addDiagonalThreats(board.getBoard(), index, true, true);
        addDiagonalThreats(board.getBoard(), index, true, false);
        addDiagonalThreats(board.getBoard(), index, false, true);
        addDiagonalThreats(board.getBoard(), index, false, false);
    }

    private void addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        int[] nextIndex;
        if (goingRight && goingUp) {
            nextIndex = new int[]{currentIndex[0] - 1, currentIndex[1] + 1};
        } else if (goingRight) {
            nextIndex = new int[]{currentIndex[0] + 1, currentIndex[1] + 1};
        } else if (goingUp) {
            nextIndex = new int[]{currentIndex[0] - 1, currentIndex[1] - 1};
        } else {
            nextIndex = new int[]{currentIndex[0] + 1, currentIndex[1] - 1};
        }


        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length && nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck && board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            addDiagonalThreats(board, nextIndex, goingUp, goingRight);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);

        } else if (indexCheck) {

            board[nextIndex[0]][nextIndex[1]].addPossibleMovement(this);

        }


    }

}
