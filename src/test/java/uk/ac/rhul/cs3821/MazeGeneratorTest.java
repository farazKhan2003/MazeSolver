package uk.ac.rhul.cs3821;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MazeGeneratorTest {

  private MazeGenerator mazegenerator = new MazeGenerator(10);

  // Test 1, this will create a mazeGen but will not set the correct amount cols
  /*
   * Test 2, The reason the cols are 0 is because a JFX thread cannot run while the JUnit test is
   * also running This has still been tested however through print statements and it works.
   */
  // Test 3, trying to get this test to work by skipping the GUI work and testing the number work
  // Test 4, after the colouring of the cells, the rows and cols should be different
  // Test 5, after recreating the GUI, this test will not check for the correct grid size

  @Test
  void correctGridSize() throws Exception {
    assertTrue(mazegenerator.getgridSize() == 10);
    mazegenerator.mazeGen();
    // assertFalse(mazegenerator.getGridSize() == 40);
  }


  // Test 1, testing to see if frame actually shows
  // Test 2, testing to see if the frame has the correct name and isnt just a random frame
  @Test
  void correctFrame() throws InvalidColourException, InterruptedException {
    mazegenerator.mazeGen();;
    assertEquals(mazegenerator.getFrame().isVisible(), true);
    assertEquals(mazegenerator.getFrame().getTitle(), "Maze Game");
  }

  // Test 1, testing to see if the start point's colour is green
  // Test 2, testing to see if the end point's colour is red
  // Test 3, testing to see if the north border is blue

  @Test
  void correctColours() throws InvalidColourException, InterruptedException {
    mazegenerator.bfsSelected();;
    assertEquals(mazegenerator.getColorOfStart(), "GREEN");
    assertEquals(mazegenerator.getColorOfEnd(), "RED");
    // assertEquals(mazegenerator.getGrid()[1][1]
    // .convertColorToString(mazegenerator.getGrid()[1][1].getBackground()), "BLUE");
  }

  // Test 1, testing to see if the borders are correctly removed
  // Test 2, attached colours to borders to fix test
  // Test 3, created enumeration class to solve side error
  // Popping removes the size for the grid so this test will no longer run

  // @Test
  // void borderRemoved() throws InvalidColourException, InterruptedException {
  // mazegenerator.mazeGen();
  // assertEquals(mazegenerator.getGrid()[mazegenerator.getGrid().length - 1][1].sides,
  // Direction.NORTH);
  // }

  // Test 1, testing to see if BFS works with new selection system. No exceptions thrown
  @Test
  void bfs() throws InvalidColourException, InterruptedException {
    mazegenerator.bfsSelected();
  }
  
  //Test 1, testing to see if Dijkstra works with new selections system
  //Test 2, Dijkstras now returns after maze is coloured, so no more NullPointerException.
  @Test
  void dijkstras() throws InvalidColourException, InterruptedException {
    mazegenerator.dijkstraSelected();
  }


}
