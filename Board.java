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
     * Calcola in automatico il toString (copia dalla stringa passata come parametro), il manhattan (hCost) e la posizione
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
            else
                hCost +=  Math.abs(i - (tiles[i][j]-1)/Solver.n) + Math.abs(j - (tiles[i][j]-1)%Solver.n);
            
        }
        
    }

    /**
     * Costruttore per trasformare una int[][] in oggetto di tipo Board
     * Chiamato solo dal metodo nearby(), per questo i calcoli che erano automatici nel costruttore pubblici
     * sono lasciati al metodo nearby()
     * Calcola la posizione della cella vuota, valore necessario per il metodo switcher(..), chiamato subito dopo la costruzione
     * della Board nel metodo nearby
     * 
     * Complessità di O(n^2)
     * 
     * @param inTiles
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
     * Metodo chiamato solo una volta dalla radice per impostare alcune variabili statiche globali.
     * 
     * Complessità di O(n^2) ereditata dai metodi Solver.setGoalBoard() e Solver.setNullBoard() 
     */
    public void root() {

        Solver.setGoalBoard();
        
        setCost(0, hCost);
        father = Solver.setNullBoard();
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
     * Complessità di O(n^2) ereditata dal costruttore privato Board(.) e dal metodo CalculateString()
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
                switches[counter++] = off[i];
        
        }

        Board[] ret = new Board[counter];

        for (byte i = 0; i < counter; i++) {

            ret[i] = new Board(tiles);
            ret[i].switcher(z, switches[i], hCost);
            ret[i].calculateString();
            ret[i].setFather(toString);
            ret[i].setFatherZero(zero);
            ret[i].setGCost(gCost + 1);
            
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
    public int fCost() { return hCost + gCost; }

    /**
     * Metodo chiamato per accedere al numero di passi compiuti
     * 
     * Complessità costante
     * 
     * @return Il numero di passi compiuti
     */
    public int gCost() { return gCost; }

    /**
     * Metodo chiamato per accedere alla rappresentazione a stringa della posizione precedente alla chiamante
     * 
     * Complessità costante
     * 
     * @return La rappresentazione a stringa della posizione precedente alla chiamante
     */
    public String father() { return father; }

    /**
     * Metodo chiamato per accedere alla rappresentazione a stringa della Board
     * 
     * Complessità costante
     * 
     * @return La rappresentazione a stringa della Board
     */
    public String toString() { return toString; }
    
    /**
     * Metodo chiamato per impostare salvare l'euristica e il numero di passi eseguiti calcolando il costo della Boar
     * 
     * Complessità costante
     * 
     * @param g Il numero di passi eseguiti
     * @param h L'euristica della Board
     * @return Il costo del nodo
     */
    private int setCost(int g, int h) {

        gCost = g;
        hCost = h;
        return fCost();

    }
    
    /**
     * Metodo chiamato per impostare salvare il numero di passi eseguiti
     * 
     * Complessità costante
     * 
     * @param g Il numero di passi eseguiti
     * @return Il costo del nodo
     */
    private void setGCost(int g) {

        gCost = g;

    }

    /**
     * Metodo chiamato per impostare la rappresentazione a stringa della posizione precedente alla Board chiamante
     * 
     * Complessità costante
     * 
     * @param f La rappresentazione a stringa della posizione precedente alla Board chiamante
     */
    private void setFather(String f) { father = f; }

    /**
     * Metodo chiamato per importare la posizione della cella vuota nella posizione precedente alla Board chiamante
     * 
     * Complessità costante
     * 
     * @param z La posizione della cella vuota nella posizione precedente alla Board chiamante
     */
    private void setFatherZero(int z) { fatherZero = z; }
    
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
     * @param n_in Posizione attuale della cella da sostituire alla cella vuota
     * @param h Valore dell'euristica della Board precedente
     */
    private void switcher(int[] z, int[] n_in, int h){

        hCost = h;

        hCost -=  Math.abs(n_in[0] - (tiles[n_in[0]][n_in[1]]-1)/Solver.n) + Math.abs(n_in[1] - (tiles[n_in[0]][n_in[1]]-1)%Solver.n);

        tiles[z[0]][z[1]] = tiles[n_in[0]][n_in[1]];
        tiles[n_in[0]][n_in[1]] = 0;

        hCost +=  Math.abs(z[0] - (tiles[z[0]][z[1]]-1)/Solver.n) + Math.abs(z[1] - (tiles[z[0]][z[1]]-1)%Solver.n);

        zero = n_in[0] * Solver.n + n_in[1];

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