package main;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

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
    protected boolean firstMove = true;//
    protected String kingSideRook;
    protected String queenSideRook;
    protected Square kingSideCastlePosition = null, queenSideCastlePosition = null,
            kingSideCastleKingPos = null, queenSideCastleKingPos = null;

    /**
     * @param position the address of the piece on the board
     * @param player the player which owns the piece
     * @param type the type of the piece
     */
    public Piece(String position, Player player, Type type) { //Each piece has its own parameters
        this.type = type;
        this.player = player;
        this.position = position;
        this.playerColor = player.getColor();
        String imageName = "src/images/" + player.getColor().toString() + "_" + type.toString() + ".png";
        this.image = new ImageIcon(imageName).getImage();
        this.image = this.image.getScaledInstance(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE, 0);
        this.possibleMovementSquares = new ArrayList<>(); //checks which  spaces you can move to
    }

    /**
     * @param board
     */
    public abstract void occupy(Board board);

    /**
     * @param pos
     */
    public void move(Square pos) {
        if (possibleMovementSquares.contains(pos)) {
            position = pos.getPosition();
            currentSquare.setPiece(null);
            currentSquare = pos;
            pos.setPiece(this);
            firstMove = false;
        }
    }
    /**
     * @param g
     * @param x
     * @param y
     */
    public void display(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, null);
    }

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
        if(pinned)
            possibleMovementSquares = new ArrayList<>();
    }

    public void protect(){
        currentSquare.disallowKingMovement(playerColor);
    }

    public ArrayList<Square> getPossibleMovementSquares() {
        return possibleMovementSquares;
    }

    public Player getPlayer() {
        return player;
    }

    public void reset(){
        possibleMovementSquares = new ArrayList<>();
        pathAsAThreat = new ArrayList<>();
        isPinned = false;
    }

    public boolean hasntMoved(){
        return firstMove;
    }
    public ArrayList<Square>possibleCastleSquares(){
        ArrayList<Square> ret = new ArrayList<>();
        ret.add(queenSideCastleKingPos);
        ret.add(kingSideCastleKingPos);
        return ret;
    }

}
