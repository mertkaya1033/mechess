package main;

import pieces.*;

import java.util.ArrayList;

public class Player {
    private Piece.CPlayer color;
    private ArrayList<Piece> pieces;
    private Board board;

    public Player(Piece.CPlayer color, Board board) {
        this.board = board;
        this.pieces = new ArrayList<Piece>();
        this.color = color;
        int num, pawn;
        if (this.color == Piece.CPlayer.white) {
            num = 1;
            pawn = 2;

        } else {
            num = 8;
            pawn =7;
        }

        for (int j = 0; j < 8; j++) {
            String pos = (char) (j + 97) + "" + (pawn);
            this.pieces.add(new Pawn(pos, this.color));
        }
        this.pieces.add(new Rook("a" + num, this.color));
        this.pieces.add(new Knight("b" + num, this.color));
        this.pieces.add(new Bishop("c" + num, this.color));
        this.pieces.add(new Queen("d" + num, this.color));
        this.pieces.add(new King("e" + num, this.color));
        this.pieces.add(new Bishop("f" + num, this.color));
        this.pieces.add(new Knight("g" + num, this.color));
        this.pieces.add(new Rook("h" + num, this.color));



        for(Piece piece: pieces){
            piece.setCurrentSquare(board.findSquare(piece.getPosition()));
            board.findSquare(piece.getPosition()).setPiece(piece);
        }
        for(Piece piece: this.pieces){
            piece.occupy(board);
        }
    }

    public ArrayList<Piece> getPieces(){
        return this.pieces;
    }
}
