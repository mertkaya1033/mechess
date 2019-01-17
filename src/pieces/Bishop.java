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
        pathAsAThreat = new ArrayList<>();
        pms = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pms.add(new ArrayList<>());
        }
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, true, 0);
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, false, 1);
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, true, 2);
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, false, 3);

        for (ArrayList<Square> c : pms) {
            if (!c.isEmpty() && !c.get(c.size() - 1).isPieceNull() && c.get(c.size() - 1).getPiece().getType() == Type.KING) {
                pathAsAThreat.addAll(c);
                break;
            }
        }
        if (isPinned) {
            setPinned(true);
        }
    }

    private void addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType) {
        int[] nextIndex = findNextDiagonalIndex(board, currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addDiagonalThreats(board, nextIndex, goingUp, goingRight, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkDiagonalPin(board, currentIndex, goingUp, goingRight));
                }else{
                    disallowDiagonalKingMovement(board, currentIndex, goingUp, goingRight);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    private boolean checkDiagonalPin(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        int[] nextIndex = findNextDiagonalIndex(board, currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                return checkDiagonalPin(board, nextIndex, goingUp, goingRight);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    private void disallowDiagonalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight){

        int[] nextIndex = findNextDiagonalIndex(board, currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowDiagonalKingMovement(board, currentIndex, goingUp, goingRight);

            }
        }
    }

    private static int[] findNextDiagonalIndex(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight){
        if (goingRight && goingUp) {
           return new int[]{currentIndex[0] - 1, currentIndex[1] + 1};//top right direction
        } else if (goingRight) {
            return new int[]{currentIndex[0] + 1, currentIndex[1] + 1};//bottom right direction
        } else if (goingUp) {
            return new int[]{currentIndex[0] - 1, currentIndex[1] - 1};//top left direction
        } else {
            return new int[]{currentIndex[0] + 1, currentIndex[1] - 1};//bottom left direction
        }
    }
}
