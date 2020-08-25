/*
 * A generic class to solve pathfinding problems using the A* pathfinding algorithm
 * To see an example of this, refer to the PuzzleState class
 * Works independently of PuzzleSolve and PuzzleState classes, dependent on AIState class
 * 4/14/20
 */
package astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author Joseph
 * @param <T>, object represents an AI state
 */
public abstract class AStarSolver<T extends AStarSolver.AIState> {
    
    public static class AIState {

        public int f = 0;
        public int g = 0;

    }
    
    private class Tree {
        
        private final Node root;
    
        /**
         * @param data for the root of the tree
         */
        private Tree(T data) {
            root = new Node(data);
        }

        /*
         *    Implementation of a parent pointer non-binary tree used in A star algorithm
         *    Each node represents a state and the heuristic values
         */
    
        private class Node {

            private final T data;
            private Node parent = null;

            /**
             * @param data, the data to be added to the node
             */
            private Node(T data) {
                this.data = data;
            }

            /**
             * @param node
             * @param parent 
             */
            private void addChild(Node node) {
                node.parent = this;
            }
        }
        
        /**
         * Uses the AStar algorithm to search the tree
         * @param goal, the goal to search for
         * @return the shortest path
         */
        private ArrayList<T> AStar(T goal) {
            ArrayList<T> closedSet = new ArrayList<>();
            closedSet.add(this.root.data);
            PriorityQueue<Tree.Node> openSet = new PriorityQueue<>(1, new NodeComparator());
            openSet.add(this.root);
            while(!openSet.isEmpty()) {
                Tree.Node current = openSet.poll();
                closedSet.add(current.data);
                if(isEqualTo(current.data, goal)) {
                    return reconstructPath(current);
                }
                ArrayList<T> toQueue = calculateStates(current.data.g, current.data, closedSet, goal);
                toQueue.forEach((T state) -> {
                    Tree.Node node = new Tree.Node(state);
                    openSet.add(node);
                    current.addChild(node);
                });
            }
            return null;
        }
        
        /**
         * Reconstructs the shortest path from the tree, used in AStar()
         * @param bottomLeaf, the leaf to climb up from
         * @return the shortest path
         */
        private ArrayList<T> reconstructPath(Tree.Node bottomLeaf) {
            boolean atRoot = false;
            Tree.Node current = bottomLeaf;
            ArrayList<T> list = new ArrayList<>();
            while (!atRoot) {
                list.add(current.data);
                if (current.parent != null) {
                    current = current.parent;
                } else {
                    atRoot = true;
                }
            }
            Collections.reverse(list);
            return list;
         }

    }
    
    /** 
     * Constructs a tree and finds the shortest path to goal with AStar algorithm
     * @param initial, the initial state to calculate from
     * @param goal, the goal state to calculate to
     * @return the shortest path
     */
    public ArrayList<T> AStar(T initial, T goal) {
        Tree tree = new Tree(initial);
        return tree.AStar(goal);
    }
     

    private class NodeComparator implements Comparator<Tree.Node>{

        /**
         * Implementation of comparator used for Queue
         * @param o1, the first node to compare
         * @param o2, the second node to compare
         * @return -1 if o1 is less, 1 if o1 is more, 0 if equal
         */
        @Override
        public int compare(Tree.Node o1, Tree.Node o2) {
            if(o1.data.f < o2.data.f) {
                return -1;
            } else if(o1.data.f > o2.data.f){
                return 1;
            }   
            return 0;
        }
        
    }
    
    /**
     * Returns whether two states are equal
     * @param current, the current state
     * @param goal, the goal state to be compared to
     * @return true or false
     */
    public abstract boolean isEqualTo(T current, T goal);
    
    /**
     * Calculates the next possible states based off the current node and closed nodes
     * @param pathCost, the pathCost of the current state
     * @param current, the current state
     * @param closedNodes, nodes already expanded
     * @param goalNode, the goal node 
     * @return a list of states
     */
    public abstract ArrayList<T> calculateStates(int pathCost, T current, ArrayList<T> closedNodes, T goalNode);
    
  
}
