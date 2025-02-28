package uk.ac.rhul.cs3821;


/**
 * Custom Exception which is thrown when colour is not apart of the java.awt.Color library,
 */

public class InvalidColourException extends Exception {

  public String toString() {
    return "This colour is not apart of the collection";
  }
}
