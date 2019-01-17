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
        possibleMovementSquares = new ArrayList<>();
        pathAsAThreat = new ArrayList<>();
        pms = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pms.add(new ArrayList<>());
        }

        addHorizontalThreats(board.getBoard(), currentSquare.getIndex(), true, 0);
        addHorizontalThreats(board.getBoard(), currentSquare.getIndex(), false, 1);
        addVerticalThreats(board.getBoard(), currentSquare.getIndex(), true, 2);
        addVerticalThreats(board.getBoard(), currentSquare.getIndex(), false, 3);

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

    private void addVerticalThreats(Square[][] board, int[] currentIndex, boolean goingUp, int pmsType) {
        int[] nextIndex = findNextVerticalIndex(board, currentIndex, goingUp);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addVerticalThreats(board, nextIndex, goingUp, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkVerticalPin(board, currentIndex, goingUp));
                }else{
                    disallowVerticalKingMovement(board, currentIndex, goingUp);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    private boolean checkVerticalPin(Square[][] board, int[] currentIndex, boolean goingUp) {
        int[] nextIndex = findNextVerticalIndex(board, currentIndex, goingUp);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                return checkVerticalPin(board, nextIndex, goingUp);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    private void disallowVerticalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp){

        int[] nextIndex = findNextVerticalIndex(board, currentIndex, goingUp);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowVerticalKingMovement(board, currentIndex, goingUp);

            }
        }
    }

    private void addHorizontalThreats(Square[][] board, int[] currentIndex, boolean goingRight, int pmsType) {
        int[] nextIndex = findNextHorizontalIndex(board, currentIndex, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addHorizontalThreats(board, nextIndex,  goingRight, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkHorizontalPin(board, currentIndex, goingRight));
                }else{
                    disallowHorizontalKingMovement(board, currentIndex, goingRight);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    private boolean checkHorizontalPin(Square[][] board, int[] currentIndex, boolean goingRight) {
        int[] nextIndex = findNextHorizontalIndex(board, currentIndex, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                return checkHorizontalPin(board, nextIndex, goingRight);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    private void disallowHorizontalKingMovement(Square[][] board, int[] currentIndex, boolean goingRight){

        int[] nextIndex = findNextHorizontalIndex(board, currentIndex, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowHorizontalKingMovement(board, currentIndex, goingRight);

            }
        }
    }
    private static int[] findNextHorizontalIndex(Square[][] board, int[] currentIndex, boolean goingRight){
        if (goingRight) {
            return new int[]{currentIndex[0], currentIndex[1] + 1};
        } else {
            return new int[]{currentIndex[0], currentIndex[1] - 1};
        }
    }
    private static int[] findNextVerticalIndex(Square[][] board, int[] currentIndex, boolean goingUp){
        if (goingUp) {
            return new int[]{currentIndex[0] - 1, currentIndex[1]};
        } else {
            return new int[]{currentIndex[0] + 1, currentIndex[1]};
        }
    }
}
