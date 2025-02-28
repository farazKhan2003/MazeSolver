package uk.ac.rhul.cs3821;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class will randomly generate a maze.
 *
 * @author ZKAC354
 */

public class MazeGenerator extends JFrame {

  Cells start; // beginning cell
  Cells end; // ending cell

  private Cells[][] grid; // grid of Cells

  private int[] brCorner;
  private int[] tlCorner;

  private Boolean[][] visited;

  private JFrame frame;
  private JPanel panel;

  private int dimension;
  private int gridSize;
  private int rows = 0;
  private int cols = 0;
  private int colBeginning;
  private int rowBeginning;
  private int pathCount = 0;
  private int correctpathCount = 0;

  private double rand = 0;
  private double durationSeconds;

  long startTime;
  long endTime;



  private Colour colour = new Colour(0.0f, 0.0f, 0.0f, 0.0f);


  /**
   * Parses the grid size from the main GUI.
   *
   * @param gridSize the size of the maze the user inputs in the GUI
   */

  public MazeGenerator(int gridSize) {
    this.gridSize = gridSize;
    this.rows = rows;
    this.cols = cols;
    this.brCorner = brCorner;
  }

  /**
   * Defaults grid size to 30 for testing.
   */

  public MazeGenerator() {
    this.gridSize = 30;
    this.rows = rows;
    this.cols = cols;
    this.brCorner = brCorner;
  }



  /**
   * This class will create and display the maze and its random generation.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */
  public void mazeGen() throws InvalidColourException, InterruptedException {
    gridSize++;
    dimension = gridSize * 10;

    grid = new Cells[gridSize][gridSize]; // [row][col]
    panel = new JPanel();
    panel.setLayout(null); // create empty panel for grid to go on
    frame = new JFrame(); // create actual window for the maze
    frame.setTitle("Maze Game");
    frame.setSize(dimension + 2 * dimension / gridSize, dimension + 4 * dimension / gridSize);
    frame.setAlwaysOnTop(true);
    Container con = getContentPane();
    con.add(panel);
    int section = gridSize;
    int width = dimension / section;
    gridSize *= gridSize; // this will make a perfect square

    for (int i = 0; i < dimension; i += width) { // for each section (row)
      for (int j = 0; j < dimension; j += width) { // for each col
        JButton cell = new Cells();
        cell.setBounds(j, i, width, width); // create uniform square cells at (i,j)
        cell.setBorderPainted(false);
        grid[getRows()][getCols()] = (Cells) cell;
        grid[getRows()][getCols()].setBackground(colour.convertStringtoColour("BLACK"));
        cell.setEnabled(false); // to ensure the cell cannot be pressed
        panel.add(cell);
        this.cols++;
        setCols(this.cols);
      }
      this.rows++;
      setRows(this.rows);
      setCols(0);
    }
    setCols(this.cols);
    setRows(this.rows);


    frame.setLocationRelativeTo(null);
    frame.add("Center", con);
    frame.setDefaultCloseOperation(2);
    frame.setVisible(true);

    // start creating the maze
    constructBorders();
    recBacktracking();
    reconstruct();
  }

  /**
   * Runs BFS if selected from GUI.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   * 
   */

  public void bfsSelected() throws InvalidColourException, InterruptedException {
    mazeGen();
    bfs();
    if (((pathCount - correctpathCount) - 2) <= 0) {
      System.out.println(0);
    } else {
      System.out
          .println("The number of wrong cells discovered: " + ((pathCount - correctpathCount) - 2));
      // seems to have a +-2 bound
    }
  }

  /**
   * Run A* if selected from the GUI.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */

  public void astarSelected() throws InvalidColourException, InterruptedException {
    // A* algorithm
    mazeGen();
    astar();
    System.out
        .println("The number of wrong cells discovered: " + ((pathCount - correctpathCount) + 1));
  }

  /**
   * Run Dijkstra if selected from GUI.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */

  public void dijkstraSelected() throws InvalidColourException, InterruptedException {
    mazeGen();
    dijkstra();
    System.out
        .println("The number of wrong cells discovered: " + ((pathCount - correctpathCount) + 1));
  }

