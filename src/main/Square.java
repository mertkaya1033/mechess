package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Square {
    private String position;
    private int index[];
    private ArrayList<Piece> threads = new ArrayList<>();
    private Piece piece = null;
    private Color color;
    private boolean aPieceCanMove = false;
    private boolean selected = false;

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
//        g.setColor(Color.black);
//        g.drawString(position, x + 10, y + 10);
        if (piece != null) {
            piece.display(g, x, y);
        }

        if (selected) {
            g.setColor(Color.GREEN);
            for(int i = 0; i < 3; i++)
                g.drawRect(x + i, y + i, Constants.SQUARE_SIZE - (i * 2), Constants.SQUARE_SIZE- (i * 2));
            if (piece != null) {
                ArrayList<Square> th = piece.getThreads();
                for (int i = 0; i < th.size(); i++) {
//                    System.out.println(th.get(i).getIndex()[0] + "\t" + th.get(i).getIndex()[1]);
                    th.get(i).setaPieceCanMove(true);
                }
            }
        } else if (piece != null) {
            for (int i = 0; i < piece.getThreads().size(); i++) {
                piece.getThreads().get(i).setaPieceCanMove(false);
            }
        }

        if (aPieceCanMove) {
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
        this.threads.add(piece);
    }

    public void setaPieceCanMove(boolean aPieceCanMove) {
        this.aPieceCanMove = aPieceCanMove;
    }

    public void clicked() {
        this.selected = !this.selected;
        System.out.println(piece == null);
    }

}
