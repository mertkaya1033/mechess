package main;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class Piece {
    public enum Type {
        KING, QUEEN, ROOK, PAWN, BISHOP, KNIGHT
    }

    public enum CPlayer {
        white, black
    }

    protected Type type;
    protected ArrayList<Square> threads;
    protected String position;
    protected int index[];
    protected Image image;
    protected Square currentSquare = null;
    protected CPlayer color;

    public Piece(String position, CPlayer color, Type type) {
        this.position = position;
        this.type = type;
        this.color = color;
        this.image = new ImageIcon("src/images/" + color.toString() + "_"
                + type.toString() + ".png").getImage();
        this.image = this.image.getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, 0);
        this.threads = new ArrayList<>();
    }

    public abstract void occupy(Board board);

    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
        this.index = square.getIndex();
    }


    public void move(Square pos) {
        if (threads.contains(pos)) {
            this.position = pos.getPos();
        }
    }

    public String getPosition() {
        return this.position;
    }

    public void display(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, null);
    }

    public ArrayList<Square>getThreads(){
        return this.threads;
    }

    public CPlayer getColor(){
        return this.color;
    }
}
