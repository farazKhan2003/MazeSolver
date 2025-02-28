package uk.ac.rhul.cs3821;

/**
 * Will hold the direction for the cells neigbours.
 */
public enum Direction {
  NORTH, EAST, SOUTH, WEST;

  /**
   * This method gives a text representation for the direction of the neighbour.
   */
  public String toString() {
    switch (this) {
      case NORTH:
        return "NORTH";
      case EAST:
        return "EAST";
      case SOUTH:
        return "SOUTH";
      case WEST:
        return "WEST";
      default:
        return "INVALID";
    }
  }
}

