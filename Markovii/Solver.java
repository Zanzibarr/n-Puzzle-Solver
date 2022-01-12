import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solver {
    public static void main(String[] args) throws FileNotFoundException {

            int[][] tiles;
            int size;

            File f = new File(args[0]);
            Scanner builder = new Scanner(f);
            size = builder.nextInt();

            tiles = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    tiles[row][column] = builder.nextInt();
                }
            }
            builder.close();
            //System.out.println(size);

            Board b = new Board(tiles);

            solve(b);
    }

    public static void solve(Board b) {
        PriorityQueue<Board> pq = new PriorityQueue<>();
        HashSet<String> hs = new HashSet<>();
        HashMap<String, String> hm = new HashMap<>();

        while (true) {
            assert b != null;
            hs.add(b.toString());
            if (b.getParent() == null) {
                hm.put(b.toString(), "0");
            } else
                hm.put(b.toString(), b.getParent().toString());

            if (b.manhattan() == 0) {
                System.out.println("Solved");
                System.out.println("Fastest way found:");
                int steps = 1;
                String line = b.toString();
                String[] ssteps = new String[100];
                int i = 0;
                while (!line.equals("0")) {
                    ssteps[i++] = line;
                    line = hm.get(line);
                }

                int counter = 0;
                for (int k = i-1; k>= 0; k--){
                    System.out.println(((counter==0 ? "Starting -> " : "    "+counter+ (counter>=10 ? "" : " ")+"   -> " )+ ssteps[k]));
                    counter++;
                }
                System.out.println("Steps required: " + --counter);

                break;
            }

            Board[] movables = b.moveVector();
            for (Board movable : movables) {
                if (movable != null) {
                    if (!hs.contains(movable.toString()))
                        pq.add(movable);
                }
            }
            b = pq.poll();
        }
    }
}