package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(String position, Player player) {
        super(position, player, Type.QUEEN);
    }

    ArrayList<ArrayList<Square>> pms = new ArrayList<>();

    @Override
    public void occupy(Board board) {
        possibleMovementSquares = new ArrayList<>();
        pathAsAThreat = new ArrayList<>();
        pms = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            pms.add(new ArrayList<Square>());
        }
        addHorizontalThreats(board.getBoard(), index, true, 0);
        addHorizontalThreats(board.getBoard(), index, false, 1);
        addVerticalThreats(board.getBoard(), index, true, 2);
        addVerticalThreats(board.getBoard(), index, false, 3);
        addDiagonalThreats(board.getBoard(), index, true, true, 4);
        addDiagonalThreats(board.getBoard(), index, true, false, 5);
        addDiagonalThreats(board.getBoard(), index, false, true, 6);
        addDiagonalThreats(board.getBoard(), index, false, false, 7);

        for (ArrayList<Square> c : pms) {
            if (!c.isEmpty() && !c.get(c.size() - 1).isPieceNull() && c.get(c.size() - 1).getPiece().getType() == Type.KING) {
                pathAsAThreat.addAll(c);
                break;
            }
        }
    }

    public void addHorizontalThreats(Square[][] board, int[] currentIndex, boolean goingUp, int pmsType) {
        int[] nextIndex;
        if (goingUp) {
            nextIndex = new int[]{currentIndex[0] - 1, currentIndex[1]};
        } else {
            nextIndex = new int[]{currentIndex[0] + 1, currentIndex[1]};
        }


        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length && nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck && board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
            addHorizontalThreats(board, nextIndex, goingUp, pmsType);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {


            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
        } else if (indexCheck) {
            board[nextIndex[0]][nextIndex[1]].addPossibleMovement(this);
        }

    }

    public void addVerticalThreats(Square[][] board, int[] currentIndex, boolean goingRight, int pmsType) {
        int[] nextIndex;
        if (goingRight) {
            nextIndex = new int[]{currentIndex[0], currentIndex[1] + 1};
        } else {
            nextIndex = new int[]{currentIndex[0], currentIndex[1] - 1};
        }


        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length && nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck && board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);

        } else if (indexCheck) {
            board[nextIndex[0]][nextIndex[1]].addPossibleMovement(this);
        }
    }

    private void addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType) {
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
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
            addDiagonalThreats(board, nextIndex, goingUp, goingRight, pmsType);

        } else if (indexCheck && board[nextIndex[0]][nextIndex[1]].getPiece().getPlayer() != this.player) {

            possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
            board[nextIndex[0]][nextIndex[1]].addThreat(this);
            pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);

        } else if (indexCheck) {

            board[nextIndex[0]][nextIndex[1]].addPossibleMovement(this);

        }
    }
}
