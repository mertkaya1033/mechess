package main;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Piece.java
 * <p>
 * Description: An abstract class for each type of piece.
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public abstract class Piece {
    //represents types of pieces
    public enum Type {
        KING, QUEEN, ROOK, PAWN, BISHOP, KNIGHT
    }

    //represents the side/color of the piece
    public enum Side {
        white, black
    }


    protected ArrayList<Square> possibleMovementSquares;//all the possible squares that this piece can legally move
    protected ArrayList<Square> pathAsAThreat;//shows the path to the king of the opposite player
    protected boolean isPinned = false;//if the piece can't move because if it does the king can be captured
    protected Side playerColor;//the side/color of the piece
    protected Player player;//the player which owns this piece
    protected Square currentSquare = null;//the square that the piece is currently placed on
    protected Image image;//the image of the square
    protected Type type;//the type of the piece
    protected String position;//the address of the piece on the board
    protected ArrayList<ArrayList<Square>> pms;//used to determine different paths of the piece
    protected boolean firstMove = true;//if it is the pieces first move

    //only used by the king
    protected String kingSideRook;//the position of the rook that is on the king side
    protected String queenSideRook;//the position of the rook that is on the queen side
    protected Square kingSideCastlePosition = null, queenSideCastlePosition = null,//where the rook's position will be after castling
            kingSideCastleKingPos = null, queenSideCastleKingPos = null;//where the king's position will be after castling

    /**
     * Piece(String position, Player player, Type type)
     * <p>
     * Description: constructor
     *
     * @param position the address of the piece on the board
     * @param player   the player which owns the piece
     * @param type     the type of the piece
     */
    public Piece(String position, Player player, Type type) { //Each piece has its own parameters
        this.type = type;
        this.player = player;
        this.position = position;
        this.playerColor = player.getColor();
        String imageName = "src/images/" + player.getColor().toString() + "_" + type.toString() + ".png";
        this.image = new ImageIcon(imageName).getImage();
        this.image = this.image.getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, 0);//resizes the image
        this.possibleMovementSquares = new ArrayList<>(); //checks which  spaces you can move to
    }

    /**
     * occupy(Board board)
     * <p>
     * Description: This abstract method allows each piece to find/occupy each square it
     * can legally move on the board.
     *
     * @param board the board
     */
    public abstract void occupy(Board board);

    /**
     * move(Square pos)
     * <p>
     * Description: moves the piece to the given square
     *
     * @param pos the square that the piece is going to move
     */
    public void move(Square pos) {
        if (possibleMovementSquares.contains(pos)) {//if the piece can move to that square...
            position = pos.getPosition();
            currentSquare.setPiece(null);
            currentSquare = pos;
            pos.setPiece(this);
            firstMove = false;
        }
    }

    /**
     * display(Graphics g, int x, int y)
     * <p>
     * Description: displays the piece on the given graphics within the given position
     *
     * @param g graphics where the piece is going to be displayed
     * @param x the x position of the piece
     * @param y the y position of the piece
     */
    public void display(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, null);
    }

    /**
     * protect()
     * <p>
     * Description: disallows the opponent's king to move to its square, in other words,
     * does not allow the opponent's king to capture this piece.
     */
    public void protect() {
        currentSquare.disallowKingMovement(playerColor);
    }

    /**
     * reset()
     * <p>
     * Description: resets the piece
     */
    public void reset() {
        possibleMovementSquares = new ArrayList<>();
        pathAsAThreat = new ArrayList<>();
        isPinned = false;
    }

    /**
     * possibleCastleSquares()
     * <p>
     * Description: only used by the king, determining the squares where it can move when it castles
     *
     * @return the possible square that the king can move when castling
     */
    public ArrayList<Square> possibleCastleSquares() {
        ArrayList<Square> ret = new ArrayList<>();
        ret.add(queenSideCastleKingPos);
        ret.add(kingSideCastleKingPos);
        return ret;
    }

    /**** GETTERS AND SETTERS *****/
    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public String getPosition() {
        return position;
    }

    public Side getPlayerColor() {
        return playerColor;
    }

    public Type getType() {
        return type;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
        if (pinned)
            possibleMovementSquares = new ArrayList<>();//a piece cannot move if it is pinned
    }


    public ArrayList<Square> getPossibleMovementSquares() {
        return possibleMovementSquares;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean hasntMoved() {
        return firstMove;
    }


}