  /**
   * A Dijkstra Algorithm inspired by
   * https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-in-java-using-priorityqueue/
   * and adapted to suit my algorithm and my current maze game.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */

  private void dijkstra() throws InvalidColourException, InterruptedException {
    startTime = System.currentTimeMillis(); // Record start time

    // Create a priority queue to store cells, ordered by their cost
    PriorityQueue<Cells> queue = new PriorityQueue<>(Comparator.comparingInt(c -> c.cost));

    // Initialise the cost and previous cell pointers for all cells in the grid
    for (Cells[] row : grid) {
      for (Cells cell : row) {
        cell.cost = Integer.MAX_VALUE; // Set cost to maximum value
        cell.prev = null; // Set previous cell pointer to null
      }
    }

    Cells startPoint = start;
    startPoint.cost = 0; // Set the cost of the start cell to 0
    queue.add(startPoint); // Add the start cell to the queue

    // Iterate until the queue is empty
    while (!queue.isEmpty()) {
      Cells cur = queue.poll(); // Remove and return the cell with the lowest cost
      int[] index = getIndex(cur); // Get the row and column indices of the current cell
      int row = index[0];
      int col = index[1];

      // If the current cell is the end cell, reconstruct and highlight the shortest path
      if (cur == end) {
        while (cur != null) {
          Thread.sleep(10); // Pause so the path can be visualised
          cur.setBackground(colour.convertStringtoColour("MAGENTA")); // Highlight the cell
          correctpathCount++; // Increment the correct path count
          cur = cur.prev; // Move to the previous cell in the path
        }
        start.setBackground(colour.convertStringtoColour("GREEN")); // Colour the start cell green
        end.setBackground(colour.convertStringtoColour("RED")); // Highlight the end cell in red
        endTime = System.currentTimeMillis(); // Record end time
        durationSeconds = (endTime - startTime) / 1000.0; // Calculate duration in seconds
        System.out.printf("Dijkstra's algorithm took %.3f seconds to run.\n", durationSeconds);
        // Print the completion time
        return;
      }

      cur.setBackground(colour.convertStringtoColour("YELLOW")); // Highlight the current cell
      pathCount++; // Increment the path count

      // Explore neighbors of the current cell
      for (Direction dir : cur.sides) {
        Cells neighbour = getNeighbour(row, col, dir); // Get the neighboring cell
        if (neighbour != null && !neighbour.isWall && cur.cost + 1 < neighbour.cost) {
          // If the neighbour is not a wall and the cost to reach it from the current cell is less
          // than its current cost
          neighbour.cost = cur.cost + 1; // Update the cost to reach the neighbour
          neighbour.prev = cur; // Update the previous cell pointer of the neighbour
          queue.add(neighbour); // Add the neighbour to the queue
          Thread.sleep(7); // Pause for visualisation
        }
      }
    }
  }


  /**
   * A helper method to find the neighbour of a cell.
   *
   * @param row current row
   * @param col current col
   * @param dir direction of neighbour that wishes to be found
   * @return neighbouring cell
   */
  private Cells getNeighbour(int row, int col, Direction dir) {
    // I am aware that the else statements are redundant, they are purely for checkstyle purposes
    switch (dir) {
      case NORTH:
        // Check if the neighbour is within the grid boundaries
        if (row > 0) {
          // Return the cell to the north
          return grid[row - 1][col];
        } else {
          // If the neighbour is outside the grid boundaries, return null
          return null;
        }
      case SOUTH:
        // Check if the neighbour is within the grid boundaries
        if (row < grid.length - 1) {
          // Return the cell to the south
          return grid[row + 1][col];
        } else {
          // If the neighbour is outside the grid boundaries, return null
          return null;
        }
      case EAST:
        // Check if the neighbour is within the grid boundaries
        if (col < grid[0].length - 1) {
          // Return the cell to the east
          return grid[row][col + 1];
        } else {
          // If the neighbour is outside the grid boundaries, return null
          return null;
        }
      case WEST:
        // Check if the neighbour is within the grid boundaries
        if (col > 0) {
          // Return the cell to the west
          return grid[row][col - 1];
        } else {
          // If the neighbour is outside the grid boundaries, return null
          return null;
        }
      default:
        // If the direction is invalid, return null
        return null;
    }
  }



