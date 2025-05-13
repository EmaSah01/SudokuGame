package logic;

import java.util.*;

public class Sudoku {
    private int[][] board;
    private int[][] solutionGrid;
    private Random rand;

    public Sudoku() {
        this.board = new int[9][9];
        this.solutionGrid = new int[9][9];
        this.rand = new Random();
    }

    public int[][] emptyGrid() {
        return new int[9][9];
    }

    public boolean generateSolution(int[][] grid) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Collections.shuffle(numbers); // Shuffle numbers for randomness

        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;

            if (grid[row][col] == 0) {
                for (int num : numbers) {
                    if (isValidMove(grid, row, col, num)) {
                        grid[row][col] = num;

                        if (!hasEmptyCell(grid) || generateSolution(grid)) {
                            return true;
                        }

                        grid[row][col] = 0;
                    }
                }
                return false; // No valid numbers for this cell
            }
        }
        return true; 
    }

    private boolean hasEmptyCell(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] createPuzzle(int[][] solvedGrid, int numClues) {
        int[][] puzzle = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(solvedGrid[i], 0, puzzle[i], 0, 9);
        }

        int cluesToRemove = 81 - numClues;
        while (cluesToRemove > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (puzzle[row][col] != 0) {
                int temp = puzzle[row][col];
                puzzle[row][col] = 0;

                if (!hasUniqueSolution(puzzle)) {
                    puzzle[row][col] = temp; // Revert if the solution is no longer unique
                } else {
                    cluesToRemove--;
                }
            }
        }
        return puzzle;
    }

    private boolean hasUniqueSolution(int[][] grid) {
        int[][] tempGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, tempGrid[i], 0, 9);
        }
        return countSolutions(tempGrid) == 1;
    }

    private int countSolutions(int[][] grid) {
        int[][] tempGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, tempGrid[i], 0, 9);
        }
        return countSolutionsHelper(tempGrid);
    }

    private int countSolutionsHelper(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    int count = 0;
                    for (int num = 1; num <= 9; num++) {
                        if (isValidMove(grid, row, col, num)) {
                            grid[row][col] = num;
                            count += countSolutionsHelper(grid);
                            if (count > 1) return count; // More than one solution
                            grid[row][col] = 0;
                        }
                    }
                    return count;
                }
            }
        }
        return 1; // A complete solution was found
    }

    public boolean isValidMove(int[][] grid, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (grid[row][x] == num || grid[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    public int[][] getSolution() {
        return solutionGrid;
    }

    public int[][] generatePuzzle(int difficulty) {
        generateSolution(solutionGrid); 
        int numClues = determineClues(difficulty); 
        this.board = createPuzzle(solutionGrid, numClues); 
        return this.board;
    }

    private int determineClues(int difficulty) {
        switch (difficulty) {
            case 1: return 40; // Easy
            case 2: return 30; // Medium
            case 3: return 20; // Hard
            default: throw new IllegalArgumentException("Invalid difficulty level.");
        }
    }
}  
