// STUDENT NAME - H.K Pramuditha Dilshan
// UOW NO -W1956186
// IIT NO -20221528

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

// Define a Cell class to represent a cell in the maze
class Cell {
    int x, y;// coordinates of the cell
    String direction;//direction taken to reach this cell from its parent
    Cell parent;// parent cell

    // Constructor to initialize a cell with its coordinates, direction, and parent
    public Cell(int x, int y, String direction, Cell parent) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.parent = parent;
    }
}

public class SliddingPuzzle {
    private static char[][] maze;// 2D  array to represent the maze
    private static int rows, cols;// number of rows and columns in the maze
    private static Cell start, finish;// starting and finishing cells in the maze

     //Main method to start the program
     public static void main(String[] args) {
         readMazeFromFile("Maze.txt");
         findStartAndFinish();

         long startTime = System.nanoTime(); // Record start time
         Cell solution = bfs();
         long endTime = System.nanoTime(); // Record end time

         printSolution(solution);

         // Calculate and print execution time in seconds
         double executionTimeInSeconds = (endTime - startTime) / 1e9;
         System.out.printf("Execution time: %.6f seconds\n", executionTimeInSeconds);
}


    //Read the maze from a file
    private static void readMazeFromFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));//create  a scanner to read from the file
            rows = 0;
            // Count the number of rows and find the number of columns in the maze
            while (scanner.hasNextLine()) {
                rows++;
                String line = scanner.nextLine();
                if (cols == 0) cols = line.length();
            }
            scanner.close(); // close the scanner

            // Initialize the maze array with the correct dimensions
            maze = new char[rows][cols];
            scanner = new Scanner(new File(filename)); // create a new scanner to read from the file
            // Read the maze character by character and store it in the maze array
            for (int i = 0; i < rows; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < cols; j++) {
                    maze[i][j] = line.charAt(j);
                }
            }
            scanner.close(); // Close the Scanner
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // print the error message if the file is not found
        }
    }

    // Find the starting and finishing cells in the maze
    private static void findStartAndFinish() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    start = new Cell(j, i, "", null); // set the starting cell
                } else if (maze[i][j] == 'F') {
                    finish = new Cell(j, i, "", null); //set the finishing cell
                }
            }
        }
    }

    // Find the solution to the maze using Breadth-First Search
    private static Cell bfs() {
        boolean[][] visited = new boolean[rows][cols];// array to keep track of visited cells
        Queue<Cell> queue = new ArrayDeque<>();// Initializing queue for BFS traversal
        queue.offer(start);// enqueue the starting cell
        visited[start.y][start.x] = true; // mark the starting cell as visited

        // Arrays for moving in four directions: down, up, right, left
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        String[] directionSymbol = {"Down", "Up", "Right", "Left"};

        // Perform BFS traversal until the queue is empty
        while (!queue.isEmpty()) {
            Cell current = queue.poll();//dequeue a cell
            // If the current cell is the finishing cell, return it
            if (current.x == finish.x && current.y == finish.y) {
                return current;
            }
            // Explore neighboring cells in all four directions
            for (int i = 0; i < 4; i++) {
                int nx = current.x;
                int ny = current.y;
                // Move in the current direction until hitting a wall or the maze boundary
                while (true) {
                    nx += dx[i];
                    ny += dy[i];
                    if (!isValid(nx, ny) || maze[ny][nx] == '0') {
                        break;
                    }
                    // If the finishing cell is reached, return the solution
                    if (maze[ny][nx] == 'F') {
                        return new Cell(nx, ny, directionSymbol[i], current);
                    }
                }
                nx -= dx[i];
                ny -= dy[i];
                // If the neighboring cell is not visited, enqueue it and mark it as visited
                if (!visited[ny][nx]) {
                    queue.offer(new Cell(nx, ny, directionSymbol[i], current));
                    visited[ny][nx] = true;
                }
            }
        }
        return null; // No solution found
    }

    // Check if a cell is within the maze boundaries
    private static boolean isValid(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    // Print the solution path
    private static void printSolution(Cell solution) {
        System.out.println("SLIDDING PUZZLE GAME" + "\n");
        if (solution == null) {
            System.out.println("No solution found.");// If no solution is found, print an error message
            return;
        }
        Stack<String> steps = new Stack<>();// Stack to store the steps of the solution path
        // Traverse back from the finishing cell to the starting cell and push the steps onto the stack
        while (solution.parent != null) {
            steps.push("Move " + solution.direction + " to (" + (solution.x + 1) + ", " + (solution.y + 1) + ")");
            solution = solution.parent;
        }
        steps.push("Start at (" + (start.x + 1) + ", " + (start.y + 1) + ")");
        int stepNumber = 1;
        // Pop each step from the stack and print it along with the step number
        while (!steps.isEmpty()) {
            System.out.println(stepNumber++ + ". " + steps.pop());
        }
        System.out.println(stepNumber++ + ". " + "Done !"); // Print "Done!" as the final step
    }
}
