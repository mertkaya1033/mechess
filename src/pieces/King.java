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
        boolean possibleThreat;
        boolean isSquareUnderThreat;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                        index[1] + j >= 0 && index[1] + j < board[index[0]].length &&
                        !(i == 0 && j == 0);

                if (indexCheck) {
                    possibleThreat = !board[index[0] + i][index[1] + j].isPieceNull() &&
                            board[index[0] + i][index[1] + j].getPiece().getPlayer() != this.player &&
                            board[index[0] + i][index[1] + j].isPossibleMovement(this.player);

                    isSquareUnderThreat = board[index[0] + i][index[1] + j].isPieceNull() &&
                            (board[index[0] + i][index[1] + j].isUnderThreat(this.player).isEmpty() &&
                                    board[index[0] + i][index[1] + j].isPossibleMovement(this.player));

                    if (isSquareUnderThreat || possibleThreat) {
                        possibleMovementSquares.add(board[index[0] + i][index[1] + j]);
                        board[index[0] + i][index[1] + j].addThreat(this);
                    } else {
                        board[index[0] + i][index[1] + j].addPossibleMovement(this);
                    }
                }
            }
        }

    }

}
