package main;

import pieces.*;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Player {
    private Piece.CPlayer color;
    private ArrayList<Piece> pieces;
    private Board board;

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
        this.pieces.add(new King("e" + num, this));
        this.pieces.add(new Bishop("f" + num, this));
        this.pieces.add(new Knight("g" + num, this));
        this.pieces.add(new Rook("h" + num, this));


        for (Piece piece : this.pieces) {
            piece.setCurrentSquare(board.findSquare(piece.getPosition()));
            board.findSquare(piece.getPosition()).setPiece(piece);
        }
        for (Piece piece : this.pieces) {
            piece.occupy(board);
        }
    }

    public Piece.CPlayer getColor() {
        return color;
    }
    //work here
    public void clicked(MouseEvent event) {
        Square clickedSquare = this.board.getClickedSquare(event);
        if (clickedSquare != null && pieces.contains(clickedSquare.getPiece())) {
            board.selectSquare(clickedSquare, true);
        } else if (clickedSquare != null && clickedSquare.getThePieceCanMove() != null) {
            clickedSquare.getThePieceCanMove().move(clickedSquare);
            for (Piece piece : this.pieces) {
                piece.occupy(board);
            }
        }
    }
}
