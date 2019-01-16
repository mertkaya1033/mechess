package main;

import pieces.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private Piece.CPlayer color;
    private ArrayList<Piece> pieces;
    private King king;
    private Board board;

    /**
     * @param color
     * @param board
     */
    public Player(Piece.CPlayer color, Board board) {
        this.board = board;
        this.pieces = new ArrayList<>();
        this.color = color;

        int num, pawn;
        if (this.color == Piece.CPlayer.white) {
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
        king.occupy(board);

    }

    /**
     * @return
     */
    public Piece.CPlayer getColor() {
        return color;
    }

    /**
     * @param piece
     * @param square
     */
    public void move(Piece piece, Square square) {
        if (this.pieces.contains(piece)) {
            if(!square.isPieceNull()){
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
        }
        /**ADD CHECKMATE**/
    }

    /**
     *
     */
    public void occupyKing() {
        this.king.occupy(board);
    }

    public void checkKingThreats() {
        for (int i = 0; i < king.getPossibleMovementSquares().size(); i++) {
            Square pMovement = king.getPossibleMovementSquares().get(i);
            if (!pMovement.isUnderThreat(this).isEmpty() || !pMovement.isPossibleMovement(this)) {
                king.getPossibleMovementSquares().remove(pMovement);

                if (pMovement.getThreats().contains(king))
                    pMovement.getThreats().remove(king);

            }
        }
    }

    public boolean isKingUnderThreat() {
        if (this.king.getCurrentSquare().isUnderThreat(this).isEmpty()) {
            return false;
        }
        return true;
    }

    public ArrayList<Piece> threatsToKing() {
        return this.king.getCurrentSquare().isUnderThreat(this);
    }


    private HashMap<Piece, Square> rescuers = new HashMap<>();
//    private ArrayList<Piece> piecesRescueKing = new ArrayList<>();

    public String isCheckMate() {
        rescuers = new HashMap<>();
        ArrayList<Piece> threatsToKing = threatsToKing();

        if (threatsToKing.size() > 1 && this.king.getPossibleMovementSquares().isEmpty()) {
            return "mate";
        } else if (threatsToKing.size() > 1) {
            return "king has to move";
        }else {
            Piece threat = threatsToKing.get(0);
            if ((threat.type == Piece.Type.KNIGHT || threat.type == Piece.Type.PAWN) && threat.currentSquare.capturable(this).isEmpty()) {
                return "mate";
            } else {
                /**
                 * blocking the path of the threat
                 */
                for (int i = 0; i < threat.pathAsAThreat.size(); i++) {//change it to pathAsAThreat.size
                    Square possibleBlock = threat.pathAsAThreat.get(i);
                    if (!possibleBlock.capturable(this).isEmpty()) {
                        for(Piece p: possibleBlock.capturable(this)){
                            rescuers.put(p, possibleBlock);
                        }
                    }else{
                        /**
                         * this is needed because pawns' work differently
                         */
                        for(Piece p: pieces){
                            if(p.type == Piece.Type.PAWN && !p.possibleMovementSquares.isEmpty() && p.possibleMovementSquares.contains(possibleBlock)){
                                rescuers.put(p, possibleBlock);
                            }
                        }
                    }
                }
                /**
                 * Is the treat itself capturable
                 */
                if (!threat.currentSquare.capturable(this).isEmpty()) {
                    for(Piece p: threat.currentSquare.capturable(this)){
                        rescuers.put(p, threat.currentSquare);
                    }
                }

                /**
                 * if the king can move
                 */


                if (rescuers.isEmpty() && this.king.getPossibleMovementSquares().isEmpty()) {
                    return "mate";
                }else if(rescuers.isEmpty()){
                    return "king has to move";
                }
                return "just check";
            }
        }
    }
    public void emptyOccupies(){
        for(int i = 0; i < pieces.size(); i++){
            pieces.get(i).emptyOccupies();
        }
    }

    public void occupyKingRescuers(){
        for (Piece piece : rescuers.keySet()) {
            piece.possibleMovementSquares.add(rescuers.get(piece));
        }
    }
}

