import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;

public class Solver {

	static final OutputStream out = new BufferedOutputStream ( System.out );
	static final PriorityQueue<Board> priorityQueue = new PriorityQueue<Board>();
	static final HashMap<String, String> boardDatabase = new HashMap<>();
	static int size;

	public static Board reachInput (String file) throws FileNotFoundException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		size = Integer.parseInt(br.readLine());
		StringTokenizer st = new StringTokenizer(br.readLine());
		final int[] tiles = new int[size*size];

		for(int i = 0; i < tiles.length; i++){ tiles[i] = Integer.parseInt(st.nextToken()); }
		br.close();
		
		return new Board(tiles);
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		double initial = System.currentTimeMillis();
		priorityQueue.add(reachInput(args[0]));
		while(priorityQueue.peek().data[1] != 0){ solve(priorityQueue.poll()); }
		
		boardDatabase.put(priorityQueue.peek().toString(), priorityQueue.peek().pater);
		final String[] path = path(priorityQueue.peek());
		
		for (String passage : path) {
			out.write((passage+ "\n").getBytes());
		}
		out.flush();

		double finale = System.currentTimeMillis();
		System.out.println(finale-initial);


	}
	
	static String[] path(Board destination) {
		final int numberOfMove = destination.data[0];
		System.out.println(numberOfMove);
		final String[] path = new String[numberOfMove + 1];
		String s = destination.toString();

		for (int i = numberOfMove; i >= 0; i--) {
			path[i] = s;
			s = boardDatabase.get(s);
		}

		return path;

	}

	static void solve (Board currentBoard){
		final Board[] children = currentBoard.getChildren();
		for (Board child : children) {
			if(child != null && !boardDatabase.containsKey(child.toString())) { priorityQueue.add(child); }
		}
		
		if(!boardDatabase.containsKey(currentBoard.toString()))boardDatabase.put(currentBoard.toString(), currentBoard.pater);
	}
}
