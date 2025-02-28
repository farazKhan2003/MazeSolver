package uk.ac.rhul.cs3821;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GuiTest {


  private GuiView gui;
  private int rowNum;
  private int colNum;


  // Test 1, testing to see that the GUI launches, as its the first frame and unnamed it should be
  // frame0
  @Test
  void creation() {
    gui = new GuiView();
    assertEquals(gui.getName(), "frame0");
  }

}
