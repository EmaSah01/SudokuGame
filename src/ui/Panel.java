 package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Panel extends JPanel {
    private int[][] board;  
    private int[][] solution; 
    private int mistakes;  
    private boolean gameOver;   
    private JLabel mistakesLabel;  

    public Panel() {
        this.board = new int[9][9];  
        this.solution = new int[9][9];  
        this.mistakes = 0;  
        this.gameOver = false;  

        this.mistakesLabel = new JLabel("Mistakes: 0");
        this.mistakesLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        setPreferredSize(new Dimension(500, 500));  
        setBackground(Color.WHITE);  

        addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        	    if (gameOver) return;  

        	    int cellSize = Math.min(getWidth(), getHeight()) / 9;
        	    int row = e.getY() / cellSize;
        	    int col = e.getX() / cellSize;

        	    if (row >= 9) row = 8;
        	    if (col >= 9) col = 8;

        	    if (board[row][col] == 0) {  
        	        String input = JOptionPane.showInputDialog(Panel.this, "Enter number (1-9):");
        	        if (input == null || !input.matches("[1-9]")) {
        	            JOptionPane.showMessageDialog(Panel.this, "Invalid input. Please enter a number between 1 and 9.");
        	            return;
        	        }
        	        int num = Integer.parseInt(input);
        	        if (num == solution[row][col]) {
        	            board[row][col] = num;  
        	        } else {
        	            mistakes++;  
        	            mistakesLabel.setText("Mistakes: " + mistakes);
        	            if (mistakes >= 30) {
        	                gameOver = true;  
        	                JOptionPane.showMessageDialog(Panel.this, "Game Over! You made 3 mistakes.");
        	            }
        	        }
        	        repaint();  
        	    }
        	}

        });
    }

    public void setBoard(int[][] newBoard, int[][] solvedBoard) {
        this.board = newBoard;
        this.solution = solvedBoard;
        this.mistakes = 0;
        this.gameOver = false;
        mistakesLabel.setText("Mistakes: 0");
        repaint();  
    }

    public void showSolution() {
        if (!gameOver) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    board[row][col] = solution[row][col]; 
                }
            }
            gameOver = true;  
            repaint();  
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellSize = Math.min(getWidth(), getHeight()) / 9;

        int boardWidth = cellSize * 9;
        int boardHeight = cellSize * 9;
        int xOffset = (getWidth() - boardWidth) / 2; 
        int yOffset = (getHeight() - boardHeight) / 2; 

        drawGrid(g, cellSize, xOffset, yOffset);

        drawNumbers(g, cellSize, xOffset, yOffset);

        if (gameOver) {
            drawGameOver(g);
        }
    }

    private void drawGrid(Graphics g, int cellSize, int xOffset, int yOffset) {
        for (int i = 0; i <= 9; i++) {
            g.setColor(i % 3 == 0 ? Color.BLACK : Color.LIGHT_GRAY);
            int pos = i * cellSize;
            g.drawLine(xOffset + pos, yOffset, xOffset + pos, yOffset + cellSize * 9); // Vertikalne linije
            g.drawLine(xOffset, yOffset + pos, xOffset + cellSize * 9, yOffset + pos); // Horizontalne linije
        }
    }

    private void drawNumbers(Graphics g, int cellSize, int xOffset, int yOffset) {
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != 0) {
                    String num = String.valueOf(board[row][col]);
                    FontMetrics fm = g.getFontMetrics();
                    int x = xOffset + col * cellSize + (cellSize - fm.stringWidth(num)) / 2;
                    int y = yOffset + row * cellSize + (cellSize + fm.getAscent() - fm.getDescent()) / 2;
                    g.setColor(Color.BLACK);
                    g.drawString(num, x, y);
                }
            }
        }
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        String message = "GAME OVER!";
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = (getHeight() + fm.getAscent()) / 2;
        g.drawString(message, x, y);
    }


    // Getter for mistakes label
    public JLabel getMistakesLabel() {
        return mistakesLabel;
    }
} 
