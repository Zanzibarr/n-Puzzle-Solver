import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;

public class Solver {

    public static String goal = "";
    public static int n = 0;
    
    private static class BoardComparator implements Comparator<Board> {
        public int compare(Board b1, Board b2) { return b1.priority() - b2.priority(); }
    }

    public static void generateGoal() {

        StringBuilder goalBuilder = new StringBuilder();
        for(int i = 1; i < n*n; i++) {
            goalBuilder.append(i);
            goalBuilder.append(" ");
        }
        goalBuilder.append("0");
        goal = goalBuilder.toString();
    }

    public static boolean isGoal(Board gameNode) { 
        return gameNode.getString().equals(goal);
    }

    public static void main(String[] args)  throws IOException {

        if(args.length < 1) {
            System.out.println("Missing Input File.");
            System.exit(1);
        }

        long start = System.nanoTime();

        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        n = Integer.parseInt(in.readLine());
        String first = in.readLine().trim();
        in.close();
        generateGoal();
    
        Board root = new Board(first);

        PriorityQueue<Board> q = new PriorityQueue<Board>(new BoardComparator());
        HashMap<String, String> visited = new HashMap<String, String>();
        
        while(!isGoal(root)) {
            Board[] sons = root.generateSons();
            for(int i = 0; i < sons.length; i++) {
                if(sons[i] != null && !visited.containsKey(sons[i].getString())) {
                    q.add(sons[i]);
                }
            }
            if(!visited.containsKey(root.getString()))
                visited.put(root.getString(), root.getParent());
            root = q.poll();
        }
        visited.put(root.getString(), root.getParent());

        
        int mosse = root.getMoves();
        System.out.println(mosse);
        String[] path = new String[mosse + 1];
        String sus = root.getString();

        for (int i = path.length - 1; i >= 0; i--) {
            path[i] = sus;
            sus = visited.get(sus);
        }
        for (int i = 0; i < path.length; i++) {
            System.out.println(path[i]);
        }
        
        long end = System.nanoTime();
        System.out.println((double)(end - start)/1000000000l);
        
    }
}