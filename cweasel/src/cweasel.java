import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.*;

public class cweasel extends JPanel implements ActionListener {

    // constants
    private final int numImages = 13;
    private final int cellSize = 30;

    private final int cellCover = 10;
    private final int emptyCell = 0;
    private final int thermalCell = 9;
    private final int coveredThermalCell = thermalCell + cellCover;

    private final int drawThermal = 9;
    private final int drawCover = 10;

    private int numTraps = 60;
    private int numRows = 15;
    private int numColumns = 15;

    private int boardWidth = numColumns * cellSize + 1;
    private int boardHeight = numRows * cellSize + 1;

    private int[] field;
    private boolean inGame;
    private int trapsLeft;
    private Image[] img;

    private int allCells;

    private JMenuBar mb;
    private JLabel trapCounter, timeCounter;
    private JButton start, quit;
    private JMenu settings, difficulty, menuGridSize, menuTraps, help;
    private JMenuItem easy, medium, hard, traps;
    private JMenuItem helpDialogue;
    private long previousTime;
    private long currentTime;
    private double elapsedTime;
    private double elapsedTime2;
    private long currentTime2;

    boolean firstClick;

    // constructor
    public cweasel(JMenuBar mb, JButton start, JMenu settings, JMenu difficulty, JMenu menuTraps, JMenuItem traps,
                   JMenuItem easy, JMenuItem medium, JMenuItem hard,
                   JMenu help, JMenuItem JHelpDialogue, JButton quit, JLabel trapCounter, JLabel timeCounter) {
        this.mb = mb;
        this.start = start;
        start.addActionListener(this);
        this.settings = settings;
        this.difficulty = difficulty;
        this.easy = easy;
        easy.addActionListener(this);
        this.medium = medium;
        medium.addActionListener(this);
        this.hard = hard;
        hard.addActionListener(this);
        this.help = help;
        this.helpDialogue = helpDialogue;
        this.quit = quit;
        quit.addActionListener(this);
        this.trapCounter = trapCounter;
        this.timeCounter = timeCounter;

        previousTime = System.currentTimeMillis();

        settings.add(difficulty);
        settings.add(menuTraps);
        settings.add(timeCounter);
        difficulty.add(easy);
        difficulty.add(medium);
        difficulty.add(hard);

        for (int i = 0; i < 75; i++) {
            this.menuTraps = menuTraps;
            menuTraps.addActionListener(this);
        }

        mb.add(start);
        mb.add(settings);
        mb.add(help);
        mb.add(quit);
        mb.add(timeCounter);
        mb.add(trapCounter);

        setupBoard();
    }


    /*method.... SETUP BOARD: Just makes an array of images and sets board size, adds moust listener*/
    private void setupBoard() {

        setPreferredSize(new Dimension(boardWidth, boardHeight)); // set size of board

        img = new Image[numImages]; // declare array for number images

        for (int i = 0; i < numImages; i++) {
            var path = i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new cellsClicked()); // add mouse listener to the board
        newGame(); // call new game
    }

