import static java.util.Arrays.binarySearch;
public class Board implements Comparable<Board> {

	String pater;
	private final int[] board;
	int move;
	int manhattan;
	private byte lastMove; //0 up, 1 down, 2 left, 3 right; 
	private int zero;

	public static int linearConflict(int[] tiles, int rc, int num){
		int inversions = 0;

		if(rc == 0) {
			int[] a = new int[Solver.size];
			System.arraycopy(tiles, Solver.size*num, a, 0, Solver.size);

			int min = Solver.size*num + 1;
			int max = (Solver.size*num + Solver.size<0) ? Solver.size*num + Solver.size : 0;

			int x = max;
			boolean end = ((a[0] != 0) && a[0] == x--);
			for (int i = 1; i < Solver.size; i++) { end &= ((a[i] != 0) && a[i] == x--); }

			if(end) return Solver.size-1;

			for (int i = 1; i < Solver.size; i++) {
				if (a[i] != 0 && a[i] >= min && a[i] <= max) {
					for (int j = 0; j < i; j++) {
						if (a[j] != 0 && a[j] >= min && a[j] <= max) {
							if ((a[i] < a[j]) != (i < j)) {
								inversions++;
							}
						}
					}
				} 
			}


		}
		else {
			int[] a = new int[Solver.size];
				for (int i = 0, j = num; i < Solver.size; i++, j+= Solver.size) {
					a[i] = tiles[j];
				}
			int[] des = new int[Solver.size];
			for (int i = 0, j = num+1; i < Solver.size; i++, j+= Solver.size) {
				des[i] = (j < Solver.size*Solver.size) ? j : 0;
			}

			int x = Solver.size-1;
			boolean end = ((a[0] != 0) && a[0] == des[x--]);
			for (int i = 1; i < Solver.size; i++) {
				end &= ((a[i] != 0) && a[i] == des[x--]);
			}
			if(end) return Solver.size-1;

			for (int i = 1, iPos; i < Solver.size; i++) {
				if (a[i] != 0 && 0 <= (iPos = binarySearch(des, a[i]))) {
					for (int j = 0, jPos; j < i; j++) {
						if (a[j] != 0 && 0 <= (jPos = binarySearch(des, a[j]))) {
							if ((a[i] < a[j]) != (i < j)) {
								inversions++;
							}
						}
					}
				}
			}
		}
			return inversions;
	}
	
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
	}
	public Board(int[] tiles, String _pater, int _move, byte _lastMove, int _manhattan, int _zero) {
		board = tiles;
		pater = _pater;
		move = _move;
		lastMove = _lastMove;
		manhattan = _manhattan;
		zero = _zero;
	}

	//0 riga, 1 colonna
	


	public int manhattan() {
		int tmp = 0;
		int l = board.length;
		for (int i = 0; i < l; i++) {
			if(board[i] == 0) { zero = i; }
			else {
				tmp += mh(board[i], i);
			}
		}

		int conflict = 0;
		for (int j = 0; j < Solver.size; j++) { 
			conflict += linearConflict(board, 0, j);
			conflict += linearConflict(board, 1, j);
		}
		tmp += (2*conflict);
		return tmp;
	}

	private static boolean VeryBadlinearConflict(int[] board, int s) { return (board[s] != 0) && (s/Solver.size == (board[s]-1)/Solver.size || s%Solver.size == (board[s]-1)%Solver.size ) && ( s+1 != board[s] ) && ( s+1 == board[((board[s]-1)/Solver.size)*Solver.size+((board[s]-1)%Solver.size)]); }
	private static int mh(int tile, int index) { return Math.abs(index/Solver.size - (tile-1)/Solver.size) + Math.abs(index%Solver.size - (tile-1)%Solver.size); };
	private int getPriority() { return move + manhattan; }
	
	public int compareTo(Board o) {	 return this.getPriority() - o.getPriority(); }
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i : board) {
			sb.append(i);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private Board move(int xIndex, int yIndex) {
		final int[] zArray = {(zero/Solver.size),(zero%Solver.size)};
		final int tile = (zArray[0]+xIndex)*Solver.size+(zArray[1]+yIndex);

		if(((zArray[0]+xIndex)<0 || (zArray[0]+xIndex)>=Solver.size) || ((zArray[1]+yIndex)<0 || (zArray[1]+yIndex)>=Solver.size)) return null;

		int[] tmpTiles = new int[board.length];
		System.arraycopy(board, 0, tmpTiles, 0, board.length);
		int newManhattan = manhattan;

		newManhattan -= mh(tmpTiles[tile], tile);
        //if (VeryBadlinearConflict(tmpTiles, tile)) newManhattan -= 2;
		/*for (int i = 0; i < Solver.size; i++) {
			newManhattan -= 2*linearConflict(tmpTiles, 0, i);
			newManhattan -= 2*linearConflict(tmpTiles, 1, i);
		}*/
		newManhattan -= 2*linearConflict(tmpTiles, 0, tile/Solver.size);
		newManhattan -= 2*linearConflict(tmpTiles, 1, tile%Solver.size);

		tmpTiles[zero] = tmpTiles[tile];
		tmpTiles[tile] = 0;
		
		newManhattan += mh(tmpTiles[zero], zero);
        //if (VeryBadlinearConflict(tmpTiles, zero)) newManhattan += 2;
		/*for (int i = 0; i < Solver.size; i++) {
			newManhattan += 2*linearConflict(tmpTiles, 0, i);
			newManhattan += 2*linearConflict(tmpTiles, 1, i);
		}*/
		newManhattan += 2*linearConflict(tmpTiles, 0, zArray[0]);
		newManhattan += 2*linearConflict(tmpTiles, 1, zArray[1]);

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