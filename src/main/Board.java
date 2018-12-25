package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Board {
    private Square[][] board = new Square[8][8];
    private Player whitePlayer;
    private Player blackPlayer;

    private int x, y;

    public Board() {

        this.x = (Constants.FRAME_WIDTH - Constants.BOARD_SIZE) / 2;
        this.y = (Constants.FRAME_HEIGHT - Constants.BOARD_SIZE) / 2;

        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                boolean isDark = j % 2 != i % 2;
                String pos = (char) (j + 97) + "" + (board.length - i);
                board[i][j] = new Square(pos, new int[]{i, j}, isDark);
            }
        }

        whitePlayer = new Player(Piece.CPlayer.white, this);
        blackPlayer = new Player(Piece.CPlayer.black, this);

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j].getPos() + "\t");
            }
            System.out.println();
        }
    }

    public void display(Graphics g) {

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].display(g, col * Constants.SQUARE_SIZE + this.x, row * Constants.SQUARE_SIZE + this.y);
            }
        }
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public Square findSquare(String pos) {
        return board[board.length - Integer.parseInt(pos.charAt(1) + "")][(((int) pos.charAt(0)) - 97)];
    }

    public Square getClickedSquare(MouseEvent event) {
        int mouseX = event.getX();
        int mouseY = event.getY();

        boolean xInBounds = (mouseX >= this.x && mouseX <= this.x + Constants.BOARD_SIZE);
        boolean yInBounds = (mouseY >= this.y && mouseY <= this.y + Constants.BOARD_SIZE);

        if (xInBounds && yInBounds) {
            return board[(mouseY - this.y) / Constants.SQUARE_SIZE][(mouseX - this.x) / Constants.SQUARE_SIZE];
        }
        return null;
    }

}
