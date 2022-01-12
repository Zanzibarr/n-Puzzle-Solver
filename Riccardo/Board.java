public class Board implements Comparable<Board> {

	String pater;
	private String toString;
	private final int[] board;
	int move;
	int manhattan;
	private byte lastMove; //0 up, 1 down, 2 left, 3 right; 
	private int zero;

	public Board(int [][] tiles) {
		this(matrixToArray(tiles));
	}
	private static int[] matrixToArray(int[][] tiles) {
		final int[] tmp = new int[Solver.size*Solver.size];
		
		for (int i = 0; i < Solver.size; i++) {
			for (int j = 0; j < Solver.size; j++) {
				tmp[i*Solver.size+j] = tiles[i][j];
			}
		}
		return tmp;
	}

	public Board(int[] tiles) {
		board = tiles;
		pater = " ";
		move = 0; //0 move initial state
		manhattan = manhattan();
		lastMove = -1;
		toString = personaltoString(board);
	}

	public Board(int[] tiles, String _pater, int _move, byte _lastMove, int _manhattan, int _zero) {
		board = tiles;
		pater = _pater;
		move = _move;
		lastMove = _lastMove;
		manhattan = _manhattan;
		zero = _zero;
		toString = personaltoString(board);
	}

	public int manhattan() {
		int tmp = 0;
		int l = board.length;
		for (int i = 0; i < l; i++) {
			if(board[i] == 0) { zero = i; }
			else {
				tmp += mh(board[i], i);
			}
		}

		for (int j = 0; j < board.length; j++) { if(VeryBadlinearConflict(board, j)) tmp++; }

		return tmp;
	}

	private static boolean VeryBadlinearConflict(int[] board, int s) { return (board[s] != 0) && (s/Solver.size == (board[s]-1)/Solver.size || s%Solver.size == (board[s]-1)%Solver.size ) && ( s+1 != board[s] ) && ( s+1 == board[((board[s]-1)/Solver.size)*Solver.size+((board[s]-1)%Solver.size)]); }
	private static int mh(int tile, int index) { return Math.abs(index/Solver.size - (tile-1)/Solver.size) + Math.abs(index%Solver.size - (tile-1)%Solver.size); };
	private int getPriority() { return move + manhattan; }
	
	public int compareTo(Board o) {	 return this.getPriority() - o.getPriority(); }
	private static String personaltoString(int[] board) {
		StringBuilder sb = new StringBuilder();
		
		for (int i : board) {
			sb.append(i);
			sb.append(" ");
		}
		return sb.toString();
	}

	public String toString() { return toString; }
	
	private Board move(int xIndex, int yIndex) {
		final int[] zArray = {(zero/Solver.size),(zero%Solver.size)};
		final int tile = (zArray[0]+xIndex)*Solver.size+(zArray[1]+yIndex);

		if(((zArray[0]+xIndex)<0 || (zArray[0]+xIndex)>=Solver.size) || ((zArray[1]+yIndex)<0 || (zArray[1]+yIndex)>=Solver.size)) return null;

		int[] tmpTiles = new int[board.length];
		System.arraycopy(board, 0, tmpTiles, 0, board.length);
		int newManhattan = manhattan;

		newManhattan -= mh(tmpTiles[tile], tile);
        if (VeryBadlinearConflict(tmpTiles, tile)) newManhattan -= 2;

		tmpTiles[zero] = tmpTiles[tile];
		tmpTiles[tile] = 0;
		
		newManhattan += mh(tmpTiles[zero], zero);
        if (VeryBadlinearConflict(tmpTiles, zero)) newManhattan += 2;

		byte newLastMove;
		if(xIndex == 0 && yIndex != 0){
			newLastMove = (yIndex > 0) ? (byte)3 : (byte)2;
		}
		else newLastMove = (xIndex > 0) ? (byte)0 : (byte)1;

		return new Board(tmpTiles, this.toString(), move+1, newLastMove, newManhattan, tile);
	}
	public Board[] getChildren(){
		final Board[] children = new Board[4];
		byte k = 0;
		
		switch(lastMove) {
			case 0:
				children[k++] = move(1, 0);
				children[k++] = move(0, -1);
				children[k++] = move(0, 1);
				break;
			case 1:
				children[k++] = move(-1, 0);
				children[k++] = move(0,-1);
				children[k++] = move(0, 1);
				break;
			case 2:
				children[k++] = move(-1,0);
				children[k++] = move(1, 0);
				children[k++] = move(0, -1);
				break;
			case 3:
				children[k++] = move(-1,0);
				children[k++] = move(1,0);
				children[k++] = move(0,1);
				break;
			default:
				children[k++] = move(-1,0);
				children[k++] = move(1,0);
				children[k++] = move(0,1);
				children[k++] = move(0,-1);
			}
		return children;
	}
}