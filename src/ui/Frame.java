package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import logic.Sudoku;

public class Frame extends JFrame {
	
	private JLabel timerLabel;
	private Timer timer;
	private int elapsedTime;

    public Frame() {
        setLayout(new BorderLayout());

        Panel gamePanel = new Panel();  
        this.add(gamePanel, BorderLayout.CENTER);  

        Border buttonBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        
        JPanel buttonPanel = new JPanel();  
        JButton newGameButton = new JButton("Start New Game");
        JButton solutionButton = new JButton("See the Solution");
        
        newGameButton.setForeground(Color.BLACK); 
        newGameButton.setBorder(buttonBorder);
        newGameButton.setFocusPainted(false); 

        solutionButton.setForeground(Color.BLACK); 
        solutionButton.setBorder(buttonBorder);
        solutionButton.setFocusPainted(false);
        
        buttonPanel.add(newGameButton);  
        buttonPanel.add(solutionButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 
        this.add(buttonPanel, BorderLayout.SOUTH);  

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0)); 

        JLabel mistakesLabel = gamePanel.getMistakesLabel();
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(mistakesLabel);
        topPanel.add(timerLabel);
        this.add(topPanel, BorderLayout.NORTH);
        
        elapsedTime = 0;
        timer = new Timer(1000, e -> {
            elapsedTime++;
            int minutes = elapsedTime / 60;
            int seconds = elapsedTime % 60;
            timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
        });
        timer.start();

        Sudoku sudoku = new Sudoku();

        int[][] puzzle = sudoku.generatePuzzle(2);  
        int[][] solution = sudoku.getSolution();  

        System.out.println("Sudoku Solution:");
        sudoku.printGrid(solution);

        gamePanel.setBoard(puzzle, solution);

        this.setTitle("Sudoku");
        setPreferredSize(new Dimension(600, 720));  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        newGameButton.addActionListener(e -> {
            int[][] newPuzzle = sudoku.generatePuzzle(2);
            int[][] newSolution = sudoku.getSolution();
            gamePanel.setBoard(newPuzzle, newSolution);
            elapsedTime = 0; 
            timer.restart();
        });

        solutionButton.addActionListener(e -> {
            gamePanel.showSolution();
            timer.stop(); 
        });
    }

    public static void main(String[] args) {
        new Frame();  
    }
}


