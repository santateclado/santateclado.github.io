import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;

// Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = chooseColor(new Random());
    this.flooded = flooded;
  }

  // Constructor for part 1 testing
  Cell(Color color) {
    this.x = 10;
    this.y = 10;
    this.color = color;
    this.flooded = false;
  }

  Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = false;
  }

  // initializes the color list with the colors below
  Color chooseColor(Random random) {
    ArrayList<Color> listOfColors = new ArrayList<Color>();
    listOfColors.add(Color.RED);
    listOfColors.add(Color.GREEN);
    listOfColors.add(Color.BLUE);
    listOfColors.add(Color.YELLOW);
    listOfColors.add(Color.MAGENTA);
    listOfColors.add(Color.CYAN);
    listOfColors.add(Color.PINK);
    listOfColors.add(Color.ORANGE);
    return listOfColors.get(random.nextInt((7 - 0) + 1));
  }

  // draws a tile
  WorldImage draw() {
    return new RectangleImage(35, 35, OutlineMode.SOLID, this.color);
  }

}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<Cell> board;

  // Defines an int constant for the sides of a square board
  // a BOARD_SIZE value of 10 will create a 10*10 board
  int boardSize;

  FloodItWorld(int size) {
    this.boardSize = size;
  }

  // constructor for part 1 testing
  FloodItWorld() {
    this.boardSize = 2;
  }

  // constructor for testing purposes
  FloodItWorld(ArrayList<Cell> inputBoard) {
    this.board = inputBoard;
    this.boardSize = 2;
  }

  // creates the board with new random tiles and connects them together
  void populateBoardAndLink() {
    // part 1: populating the board with unlinked titles
    int sizeNum = this.boardSize;
    int trueBoardSize = (sizeNum * sizeNum); // the "area" of the board
    ArrayList<Cell> newBoard = new ArrayList<Cell>(trueBoardSize);
    // nested loops, r loop for rows, and c loop inside r loop for moving along the
    // row into new columns
    for (int r = 0; r <= sizeNum; r++) { // r for rows
      for (int c = 0; c <= sizeNum; c++) { // c for columns
        if (r == 0 && c == 0) {
          newBoard.add(new Cell(1, 1, true));
        }
        else {
          newBoard.add(new Cell(c + 1, r + 1, false));
        }
      }
    }
    // part 2: connecting the tiles in the populated board
    for (int i = 0; i <= trueBoardSize; i++) {
      Cell pulledCell = newBoard.get(i);
      // if i minus boardSize is less than 0, than it is on the top row
      if ((i - sizeNum) < 0) {
        pulledCell.top = null;
      }
      else {
        pulledCell.top = (newBoard.get(i - sizeNum));
      }
      // if i + 6 is greater than trueBoardSize, than it is on the bottom row
      if ((i + 6) > trueBoardSize) {
        pulledCell.bottom = null;
      }
      else {
        pulledCell.bottom = (newBoard.get(i + sizeNum));
      }
      // if i modulo sizeNum is equal to zero, than it is on the left edge
      if ((i % sizeNum) == 0) {
        pulledCell.left = null;
      }
      else {
        pulledCell.left = (newBoard.get(i - 1));
      }
      // if (i + 1) modulo sizeNum is equal to zero, than it is on the right edge
      if (((i + 1) % sizeNum) == 0) {
        pulledCell.right = null;
      }
      else {
        pulledCell.right = (newBoard.get(i + 1));
      }
    }
    this.board = newBoard;
  }

  // draws the game
  public WorldScene makeScene() {
    WorldScene aScene = new WorldScene(600, 600);
    for (Cell pulledCell : board) {
      aScene.placeImageXY(pulledCell.draw(), (pulledCell.x * 35 + 100), (pulledCell.y * 35 + 100));
    }
    return aScene;
  }

  // runs the game
  public void initiateGame() {
    this.bigBang(600, 600, 0.1);
  }
}

class ExamplesFloodItGame {

  FloodItWorld world;

  FloodItWorld testMakeSceneWorld;
  ArrayList<Cell> testMakeSceneCellList;
  Cell testMakeSceneCell1;
  Cell testMakeSceneCell2;
  Cell testMakeSceneCell3;
  Cell testMakeSceneCell4;

