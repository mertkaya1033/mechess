package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Square {
    private String position;
    private int index[];
    private ArrayList<Piece> threats = new ArrayList<>();
    private ArrayList<Piece> possibleMovement = new ArrayList<>();
    private Piece piece = null;
    private Color color;
    private Piece thePieceCanMove = null;
    private boolean selected = false;
    private boolean showThreats = false;
    private boolean underThreatByWhite = false;
    private boolean underThreatByBlack = false;

    public Square(String pos, int[] index, boolean isDark) {
        this.index = index;
        this.position = pos;
        if (isDark)
            this.color = new Color(163, 92, 45);
        else
            this.color = Color.white;
    }

    public String getPos() {
        return this.position;
    }

    public void display(Graphics g, int x, int y) {

        g.setColor(this.color);
        g.fillRect(x, y, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
        if (piece != null) {
            piece.display(g, x, y);
        }

        if (selected) {
            g.setColor(Color.GREEN);
            for (int i = 0; i < 3; i++)
                g.drawRect(x + i, y + i, Constants.SQUARE_SIZE - (i * 2), Constants.SQUARE_SIZE - (i * 2));
        }

        if (thePieceCanMove != null) {
            int elX, elY, elSize;
            elSize = Constants.SQUARE_SIZE / 3;
            elX = x + (Constants.SQUARE_SIZE - elSize) / 2;
            elY = y + (Constants.SQUARE_SIZE - elSize) / 2;

            g.setColor(Color.GREEN);
            g.fillOval(elX, elY, elSize, elSize);

        }


    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean isPieceNull() {
        return this.piece == null;
    }

    public int[] getIndex() {
        return this.index;
    }

    public void addThreat(Piece piece) {
        this.threats.add(piece);
    }

    public void emptyThreats() {
        this.threats = new ArrayList<>();
    }

    /**
     *
     * WORK ON THIS PART
     *
     */
    public void addPossibleMovement(Piece piece){
        this.possibleMovement.add(piece);
    }

    public void emptyPossibleMovement(){
        this.possibleMovement = new ArrayList<>();
    }

    public void setThePieceCanMove(Piece piece) {
        this.thePieceCanMove = piece;
    }

    public Piece getThePieceCanMove() {
        return thePieceCanMove;
    }

    public void clicked(boolean showThreatsForPiece) {
        this.selected = !this.selected;
        showThreats = showThreatsForPiece;
        if (selected && piece != null && showThreats) {
            ArrayList<Square> th = piece.getPossibleMovementSquares();
            for (int i = 0; i < th.size(); i++) {
                th.get(i).setThePieceCanMove(piece);
            }
        } else {
            ArrayList<Square> th = piece.getPossibleMovementSquares();
            for (int i = 0; i < th.size(); i++) {
                th.get(i).setThePieceCanMove(null);
            }
        }
    }

    public void checkThreatsByPlayers() {
        underThreatByWhite = false;
        underThreatByBlack = false;
        for (Piece piece : threats) {
            underThreatByWhite = underThreatByWhite || piece.getColor() == Piece.CPlayer.white;
            underThreatByBlack = underThreatByBlack || piece.getColor() == Piece.CPlayer.black;
            if (underThreatByBlack && underThreatByWhite) {
                break;
            }
        }
    }

    public boolean isUnderThreat(Piece.CPlayer playerColor) {
        return (playerColor == Piece.CPlayer.white) ? this.underThreatByBlack : this.underThreatByWhite;
    }

}
