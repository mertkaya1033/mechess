package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

/**
 * Queen.java
 * <p>
 * Description: The class that represents the type of piece called Queen, which can move diagonally,
 * horizontally and vertically on the board.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Queen extends Piece {
    /**
     * Queen(String position, Player player)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece
     * @param player   the player that posses this piece
     */
    public Queen(String position, Player player) {
        super(position, player, Type.QUEEN);
    }

    /**
     * occupy(Board board)
     * <p>
     * Description: This abstract method allows this piece to find/occupy each square it
     * can legally move on the board.
     *
     * @param board the board
     */
    @Override
    public void occupy(Board board) {
        possibleMovementSquares = new ArrayList<>(); //reset
        pathAsAThreat = new ArrayList<>();//reset
        pms = new ArrayList<>();//reset
        for (int i = 0; i < 8; i++) {
            pms.add(new ArrayList<>());
        }
        addHorizontalThreats(board.getBoard(), currentSquare.getIndex(), true, 0);//right
        addHorizontalThreats(board.getBoard(), currentSquare.getIndex(), false, 1);///left
        addVerticalThreats(board.getBoard(), currentSquare.getIndex(), true, 2);//up
        addVerticalThreats(board.getBoard(), currentSquare.getIndex(), false, 3);//down
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, true, 4);//up-right
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), true, false, 5);//up-left
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, true, 6);//down-right
        addDiagonalThreats(board.getBoard(), currentSquare.getIndex(), false, false, 7);//down-left

        //to check if it threatens the opponent's king
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

    /**
     * addVerticalThreats(Square[][] board, int[] currentIndex, boolean goingUp, int pmsType)
     * <p>
     * Description: adds the threats that are vertical to this piece
     *
     * @param board        the board
     * @param currentIndex the index of the square that it currently is checking
     * @param goingUp      the direction of the check
     * @param pmsType      which pms is the path going to be saved
     */
    private void addVerticalThreats(Square[][] board, int[] currentIndex, boolean goingUp, int pmsType) {
        int[] nextIndex = findNextVerticalIndex(currentIndex, goingUp);//find the next square's index

        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;//check if it is within the board

        if (indexCheck) {


            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                //add the square to the possible movement squares and then move on with the next square
                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addVerticalThreats(board, nextIndex, goingUp, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                //threaten the piece
                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                //check if there are any pins if the piece is not king
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkVerticalPin(board, nextIndex, goingUp));
                } else {
                    //don't allow the king to move to a place where this piece still capture it
                    disallowVerticalKingMovement(board, nextIndex, goingUp);
                }

            } else {
                //protect the piece that is owned by the same player
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    /**
     * checkVerticalPin(Square[][] board, int[] currentIndex, boolean goingUp)
     * <p>
     * Description: checks if this piece pins another piece
     *
     * @param board        the board
     * @param currentIndex the index of the current square that it is currently checking
     * @param goingUp      the direction of the pin
     * @return if it does pin the piece or not
     */
    private boolean checkVerticalPin(Square[][] board, int[] currentIndex, boolean goingUp) {
        int[] nextIndex = findNextVerticalIndex(currentIndex, goingUp);

        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                return checkVerticalPin(board, nextIndex, goingUp);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {
                //if the king is behind this piece, return true
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * disallowVerticalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp)
     * <p>
     * Description: doesn't allow the opponent's king to move to a place where this piece still capture it
     *
     * @param board        the board
     * @param currentIndex the index of the current square that the method is currently on
     * @param goingUp      the direction of the threats
     */
    private void disallowVerticalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp) {

        int[] nextIndex = findNextVerticalIndex(currentIndex, goingUp);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowVerticalKingMovement(board, nextIndex, goingUp);

            }
        }
    }

    /**
     * addHorizontalThreats(Square[][] board, int[] currentIndex, boolean goingRight, int pmsType)
     * <p>
     * Description: adds the threats that are horizontal to this piece
     *
     * @param board        the board
     * @param currentIndex the index of the square that it currently is checking
     * @param goingRight   the direction of the check
     * @param pmsType      which pms is the path going to be saved
     */
    private void addHorizontalThreats(Square[][] board, int[] currentIndex, boolean goingRight, int pmsType) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextHorizontalIndex(currentIndex, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {

            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                addHorizontalThreats(board, nextIndex, goingRight, pmsType);

            } else if (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor) {

                possibleMovementSquares.add(board[nextIndex[0]][nextIndex[1]]);
                board[nextIndex[0]][nextIndex[1]].addPieceThatCanMove(this);
                pms.get(pmsType).add(board[nextIndex[0]][nextIndex[1]]);
                if (board[nextIndex[0]][nextIndex[1]].getPiece().getType() != Type.KING) {
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkHorizontalPin(board, nextIndex, goingRight));
                } else {
                    disallowHorizontalKingMovement(board, nextIndex, goingRight);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    /**
     * checkHorizontalPin(Square[][] board, int[] currentIndex, boolean goingRight)
     * <p>
     * Description: checks if this piece pins another piece
     *
     * @param board        the board
     * @param currentIndex the index of the current square that it is currently checking
     * @param goingRight   the direction of the pin
     * @return if it does pin the piece or not
     */
    private boolean checkHorizontalPin(Square[][] board, int[] currentIndex, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextHorizontalIndex(currentIndex, goingRight);
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

    /**
     * disallowHorizontalKingMovement(Square[][] board, int[] currentIndex, boolean goingRight)
     * <p>
     * Description: doesn't allow the opponent's king to move to a place where this piece still capture it
     *
     * @param board        the board
     * @param currentIndex the index of the current square that the method is currently on
     * @param goingRight   the direction of the threats
     */
    private void disallowHorizontalKingMovement(Square[][] board, int[] currentIndex, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextHorizontalIndex(currentIndex, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull()) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowHorizontalKingMovement(board, nextIndex, goingRight);

            }
        }
    }

    /**
     * addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType)
     * <p>
     * Description: adds the threats that are diagonal to this piece
     *
     * @param board        the board
     * @param currentIndex the index of the square that it currently is checking
     * @param goingRight   the direction of the check
     * @param goingUp      the direction of the check
     * @param pmsType      which pms is the path going to be saved
     */
    private void addDiagonalThreats(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight, int pmsType) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
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
                    board[nextIndex[0]][nextIndex[1]].getPiece().setPinned(checkDiagonalPin(board, nextIndex, goingUp, goingRight));
                } else {
                    disallowDiagonalKingMovement(board, nextIndex, goingUp, goingRight);
                }

            } else {
                board[nextIndex[0]][nextIndex[1]].getPiece().protect();
            }
        }
    }

    /**
     * checkDiagonalPin(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: checks if this piece pins another piece
     *
     * @param board        the board
     * @param currentIndex the index of the current square that it is currently checking
     * @param goingUp      the direction of the pin
     * @param goingRight   the direction of the pin
     * @return if it does pin the piece or not
     */
    private boolean checkDiagonalPin(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
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

    /**
     * disallowDiagonalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: doesn't allow the opponent's king to move to a place where this piece still capture it
     *
     * @param board        the board
     * @param currentIndex the index of the current square that the method is currently on
     * @param goingUp      the direction of the threats
     * @param goingRight   the direction of the threats
     */
    private void disallowDiagonalKingMovement(Square[][] board, int[] currentIndex, boolean goingUp, boolean goingRight) {
        //works with the same logic as with the vertical check
        int[] nextIndex = findNextDiagonalIndex(currentIndex, goingUp, goingRight);
        boolean indexCheck = nextIndex[0] >= 0 && nextIndex[0] < board.length &&
                nextIndex[1] >= 0 && nextIndex[1] < board[nextIndex[0]].length;

        if (indexCheck) {
            if (board[nextIndex[0]][nextIndex[1]].isPieceNull() || (board[nextIndex[0]][nextIndex[1]].getPiece().getPlayerColor() != playerColor && board[nextIndex[0]][nextIndex[1]].getPiece().getType() == Type.KING)) {
                board[nextIndex[0]][nextIndex[1]].disallowKingMovement(playerColor);
                disallowDiagonalKingMovement(board, nextIndex, goingUp, goingRight);

            }
        }
    }

    /**
     * findNextHorizontalIndex(int[] currentIndex, boolean goingRight)
     * <p>
     * Description: finds the index of the next square of a horizontal check, given its direction
     *
     * @param currentIndex the current index
     * @param goingRight   the direction of the check
     * @return the next index
     */
    private static int[] findNextHorizontalIndex(int[] currentIndex, boolean goingRight) {
        if (goingRight) {
            return new int[]{currentIndex[0], currentIndex[1] + 1};
        } else {
            return new int[]{currentIndex[0], currentIndex[1] - 1};
        }
    }

    /**
     * findNextVerticalIndex(int[] currentIndex, boolean goingUp)
     * <p>
     * Description: finds the index of the next square of a vertical check, given its direction
     *
     * @param currentIndex the current index
     * @param goingUp      the direction of the check
     * @return the next index
     */
    private static int[] findNextVerticalIndex(int[] currentIndex, boolean goingUp) {
        if (goingUp) {
            return new int[]{currentIndex[0] - 1, currentIndex[1]};
        } else {
            return new int[]{currentIndex[0] + 1, currentIndex[1]};
        }
    }

    /**
     * findNextDiagonalIndex(int[] currentIndex, boolean goingUp, boolean goingRight)
     * <p>
     * Description: finds the index of the next square of a diagonal check, given its direction
     *
     * @param currentIndex the current index
     * @param goingUp      the direction of the check
     * @return the next index
     */
    private static int[] findNextDiagonalIndex(int[] currentIndex, boolean goingUp, boolean goingRight) {
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
