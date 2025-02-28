package uk.ac.rhul.cs3821;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javax.swing.JFrame;



/**
 * This class will be a model for the GUI and will control any actions that occur on the menu.
 *
 * @author ZKAC354
 */


public class GuiView extends JFrame {

  private String gridSizeInp;
  private int gridSize;
  private MazeGenerator mg;

  private ObservableList<String> mazeAlgs =
      FXCollections.observableArrayList("Recursive Backtracking");

  private ObservableList<String> solveAlgs =
      FXCollections.observableArrayList("BFS", "Dijkstra", "A*");

  @FXML
  private Button generateButton;

  @FXML
  private TextField inputGridSize;

  @FXML
  private VBox root;

  @FXML
  private ComboBox<String> selectMazeAlgorithm;

  @FXML
  private ComboBox<String> selectSolveAlgorithm;

  @FXML
  private Label mazeAlgLabel;

  @FXML
  private Label solveAlgLabel;


  @FXML
  private Label testGridSize;

  @FXML
  void gridSizeInp(KeyEvent event) {
    getGridSizeInp();
  }

  @FXML
  void generate(ActionEvent event) throws InvalidColourException, InterruptedException {
    boolean[] boolList;
    boolList = checkInps();
    try {
      String searchAlg = selectSolveAlgorithm.getValue();
      testGridSize.setText("Please select an INTEGER Grid Size > 4");
      mazeAlgLabel.setText("Maze Algorithm");
      solveAlgLabel.setText("Solve Algorithm");

      if (boolList[0] && boolList[1] && boolList[2]) {
        setGridSize(Integer.parseInt(getGridSizeInp()));
        testGridSize.setText("The Grid Size you have entered: " + getGridSizeInp());
        mg = new MazeGenerator(getGridSize());
        if (searchAlg.contentEquals("Dijkstra")) {
          mg.dijkstraSelected();
        } else if (searchAlg.contentEquals("BFS")) {
          mg.bfsSelected();
        } else if (searchAlg.contentEquals("A*")) {
          mg.astarSelected();
        } else {
          System.out.println(searchAlg);
        }
      } else {
        if (!boolList[0]) {
          mazeAlgLabel.setText("Please select a valid Maze Algorithm ->");
        }
        if (!boolList[1]) {
          solveAlgLabel.setText("Please select a valid Solve Algorithm ->");
        }
        if (boolList[2]) {
          setGridSize(Integer.parseInt(getGridSizeInp()));
          testGridSize.setText("The Grid Size you have entered: " + getGridSizeInp());
        }
      }
    } catch (Exception e) {
      System.out.println("CRASH");
      e.printStackTrace();
    }

  }

  /**
   * Checks what prerequisites are needed to launch the game, will only launch if all 3 are met.
   *
   * @return bool[] a set of all prerequisites
   */

  public boolean[] checkInps() {
    boolean maze = true;
    boolean solve = true;
    boolean size = true;

    if (selectMazeAlgorithm.getValue() == null
        || selectMazeAlgorithm.getValue().contentEquals("Maze Algorithm")) {
      maze = false;
    }
    if (selectSolveAlgorithm.getValue() == null
        || selectSolveAlgorithm.getValue().contentEquals("Solve Algorithm")) {
      solve = false;
    }
    if ((getGridSizeInp().isBlank()
        || !(Integer.valueOf(Integer.parseInt(getGridSizeInp())) instanceof Integer)
        || Integer.parseInt(getGridSizeInp()) < 5)) {
      size = false;
    }

    boolean[] bool = new boolean[3];

    bool[0] = maze;
    bool[1] = solve;
    bool[2] = size;
    return bool;

  }



  private static volatile GuiView instance = null;

  @FXML
  void initialize() {
    selectMazeAlgorithm.setItems(mazeAlgs);
    selectSolveAlgorithm.setItems(solveAlgs);
    instance = this;
  }

  /**
   * The Singleton class of the GUI view, ensures only one GUI can be loaded at once.
   *
   * @author Dave Cohen
   * @return GuiView the GUI made on scenebuilder located in resources/GUI.fxml
   * @throws Exception Exception thrown when there is no instance of the GUI created
   */
  public static synchronized GuiView getInstance() throws Exception {
    if (instance == null) {
      new Thread(() -> Application.launch(Driver.class)).start();
      // Wait until the instance is ready since initialise has executed.
      while (instance == null) {
        throw new Exception("instance is null");
      }
    }

    return instance;
  }

  /**
   * This method closes the GuiView if a force close is needed.
   */
  public void close() {
    instance.getDefaultCloseOperation();
  }


  public String getGridSizeInp() {
    gridSizeInp = inputGridSize.getText();
    return gridSizeInp;
  }


  public void setGridSizeInp(String gridSizeInp) {
    inputGridSize.setText(gridSizeInp);
  }

  public int getGridSize() {
    return gridSize;
  }

  public void setGridSize(int gridSize) {
    this.gridSize = gridSize;
  }


}


