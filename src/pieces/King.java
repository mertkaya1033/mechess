package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

public class King extends Piece {


    public King(String position, Player player) {
        super(position, player, Type.KING);
        queenSideRook = (playerColor == Side.white) ? "a1" : "a8";
        kingSideRook = (playerColor == Side.white) ? "h1" : "h8";
    }


    @Override
    public void occupy(Board b) {
        possibleMovementSquares = new ArrayList<>();
        Square[][] board = b.getBoard();
        boolean indexCheck;
        boolean isCapturable;
        int[] index = currentSquare.getIndex();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                        index[1] + j >= 0 && index[1] + j < board[index[0]].length &&
                        !(i == 0 && j == 0);

                if (indexCheck) {
                    isCapturable = (!board[index[0] + i][index[1] + j].isPieceNull() && board[index[0] + i][index[1] + j].getPiece().getPlayerColor() != playerColor
                    );
                    if (board[index[0] + i][index[1] + j].canKingMove(playerColor) && (board[index[0] + i][index[1] + j].isPieceNull() || isCapturable)) {
                        possibleMovementSquares.add(board[index[0] + i][index[1] + j]);
                        board[index[0] + i][index[1] + j].addPieceThatCanMove(this);
                    } else {
                        board[index[0] + i][index[1] + j].disallowKingMovement(playerColor);
                    }
                }
            }
        }
        isPossibleToCastle();
        checkMovements();
    }

    public void checkMovements() {
        for (int i = 0; i < possibleMovementSquares.size(); i++) {
            Square square = possibleMovementSquares.get(i);
            if (!square.canKingMove(playerColor)) {
                possibleMovementSquares.remove(square);
                square.getPiecesThatCanMove(playerColor).remove(this);
            }
        }
    }

    public void isPossibleToCastle() {
        Board board = player.getBoard();


        boolean kingSideRookCheck = !board.findSquare(kingSideRook).isPieceNull() && board.findSquare(kingSideRook).getPiece().getType() == Type.ROOK && board.findSquare(kingSideRook).getPiece().hasntMoved();
        boolean queenSideRookCheck = !board.findSquare(queenSideRook).isPieceNull() && board.findSquare(queenSideRook).getPiece().getType() == Type.ROOK && board.findSquare(queenSideRook).getPiece().hasntMoved();
        if (firstMove) {
            boolean canMove = true;
            if (kingSideRookCheck) {
                Square current = board.findSquare((char) ((int) position.charAt(0) + 1) + ("" + position.charAt(1)));
                while (current.getPosition().charAt(0) != 'h') {
                    if (!(current.canKingMove(playerColor) && current.isPieceNull())) {
                        canMove = false;
                        break;
                    }
                    String nextPos = (char) ((int) current.getPosition().charAt(0) + 1) + (current.getPosition().charAt(1) + "");
                    current = board.findSquare(nextPos);
                }
                if (!current.canKingMove(playerColor)){
                    canMove = false;
                }
                if (canMove) {
                    kingSideCastleKingPos = board.findSquare((char) ((int) position.charAt(0) + 2) + ("" + position.charAt(1)));
                    kingSideCastlePosition = board.findSquare((char) ((int) position.charAt(0) + 1) + ("" + position.charAt(1)));
                }
            }
            canMove = true;
            if (queenSideRookCheck) {
                Square current = board.findSquare((char) ((int) position.charAt(0) - 1) + ("" + position.charAt(1)));
                while (current.getPosition().charAt(0) != 'a') {
                    if (!(current.canKingMove(playerColor) && current.isPieceNull())) {
                        canMove = false;
                        break;
                    }
                    String nextPos = (char) ((int) current.getPosition().charAt(0) - 1) + (current.getPosition().charAt(1) + "");
                    current = board.findSquare(nextPos);
                }
                if (!current.canKingMove(playerColor)){
                    canMove = false;
                }
                if (canMove) {
                    queenSideCastlePosition = board.findSquare((char) ((int) position.charAt(0) - 1) + ("" + position.charAt(1)));
                    queenSideCastleKingPos = board.findSquare((char) ((int) position.charAt(0) - 2) + ("" + position.charAt(1)));
                }
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        queenSideCastlePosition = null;
        kingSideCastlePosition = null;
        queenSideCastleKingPos = null;
        kingSideCastleKingPos = null;
    }

    @Override
    public void move(Square pos) {
        super.move(pos);
        if (firstMove) {
            if (pos == queenSideCastleKingPos||pos == kingSideCastleKingPos) {
                position = pos.getPosition();
                currentSquare.setPiece(null);
                currentSquare = pos;
                pos.setPiece(this);
                firstMove = false;
                if(pos == queenSideCastleKingPos)
                    player.getBoard().findSquare(queenSideRook).getPiece().move(queenSideCastlePosition);
                else
                    player.getBoard().findSquare(kingSideRook).getPiece().move(kingSideCastlePosition);
            }
        }
    }
}
