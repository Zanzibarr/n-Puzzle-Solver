
This java application was an university project for my Data Structures and Algorithms exam.
The project was designed as a competition between students, to design the fastest algorithm to solve a generalized <a href="https://en.wikipedia.org/wiki/15_Puzzle" target="_blank" rel="noreferrer">15-Puzzle</a> with the minimum number of moves.

## What it does?
We had to design an algorithm based on the <a href="https://en.wikipedia.org/wiki/A*_search_algorithm" target="_blank" rel="noreferrer">A* search</a>, taking as input a txt file formatted as follows:
```txt
4
1 3 2 6 8 9 4 10 7 5 11 15 12 14 13 0
```
- The first line is reserved to the size of the problem
- The second line is reserved to the elements of the puzzle (row major, left to right) using the 0 as the empty cell.

## How it works
### The structure
First I've built the class for the board, which contains:
- A square matrix to contain the current position of the puzzle
- The position of the white cell for the current and the previous positions
- Some heuristics values
- The current and previous positions string representations

Then I've build the main class where I wrote the A* algorithm.

### The logic
I used a priority queue populated with different Board objects used to represent the various positions of the puzzle.
The priority of the queue is dictated by the heuristics values: the one with the smallest cost has an higher priority.

Then I used the priority queue to explore one position at a time, exploring the most promising ones first:
- For each board object visited, I generated all the new boards that can be reached from the visited one: if I generate a position that I've already visited I don't put it in the queue since this would generate cycles.
- Once I've found the target position I can halt my search and return the list of positions explored to rebuild the path.

### The Heuristics
Before I've mentioned that the position with the lowest cost is the one with higher priority; but how did I obtain this value?

When we use the A* algorithm, we have a function $f(x)$ which is obtained as the sum of the heuristic function and a cost function:
$$f(x)=g(x)+h(x)$$
- $g(x)$ is the cost of reaching the current position $x$ (the number of steps)
- $h(x)$ is an estimate of the missing steps required to reach the final position.
    I've used the manhattan heuristic combined with the linear conflict heuristic

#### Manhattan Heuristic
The manhattan heuristic is obtained by adding for each cell the distance to it's final position:
$$
h(c)=|c.x-\text{goal}(c).x|+|c.y-\text{goal}(c).y|
$$

#### Linear Conflict
The linear conflict consists in the fact that if two cells in the same line (or column) must eventually swap places, then one of them must change line (or column) in order to let the other one through, thus if a conflict happens we can consider 2 more moves in the heuristics.