  void initailizeWorld() {
    world = new FloodItWorld();

    testMakeSceneCellList = new ArrayList<Cell>();

    testMakeSceneCell1 = new Cell(10, 10, Color.BLACK);
    testMakeSceneCell2 = new Cell(15, 15, Color.BLUE);
    testMakeSceneCell3 = new Cell(16, 16, Color.GREEN);
    testMakeSceneCell4 = new Cell(7, 7, Color.ORANGE);

    testMakeSceneCellList.add(testMakeSceneCell1);
    testMakeSceneCellList.add(testMakeSceneCell2);
    testMakeSceneCellList.add(testMakeSceneCell3);
    testMakeSceneCellList.add(testMakeSceneCell4);

    testMakeSceneWorld = new FloodItWorld(testMakeSceneCellList);
  }

  void testDraw(Tester t) {
    Cell cell1 = new Cell(Color.BLUE);
    Cell cell2 = new Cell(Color.GREEN);
    Cell cell3 = new Cell(Color.YELLOW);

    t.checkExpect(cell1.draw(), new RectangleImage(35, 35, OutlineMode.SOLID, Color.BLUE));
    t.checkExpect(cell2.draw(), new RectangleImage(35, 35, OutlineMode.SOLID, Color.GREEN));
    t.checkExpect(cell3.draw(), new RectangleImage(35, 35, OutlineMode.SOLID, Color.YELLOW));
  }

  void testChooseColor(Tester t) {
    Cell cell1 = new Cell(Color.BLACK);
    Cell cell2 = new Cell(Color.CYAN);
    Cell cell3 = new Cell(Color.GREEN);

    t.checkExpect(cell1.chooseColor(new Random(1)), Color.CYAN);
    t.checkExpect(cell2.chooseColor(new Random(10000)), Color.ORANGE);
    t.checkExpect(cell3.chooseColor(new Random(4829048)), Color.BLUE);

  }

  void testMakeScene(Tester t) {

    initailizeWorld();

    WorldScene testScene = new WorldScene(600, 600);

    Cell cell1 = new Cell(10, 10, Color.BLACK);
    Cell cell2 = new Cell(15, 15, Color.BLUE);
    Cell cell3 = new Cell(16, 16, Color.GREEN);
    Cell cell4 = new Cell(7, 7, Color.ORANGE);

    testScene.placeImageXY(cell1.draw(), (cell1.x * 35 + 100), (cell1.y * 35 + 100));
    testScene.placeImageXY(cell2.draw(), (cell2.x * 35 + 100), (cell2.y * 35 + 100));
    testScene.placeImageXY(cell3.draw(), (cell3.x * 35 + 100), (cell3.y * 35 + 100));
    testScene.placeImageXY(cell4.draw(), (cell4.x * 35 + 100), (cell4.y * 35 + 100));

    t.checkExpect(testMakeSceneWorld.makeScene(), testScene);

  }

  void testPopulateBoardAndLink(Tester t) {

    initailizeWorld();

    ArrayList<Color> listOfColors = new ArrayList<Color>();
    listOfColors.add(Color.RED);
    listOfColors.add(Color.GREEN);
    listOfColors.add(Color.BLUE);
    listOfColors.add(Color.YELLOW);
    listOfColors.add(Color.MAGENTA);
    listOfColors.add(Color.CYAN);
    listOfColors.add(Color.PINK);
    listOfColors.add(Color.ORANGE);

    world.populateBoardAndLink();

    Cell firstCell = world.board.get(0);
    t.checkExpect(firstCell.flooded, true);

    for (int i = 0; i < world.board.size(); i++) {

      Cell pulledCell = world.board.get(i);

      t.checkRange(pulledCell.x, 0, 4);
      t.checkRange(pulledCell.y, 0, 4);

      t.checkExpect(listOfColors.contains(pulledCell.color), true);

      if (i == 0) {
        t.checkExpect(pulledCell.top, null);
        t.checkExpect(pulledCell.left, null);
      }
      else if (i % 2 == 0) {
        t.checkExpect(pulledCell.left, null);
      }
      else if ((i + 1) % 2 == 0) {
        t.checkExpect(pulledCell.right, null);
      }
      else if (i == 4) {
        t.checkExpect(pulledCell.right, null);
      }
    }

    for (int p = 1; p < world.board.size(); p++) {
      Cell pulledCell = world.board.get(p);
      t.checkExpect(pulledCell.flooded, false);
    }

  }

  // testing to see if the world function works
  void testGame(Tester t) {
    initailizeWorld();
    world.populateBoardAndLink();
    world.initiateGame();
  }

}
