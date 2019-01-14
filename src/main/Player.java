package main;

import pieces.*;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player {
    private Piece.CPlayer color;
    private ArrayList<Piece> pieces;
    private King king;
    private Board board;

    /**
     * @param color
     * @param board
     */
    public Player(Piece.CPlayer color, Board board) {
        this.board = board;
        this.pieces = new ArrayList<>();
        this.color = color;

        int num, pawn;
        if (this.color == Piece.CPlayer.white) {
            num = 1;
            pawn = 2;

        } else {
            num = 8;
            pawn = 7;
        }

        for (int j = 0; j < 8; j++) {
            String pos = (char) (j + 97) + "" + (pawn);
            this.pieces.add(new Pawn(pos, this));
        }
        this.pieces.add(new Rook("a" + num, this));
        this.pieces.add(new Knight("b" + num, this));
        this.pieces.add(new Bishop("c" + num, this));
        this.pieces.add(new Queen("d" + num, this));
        this.pieces.add(new Bishop("f" + num, this));
        this.pieces.add(new Knight("g" + num, this));
        this.pieces.add(new Rook("h" + num, this));
        this.king = new King("e" + num, this); /**BUG BUG BUG**/
        this.pieces.add(this.king); /**BUG BUG BUG**/



        for (Piece piece : this.pieces) {
            piece.setCurrentSquare(board.findSquare(piece.getPosition()));
            board.findSquare(piece.getPosition()).setPiece(piece);
        }
        for (Piece piece : this.pieces) {
            piece.occupy(board);
        }
        king.occupy(board);/**BUG BUG BUG**/

    }

    /**
     * @return
     */
    public Piece.CPlayer getColor() {
        return color;
    }

    /**
     * @param piece
     * @param square
     */
    public void move(Piece piece, Square square) {
        if (this.pieces.contains(piece)) {
            piece.move(square);
        }
    }

    /**
     * @param board
     */
    public void occupy(Board board) {
        for (Piece piece : this.pieces) {
            piece.occupy(board);
        }
    }

    /**
     * @param board
     */
    public void occupyKing(Board board) {
        this.king.occupy(board);
    }

    public void checkKingThreats() {
        for (Square pMovement : king.getPossibleMovementSquares()) {
            if (pMovement.isUnderThreat(this) || !pMovement.isPossibleMovement(this)) {
                king.getPossibleMovementSquares().remove(pMovement);

                if (pMovement.getThreats().contains(king))
                    pMovement.getThreats().remove(king);

            }
        }
    }
}
