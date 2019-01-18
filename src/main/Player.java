package main;

import pieces.*;

import java.util.ArrayList;

/**
 * Player.java
 * <p>
 * Description: The class that represents a player in the game
 *
 * @author mert
 * @version 1.0.0 (updated: Jan 17, 2019)
 */
public class Player {
    private Piece.Side color;//the color/side of the player
    private ArrayList<Piece> pieces;//the pieces that the player posses
    private King king;//player's king
    private Board board;//the board where the player "plays"
    private boolean doesHaveMove; //determines if the player have any possible moves

    /**
     * Player(Piece.Side color, Board board)
     * <p>
     * Descriptions: constructor
     *
     * @param color the side/color of the player
     * @param board the board in which the player "plays"
     */
    public Player(Piece.Side color, Board board) {
        this.board = board;
        this.pieces = new ArrayList<>();
        this.color = color;

        int num, pawn;//used to determine row numbers for each piece
        if (this.color == Piece.Side.white) {
            num = 1;
            pawn = 2;
        } else {
            num = 8;
            pawn = 7;
        }

        //to initialize the pawns into the player
        for (int j = 0; j < 8; j++) {
            String pos = (char) (j + 97) + "" + (pawn);//uses ascii to determine the position of the pawn
            this.pieces.add(new Pawn(pos, this));
        }
        //to initialize other pieces
        this.pieces.add(new Rook("a" + num, this));
        this.pieces.add(new Knight("b" + num, this));
        this.pieces.add(new Bishop("c" + num, this));
        this.pieces.add(new Queen("d" + num, this));
        this.pieces.add(new Bishop("f" + num, this));
        this.pieces.add(new Knight("g" + num, this));
        this.pieces.add(new Rook("h" + num, this));
        this.king = new King("e" + num, this);
        this.pieces.add(this.king);

        //locate the pieces onto the board
        for (Piece piece : this.pieces) {
            piece.setCurrentSquare(board.findSquare(piece.getPosition()));
            board.findSquare(piece.getPosition()).setPiece(piece);
        }

        //occupy the possible places where each piece can move
        for (Piece piece : this.pieces) {
            piece.occupy(board);
        }
    }

    /**
     * move(Piece piece, Square square)
     * <p>
     * Description: moves the specific piece to the given square
     *
     * @param piece  the piece that is going to be moved
     * @param square the square in which the piece is going to be moved
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
     * occupy()
     * <p>
     * Description: calls occupy method of each piece
     */
    public void occupy() {
        for (Piece piece : this.pieces) {
            piece.occupy(board);
            if (!piece.possibleMovementSquares.isEmpty()) {
                doesHaveMove = true;
            }
        }
    }

    /**
     * checkKingMovements()
     * <p>
     * Description: checks each movement of its king if it can actually move to the places
     * that it thinks it can.
     */
    public void checkKingMovements() {
        this.king.checkMovements();
    }

    /**
     * reset()
     * <p>
     * Description: resets the player
     */
    public void reset() {
        for (Piece piece : pieces) {
            piece.reset();
        }
        doesHaveMove = false;
    }

    /**
     * isKingUnderThreat()
     * <p>
     * Description: checks if the king is under threat, in other words, checks if it is check
     * or not
     *
     * @return is the king under threat
     */
    public boolean isKingUnderThreat() {
        return !king.currentSquare.canKingMove(color);
    }

    /**
     * getPossibleEscapes()
     * <p>
     * Description: finds every possible way of neutralizing the threat to the king
     *
     * @return all the possible escapes
     */
    public ArrayList<Constants.Rescuer> getPossibleEscapes() {
        king.checkMovements();//check the kings movement again just in case
        Piece.Side otherColor = (color == Piece.Side.white) ? Piece.Side.black : Piece.Side.white;//get the opposite players color
        ArrayList<Constants.Rescuer> possibleEscapes = new ArrayList<>();//where all the possible escapes are being stored
        ArrayList<Piece> threats = king.currentSquare.getPiecesThatCanMove(otherColor);//all the treats to the king

        if (threats.size() > 1 && !king.possibleMovementSquares.isEmpty()) {//if there is more than one threat towards the king
            for (Square square : king.possibleMovementSquares) {
                possibleEscapes.add(new Constants.Rescuer(king, square));
            }
        } else if (threats.size() == 1) {
            Piece threat = threats.get(0);
            ArrayList<Square> path = threat.pathAsAThreat;//gets the whole path of the threat to find out if it could be blocked
            for (Square possibleBlock : path) {
                for (Piece piece : possibleBlock.getPiecesThatCanMove(color)) {
                    possibleEscapes.add(new Constants.Rescuer(piece, possibleBlock));//add all the pieces that can block the threat
                }
                //this loop is needed because the pawns work differently
                for (Piece piece : pieces) {
                    if (piece.type == Piece.Type.PAWN && !piece.possibleMovementSquares.isEmpty()
                            && piece.possibleMovementSquares.contains(possibleBlock)) {
                        possibleEscapes.add(new Constants.Rescuer(piece, possibleBlock));
                    }
                }
            }

            //to check if there are any pieces that can capture the threat
            for (Piece piece : pieces) {
                if (!piece.possibleMovementSquares.isEmpty() && piece.possibleMovementSquares.contains(threat.currentSquare)) {
                    possibleEscapes.add(new Constants.Rescuer(piece, threat.currentSquare));
                }
            }

            //add the escapes of the king itself by moving away
            if (!king.possibleMovementSquares.isEmpty()) {
                for (Square square : king.possibleMovementSquares) {
                    if (square.canKingMove(color))
                        possibleEscapes.add(new Constants.Rescuer(king, square));
                }
            }
        }
        return possibleEscapes;
    }

    /*** GETTERS AND SETTERS ***/
    public boolean hasMove() {
        return doesHaveMove;
    }

    public Board getBoard() {
        return board;
    }

    public Piece.Side getColor() {
        return color;
    }

}

