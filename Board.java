public class Board {

    private int[][] tiles;

    private int zero;
    private int fatherZero;
    private int hCost;
    private int gCost;
    
    private String toString;
    private String father;

    public Board(String inTiles) {

        toString = inTiles;

        final String[] values = inTiles.split("\\s+");

        tiles = new int[Solver.n][Solver.n];
        hCost = 0;

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            tiles[i][j] = Integer.parseInt(values[i*Solver.n+j]);

            if (tiles[i][j] == 0)
                zero = i*Solver.n+j;
            else
                hCost +=  Math.abs(i - (tiles[i][j]-1)/Solver.n) + Math.abs(j - (tiles[i][j]-1)%Solver.n);
            
        }
        
    }

    private Board(int[][] inTiles) {

        tiles = new int[Solver.n][Solver.n];

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            tiles[i][j] = inTiles[i][j];

            if (tiles[i][j] == 0)
                zero = i*Solver.n+j;
                
        }

    }

    public void root() {

        Solver.setGoalBoard();
        
        setCost(0, hCost);
        father = Solver.setNullBoard();
        fatherZero = -1;

    }

    public Board[] children() {

        byte counter = 0;
        int n1;
        int n2;
        final int[] z = {(zero/Solver.n), (zero%Solver.n)};
        final int[][] off = {{(z[0]-1),z[1]},{z[0],(z[1]-1)},{(z[0]+1),z[1]},{z[0],(z[1]+1)}};
        int[][] switches = new int[4][2];

        for (int i = 0; i < 4; i++) {

            n1 = off[i][0];
            n2 = off[i][1];

            if (n1 >= 0 && n1 < Solver.n && n2 >= 0 && n2 < Solver.n && n1*Solver.n+n2 != fatherZero)
                switches[counter++] = off[i];
        
        }

        Board[] ret = new Board[counter];

        for (byte i = 0; i < counter; i++) {

            ret[i] = new Board(tiles);
            ret[i].switcher(z, switches[i], hCost);
            ret[i].calculateString();
            ret[i].setFather(toString);
            ret[i].setFatherZero(zero);
            ret[i].setCost(gCost + 1, ret[i].hCost());
            
        }
        
        return ret;

    }

    public int fCost() { return hCost + gCost; }

    public int gCost() { return gCost; }

    public int hCost() { return hCost; }

    public int[][] tiles() { return tiles; }

    public String father() { return father; }

    public String toString() { return toString; }
    
    private int setCost(int g, int h) {

        gCost = g;
        return gCost + h;

    }

    private void setFather(String f) { father = f; }

    private void setFatherZero(int z) { fatherZero = z; }
    
    private void switcher(int[] z, int[] n_in, int h){

        hCost = h;

        hCost -=  Math.abs(n_in[0] - (tiles[n_in[0]][n_in[1]]-1)/Solver.n) + Math.abs(n_in[1] - (tiles[n_in[0]][n_in[1]]-1)%Solver.n);

        tiles[z[0]][z[1]] = tiles[n_in[0]][n_in[1]];
        tiles[n_in[0]][n_in[1]] = 0;

        hCost +=  Math.abs(z[0] - (tiles[z[0]][z[1]]-1)/Solver.n) + Math.abs(z[1] - (tiles[z[0]][z[1]]-1)%Solver.n);

        zero = n_in[0] * Solver.n + n_in[1];

    }

    private void calculateString() {

        StringBuilder strBuild = new StringBuilder();

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            strBuild.append(tiles[i][j]);
            strBuild.append(" ");

        }

        toString = strBuild.toString();

    }

}