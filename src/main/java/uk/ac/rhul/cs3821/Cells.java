package uk.ac.rhul.cs3821;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 * This class will determine the walls and will essentially represent each 'cell' of the grid.
 */

public class Cells extends JButton {
  ArrayList<Direction> sides = new ArrayList<>();
  boolean isWall = true;
  boolean visited = false;
  Cells end;
  Cells start;
  Colour colour = new Colour(1.0f, 1.0f, 1.0f, 1.0f);
  Cells prev;
  boolean paths = false;
  int cost;
  int gscore;
  int fscore;

  public Cells getEnd() {
    return end;
  }

  public void setEnd(Cells end) {
    this.end = end;
  }


  public Cells getStart() {
    return start;
  }

  public void setStart(Cells start) {
    this.start = start;
  }



  /**
   * Converts a string to the corresponding Java.awt.Color.
   *
   * @param col colour wishing to be converted
   * @return returns Color corresponding to the string input by the user
   * @throws InvalidColourException Throws custom exception if col is not a valid colour used in the
   *         maze design
   */

  public Color convertStringToColor(String col) throws InvalidColourException {
    return colour.convertStringtoColour(col);
  }

  /**
   * Converts a Java.awt.Color to the corresponding String, mostly used for testing.
   *
   * @param col Color wishing to be converted to string
   * @return returns String corresponding to the Color input by the user
   * @throws InvalidColourException Throws custom exception if col is not a valid colour used in the
   *         maze design
   */
  public String convertColorToString(Color col) throws InvalidColourException {
    return colour.convertColourtoString(col);
  }
}
