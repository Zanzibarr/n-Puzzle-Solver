public class Board {
    private int[][] matrix;
    private int row0, col0;
    private int gCost;
    private Board parent;
    private int hCost;
    private String toString;

    public Board(int[][] tiles, int m, Board p) {
        gCost = m;
        parent = p;
        toString = "";
        hCost = 0;
        matrix = new int[Solver.n][Solver.n];

        for (int i = 0; i < Solver.n; i++) { 
            for (int j = 0; j < Solver.n; j++) {
                matrix[i][j] = tiles[i][j];
                toString += tiles[i][j] + " ";
                if(tiles[i][j] == 0) {
                    row0 = i;
                    col0 = j;
                }
            } 
        }
        toString = toString.trim();
    }

    public Board(String tiles, Board avoid_error) {
        gCost = 0;
        toString = tiles;
        matrix = new int[Solver.n][Solver.n];
        parent = avoid_error;
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
        //matrix[i][j] != 0                                                                     se c'è lo 0 non faccio il controllo
        //(i == (matrix[i][j] -1) / Solver.n || j == (matrix[i][j] -1) % Solver.n)              elemento è alla riga o colonna giusta
        //(matrix[i][j] != i*Solver.n + j+1)                                                    elemento non deve essere nella sua posizione giusta
        //i*Solver.n +j+1 = matrix[(matrix[i][j] -1) / Solver.n][(matrix[i][j] -1) % Solver.n]  controllo che nella posizione dove dovrei andare c'è l'elemento che dovrebbe andare nella mia posizione
        
        return ( inTiles[i][j] != 0 ) && ( i == (inTiles[i][j]-1)/Solver.n || j == (inTiles[i][j]-1)%Solver.n ) && ( i*Solver.n+j+1 != inTiles[i][j] ) && ( i*Solver.n+j+1 == inTiles[(inTiles[i][j]-1)/Solver.n][(inTiles[i][j]-1)%Solver.n] );
    }

    public void updateString(int[][] m) {
        toString = "";
        for (int i = 0; i < Solver.n; i++) { 
            for (int j = 0; j < Solver.n; j++) {
                toString += m[i][j] + " ";
            } 
        }
        toString = toString.trim();
    }

    public int priority() { return hCost + gCost; }

    public void swap0(int row, int col) {
        hCost = getParent().getHCost();
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
        byte count = 0;
        Board b1 = null;
        Board b2 = null;
        Board b3 = null;
        Board b4 = null;
        if(row0 - 1 >= 0) { 
            b1 = new Board(matrix, gCost + 1, this);
            b1.swap0(row0 - 1, col0);
            if(!parent.getString().equals(b1.getString())) { 
                count++;
            } 
            else b1 = null;
        }
        if(row0 + 1 < Solver.n) {
            b2 = new Board(matrix, gCost + 1, this);
            b2.swap0(row0 + 1, col0);
            if(!parent.getString().equals(b2.getString())) {
               count++;
            }
            else b2 = null;
        }
        if(col0 + 1 < Solver.n) {
            b3 = new Board(matrix, gCost + 1, this);
            b3.swap0(row0, col0 + 1);
            if(!parent.getString().equals(b3.getString())) {
                count++;
            }
            else b3 = null;
        }
        if(col0 - 1 >= 0){
            b4 = new Board(matrix, gCost + 1, this);
            b4.swap0(row0, col0 - 1);
            if(!parent.getString().equals(b4.getString())) {
                count++;
            }
            else b4 = null;
        }
        Board[] figli = new Board[count];
        byte i = 0;
        if(b1 != null) {
            figli[i++] = b1;
        }
        if(b2 != null) {
            figli[i++] = b2;
        }
        if(b3 != null) {
            figli[i++] = b3;
        }
        if(b4 != null) {
            figli[i++] = b4;
        }
        return figli;
    }

    public int getMoves() { return gCost; }
    public int getHCost() { return hCost; }
    public Board getParent() { return parent; }
    public String getString() { return toString; }
}