  /**
   * This is my interpretation of an A* algorithm that uses the Manhattan distance heuristic to
   * search the maze due to the walls not guaranteeing direct diagonals.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */

  public void astar() throws InvalidColourException, InterruptedException {
    startTime = System.currentTimeMillis(); // Record start time

    // Initialize scores and predecessors for all cells
    for (Cells[] row : grid) {
      for (Cells cell : row) {
        cell.gscore = Integer.MAX_VALUE;
        cell.fscore = Integer.MAX_VALUE;
        cell.prev = null;
      }
    }

    // Set the start cell as the current cell and initialise the priority queue
    Cells startCell = start;
    PriorityQueue<Cells> queue = new PriorityQueue<>(Comparator.comparingInt(c -> c.fscore));
    // similar structure to dijkstras will be followed
    startCell.gscore = 0;

    startCell.fscore = manhattanHeuristic(startCell, end);
    // use Manhattan heuristic to estimate distance to end

    queue.add(startCell);

    // Main loop of the A* algorithm
    while (!queue.isEmpty()) {
      Cells cur = queue.poll(); // Get the cell with the lowest fscore from the queue
      int[] pos = getIndex(cur);
      int row = pos[0];
      int col = pos[1];

      // If the current cell is the end cell, reconstruct and colour the path
      if (cur == end) {
        while (cur != null) {
          Thread.sleep(10); // allow for the path to be visualised
          cur.setBackground(colour.convertStringtoColour("MAGENTA"));
          correctpathCount++;
          cur = cur.prev;
        }
        start.setBackground(colour.convertStringtoColour("GREEN")); // Colour the start green
        end.setBackground(colour.convertStringtoColour("RED")); // Highlight the end cell in red
        endTime = System.currentTimeMillis(); // Record end time
        durationSeconds = (endTime - startTime) / 1000.0; // Calculate duration in seconds
        System.out.printf("A* algorithm took %.3f seconds to run.\n", durationSeconds);
        return;
      }

      // Colour the current cell and increment the path count
      cur.setBackground(colour.convertStringtoColour("YELLOW"));
      pathCount++;

      // Explore neighbours of the current cell
      for (Direction dir : cur.sides) {
        Cells neighbour = getNeighbour(row, col, dir);
        if (neighbour != null && !neighbour.isWall) {
          int tempgScore = cur.gscore + 1;
          if (tempgScore < neighbour.gscore) {
            neighbour.prev = cur;
            neighbour.gscore = tempgScore;
            neighbour.fscore = neighbour.gscore + manhattanHeuristic(neighbour, end);
            if (!queue.contains(neighbour)) {
              queue.add(neighbour);
              Thread.sleep(7);
            }
          }
        }
      }
    }
  }

  /*
   * Manhattan Distance inspired by
   * https://www.geeksforgeeks.org/calculate-the-manhattan-distance-between-two-cells-of-given-2d-
   * array/ Calculates the difference between the current cell and the end point in both the x and y
   * axis.
   */

  private int manhattanHeuristic(Cells cell, Cells goal) {
    int[] cellPos = getIndex(cell);
    int[] goalPos = getIndex(goal);
    return Math.abs(cellPos[0] - goalPos[0]) + Math.abs(cellPos[1] - goalPos[1]);
    // return the manhattan distance from the start to the end node
  }


  /**
   * Randomly generates a maze using a recursive backtracking algorithm.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   */

