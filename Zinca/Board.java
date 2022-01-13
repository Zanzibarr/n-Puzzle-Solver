public class Board {
    private int[][] matrix;
    private int row0, col0;

    private int gCost;
    private int hCost;

    private String parent;
    private String toString;

    public Board(int[][] tiles, int mov, int hCostParent, String par) { //costructor for sons
        gCost = mov;
        parent = par;
        hCost = hCostParent; //updated later
        toString = ""; //updated later

        matrix = new int[Solver.n][Solver.n];
        for (int i = 0; i < Solver.n; i++) { 
            for (int j = 0; j < Solver.n; j++) {
                matrix[i][j] = tiles[i][j];
                if(tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
            } 
        }
    }

    public Board(String tiles) { //first constructor
        gCost = 0;
        toString = tiles;
        matrix = new int[Solver.n][Solver.n];
        parent = " ";
        hCost = 0;

        int k = 0;
        String[] insertion = tiles.split(" ");
        for (int i = 0; i < Solver.n; i++) { 
            for (int j = 0; j < Solver.n; j++) {
                matrix[i][j] = Integer.parseInt(insertion[k++]);
                if(matrix[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
                else  hCost += manhattan(matrix, i, j);    
            } 
        }
       
        for (int i = 0; i < Solver.n; i++) {
            for (int j = 0; j < Solver.n; j++) {
                if(linearConflict(matrix, i , j)) hCost++;
            }
        }
    }

    public static int manhattan(int[][] tiles, int i, int j) {
        return Math.abs(((tiles[i][j] - 1) / Solver.n) - i) + Math.abs(((tiles[i][j] - 1) % Solver.n) - j);
    }

    public static boolean linearConflict(int[][] inTiles, int i, int j) {                    
        return ( inTiles[i][j] != 0 ) && ( i == (inTiles[i][j]-1)/Solver.n || j == (inTiles[i][j]-1)%Solver.n ) && ( i*Solver.n+j+1 != inTiles[i][j] ) && ( i*Solver.n+j+1 == inTiles[(inTiles[i][j]-1)/Solver.n][(inTiles[i][j]-1)%Solver.n] );
    }

    public void updateString(int[][] m) {
        StringBuilder strBuild = new StringBuilder();
        for (int i = 0; i < Solver.n; i++) { 
            for (int j = 0; j < Solver.n; j++) {
                strBuild.append(m[i][j]);
                strBuild.append(" ");
            } 
        }
        toString = strBuild.toString();
        toString = toString.trim();
    }

    public int priority() { return hCost + gCost; }

    public void swap0(int row, int col) {
        int temp = matrix[row][col];
        if(linearConflict(matrix, row, col)) hCost -= 2;
        hCost -= manhattan(matrix, row, col);
        matrix[row][col] = 0;
        matrix[row0][col0] = temp;

        hCost += manhattan(matrix, row0, col0);
        if(linearConflict(matrix, row0, col0)) hCost += 2;
        row0 = row;
        col0 = col;     
        updateString(matrix);
    }
    
    public Board[] generateSons() {
        byte i = 0;
        Board[] figli = new Board[4];

        if(row0 - 1 >= 0) { 
            Board b1 = new Board(matrix, gCost + 1, getHCost(), getString());
            b1.swap0(row0 - 1, col0);
            if(!parent.equals(b1.getString())) { 
                figli[i++] = b1;
            } 
        }
        if(row0 + 1 < Solver.n) {
            Board b2 = new Board(matrix, gCost + 1, getHCost(), getString());
            b2.swap0(row0 + 1, col0);
            if(!parent.equals(b2.getString())) {
                figli[i++] = b2;
            }
        }
        if(col0 + 1 < Solver.n) {
            Board b3 = new Board(matrix, gCost + 1, getHCost(), getString());
            b3.swap0(row0, col0 + 1);
            if(!parent.equals(b3.getString())) {
                figli[i++] = b3;
            }
        }
        if(col0 - 1 >= 0){
            Board b4 = new Board(matrix, gCost + 1, getHCost(), getString());
            b4.swap0(row0, col0 - 1);
            if(!parent.equals(b4.getString())) {
                figli[i++] = b4;
            }
        }
        return figli;
    }

    public int getMoves() { return gCost; }
    public int getHCost() { return hCost; }
    public String getParent() { return parent; }
    public String getString() { return toString; }
}