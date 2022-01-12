public class Board {

    private int[][] tiles;

    private int zero;
    private int fatherZero;
    private int hCost;
    private int gCost;
    
    private String toString;
    private String father;

    /**
     * Costruttore per trasformare una String in oggetto di tipo Board
     * Chiamato solo dalla radice (tavola da risolvere), per le altre chiamate viene usato il costruttore privato
     * Calcola in automatico il toString (copia dalla stringa passata come parametro), il manhattan + linear Conflict (hCost) e la posizione
     * della cella vuota (zero)
     * 
     * Complessità di O(n^2)
     * 
     * @param inTiles La stringa da trasformare in Board
     */
    public Board(String inTiles) {

        toString = inTiles;

        final String[] values = inTiles.split("\\s+");

        tiles = new int[Solver.n][Solver.n];
        hCost = 0;

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            tiles[i][j] = Integer.parseInt(values[i*Solver.n+j]);

            if (tiles[i][j] != 0)
                hCost +=  manhattan(tiles, i, j);  //manhattan
            else
                zero = i*Solver.n+j;
            
        }
        
        //Il linear conflict può essere usato solo a matrice completa
        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) if (linearConflict(tiles, i, j)) hCost++;  //linear conflict
        
        gCost = 0;
        father = null;
        fatherZero = -1;
        
    }
    
    /**
     * Costruttore per trasformare la matrice della mossa precedente int[][] in oggetto di tipo Board modificando opportunamente i vari valori
     * Chiamato dal metodo nearby(), effettua tutti gli scambi e controlli necessari per generare una delle disposizioni vicini alla Board chiamante
     * 
     * Complessità di O(n^2)
     * 
     * @param inTiles La matrice da ricopiare (deep copy)
     * @param z Le coordinate dello zero nemma disposizione chiamante
     * @param nIn Le coordinate della cella da cambiare con lo zero
     * @param h Il manhattan della Board chiamante
     * @param g Il numero di passi della Board chiamante
     * @param f La rappresentazione a stringa della Board chiamante
     * @param fZero La posizione dello zero nella Board chiamante
     */
    private Board(int[][] inTiles, int[] z, int[] nIn, int h, int g, String f, int fZero) {

        StringBuilder strBuild = new StringBuilder();
        tiles = new int[Solver.n][Solver.n];

        hCost = h;

        //Rimuovo il contributo all'euristica dato dalla cella che viene spostata
        hCost -= manhattan(inTiles, nIn[0], nIn[1]);
        if (linearConflict(inTiles, nIn[0], nIn[1])) hCost -= 2;

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            if (z[0] == i && z[1] == j)
                tiles[i][j] = inTiles[nIn[0]][nIn[1]];
            else if (nIn[0] == i && nIn[1] == j)
                tiles[i][j] = 0;
            else
                tiles[i][j] = inTiles[i][j];
            
            strBuild.append(tiles[i][j]);
            strBuild.append(" ");

            if (tiles[i][j] == 0)
                zero = i*Solver.n+j;
                
        }

        //Aggiungo il contributo all'euristica dato dalla cella spostata
        hCost += manhattan(tiles, z[0], z[1]);
        if (linearConflict(tiles, z[0], z[1])) hCost += 2;
        
        zero = nIn[0] * Solver.n + nIn[1];
        gCost = g + 1;
        toString = strBuild.toString();
        father = f;
        fatherZero = fZero;

    }

    /**
     * Metodo chiamato per generare le tavole che rappresentano le possibili mosse successive alla tavola chiamante.
     * Esegue un controllo in modo da evitare che si raggiunga una posizione impossibile (out of bound) e che nessuna
     * delle possibili mosse sia la posizione da cui proviene la Board chiamante.
     * 
     * Genera le possibili posizioni raggiungibili copiando la posizione attuale, e facendo scambi accurati
     * tra la cella vuota e una delle celle adiacenti, aggiornando tutte le variabili interne sensibili a questi cambiamenti.
     * 
     * Complessità di O(n^2) ereditata dal costruttore privato Board()
     * 
     * @return Un array contenente le possibili mosse raggiungibili dalla Board chiamante
     */
    public Board[] nearby() {
        
        final int[] z = {(zero/Solver.n), (zero%Solver.n)};
        final int[][] off = {{(z[0]-1),z[1]},{z[0],(z[1]-1)},{(z[0]+1),z[1]},{z[0],(z[1]+1)}};

        Board[] ret = new Board[4];
        byte counter = 0;

        for (byte i = 0; i < 4; i++) {

            if (off[i][0] >= 0 && off[i][0] < Solver.n && off[i][1] >= 0 && off[i][1] < Solver.n && off[i][0]*Solver.n+off[i][1] != fatherZero)
                ret[counter++] = new Board(tiles, z, off[i], hCost, gCost, toString, zero);

        }

        for (byte i = counter; i < 4; i++) ret[i] = null;

        return ret;

    }

    /**
     * Metodo chiamato per accedere al "costo" di una Board.
     * Il costo consiste nella somma tra l'euristica (stima del numero di passi mancanti alla soluzione) sommato al numero
     * di passi fatti fin'ora.
     * 
     * Complessità costante
     * 
     * @return La somma tra l'euristica e il numero di passi compiuti
     */
    public int fCost() {

        return hCost + gCost;

    }

    /**
     * Metodo chiamato per accedere all'euristica del nodo
     * 
     * Complessità costante
     * 
     * @return L'euristica del nodo
     */
    public int hCost() {

        return hCost;
    
    }

    /**
     * Metodo chiamato per accedere al numero di passi compiuti
     * 
     * Complessità costante
     * 
     * @return Il numero di passi compiuti
     */
    public int gCost() {
        
        return gCost;
    
    }

    /**
     * Metodo chiamato per accedere alla rappresentazione a stringa della posizione precedente alla chiamante
     * 
     * Complessità costante
     * 
     * @return La rappresentazione a stringa della posizione precedente alla chiamante
     */
    public String father() {
        
        return father;
    
    }

    /**
     * Metodo chiamato per accedere alla rappresentazione a stringa della Board
     * 
     * Complessità costante
     * 
     * @return La rappresentazione a stringa della Board
     */
    public String toString() {
        
        return toString;
    
    }

    /**
     * Metodo chiamato per calcolare la manhattan distance della cella nella posizione (i, j)
     * 
     * Complessità costante
     * 
     * @param inTiles La matrice di riferimento
     * @param i Indice di riga della cella
     * @param j Indice di colonna della cella
     * @return La manhattan distance della cella in posizione (i, j)
     */
    private static int manhattan(int[][] inTiles, int i, int j) {
        
        return Math.abs(i - (inTiles[i][j]-1)/Solver.n) + Math.abs(j - (inTiles[i][j]-1)%Solver.n);
    
    }

    /**
     * Metodo chiamato per determinare se la cella nella posizione (i, j) genera un conflitto lineare
     * 
     * Complessità costante
     * 
     * @param inTiles La matrice di riferimento
     * @param i Indice di riga della cella
     * @param j Indice di colonna della cella
     * @return True se avviene un conflitto lineare, false altrimenti
     */
    private static boolean linearConflict(int[][] inTiles, int i, int j) {

        //  ( inTiles[i][j] != 0 );                                                                     Controllo che l'eventuale conflitto non sia con la cella vuota
        //  ( i == (inTiles[i][j]-1)/Solver.n || j == (inTiles[i][j]-1)%Solver.n );                     Controllo se l'eventuale conflitto avviene sulla stessa riga / colonna
        //  ( i*Solver.n+j+1 != inTiles[i][j] );                                                        Controllo che la cella non sia già in posizione
        //  ( i*Solver.n+j+1 == inTiles[(inTiles[i][j]-1)/Solver.n][(inTiles[i][j]-1)%Solver.n] );      Controllo se è presente il conflitto

        return ( inTiles[i][j] != 0 ) && ( i == (inTiles[i][j]-1)/Solver.n || j == (inTiles[i][j]-1)%Solver.n ) && ( i*Solver.n+j+1 != inTiles[i][j] ) && ( i*Solver.n+j+1 == inTiles[(inTiles[i][j]-1)/Solver.n][(inTiles[i][j]-1)%Solver.n] );
    
    }

}