    /* method... NEW GAME: This method sets up the mines randomly and increments all of the blocks around
     *  the mines by 1
     * */
    private void newGame() {
        // initialize how I did time and the first click variable
        firstClick = true;
        timeCounter.setText("");
        currentTime = System.currentTimeMillis();
        currentTime2 = 0;
        elapsedTime = 0;
        elapsedTime2 = 0;
        inGame = true;
        int cell;

        var random = new Random();

        trapsLeft = numTraps;

        allCells = numRows * numColumns; // calculate number of cells in game
        field = new int[allCells]; // new number array that holds values of the cells

        for (int i = 0; i < allCells; i++) {
            field[i] = cellCover;  // iterate through the array and set all cells to be covered
        }

        trapCounter.setText("Traps:" + trapsLeft); // set traps left

        int i = 0;
        while (i < numTraps) { // this loop is responsible randomly placing the mines in the field

            int position = (int) (allCells * random.nextDouble()); // find a random position to use for a mine

            if ((position < allCells) && (field[position] != coveredThermalCell)) {
                // if the position is in the array and it is not already a covered trap
                int current_col = position % numColumns; // find the current column by using modulo
                field[position] = coveredThermalCell; // make this a covered trap
                i++; // increment

                // here, we start incrementing all of the cells surrounding the trap
                // always check to make sure the cell is in the array, not outside of it
                // additionally, always check to make sure it isn't already a thermal trap
                if (current_col > 0) { // as long as it isn't in the first column, increment the ones to the left
                    cell = position - 1 - numColumns; // top left cell
                    if (cell >= 0) { // make sure the cell exists...
                        if (field[cell] != coveredThermalCell) { // make sure it isn't a thermal trap....
                            field[cell] += 1; // add to the counter, which indicates how many mines are around
                        }
                    }
                    cell = position - 1; // straight left cell
                    if (cell >= 0) {
                        if (field[cell] != coveredThermalCell) {
                            field[cell] += 1;
                        }
                    }

                    cell = position + numColumns - 1; // bottom left cell
                    if (cell < allCells) {
                        if (field[cell] != coveredThermalCell) {
                            field[cell] += 1;
                        }
                    }
                }

                cell = position - numColumns; // cell above
                if (cell >= 0) {
                    if (field[cell] != coveredThermalCell) {
                        field[cell] += 1;
                    }
                }

                cell = position + numColumns; // cell below
                if (cell < allCells) {
                    if (field[cell] != coveredThermalCell) {
                        field[cell] += 1;
                    }
                }

                if (current_col < (numColumns - 1)) { // As long as we aren't in the last column, increment
                    // the cells to the right of the traps
                    cell = position - numColumns + 1;  // the cell above and to the right
                    if (cell >= 0) {
                        if (field[cell] != coveredThermalCell) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + numColumns + 1; // cell below and to the right
                    if (cell < allCells) {
                        if (field[cell] != coveredThermalCell) {
                            field[cell] += 1;
                        }
                    }
                    cell = position + 1; // cell to the right
                    if (cell < allCells) {
                        if (field[cell] != coveredThermalCell) {
                            field[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    /* method... FIND EMPTY CELLS: This method is used when a blank cell is clicked.
     * It recursively calls itself in every direction of the blank cell. */
    private void find_empty_cells(int j) {

        int current_col = j % numColumns; // find current column
        int cell;

        if (current_col > 0) {  // if not in the first column.....
            cell = j - numColumns - 1; // cell above and to the left
            if (cell >= 0) {
                if (field[cell] > thermalCell) { // don't reveal thermal cells
                    field[cell] -= cellCover; // remove the cover and reveal #
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell); // if it is empty, call empty cells again
                    }
                }
            }

            cell = j - 1; // cell to the left
            if (cell >= 0) {
                if (field[cell] > thermalCell) {
                    field[cell] -= cellCover;
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + numColumns - 1; // cell below and to the left
            if (cell < allCells) {
                if (field[cell] > thermalCell) {
                    field[cell] -= cellCover;
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell);
                    }
                }
            }
        }

        cell = j - numColumns; // cell directly above
        if (cell >= 0) {
            if (field[cell] > thermalCell) {
                field[cell] -= cellCover;
                if (field[cell] == emptyCell) {
                    find_empty_cells(cell);
                }
            }
        }

        cell = j + numColumns; // cell directly below
        if (cell < allCells) {
            if (field[cell] > thermalCell) {
                field[cell] -= cellCover;
                if (field[cell] == emptyCell) {
                    find_empty_cells(cell);
                }
            }
        }

        if (current_col < (numColumns - 1)) { // if it's not in the last row
            cell = j - numColumns + 1; // cell above and to the right
            if (cell >= 0) {
                if (field[cell] > thermalCell) {
                    field[cell] -= cellCover;
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + numColumns + 1; // cell below and to the right
            if (cell < allCells) {
                if (field[cell] > thermalCell) {
                    field[cell] -= cellCover;
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell);
                    }
                }
            }

            cell = j + 1; // cell to the right
            if (cell < allCells) {
                if (field[cell] > thermalCell) {
                    field[cell] -= cellCover;
                    if (field[cell] == emptyCell) {
                        find_empty_cells(cell);
                    }
                }
            }
        }
    }


    /*Use paint component to draw images in correct coordinates*/
    @Override
    public void paintComponent(Graphics g) {

        int uncover = 0; // counter to see if all cells are uncovered yet (If game is won)

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) { // iterate through each row and column
                int cell = field[(i * numColumns) + j];
                if (inGame && cell == thermalCell) { // if in game and the cell clicked is a thermal cell, game over
                    inGame = false;
                }

                if (!inGame) { // if game is over, show all uncovered thermal traps
                    if (cell == coveredThermalCell) {
                        cell = drawThermal;
                    }
                    else if (cell > thermalCell) {
                        cell = drawCover;
                    }
                }
                else { // else draw covers if it is supposed to be covered
                    if (cell > thermalCell) {
                        cell = drawCover;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * cellSize), (i * cellSize), this); // draw images
            }
        }

        if (uncover == 0 && inGame) { // if the game has been won
            inGame = false;
            trapCounter.setText("Game won");
        }
        else if (!inGame) { // if the game has been lost
            trapCounter.setText("Game lost");
        }
    }

    private class cellsClicked extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX(); // get coordinates for the mouse press
            int y = e.getY();

            int cCol = x / cellSize; // find column and row
            int cRow = y / cellSize;

            boolean doRepaint = false; // flag for signaling a repaint

            if (!inGame) { // if not in a game start a new game
                newGame();
                repaint();
            }
            if ((cRow * numColumns) + cCol < allCells) { // make sure the click is in the board area (for easy and medium)
                if (field[(cRow * numColumns) + cCol] > coveredThermalCell) { // if the cell doesn't exist, do nothing
                    return;
                }

                if ((field[(cRow * numColumns) + cCol] > thermalCell)) { // if it's a covered cell...
                    // below is what I used to calculate the time elapsed, it only updates on click of a cell
                    currentTime2 = System.currentTimeMillis();
                    elapsedTime = ((currentTime2 - currentTime) / 1000.0);
                    elapsedTime2 += elapsedTime;
                    Math.round(elapsedTime2);
                    timeCounter.setText(elapsedTime2 + "s ");
                    currentTime = currentTime2;

                    field[(cRow * numColumns) + cCol] -= cellCover; // remove cover on array
                    doRepaint = true;

                    // if they clicked on a trap the first time, reset board
                    if (field[(cRow * numColumns) + cCol] == thermalCell && firstClick) {
                        newGame();
                        repaint();
                    } else // otherwise continue game and set the firstClick boolean to false
                        firstClick = false;

                    if (field[(cRow * numColumns) + cCol] == thermalCell) { // if it is a thermal cell, game over
                        inGame = false;
                    }

                    if (field[(cRow * numColumns) + cCol] == emptyCell) { // if it's empty, call empty cell
                        find_empty_cells((cRow * numColumns) + cCol);
                    }
                }
            }

            if (doRepaint) { // if repaint flag is true repaint
                repaint();
            }

        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) { // start a new game if start is clicked
            newGame();
            repaint();
        }
        // next 3 get sources allow difficulty changes
        if (e.getSource() == easy) {
            numTraps = 5;
            numColumns = 4;
            numRows = 4;
            boardWidth = numColumns * cellSize + 1;
            boardHeight = numRows * cellSize + 1;
            newGame();
            repaint();
        }
        if (e.getSource() == medium) {
            numTraps = 14;
            numColumns = 8;
            numRows = 8;
            boardWidth = numColumns * cellSize + 1;
            boardHeight = numRows * cellSize + 1;
            newGame();
            repaint();
        }
        if (e.getSource() == hard) {
            numTraps = 60;
            numColumns = 15;
            numRows = 15;
            boardWidth = numColumns * cellSize + 1;
            boardHeight = numRows * cellSize + 1;
            newGame();
            repaint();
        }
        // couldn't get trap number selection working
        /*JMenuItem trapOption = (JMenuItem)e.getSource();
        for (int i = 0; i < 75; i ++) {
            if (trapOption.getText() == "" + i) {
                numTraps = i;
                newGame();
                repaint();
                break;
            }
        }*/
        if (e.getSource() == quit) {
            System.exit(0);
        }

    }

}