import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;

public class Solver {

    public static String goal = "";
    public static int n = 0;
    
    private static class BoardComparator implements Comparator<Board> {
        public int compare(Board b1, Board b2) { return b1.priority() - b2.priority(); }
    }

    public static void generateGoal() {
        for(int i = 1; i < n*n; i++) {
            goal += i + " ";
        }
        goal += 0; 
    }

    public static boolean isGoal(Board gameNode) { 
        return gameNode.getString().equals(goal);
    }

    public static void main(String[] args)  throws IOException {

        if(args.length < 1) {
            System.out.println("Missing Input File.");
            System.exit(1);
        }

        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        n = Integer.parseInt(in.readLine());
        String first = in.readLine().trim();
        in.close();
        generateGoal();
    
        int[][] null_matrix = new int[n][n];
        Board avoid_error = new Board(null_matrix, -1, null);
        Board root = new Board(first, avoid_error);

        PriorityQueue<Board> q = new PriorityQueue<Board>(new BoardComparator());
        HashSet<String> visited = new HashSet<String>();
        visited.add(root.getString());
        while(!isGoal(root)) {
            Board[] sons = root.generateSons();
            for(int i = 0; i < sons.length; i++) {
                if(!visited.contains(sons[i].getString())) {
                    q.add(sons[i]);
                }
            }
            visited.add(root.getString());
            root = q.poll();
        }
        
        //Printing the request, go look the proper PDF in the directory
        int mosse = root.getMoves();
        System.out.println(mosse);
        
        String[] moves = new String[mosse + 1];
        for(int i = mosse; i >= 0; i--) {
            moves[i] = root.getString();
            root = root.getParent();
        }
        for(int i = 0; i < mosse + 1; i++) {
            System.out.println(moves[i]);
        }

    }
}