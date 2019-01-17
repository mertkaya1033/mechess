package main;

public class Constants {
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 700;
	public static final int BOARD_SIZE = (FRAME_WIDTH / 9) * 8;
	public static final int SQUARE_SIZE = BOARD_SIZE / 8;

	public static class Rescuer{
		private Piece piece;
		private Square square;

		public Rescuer(Piece piece, Square square){
			this.piece = piece;
			this.square = square;
		}

		public Piece getPiece() {
			return piece;
		}

		public void setPiece(Piece piece) {
			this.piece = piece;
		}

		public Square getSquare() {
			return square;
		}

		public void setSquare(Square square) {
			this.square = square;
		}
	}
}
