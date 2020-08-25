/*
 * Inherits the AStarSolver class and provides method implementation to solve the 
 * 8puzzle algorithm.
 * Dependent on AStarSolver and PuzzleState classes
 * 4/16/20
 */
package astar;

import java.util.ArrayList;

/**
 *
 * @author Joseph
 */
public class PuzzleSolver extends AStarSolver<PuzzleState> {

    /**
     * Returns whether two states are equal
     * @param current, the current state
     * @param goal, the goal state to be compared to
     * @return true or false
     */
    @Override
    public boolean isEqualTo(PuzzleState current, PuzzleState goal) {
        return current.isEqualTo(goal.getPuzzle());
    }

    /**
     * Calculates the next possible states based off the current node and closed nodes
     * @param pathCost, the pathCost of the current state
     * @param current, the current state
     * @param closedNodes, nodes already expanded
     * @param goalNode, the goal node 
     * @return a list of states
     */
    @Override
    public ArrayList<PuzzleState> calculateStates(int pathCost, PuzzleState current, ArrayList<PuzzleState> closedNodes, PuzzleState goalNode) {
        
        ArrayList<PuzzleState> list = new ArrayList<>();
        PuzzleState upPuzzle = current.moveUp();
        if (upPuzzle != null && !listContains(closedNodes, upPuzzle.getPuzzle())) {
            upPuzzle.setAction("Up");
            upPuzzle.calculateHeursitic(pathCost, goalNode.getPuzzle());
            list.add(upPuzzle);
        }

        PuzzleState downPuzzle = current.moveDown();
        if (downPuzzle != null && !listContains(closedNodes, downPuzzle.getPuzzle())) {
            downPuzzle.setAction("Down");
            downPuzzle.calculateHeursitic(pathCost, goalNode.getPuzzle());
            list.add(downPuzzle);
        }

        PuzzleState rightPuzzle = current.moveRight();
        if (rightPuzzle != null && !listContains(closedNodes, rightPuzzle.getPuzzle())) {
            rightPuzzle.setAction("Right");
            rightPuzzle.calculateHeursitic(pathCost, goalNode.getPuzzle());
            list.add(rightPuzzle);
        }

        PuzzleState leftPuzzle = current.moveLeft();
        if (leftPuzzle != null && !listContains(closedNodes, leftPuzzle.getPuzzle())) {
            leftPuzzle.setAction("Left");
            leftPuzzle.calculateHeursitic(pathCost, goalNode.getPuzzle());
            list.add(leftPuzzle);
        }
        return list;
    }
    
    /**
     * Checks if a puzzle state list contains a puzzle
     *
     * @param closeSet the list to check
     * @param puzzle, the puzzle to check
     * @return boolean true or false
     */
    public static boolean listContains(ArrayList<PuzzleState> closeSet, int[][] puzzle) {
        return closeSet.stream().anyMatch((PuzzleState node) -> (node.isEqualTo(puzzle)));
    }
    
    /**
     * Static method to show standard usage of AStar for EightPuzzle
     *
     * @param initial, the initial puzzle
     * @param goal, the goal puzzle
     * @return, the solution as a list of PuzzleStates
     */
    public static ArrayList<PuzzleState> AStar(int[][] initial, int[][] goal) {
        PuzzleSolver solver = new PuzzleSolver();
        ArrayList<PuzzleState> list = solver.AStar(new PuzzleState(initial), new PuzzleState(goal));
        return list;
    }

    /* An example of the classes being used*/
    public static void main(String[] args) {
        
        //final long startTime = System.currentTimeMillis()

        //solved in 24 steps.
        int[][] goal = {{8, 3, 2}, 
                        {4, 7, 1}, 
                        {0, 5, 6}};
        int[][] initial = {{0, 1, 2}, 
                           {3, 4, 5}, 
                           {6, 7, 8}};
        
        if(PuzzleState.isSolvable(initial, goal) && PuzzleState.properZeros(initial) 
                && PuzzleState.properZeros(goal)) {
            System.out.println("Solvable");
            ArrayList<PuzzleState> list = AStar(initial, goal);
            list.forEach((state) -> {
                System.out.println(state.getAction());
                PuzzleState.printPuzzle(state);
            });
            int size = list.size() - 1;
            System.out.println("Solved in " + size + " steps.");
        } else {
            System.out.println("Not Solvable");
        }
        
        //final long endTime = System.currentTimeMillis();
        //System.out.println("Total execution time: " + (endTime - startTime));
        
        //100 ms runtime
    }
    
}
