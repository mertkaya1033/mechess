package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Square {
    private String position;//address of the square
    private ArrayList<Piece> whitePiecesThatCanMove = new ArrayList<>();//the white pieces that can move to this square
    private ArrayList<Piece> blackPiecesThatCanMove = new ArrayList<>();//the black pieces that can move to this square
    private boolean canWhiteKingMove = true, canBlackKingMove = true;//if a king from either color can move to this square
    private Piece piece = null;//the piece that is on this square
    private boolean selected = false;//if the square has been selected
    private Piece thePieceCanMove = null;//if a square has been selected(and it is not this one), it is the piece that is on that square can move to this square
    private Color color;//the color of the square
    private int index[];//the index of the square on the 8x8 board
    private boolean showTheThreatsForThePiece = false;//does the threats for the piece that is on this square are being displayed

    /**
     * Square(String pos, int[] index, boolean isDark)
     * <p>
     * Description: the constructor
     *
     * @param pos    address of the square
     * @param index  the index of the square on the 8x8 board
     * @param isDark if the square has the dark color
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
     * display(Graphics g, int x, int y)
     * <p>
     * Description: displays the square on the graphics
     *
     * @param g the graphics where the square can be displayed
     * @param x the x position of the square
     * @param y the x position of the square
     */
    public void display(Graphics g, int x, int y) {

        //display the square
        g.setColor(this.color);
        g.fillRect(x, y, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);

        //if there is a piece on this square, display it
        if (piece != null) {
            piece.display(g, x, y);
        }

        //if the square has been selected, display a green rectangle around it
        if (selected) {
            g.setColor(Color.GREEN);
            for (int i = 0; i < 3; i++)
                g.drawRect(x + i, y + i, Constants.SQUARE_SIZE - (i * 2), Constants.SQUARE_SIZE - (i * 2));
        }

        //if another piece can move to this square, display a green oval on the square
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
     * setPiece(Piece piece)
     * <p>
     * Description: setter
     *
     * @param piece changes the piece on this square to this piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * getPiece(void)
     * <p>
     * Description: getter
     *
     * @return the current piece on the square
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * isPieceNull()
     * <p>
     * Description: Determines if there is a piece placed on this square
     *
     * @return if there is a piece on the square
     */
    public boolean isPieceNull() {
        return this.piece == null;
    }

    /**
     * getIndex()
     * <p>
     * Description: getter
     *
     * @return the index of the square on the board
     */
    public int[] getIndex() {
        return this.index;
    }

    /**
     * setThePieceCanMove(Piece thePieceCanMove)
     * <p>
     * Description: setter
     *
     * @param thePieceCanMove the piece that can move to this square
     */
    public void setThePieceCanMove(Piece thePieceCanMove) {
        this.thePieceCanMove = thePieceCanMove;
    }


    /**
     * clicked(boolean showThreatsForPiece)
     * <p>
     * Description: It is called when the player has clicked on this square. It either selects or unselects the square.
     *
     * @param showThreatsForPiece if the threats for the piece that is on the square will be shown
     */
    public void clicked(boolean showThreatsForPiece) {
        this.selected = !this.selected;
        this.showTheThreatsForThePiece = showThreatsForPiece;

        if (selected && piece != null && showTheThreatsForThePiece) {
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

    /**
     * getPosition()
     *
     * Description: getter
     *
     * @return the address of the square on the board
     */
    public String getPosition(){
        return this.position;
    }

    /**
     * getPosition()
     *
     * Description: getter
     *
     * @return the piece that can move to this square
     */
    public Piece getThePieceCanMove() {
        return thePieceCanMove;
    }

    public void addPieceThatCanMove(Piece piece){
        if(piece.playerColor == Piece.Side.white){
            whitePiecesThatCanMove.add(piece);
            canBlackKingMove = false;
        }else{
            blackPiecesThatCanMove.add(piece);
            canWhiteKingMove = false;
        }
    }

    public void disallowKingMovement(Piece.Side playerColor){
        if(playerColor == Piece.Side.white){
            canBlackKingMove = false;
        }else{
            canWhiteKingMove = false;
        }
    }

    public boolean canKingMove(Piece.Side playerColor) {
        if(playerColor == Piece.Side.white){
            return canWhiteKingMove;
        }else {
            return canBlackKingMove;
        }
    }

    public void reset(){
        reset(true);
    }
    public void reset(boolean fully){
        if(fully){
            canBlackKingMove = true;
            canWhiteKingMove = true;
        }
        whitePiecesThatCanMove = new ArrayList<>();
        blackPiecesThatCanMove = new ArrayList<>();
    }

    public boolean isCanKingMove(Piece.Side playerColor){
        if(playerColor == Piece.Side.white){
            return canWhiteKingMove;
        }
        return canBlackKingMove;
    }

    public ArrayList<Piece>getPiecesThatCanMove(Piece.Side playerColor){
        if(playerColor == Piece.Side.white){
            return whitePiecesThatCanMove;
        }
        return blackPiecesThatCanMove;
    }
}
