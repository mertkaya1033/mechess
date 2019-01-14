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

    /**
     * @param pos
     * @param index
     * @param isDark
     */
    public Square(String pos, int[] index, boolean isDark) {
        this.index = index;
        this.position = pos;
        if (isDark)
            this.color = new Color(163, 92, 45);
        else
            this.color = Color.white;
    }

    /**
     * @return
     */
    public String getPos() {
        return this.position;
    }

    /**
     * @param g
     * @param x
     * @param y
     */
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

    /**
     * @param piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @return
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * @return
     */
    public boolean isPieceNull() {
        return this.piece == null;
    }

    /**
     * @return
     */
    public int[] getIndex() {
        return this.index;
    }

    /**
     * @param piece
     */
    public void addThreat(Piece piece) {
        this.threats.add(piece);
    }

    /**
     *
     */
    public void emptyThreats() {
        this.threats = new ArrayList<>();
    }

    public boolean isUnderThreat(Player player) {
        Piece.CPlayer colo = (player.getColor() == Piece.CPlayer.white) ? Piece.CPlayer.black : Piece.CPlayer.white;
        for (Piece piec : threats) {
            if (piec.getColor() == colo) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Piece>getThreats(){
        return threats;
    }
    /**
     * WORK ON THIS PART
     */
    /*****************************************************************************************************************/

    /**
     * @param piece
     */
    public void addPossibleMovement(Piece piece) {
        this.possibleMovement.add(piece);
    }

    /**
     *
     */
    public void emptyPossibleMovement() {
        this.possibleMovement = new ArrayList<>();
    }

    public boolean isPossibleMovement(Player player) {
        Piece.CPlayer color = (player.getColor() == Piece.CPlayer.white) ? Piece.CPlayer.black : Piece.CPlayer.white;
        for (Piece piece : possibleMovement) {
            if (piece.getColor() == color) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Piece>getPossibleMovement() {
        return possibleMovement;
    }
    /*****************************************************************************************************************/

    /**
     * @param piece
     */
    public void setThePieceCanMove(Piece piece) {
        this.thePieceCanMove = piece;
    }

    /**
     * @return
     */
    public Piece getThePieceCanMove() {
        return thePieceCanMove;
    }

    /**
     * @param showThreatsForPiece
     */
    public void clicked(boolean showThreatsForPiece) {
        this.selected = !this.selected;
        showThreats = showThreatsForPiece;
        if (selected && piece != null && showThreats) {
            ArrayList<Square> th = piece.getPossibleMovementSquares();
            for (int i = 0; i < th.size(); i++) {
                th.get(i).setThePieceCanMove(piece);
            }
        } else if (piece != null) {
            ArrayList<Square> th = piece.getPossibleMovementSquares();
            for (int i = 0; i < th.size(); i++) {
                th.get(i).setThePieceCanMove(null);
            }
        }
    }

}
