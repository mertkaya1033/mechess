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

    /**
     * ------ ADD POSSIBLE MOVEMENT FOR EACH SQUARE
     * <p>
     * ------ MAKE SURE EVEN THOUGH THE PIECE CANT MOVE TO THE SQUARE, IT MIGHT THREATEN THAT
     * SQUARE SO THAT THE OPPONENT'S KING CANT MOVE TO THAT SQUARE
     */


    protected Type type;
    protected ArrayList<Square> possibleMovementSquares;
    protected String position;
    protected int index[];
    protected Image image;
    protected Square currentSquare = null;
    protected Player player;

    /**
     *
     * @param position
     * @param player
     * @param type
     */
    public Piece(String position, Player player, Type type) {
        this.position = position;
        this.type = type;
        this.player = player;

        String imageName = "src/images/" + player.getColor().toString() + "_" + type.toString() + ".png";
        this.image = new ImageIcon(imageName).getImage();
        this.image = this.image.getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, 0);
        this.possibleMovementSquares = new ArrayList<>();
    }

    /**
     *
     * @param board
     */
    public abstract void occupy(Board board);

    /**
     *
     * @param square
     */
    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
        this.index = square.getIndex();
    }

    /**
     *
     * @param pos
     */
    public void move(Square pos) {
        if (possibleMovementSquares.contains(pos)) {
            this.position = pos.getPos();
            this.currentSquare.setPiece(null);
            this.currentSquare = pos;
            this.index = this.currentSquare.getIndex();
            pos.setPiece(this);
        }
    }

    /**
     *
     * @return
     */
    public String getPosition() {
        return this.position;
    }

    /**
     *
     * @param g
     * @param x
     * @param y
     */
    public void display(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, null);
    }

    /**
     *
     * @return
     */
    public ArrayList<Square> getPossibleMovementSquares() {
        return this.possibleMovementSquares;
    }

    /**
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     *
     * @return
     */
    public CPlayer getColor() {
        return this.player.getColor();
    }
}
