import java.util.Arrays;

public class Board implements Comparable<Board> {
    private final int[][] tiles;
    private final int gValue, manhattan, size, cost;
    private final String stringParse;
    private Board old;
    private final int[] zpos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.size = tiles[0].length;

        zpos = search(0);
        manhattan = mFunc();
        gValue = manhattan;
        this.cost = 0;
        this.stringParse = strParse();
        this.old = null;
    }

    public Board(int[][] tiles, int newManhattan, int[] zpos, Board old, int cost, int lconfl) {
        this.tiles = tiles;
        this.size = tiles[0].length;
        this.old = old;
        this.cost = cost;

        this.zpos = zpos;
        this.manhattan = newManhattan;
        gValue = manhattan + lconfl;
        this.stringParse = strParse();

    }

    // string representation of this board
    public String toString() {
        return stringParse;
    }

    public String strParse() {
        String OutputString = "";
        for (int row = 0; row<size; row++) {
            for (int column = 0; column<size; column++) {
                OutputString = OutputString + tiles[row][column] + " ";
            }
        }
        return OutputString;
    }

    private boolean linearConflict(int[][] inTiles, int i, int j) {
        return (inTiles[i][j] != 0)
                && (i == (inTiles[i][j]-1)/size || j==(inTiles[i][j]-1)%size)
                && (i*size+j+1 != inTiles[i][j])
                && (i*size+j+1 == inTiles[(inTiles[i][j]-1)/size][(inTiles[i][j]-1)%size]);
    }

    public int[] search(int n) {
        int[] pos = new int[2];
        for (int row = 0; row<size; row++) {
            for (int column = 0; column<size; column++) {
                 if (n == tiles[row][column]) {
                     pos[0] = row;
                     pos[1] = column;
                     return pos;
                 }
            }
        }
        return null;
    }

    public int manhattan() {
        return manhattan;
    }

    public int getgValue() {
        return gValue;
    }

    public int getDistance(int n, int row, int col) {
        int correctrow = (n-1)/size;
        int correctcolumn = (n-1)%size;

        return Math.abs(correctcolumn-col)+Math.abs(correctrow-row);
    }

    private int mFunc() {
        int m = 0;
        for (int row = 0; row<size; row++) {
            for (int column = 0; column<size; column++) {
                if (tiles[row][column] != 0)
                    m += getDistance(tiles[row][column], row, column);
            }
        }
        return m;
    }


    public int getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getSize() {
        return size;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public Board[] moveVector() {
        Board[] movables = new Board[4];


        if (zpos[0] != 0) {
            int[][] newtiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

            newtiles[zpos[0]][zpos[1]] = newtiles[zpos[0]-1][zpos[1]];
            newtiles[zpos[0]-1][zpos[1]] = 0;

            int m = 0;
            m += manhattan;
            m -= getDistance(newtiles[zpos[0]][zpos[1]], zpos[0]-1, zpos[1]);
            m += getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]);

            int[] znew = {zpos[0]-1, zpos[1]};

            movables[0] = new Board(newtiles, m, znew, this, cost+1, (linearConflict(newtiles, zpos[0], zpos[1]) ? 2 : 0));

        }  //UP
        if (zpos[0] != size-1) {
            int[][] newtiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

            newtiles[zpos[0]][zpos[1]] = newtiles[zpos[0]+1][zpos[1]];
            newtiles[zpos[0]+1][zpos[1]] = 0;

            int m = 0;
            m += manhattan;
            m -= getDistance(newtiles[zpos[0]][zpos[1]], zpos[0]+1, zpos[1]);
            m += getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]);
            int[] znew = {zpos[0]+1, zpos[1]};

            movables[1] = new Board(newtiles, m, znew, this, cost+1, (linearConflict(newtiles, zpos[0], zpos[1]) ? 2 : 0));
        } //DOWN
        if (zpos[1] != 0) {
            int[][] newtiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

            newtiles[zpos[0]][zpos[1]] = newtiles[zpos[0]][zpos[1]-1];
            newtiles[zpos[0]][zpos[1]-1] = 0;

            int m = 0;
            m += manhattan;
            m -= getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]-1);
            m += getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]);
            int[] znew = {zpos[0], zpos[1]-1};

            movables[2] = new Board(newtiles, m, znew, this, cost+1, (linearConflict(newtiles, zpos[0], zpos[1]) ? 2 : 0));
        } //LEFT
        if (zpos[1] != size-1) {
            int[][] newtiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);

            newtiles[zpos[0]][zpos[1]] = newtiles[zpos[0]][zpos[1]+1];
            newtiles[zpos[0]][zpos[1]+1] = 0;

            int m = 0;
            m += manhattan;
            m -= getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]+1);
            m += getDistance(newtiles[zpos[0]][zpos[1]], zpos[0], zpos[1]);

            int[] znew = {zpos[0], zpos[1]+1};
            movables[3] = new Board(newtiles, m, znew, this, cost+1, (linearConflict(newtiles, zpos[0], zpos[1]) ? 2 : 0));
        } //RIGHT

        return movables;
    }

    @Override
    public int compareTo(Board o) {
        return (this.gValue+this.cost) - (o.gValue+o.cost);
    }

    public Board getParent() {
        return old;
    }
}