public class Board {

    private int[][] board;
    private int h;
    private int g;
    private int d;
    private int z;
    private int[] f;

    public Board(String line, int[] father){
        String[] array = line.split("\\s+");
        d = (int)Math.sqrt(array.length);
        board = new int[d][d];
        for (int i = 0; i < array.length; i++){
            board[i/d][i%d] = Integer.parseInt(array[i]); 
        }
        h = manhattan();
        f = father;
        g = 0;
    }

    public int manhattan(){
        int temp = 0;
        for(int i = 0; i < d; i++){
            for(int j = 0; j < d; j++){
                if(board[i][j]!=0){
                    temp+=Math.abs(i-(board[i][j]-1)/d)+Math.abs(j-(board[i][j]-1)%d);
                } 
                else z = i*d+j;
            }
        }
        return temp;
    }   

    public int fcost(){
        return g + h;
    }

    public void setgcost(int t){
        g = t;
    }

    public int getgcost(){return g;}
    public Board[] getBoards(){

        int qw = z/d;
        int op = z%d;
        int[][] possible = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        Board[] ret = new Board[4];

        int counter = 0;

        for (int i = 0; i < 4; i++) {

            if (qw + possible[i][0] < 0 || qw + possible[i][0] >= d || op + possible[i][1] < 0 || op + possible[i][1] >= d) continue;
            if (qw + possible[i][0] == f[0] && op + possible[i][1] == f[1]) continue;

            int[][] btemp = new int[d][d];
            for (int iCounter = 0; iCounter<d; iCounter++){
                for(int j = 0; j<d; j++){
                    btemp[iCounter][j]= board[iCounter][j];
                }
            }
            btemp[qw][op] = btemp[qw+possible[i][0]][op+possible[i][1]];
            btemp[qw+possible[i][0]][op+possible[i][1]] = 0;

            String out = Costr(btemp); 
            int[] fa = {qw, op};
            ret[counter++] = new Board(out, fa);

        }

        for (int i = counter; i < 4; i++) ret[i] = null;
        
        return ret;
        
    }

    public String Costr(int[][] xx){
        String tempo = "";
        for (int i = 0; i<d; i++){
            for(int j = 0; j<d; j++){

                tempo += xx[i][j] + " ";
            }
        }
        return tempo;
    }
}