  private void recBacktracking() throws InvalidColourException {
    // Inspiration for recursive backtracking through directions from:
    // https://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
    visited = new Boolean[grid[0].length][grid[0].length];
    setRowBeginning(1 + (int) (Math.random() * ((rows - 2) - 1) + 1));
    setColBeginning(1 + (int) (Math.random() * ((cols - 2) - 1) + 1));

    // Get the number of sides of the starting cell
    int cellCount = grid[getRowBeginning()][getColBeginning()].sides.size();
    Stack<Cells> stack = new Stack<Cells>(); // using a stack to implement backtracking
    visited[getRowBeginning()][getColBeginning()] = true;
    grid[getRowBeginning()][getColBeginning()].isWall = false;
    // originally each cell was a wall, change the non border start point to a valid cell
    stack.push(grid[getRowBeginning()][getColBeginning()]);

    while (!stack.isEmpty()) {
      while (cellCount > 0) {
        int path = (int) (Math.random() * cellCount);
        visited[getRowBeginning()][getColBeginning()] = true;
        grid[getRowBeginning()][getColBeginning()].isWall = false;
        Direction dir = grid[getRowBeginning()][getColBeginning()].sides.get(path);
        String str = dir.toString();

        if (str.contentEquals("NORTH")
            // if no neighbours are visited, remove walls above the beginning point
            && grid[getRowBeginning() - 1][getColBeginning()].sides.contains(Direction.NORTH)
            // first valid row (non border) = row - 1
            && (visited[getRowBeginning() - 1][getColBeginning()] == null)
            && visited[getRowBeginning() - 2][getColBeginning()] == null
            && visited[getRowBeginning() - 1][getColBeginning() + 1] == null
            && visited[getRowBeginning() - 1][getColBeginning() - 1] == null
            && visited[getRowBeginning() - 2][getColBeginning() + 1] == null
            && visited[getRowBeginning() - 2][getColBeginning() - 1] == null) {


          visited[getRowBeginning() - 1][getColBeginning()] = true;
          visited[getRowBeginning() - 2][getColBeginning()] = true;
          setRowBeginning(--this.rowBeginning);

          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

          setRowBeginning(--this.rowBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

        } else if (str.contentEquals("SOUTH")
            // if no neighbours are visited, remove walls underneath the beginning point

            && grid[getRowBeginning() + 1][getColBeginning()].sides.contains(Direction.SOUTH)
            && visited[getRowBeginning() + 1][getColBeginning()] == null
            && visited[getRowBeginning() + 2][getColBeginning()] == null
            && visited[getRowBeginning() + 1][getColBeginning() + 1] == null
            && visited[getRowBeginning() + 1][getColBeginning() - 1] == null
            && visited[getRowBeginning() + 2][getColBeginning() + 1] == null
            && visited[getRowBeginning() + 2][getColBeginning() - 1] == null) {


          visited[getRowBeginning() + 1][getColBeginning()] = true;
          visited[getRowBeginning() + 2][getColBeginning()] = true;
          setRowBeginning(++this.rowBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

          setRowBeginning(++this.rowBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

        } else if (str.contentEquals("EAST")
            // if no neighbours are visited, remove walls to the right of the beginning point
            && grid[getRowBeginning()][getColBeginning() + 1].sides.contains(Direction.EAST)
            && visited[getRowBeginning()][getColBeginning() + 1] == null
            && visited[getRowBeginning()][getColBeginning() + 2] == null
            && visited[getRowBeginning() - 1][getColBeginning() + 1] == null
            && visited[getRowBeginning() + 1][getColBeginning() + 1] == null
            && visited[getRowBeginning() - 1][getColBeginning() + 2] == null
            && visited[getRowBeginning() + 1][getColBeginning() + 2] == null) {


          visited[getRowBeginning()][getColBeginning() + 1] = true;
          visited[getRowBeginning()][getColBeginning() + 2] = true;
          setColBeginning(++this.colBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

          setColBeginning(++this.colBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

        } else if (str.contentEquals("WEST")
            // if no neighbours are visited, remove walls to the left of the beginning point

            && grid[getRowBeginning()][getColBeginning() - 1].sides.contains(Direction.WEST)
            && visited[getRowBeginning()][getColBeginning() - 1] == null
            && visited[getRowBeginning()][getColBeginning() - 2] == null
            && visited[getRowBeginning() - 1][getColBeginning() - 1] == null
            && visited[getRowBeginning() + 1][getColBeginning() - 1] == null
            && visited[getRowBeginning() - 1][getColBeginning() - 2] == null
            && visited[getRowBeginning() + 1][getColBeginning() - 2] == null) {


          visited[getRowBeginning()][getColBeginning() - 1] = true;
          visited[getRowBeginning()][getColBeginning() - 2] = true;

          setColBeginning(--this.colBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

          setColBeginning(--this.colBeginning);
          grid[getRowBeginning()][getColBeginning()]
              .setBackground(colour.convertStringtoColour("WHITE"));
          grid[getRowBeginning()][getColBeginning()].isWall = false;
          stack.push(grid[getRowBeginning()][getColBeginning()]);

        } else {
          grid[getRowBeginning()][getColBeginning()].sides.remove(Direction.valueOf(str));
        }

        cellCount = grid[getRowBeginning()][getColBeginning()].sides.size(); // recalculate the size
      }
      if (!stack.isEmpty()) {
        Cells cur = stack.pop();
        int[] index = getIndex(cur);
        setRowBeginning(index[0]);
        setColBeginning(index[1]);
        cellCount = grid[getRowBeginning()][getColBeginning()].sides.size();
        // recalculate the size as the stack is being popped
      }
    }

    // colour in the start point green
    brCorner = findBrCorner();
    start = grid[brCorner[0]][brCorner[1]];
    start.setBackground(colour.convertStringtoColour("GREEN"));

    // colour in the end point red
    tlCorner = findTlCorner();
    end = grid[tlCorner[0]][tlCorner[1]];
    end.setBackground(colour.convertStringtoColour("RED"));
  }


  /**
   * This is my interpretation of a BFS algorithm that will search through the maze using a Queue.
   *
   * @throws InvalidColourException thrown when a colour is not in the Color dataset
   * @throws InterruptedException thrown when the thread times out
   */

  private void bfs() throws InvalidColourException, InterruptedException {
    startTime = System.currentTimeMillis(); // Record start time

    Cells cur = new Cells();
    int randCellPos;
    int cellCount;
    Queue<Cells> queue = new ArrayDeque();
    Cells temp = start;
    cur.setBackground(colour.convertStringtoColour("YELLOW"));
    // set start point to yellow

    pathCount++;
    queue.add(temp); // add starting cell to queue
    temp.prev = null; // the first element has no prev
    while (!queue.isEmpty()) {
      cur = queue.poll(); // retrieve and remove the head of queue
      int[] pos = getIndex(cur);
      int row = pos[0];
      int col = pos[1];
      getGrid()[row][col].paths = true; // mark cell as visited
      cellCount = cur.sides.size(); // get the number of valid adjacent cells
      while (cellCount > 0) {
        randCellPos = (int) (Math.random() * cellCount); // Select a random adjacent cell
        Direction dir = cur.sides.get(randCellPos); // Get the direction of the adjacent cell
        String str = dir.toString();
        // similar structure to recursive backtracking
        if (str.contentEquals("NORTH")) {
          cur.sides.remove(dir); // mark the adjacent cell as visited
          if (cur == end) {
            queue.clear();
            // goal node has been found
            break;
          }
          if (!getGrid()[row - 1][col].isWall && getGrid()[row - 1][col].paths == false) {
            // if a valid cell and hasn't been visited, visit it
            queue.add(getGrid()[row - 1][col]);
            getGrid()[row - 1][col].paths = true;
            getGrid()[row - 1][col].prev = cur; // start to build the tree
            Thread.sleep(7); // allow for path to be visualised
            cur.setBackground(colour.convertStringtoColour("YELLOW"));
            pathCount++;

            if (cur == end) {
              // goal node has been found
              queue.clear();
              break;
            }
          }
          cellCount = cur.sides.size();
        } else if (str.contentEquals("SOUTH")) {
          cur.sides.remove(dir); // mark the adjacent cell as visited
          if (cur == end) {
            queue.clear();
            break;
          }
          // if statement the same as north but opposite row
          if (!getGrid()[row + 1][col].isWall && getGrid()[row + 1][col].paths == false) {
            // if a valid cell and hasn't been visited, visit it
            queue.add(getGrid()[row + 1][col]);
            getGrid()[row + 1][col].paths = true; // mark as visited
            getGrid()[row + 1][col].prev = cur; // start to build the tree
            Thread.sleep(7);
            cur.setBackground(colour.convertStringtoColour("YELLOW"));
            pathCount++;

            if (cur == end) {
              queue.clear();
              break;
            }
          }
          cellCount = cur.sides.size();
        } else if (str.contentEquals("EAST")) {
          cur.sides.remove(dir);
          if (cur == end) {
            // goal node is found
            queue.clear();
            break;
          }
          if (!getGrid()[row][col + 1].isWall && getGrid()[row][col + 1].paths == false) {
            // if a valid cell and hasn't been visited, visit it
            queue.add(getGrid()[row][col + 1]);
            getGrid()[row][col + 1].paths = true;
            getGrid()[row][col + 1].prev = cur; // start to build the tree
            Thread.sleep(7);
            cur.setBackground(colour.convertStringtoColour("YELLOW"));
            pathCount++;

            if (cur == end) {
              queue.clear();
              break;
            }
          }
          cellCount = cur.sides.size();

        } else if (str.contentEquals("WEST")) {
          cur.sides.remove(dir);
          if (cur == end) {
            queue.clear();
            break;
          }
          // if statement the same as east but opposite col
          if (!getGrid()[row][col - 1].isWall && getGrid()[row][col - 1].paths == false) {
            // if a valid cell and hasn't been visited, visit it
            queue.add(getGrid()[row][col - 1]);
            getGrid()[row][col - 1].paths = true;
            getGrid()[row][col - 1].prev = cur; // start to build the tree
            cur.setBackground(colour.convertStringtoColour("YELLOW"));
            pathCount++;
            if (cur == end) {
              queue.clear();
              break;
            }
          }
          cellCount = cur.sides.size();
        }
      }
    }
    // when correct path is found cur is the end
    temp = end;
    while (temp.prev != null) {
      Thread.sleep(10);
      correctpathCount++;
      temp.setBackground(colour.convertStringtoColour("MAGENTA"));
      temp = temp.prev;
      // go back to the beginning, colouring each cell magenta to show the correct path
    }
    start.setBackground(colour.convertStringtoColour("GREEN")); // colour the start green
    end.setBackground(colour.convertStringtoColour("RED")); // colour the end red

    endTime = System.currentTimeMillis(); // Record end time
    durationSeconds = (endTime - startTime) / 1000.0; // Calculate duration in seconds
    System.out.printf("BFS algorithm took %.3f seconds to run.\n", durationSeconds);
  }


  /**
   * This method will return the x and y co-ords of a cell Inspired by NoWhere Man's response on
   * https://stackoverflow.com/questions/68381883/how-to-return-an-array-with-the-indexes-of-a-certain-int-from-another-array
   *
   * @param cur the cell whos coordinates you want to find
   * @return the index array holding the [x][y] coordinates
   */

  private int[] getIndex(Cells cur) {
    int[] indexArray = new int[2];
    for (int x = 0; x < this.grid[0].length; x++) {
      // iterate through each row
      for (int y = 0; y < this.grid[0].length; y++) {
        // iterate through each column
        if (grid[x][y] == cur) {
          indexArray[0] = x;
          indexArray[1] = y;
        }
      }
    }
    // return an cell position [row][col]
    return indexArray;
  }


  /**
   * This method will construct the borders so that no walls touch the borders.
   *
   * @throws InvalidColourException thrown when the colour raised is not a valid Color key
   */

  public void constructBorders() throws InvalidColourException {
    // inspired by the Ruby code on
    // https://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
    setRows(grid[1].length); // reset rows to just construct borders on empty map
    int rows = getRows();
    setCols(grid[1].length); // reset cols to just construct borders on empty map
    int cols = getCols();

    for (int i = 1; i < rows - 1; i++) {
      for (int j = 1; j < cols - 1; j++) {
        grid[i][j].sides
            .addAll(List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));
        // each cell holds a direction

        if (i == 1) {
          grid[i][j].sides.remove(Direction.NORTH);
          // if in row 1, remove all the cells on north border
        }

        if (j == 1) {
          grid[i][j].sides.remove(Direction.WEST);
          // if in col 1, remove all the cells on west border
        }

        if (i == cols - 2) {
          grid[i][j].sides.remove(Direction.SOUTH);
          // if in last row, remove all the cells on south border
        }

        if (j == rows - 2) {
          grid[i][j].sides.remove(Direction.EAST);
          // if in last col, remove all the cells on east border
        }
      }
    }
  }

  /**
   * Reconstructs the edges after walls are made.
   */
  public void reconstruct() {
    for (int i = 1; i < getGrid()[0].length - 1; i++) {
      for (int j = 1; j < getGrid()[0].length - 1; j++) {
        getGrid()[i][j].sides.clear();
        if (!getGrid()[i][j].isWall && visited[i][j] != null) {
          if (visited[i - 1][j] != null) {
            getGrid()[i][j].sides.add(Direction.NORTH);
          }
          if (visited[i + 1][j] != null) {
            getGrid()[i][j].sides.add(Direction.SOUTH);
          }
          if (visited[i][j - 1] != null) {
            getGrid()[i][j].sides.add(Direction.WEST);
          }
          if (visited[i][j + 1] != null) {
            getGrid()[i][j].sides.add(Direction.EAST);
          }
        }
      }
    }
  }


  /**
   * This method will find the maze exit point (the end).
   *
   * @return int[] corner which is the coordinates for the exit point
   */
  public int[] findTlCorner() {

    int[] corner = new int[2];

    for (int i = 1; i < grid[0].length; i++) {
      for (int j = 1; j < grid[0].length; j++) {
        if (visited[i][j] != null) {
          corner[0] = i;
          corner[1] = j;
          return corner;
        }

      }
    }
    return corner;
  }

  /**
   * This method will find the maze entry point (the beginning).
   *
   * @return int[] corner which is the coordinates for the entry point
   */
  public int[] findBrCorner() {
    int[] corner = new int[2];

    for (int i = grid[0].length - 1; i > 0; i--) {
      for (int j = grid[0].length - 1; j > 0; j--) {
        if (visited[i][j] != null) {
          corner[0] = i;
          corner[1] = j;
          return corner;
        }
      }
    }
    return corner;
  }

  public JFrame getFrame() {
    return frame;
  }

  public int getgridSize() {
    return gridSize;
  }

  public void setgridSize(int gridSize) {
    this.gridSize = gridSize;
  }

  public int getRows() {
    return this.rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getCols() {
    return this.cols;
  }

  public void setCols(int cols) {
    this.cols = cols;
  }

  public Cells[][] getGrid() {
    return grid;
  }

  public double getRand() {
    return rand;
  }

  public void setRand(double rand) {
    this.rand = rand;
  }


  public int getColBeginning() {
    return colBeginning;
  }

  public void setColBeginning(int colBeginning) {
    this.colBeginning = colBeginning;
  }

  public int getRowBeginning() {
    return rowBeginning;
  }

  public void setRowBeginning(int rowBeginning) {
    this.rowBeginning = rowBeginning;
  }

  /**
   * This getColor method will return the colour of the start cell the method is purely for testing
   * purposes, The method is inspired by RAZ_Muh_Taz's answer on
   * https://stackoverflow.com/questions/49640034/java-awt-color-return-color-name-not-rgb-value
   *
   * @return the string representation of the start cells' colour.
   */
  public String getColorOfStart() {

    if (start.getBackground().equals(Color.GREEN)) {
      return "GREEN";
    } else {
      return "Unknown Color";
    }
  }

  /**
   * This getColor method will return the colour of the end cell the method is purely for testing
   * purposes, The method is inspired by RAZ_Muh_Taz's answer on
   * https://stackoverflow.com/questions/49640034/java-awt-color-return-color-name-not-rgb-value
   *
   * @return the string representation of the end cells' colour.
   */
  public String getColorOfEnd() {

    if (end.getBackground().equals(Color.RED)) {
      return "RED";
    } else {
      return "Unknown Color";
    }
  }



}
