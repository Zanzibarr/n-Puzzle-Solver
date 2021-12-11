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

            if (tiles[i][j] == 0)
                zero = i*Solver.n+j;
            else {
                hCost +=  manhattan(i, j);  //manhattan
                if (linearConflict(i, j)) hCost++;  //linear conflict
            }
            
        }
        
    }

    /**
     * Costruttore per trasformare una int[][] in oggetto di tipo Board
     * Chiamato solo dal metodo nearby(), per questo i calcoli che erano automatici nel costruttore pubblici
     * sono lasciati al metodo nearby()
     * Calcola la posizione della cella vuota, valore necessario per il metodo switcher(), chiamato subito dopo la costruzione
     * della Board nel metodo nearby
     * 
     * Complessità di O(n^2)
     * 
     * @param inTiles La matrice da ricopiare (deep copy)
     */
    private Board(int[][] inTiles) {

        tiles = new int[Solver.n][Solver.n];

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            tiles[i][j] = inTiles[i][j];

            if (tiles[i][j] == 0)
                zero = i*Solver.n+j;
                
        }

    }

    /**
     * Metodo chiamato solo una volta dalla radice per impostare alcune variabili locali della radice.
     * 
     * Complessità costante
     */
    public void root() {
        
        gCost = 0;
        father = "0";
        fatherZero = -1;

    }

    /**
     * Metodo chiamato per generare le tavole che rappresentano le possibili mosse successive alla tavola chiamante.
     * Esegue un controllo in modo da evitare che si raggiunga una posizione impossibile (out of bound) e che nessuna
     * delle possibili mosse sia la posizione da cui proviene la Board chiamante.
     * 
     * Genera le possibili posizioni raggiungibili copiando la posizione attuale, e facendo scambi accurati
     * tra la cella vuota e una delle celle adiacenti, aggiornando tutte le variabili interne sensibili a questi cambiamenti.
     * 
     * Complessità di O(n^2) ereditata dal costruttore privato Board() e dal metodo CalculateString()
     * 
     * @return Un array contenente le possibili mosse raggiungibili dalla Board chiamante
     */
    public Board[] nearby() {

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
                switches[counter++] = off[i];   //salvo solo le posizioni accettabili
        
        }

        Board[] ret = new Board[counter];

        for (byte i = 0; i < counter; i++) {    //genero le tavole in base alle posizioni accettabili

            ret[i] = new Board(tiles);
            ret[i].switcher(z, switches[i], hCost, gCost);
            ret[i].calculateString();
            ret[i].setFather(toString);
            ret[i].setFatherZero(zero);
            
        }
        
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
     * @param i Indice di riga della cella
     * @param j Indice di colonna della cella
     * @return La manhattan distance della cella in posizione (i, j)
     */
    private int manhattan(int i, int j) {
        
        return Math.abs(i - (tiles[i][j]-1)/Solver.n) + Math.abs(j - (tiles[i][j]-1)%Solver.n);
    
    }

    /**
     * Metodo chiamato per determinare se la cella nella posizione (i, j) genera un conflitto lineare
     * 
     * Complessità costante
     * 
     * @param i Indice di riga della cella
     * @param j Indice di colonna della cella
     * @return True se avviene un conflitto lineare, false altrimenti
     */
    private boolean linearConflict(int i, int j) {

        //  ( i == (tiles[i][j]-1)/Solver.n || j == (tiles[i][j]-1)%Solver.n );                 Controllo se l'eventuale conflitto avviene sulla stessa riga / colonna
        //  ( i*Solver.n+j+1 != tiles[i][j] );                                                  Controllo che la cella non sia già in posizione
        //  ( i*Solver.n+j+1 == tiles[(tiles[i][j]-1)/Solver.n][(tiles[i][j]-1)%Solver.n] );    Controllo se è presente il conflitto

        return ( i == (tiles[i][j]-1)/Solver.n || j == (tiles[i][j]-1)%Solver.n ) && ( i*Solver.n+j+1 != tiles[i][j] ) && ( i*Solver.n+j+1 == tiles[(tiles[i][j]-1)/Solver.n][(tiles[i][j]-1)%Solver.n] );
    
    }

    /**
     * Metodo chiamato per impostare la rappresentazione a stringa della posizione precedente alla Board chiamante
     * 
     * Complessità costante
     * 
     * @param f La rappresentazione a stringa della posizione precedente alla Board chiamante
     */
    private void setFather(String f) {
        
        father = f;
    
    }

    /**
     * Metodo chiamato per importare la posizione della cella vuota nella posizione precedente alla Board chiamante
     * 
     * Complessità costante
     * 
     * @param z La posizione della cella vuota nella posizione precedente alla Board chiamante
     */
    private void setFatherZero(int z) {
        
        fatherZero = z;
    
    }
    
    /**
     * Metodo chiamato per creare una nuova matrice partendo dalla Board precedente.
     * Metodo chiamato dal metodo nearby() che genera una Board identica alla posizione precedente, per poi
     * modellarla in questo metodo.
     * Esegue scambi accurati tra la cella vuota e una delle celle adiacenti, aggiornando tutte le variabili
     * interne sensibili a questi cambiamenti.
     * 
     * Complessità costante
     * 
     * @param z Posizione attuale della cella vuota
     * @param nIn Posizione attuale della cella da sostituire alla cella vuota
     * @param h Valore dell'euristica della Board precedente
     */
    private void switcher(int[] z, int[] nIn, int h, int g){

        hCost = h;

        //Rimuovo il contributo all'euristica dato dalla cella che viene spostata
        hCost -=  manhattan(nIn[0], nIn[1]);
        if (linearConflict(nIn[0], nIn[1])) hCost -= 2;
        
        tiles[z[0]][z[1]] = tiles[nIn[0]][nIn[1]];
        tiles[nIn[0]][nIn[1]] = 0;

        //Aggiungo il contributo all'euristica dato dalla cella spostata
        hCost +=  manhattan(z[0], z[1]);
        if (linearConflict(z[0], z[1])) hCost += 2;
        
        zero = nIn[0] * Solver.n + nIn[1];
        gCost = g + 1;

    }

    /**
     * Metodo chiamato per creare la rappresentazione a stringa della Board
     * 
     * Complessità di O(n^2)
     */
    private void calculateString() {

        StringBuilder strBuild = new StringBuilder();

        for (int i = 0; i < Solver.n; i++) for (int j = 0; j < Solver.n; j++) {

            strBuild.append(tiles[i][j]);
            strBuild.append(" ");

        }

        toString = strBuild.toString();

    }

}