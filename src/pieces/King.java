package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class King extends Piece {
    public King(String position, Player player) {
        super(position, player, Type.KING);
    }

    @Override
    public void occupy(Board b) {
        possibleMovementSquares = new ArrayList<>();
        Square[][] board = b.getBoard();
        boolean indexCheck;
        int[] index = currentSquare.getIndex();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                        index[1] + j >= 0 && index[1] + j < board[index[0]].length &&
                        !(i == 0 && j == 0);

                if (indexCheck) {

                    if (board[index[0] + i][index[1] + j].canKingMove(playerColor) &&
                            (board[index[0] + i][index[1] + j].isPieceNull() ||
                                    (!board[index[0] + i][index[1] + j].isPieceNull() &&
                                            board[index[0] + i][index[1] + j].getPiece().getPlayerColor() != playerColor
                                    )
                            )
                    )
                    {
                        possibleMovementSquares.add(board[index[0] + i][index[1] + j]);
                        board[index[0] + i][index[1] + j].addPieceThatCanMove(this);
                    } else{
                        board[index[0] + i][index[1] + j].disallowKingMovement(playerColor);
                    }
                }
            }
        }
    }

    public void checkMovements() {
        for (int i = 0; i < possibleMovementSquares.size(); i++) {
            Square square = possibleMovementSquares.get(i);
            if (!square.canKingMove(playerColor)) {
                possibleMovementSquares.remove(square);
            }
        }
    }

}
