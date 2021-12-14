import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Solver {
    
    public static int n;

    public static class BoardComparator implements Comparator<Board> {
        public int compare(Board b1, Board b2) {
            return b1.fCost() - b2.fCost();
        }
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Missing Input File.");
            System.exit(1);
        }

        FileReader file = new FileReader(args[0]);
        BufferedReader fileReader = new BufferedReader(file);

        n = Integer.parseInt(fileReader.readLine());
        String rt = fileReader.readLine();

        fileReader.close();
        file.close();

        /**/long start = System.nanoTime();
        
        int moves = solve(rt);

        /**/long finish = System.nanoTime();
        /**/System.out.println((double)(finish - start) / 1000000000l+" seconds");

        System.out.println(moves+" moves");

    }

    public static int solve(String root) {

        final PriorityQueue<Board> nextBoards = new PriorityQueue<>(new BoardComparator());
        final HashMap<String, String> visited = new HashMap<>();

        Board board = new Board(root + " ");
        Board[] children;

        int index;
        int end;

        while (board.hCost() != 0) {

            children = board.nearby();

            end = children.length;
            for (index = 0; index < end; index++) {

                if (children[index] == null) break;

                if (!visited.containsKey(children[index].toString()))
                    nextBoards.add(children[index]);

            }

            if (!visited.containsKey(board.toString())) visited.put(board.toString(), board.father());
            board = nextBoards.poll();

        }

        if (!visited.containsKey(board.toString())) visited.put(board.toString(), board.father());

        String outBuilder = board.toString();
        int steps = board.gCost();

        String[] output = new String[steps+1];

        for (int i = steps; i >= 0; i--) {
            output[i] = outBuilder;
            outBuilder = visited.get(outBuilder);
        }

        for (int i = 0; i < steps + 1; i++) System.out.println(output[i]);
        
        return steps;

    }

}