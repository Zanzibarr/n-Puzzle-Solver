import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Comparator;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class Solver {
    
    public static int n;
    private static String goalBoard;

    public static class BoardComparator implements Comparator<Board> {
        public int compare(Board b1, Board b2) {
            return b1.fCost() - b2.fCost();
        }
    }

    private static String goalBoard() {

        StringBuilder strBuild = new StringBuilder();

        for (int i = 1; i < n*n; i++) {

            strBuild.append(i);
            strBuild.append(" ");

        }

        strBuild.append("0 ");

        goalBoard = strBuild.toString();

        return goalBoard;

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

        Board board = new Board(root + " ");
        board.root();

        final PriorityQueue<Board> nextBoards = new PriorityQueue<>(new BoardComparator());
        final HashMap<String, String> visited = new HashMap<>();

        int index;
        int end;

        while (board.hCost() != 0) {

            Board[] children = board.nearby();

            end = children.length;
            for (index = 0; index < end; index++) {

                if (!visited.containsKey(children[index].toString()))
                    nextBoards.add(children[index]);

            }

            visited.put(board.toString(), board.father());
            board = nextBoards.poll();

        }

        visited.put(board.toString(), board.father());
        if (!board.toString().equals(goalBoard()))
            visited.put(goalBoard, board.toString());

        Stack<String> output = new Stack<>();

        int steps = 0;

        while (!goalBoard.equals("0")) {
            output.push(goalBoard);
            goalBoard = visited.get(goalBoard);
            steps++;
        }
        
        while (!output.isEmpty()) System.out.println(output.pop());

        return steps - 1;

    }

}