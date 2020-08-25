/*
 * An object of this class represents each board state for the epuzzle problem
 * 4/15/20
 */
package astar;

import java.util.Arrays;

/**
 *
 * @author Joseph
 */
public class PuzzleState extends AStarSolver.AIState {

    /* Static variable to store the size of the board to solve, can be changed to any board size*/
    public static final int BOARD_SIZE = 3;
    /* The NxN puzzle for each state */
    private final int[][] puzzle;
    /* The action taken to get to a state*/
    private String action = "";
    /* The position of the 0 */
    private final int row;
    private final int col;

    /**
     * @param puzzle, a N x N column of any length, assuming row and column length is the same
     */
    public PuzzleState(int[][] puzzle) {
        this.puzzle = puzzle;
        //calculate the position of 0 in current
        int rowIn = -1;
        int colIn = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (puzzle[i][j] == 0) {
                    rowIn = i;
                    colIn = j;
                    break;
                }
            }
        }
        this.row = rowIn;
        this.col = colIn;
    }
    
    /**
     * @param puzzle, a N x N column of any length, assuming row and column length is the same
     * @param rowIn, the row 0 is on
     * @param colIn, the column 0 is on 
     */
    public PuzzleState(int[][] puzzle, int rowIn, int colIn) {
        this.puzzle = puzzle;
        this.row = rowIn;
        this.col = colIn;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public String getAction() {
        return action;
    }

    public boolean containsAction() {
        return !action.equals("");
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * Gets a version of the 8puzzle shifted up
     *
     * @return 8puzzle shifted up, or null if it cannot be shifted
     */
    public PuzzleState moveUp() {
        if (row - 1 < 0) {
            return null;
        }
        int[][] arrPuzz = cloneArray(this.puzzle);

        int temp = arrPuzz[row][col];
        arrPuzz[row][col] = arrPuzz[row - 1][col];
        arrPuzz[row - 1][col] = temp;

        return new PuzzleState(arrPuzz, row-1, col);
    }

    /**
     * Gets a version of the 8puzzle shifted down
     *
     * @return 8puzzle shifted down, or null if it cannot be shifted
     */
    public PuzzleState moveDown() {
        if (row + 1 > BOARD_SIZE-1) {
            return null;
        }
        int[][] arrPuzz = cloneArray(this.puzzle);

        int temp = arrPuzz[row][col];
        arrPuzz[row][col] = arrPuzz[row + 1][col];
        arrPuzz[row + 1][col] = temp;

        return new PuzzleState(arrPuzz, row+1, col);
    }

    /**
     * Gets a version of the 8puzzle shifted left
     *
     * @return 8puzzle shifted left, or null if it cannot be shifted
     */
    public PuzzleState moveLeft() {
        if (col - 1 < 0) {
            return null;
        }
        int[][] arrPuzz = cloneArray(this.puzzle);

        int temp = arrPuzz[row][col];
        arrPuzz[row][col] = arrPuzz[row][col - 1];
        arrPuzz[row][col - 1] = temp;

        return new PuzzleState(arrPuzz, row, col-1);
    }

    /**
     * Gets a version of the 8puzzle shifted right
     * 
     * @return 8puzzle shifted right, or null if it cannot be shifted
     */
    public PuzzleState moveRight() {
        if (col + 1 > BOARD_SIZE-1) {
            return null;
        }
        int[][] arrPuzz = cloneArray(this.puzzle);

        int temp = arrPuzz[row][col];
        arrPuzz[row][col] = arrPuzz[row][col + 1];
        arrPuzz[row][col + 1] = temp;

        return new PuzzleState(arrPuzz, row, col+1);
    }

    /**
     * Calculates the heuristic value for a puzzle state using manhattan distance,
     * and sets the path cost.
     *
     * @param prevCost, the cost of the previous state
     * @param goalpuzzle, the goal puzzle used to calculate heuristic value
     */
    public void calculateHeursitic(int prevCost, int[][] goalpuzzle) {
        this.g = ++prevCost;
        int h = 0;
        //calculate the manhattan distance for each different tile and add it to the g value
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int k = 0; k < BOARD_SIZE; k++) {
                    for (int l = 0; l < BOARD_SIZE; l++) {
                        if (goalpuzzle[k][l] == this.puzzle[i][j] && goalpuzzle[k][l] != 0) {
                            h = h + calcManhattanDistance(i, j, k, l);
                        }
                    }
                }
            }
        }
        this.f = g + h;
    }

    /**
     * Checks whether two 8puzzles are equal
     *
     * @param puzzle, the puzzle to check
     * @return True or false
     */
    public boolean isEqualTo(int[][] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if (this.puzzle[i][j] != puzzle[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calculates the Manhattan distance between two tiles on a 2D grid
     *
     * @param x1 x value of point1
     * @param y1 y value of point1
     * @param x2 x value of point2
     * @param y2 y value of point2
     * @return Returns the Manhattan distance
     */
    public static int calcManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    /**
     * Clones the provided array
     *
     * @param src, the array to be cloned
     * @return a new clone of the provided array
     */
    public static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    /**
     * Checks if two puzzles match
     *
     * @param start, the first puzzle
     * @param goal, the second puzzle to compare
     * @return boolean true or false
     */
    public static boolean puzzlesMatch(int[][] start, int[][] goal) {
        int[] startone = new int[BOARD_SIZE * BOARD_SIZE];
        int[] goalone = new int[BOARD_SIZE * BOARD_SIZE];

        int counter = 0;
        for (int[] start1 : start) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                startone[counter] = start1[j];
                counter++;
            }
        }

        counter = 0;
        for (int[] goal1 : goal) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                goalone[counter] = goal1[j];
                counter++;
            }
        }

        Arrays.sort(startone);
        Arrays.sort(goalone);

        return Arrays.equals(startone, goalone);
    }

    /**
     * Prints a puzzlestate
     *
     * @param puzzlestate, the puzzlestate to print
     */
    public static void printPuzzle(PuzzleState puzzlestate) {
        int[][] puzzle = puzzlestate.puzzle;
        for (int[] puzzle1 : puzzle) {
            System.out.print("{");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(puzzle1[j]);
                System.out.print(",");
            }
            System.out.println("}");
        }
        System.out.println();
    }

    /**
     * Checks if the there is one 0 in the state
     *
     * @param start, the puzzle to check
     * @return boolean true or false
     */
    public static boolean properZeros(int[][] start) {
        int counter = 0;
        for (int[] start1 : start) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (start1[j] == 0) {
                    counter++;
                }
            }
        }
        return counter == 1;
    }

    /**
     * Finds the position of the 0 from bottom
     * @param puzzle to check
     * @return the position of the 0
     */
    public static int find0Position(int puzzle[][]) { 
        for (int i = BOARD_SIZE  - 1; i >= 0; i--) {
            for (int j = BOARD_SIZE  - 1; j >= 0; j--) {
                if (puzzle[i][j] == 0) 
                    return BOARD_SIZE - i; 
            }
        }
        return -1;
    } 
  
    
    /**
     * Checks if a pair is solvable
     * @param start, the initial puzzle
     * @param goal, the goal puzzle
     * @return true or false
     */
    public static boolean isSolvable(int[][] start, int[][] goal) {
        int invCount = calcInvCount(start, goal);
        if(BOARD_SIZE % 2 == 0) {
            int zeroStart = find0Position(start);
            if(zeroStart % 2 == 0 && !(invCount % 2 == 0)) {
                return true;
            }
            else if (!(zeroStart % 2 == 0) && (invCount % 2 == 0)){
                return true;
            }
            return false;
        }
        else {
            return invCount % 2 == 0; 
        }
    }
    
    /**
     * Calculates the number of inversions for a board setting
     * @param start, the initial puzzle
     * @param goal, the goal puzzle
     * @return the number of inversions
     */
    public static int calcInvCount(int[][] start, int[][] goal) 
    { 
        int[] startone = new int[BOARD_SIZE  * BOARD_SIZE];
        int[] goalone = new int[BOARD_SIZE  * BOARD_SIZE];

        int counter = 0;
        for (int[] start1 : start) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                startone[counter] = start1[j];
                counter++;
            }
        }

        counter = 0;
        for (int[] goal1 : goal) {
            for (int j = 0; j < goal.length; j++) {
                goalone[counter] = goal1[j];
                counter++;
            }
        }

        int inversions = 0;
        for(int i = 0; i < BOARD_SIZE -1; i++) {
            for(int j = i+1; j < BOARD_SIZE ; j++) {
               int startnum1 = startone[i];
               int startnum2 = startone[j];
               int goalpos1 = -1;
               int goalpos2 = -1;
               if(startnum1 != 0 && startnum2 != 0) {
                   for (int l = 0; l < BOARD_SIZE; l++) {
                       if(startnum1 == goalone[l]) {
                           goalpos1 = l;
                       }
                       else if(startnum2 == goalone[l]) {
                           goalpos2 = l;
                       }
                   }
                   if(!(goalpos2 > goalpos1)) {
                        inversions++;
                   }
               }
            }
        }
        
        return inversions;
    } 
}
