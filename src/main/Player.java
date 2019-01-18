package main;

import pieces.*;

import java.util.ArrayList;

public class Player {
    private Piece.Side color;
    private ArrayList<Piece> pieces;
    private King king;
    private Board board;
    private boolean doesHaveMove;

    /**
     * @param color
     * @param board
     */
    public Player(Piece.Side color, Board board) {
        this.board = board;
        this.pieces = new ArrayList<>();
        this.color = color;

        int num, pawn;
        if (this.color == Piece.Side.white) {
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
        this.king = new King("e" + num, this);
        this.pieces.add(this.king);


        for (Piece piece : this.pieces) {
            piece.setCurrentSquare(board.findSquare(piece.getPosition()));
            board.findSquare(piece.getPosition()).setPiece(piece);
        }
        for (Piece piece : this.pieces) {
            piece.occupy(board);
        }
    }

    /**
     * @return
     */
    public Piece.Side getColor() {
        return color;
    }

    /**
     * @param piece
     * @param square
     */
    public void move(Piece piece, Square square) {
        if (this.pieces.contains(piece)) {
            if (!square.isPieceNull()) {
                square.getPiece().getPlayer().pieces.remove(square.getPiece());
            }
            piece.move(square);
        }
    }

    /**
     *
     */
    public void occupy() {
        for (Piece piece : this.pieces) {
            piece.occupy(board);
            if(!piece.possibleMovementSquares.isEmpty()){
                doesHaveMove = true;
            }
        }
    }

    public void checkKingMovements() {
        this.king.checkMovements();
    }

    public void reset() {
        for (Piece piece : pieces) {
            piece.reset();
        }
    }

    public boolean isKingUnderThreat() {
        return !king.currentSquare.isCanKingMove(color);
    }

    public ArrayList<Constants.Rescuer> getPossibleEscapes() {
        king.checkMovements();
        Piece.Side otherColor = (color == Piece.Side.white) ? Piece.Side.black: Piece.Side.white;
        ArrayList<Constants.Rescuer> possibleEscapes = new ArrayList<>();
        ArrayList<Piece> threats = king.currentSquare.getPiecesThatCanMove(otherColor);

        if (threats.size() > 1 && !king.possibleMovementSquares.isEmpty()) {
            for (Square square : king.possibleMovementSquares) {
                possibleEscapes.add(new Constants.Rescuer(king, square));
            }
        } else if (threats.size() == 1 ) {
            Piece threat = threats.get(0);
            ArrayList<Square> path = threat.pathAsAThreat;
            for (Square possibleBlock : path) {
                for (Piece piece : possibleBlock.getPiecesThatCanMove(color)) {
                    possibleEscapes.add(new Constants.Rescuer(piece, possibleBlock));
                }
                for (Piece piece : pieces) {
                    if (piece.type == Piece.Type.PAWN && !piece.possibleMovementSquares.isEmpty()
                            && piece.possibleMovementSquares.contains(possibleBlock)) {
                        possibleEscapes.add(new Constants.Rescuer(piece, possibleBlock));
                    }
                }
            }
            for (Piece piece : pieces) {
                if (!piece.possibleMovementSquares.isEmpty() && piece.possibleMovementSquares.contains(threat.currentSquare)) {
                    possibleEscapes.add(new Constants.Rescuer(piece, threat.currentSquare));
                }
            }
            if(!king.possibleMovementSquares.isEmpty()){
                for (Square square : king.possibleMovementSquares) {
                    if(square.canKingMove(color))
                        possibleEscapes.add(new Constants.Rescuer(king, square));
                }
            }
        }
        return possibleEscapes;
    }

    public boolean hasMove(){
        return doesHaveMove;
    }

    public Board getBoard(){
        return board;
    }
}

