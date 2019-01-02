package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(String position, Player player) {
        super(position, player, Type.ROOK);
    }

    @Override
    public void occupy(Board board) {
        threads = new ArrayList<>();
        addHorizontalThreats(board.getBoard(), index, true);
        addHorizontalThreats(board.getBoard(), index, false);
        addVerticalThreats(board.getBoard(), index, true);
        addVerticalThreats(board.getBoard(), index, false);
    }

    public void addHorizontalThreats(Square[][] board, int[] currentIndex, boolean goingUp) {
        int[] nextIndex;
        if (goingUp) {
            nextIndex = new int[]{currentIndex[0] - 1, currentIndex[1]};
        } else {
            nextIndex = new int[]{currentIndex[0] + 1, currentIndex[1]};
        }


        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length && nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck && board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

            threads.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            addHorizontalThreats(board, nextIndex, goingUp);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {

            threads.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);

        }

    }

    public void addVerticalThreats(Square[][] board, int[] currentIndex, boolean goingRight) {
        int[] nextIndex;
        if (goingRight) {
            nextIndex = new int[]{currentIndex[0], currentIndex[1] + 1};
        } else {
            nextIndex = new int[]{currentIndex[0], currentIndex[1] - 1};
        }


        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length && nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck && board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

            threads.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            addVerticalThreats(board, nextIndex, goingRight);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {

            threads.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
        }
    }
}
