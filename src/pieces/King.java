package pieces;

import main.Board;
import main.Piece;
import main.Player;
import main.Square;

import java.util.ArrayList;

/**
 * King.java
 * <p>
 * Description: The class that represents the type of piece called King, which is the most important piece in this game.
 * It can move every direction, but only 1 square away, and it cannot move to the squares which other pieces of the opponent
 * player threaten. It can also do the type of movement called castling. Castling is, when both the rook and the king has never
 * moved throughout the game, when there are no pieces between those two pieces and when there is no opponent piece blocking the gap,
 * moving king 2 squares towards the rook and moving the rook to the other side of the king.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class King extends Piece {

    /**
     * King(String position, Player player)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece
     * @param player   the player that posses this piece
     */
    public King(String position, Player player) {
        super(position, player, Type.KING);
        queenSideRook = (playerColor == Side.white) ? "a1" : "a8";
        kingSideRook = (playerColor == Side.white) ? "h1" : "h8";
    }

    /**
     * occupy(Board board)
     * <p>
     * Description: This abstract method allows this piece to find/occupy each square it
     * can legally move on the board.
     *
     * @param b the board
     */
    @Override
    public void occupy(Board b) {
        possibleMovementSquares = new ArrayList<>();
        Square[][] board = b.getBoard();
        boolean indexCheck;
        boolean isCapturable;
        int[] index = currentSquare.getIndex();
        //loop through the squares that are around the piece
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                indexCheck = index[0] + i >= 0 && index[0] + i < board.length &&
                        index[1] + j >= 0 && index[1] + j < board[index[0]].length &&
                        !(i == 0 && j == 0);

                if (indexCheck) {
                    isCapturable = (!board[index[0] + i][index[1] + j].isPieceNull() &&
                            board[index[0] + i][index[1] + j].getPiece().getPlayerColor() != playerColor);

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

    /**
     * checkMovements()
     * <p>
     * Description: checks if there are any movements saved where the piece actually cannot move, and removes the
     * movement if that is the case.
     */
    public void checkMovements() {
        for (int i = 0; i < possibleMovementSquares.size(); i++) {
            Square square = possibleMovementSquares.get(i);
            if (!square.canKingMove(playerColor)) {
                possibleMovementSquares.remove(square);
                square.getPiecesThatCanMove(playerColor).remove(this);
            }
        }
    }

    /**
     * isPossibleToCastle()
     * <p>
     * Description: checks if it is possible to castle, and initializes the 4 squares where the king and the rook can move.
     */
    public void isPossibleToCastle() {
        Board board = player.getBoard();

        //checks if the rook has moved or not
        boolean kingSideRookCheck = !board.findSquare(kingSideRook).isPieceNull() && board.findSquare(kingSideRook).getPiece().getType() == Type.ROOK && board.findSquare(kingSideRook).getPiece().hasntMoved();
        boolean queenSideRookCheck = !board.findSquare(queenSideRook).isPieceNull() && board.findSquare(queenSideRook).getPiece().getType() == Type.ROOK && board.findSquare(queenSideRook).getPiece().hasntMoved();
        if (firstMove) {
            boolean canMove = true;//checks if it is possible to castle
            if (kingSideRookCheck) {
                //checks if there are any threats or pieces between the rook and the king
                Square current = board.findSquare((char) ((int) position.charAt(0) + 1) + ("" + position.charAt(1)));
                while (current.getPosition().charAt(0) != 'h') {
                    if (!(current.canKingMove(playerColor) && current.isPieceNull())) {
                        canMove = false;
                        break;
                    }
                    String nextPos = (char) ((int) current.getPosition().charAt(0) + 1) + (current.getPosition().charAt(1) + "");
                    current = board.findSquare(nextPos);
                }
                if (!current.canKingMove(playerColor)) {
                    canMove = false;
                }
                if (canMove) {
                    kingSideCastleKingPos = board.findSquare((char) ((int) position.charAt(0) + 2) + ("" + position.charAt(1)));
                    kingSideCastlePosition = board.findSquare((char) ((int) position.charAt(0) + 1) + ("" + position.charAt(1)));
                }
            }
            canMove = true;
            if (queenSideRookCheck) {
                //same exact algorithm for the queen side
                Square current = board.findSquare((char) ((int) position.charAt(0) - 1) + ("" + position.charAt(1)));
                while (current.getPosition().charAt(0) != 'a') {
                    if (!(current.canKingMove(playerColor) && current.isPieceNull())) {
                        canMove = false;
                        break;
                    }
                    String nextPos = (char) ((int) current.getPosition().charAt(0) - 1) + (current.getPosition().charAt(1) + "");
                    current = board.findSquare(nextPos);
                }
                if (!current.canKingMove(playerColor)) {
                    canMove = false;
                }
                if (canMove) {
                    queenSideCastlePosition = board.findSquare((char) ((int) position.charAt(0) - 1) + ("" + position.charAt(1)));
                    queenSideCastleKingPos = board.findSquare((char) ((int) position.charAt(0) - 2) + ("" + position.charAt(1)));
                }
            }
        }
    }

    /**
     * reset()
     * <p>
     * Description: resets the piece
     */
    @Override
    public void reset() {
        super.reset();
        queenSideCastlePosition = null;
        kingSideCastlePosition = null;
        queenSideCastleKingPos = null;
        kingSideCastleKingPos = null;
    }

    /**
     * move(Square pos)
     * <p>
     * Description: moves the piece to the given square
     *
     * @param pos the square that the piece is going to move
     */
    @Override
    public void move(Square pos) {
        super.move(pos);//if it is a normal move
        if (firstMove) {// for a castling possibility
            if (pos == queenSideCastleKingPos || pos == kingSideCastleKingPos) {
                position = pos.getPosition();
                currentSquare.setPiece(null);
                currentSquare = pos;
                pos.setPiece(this);
                firstMove = false;

                if (pos == queenSideCastleKingPos)
                    player.getBoard().findSquare(queenSideRook).getPiece().move(queenSideCastlePosition);
                else
                    player.getBoard().findSquare(kingSideRook).getPiece().move(kingSideCastlePosition);
            }
        }
    }
}
