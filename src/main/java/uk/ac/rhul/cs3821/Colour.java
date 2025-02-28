package uk.ac.rhul.cs3821;

import java.awt.Color;

/**
 * This class will act as an adapter to the Color class and allow other classes to convert between
 * strings and Colors.
 *
 * @author ZKAC354
 * 
 */

public class Colour extends Color {


  /**
   * The constructor which implements the Color constructor.
   *
   * @param r red value in the RGB spectrum
   * @param g green value in the RGB spectrum
   * @param b blue value in the RGB spectrum
   * @param a alpha value determines brightness
   */

  public Colour(float r, float g, float b, float a) {
    super(r, g, b, a);
  }


  /**
   * This method will covert a String to its respective Color class or throw an invalid colour
   * exception.
   *
   * @param colour the colour the user wants to covert (must be a valid member of the Color class)
   * @return the Color counterpart to the string the user wants
   * @throws InvalidColourException The colour the user wants to convert isnt a valid member of the
   *         Color class
   */

  public Color convertStringtoColour(String colour) throws InvalidColourException {
    switch (colour) {
      case "BLACK":
        return Color.BLACK;
      case "BLUE":
        return Color.BLUE;
      case "GREEN":
        return Color.GREEN;
      case "YELLOW":
        return Color.YELLOW;
      case "RED":
        return Color.RED;
      case "WHITE":
        return Color.WHITE;
      case "MAGENTA":
        return Color.MAGENTA;
      default:
        throw new InvalidColourException();
    }
  }

  /**
   * This method will covert a Color to its respective String acting as a toString method.
   *
   * @param colour the colour the user wants to covert (must be a valid member of the Color class)
   * @return the String counterpart to the Color the user wants
   * @throws InvalidColourException The colour the user wants to convert isnt a valid member of the
   *         Color class
   */

  public String convertColourtoString(Color colour) throws InvalidColourException {
    if (colour == Color.BLACK) {
      return "BLACK";
    } else if (colour == Color.BLUE) {
      return "BLUE";
    } else if (colour == Color.GREEN) {
      return "GREEN";
    } else if (colour == Color.YELLOW) {
      return "YELLOW";
    } else if (colour == Color.RED) {
      return "RED";
    } else if (colour == Color.WHITE) {
      return "WHITE";
    } else if (colour == Color.MAGENTA) {
      return "MAGENTA";
    }
    throw new InvalidColourException();
  }
}
