import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Comparator;


public class Solver{

    public static class BDComparator implements Comparator <Board>{

        public int compare(Board o1, Board o2) {

            return o1.fcost()-o2.fcost();
        }

    }
    public static void main(String args[]) throws IOException{

        long start = System.nanoTime();

        if(args.length <= 0) System.exit(1);
        
        FileReader file = new FileReader(args[0]);
        BufferedReader in = new BufferedReader(file);
        int n = Integer.parseInt(in.readLine());
        String line = in.readLine();
        in.close();
        file.close();
        PriorityQueue<Board> cg = new PriorityQueue<>(new BDComparator());
        int[] f = {-1, -1};
        Board bd = new Board(line, f);
        cg.add(bd);
        while(cg.peek().manhattan() != 0) {
            Board[] nearb = bd.getBoards();
            for (int i = 0; i < nearb.length; i++){
                if (nearb[i] == null) break;
                nearb[i].setgcost(bd.getgcost() + 1);
                cg.add(nearb[i]);
            }
            bd = cg.poll();
            
        }

        bd = cg.poll();

        System.out.println(bd.getgcost());

        long end = System.nanoTime();

        System.out.println((double)(end - start)/1000000000l + " seconds");
       

    }
}