import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

public class TicTacToeWithAI extends Frame {
    private Button[][] buttons;
    private boolean isPlayerX = true;
    private boolean isGameOver = false;

    public TicTacToeWithAI() {
        buttons = new Button[3][3];
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tic Tac Toe with AI");
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 48));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);
            }
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        setSize(300, 300);
        setVisible(true);
    }

    private void checkForWin() {
        String[] symbols = {"X", "O"};

        for (String symbol : symbols) {
            // Check rows, columns, and diagonals
            for (int i = 0; i < 3; i++) {
                if (buttons[i][0].getLabel().equals(symbol) &&
                    buttons[i][1].getLabel().equals(symbol) &&
                    buttons[i][2].getLabel().equals(symbol)) {
                    announceWinner(symbol);
                    return;
                }

                if (buttons[0][i].getLabel().equals(symbol) &&
                    buttons[1][i].getLabel().equals(symbol) &&
                    buttons[2][i].getLabel().equals(symbol)) {
                    announceWinner(symbol);
                    return;
                }
            }

            if (buttons[0][0].getLabel().equals(symbol) &&
                buttons[1][1].getLabel().equals(symbol) &&
                buttons[2][2].getLabel().equals(symbol)) {
                announceWinner(symbol);
                return;
            }

            if (buttons[0][2].getLabel().equals(symbol) &&
                buttons[1][1].getLabel().equals(symbol) &&
                buttons[2][0].getLabel().equals(symbol)) {
                announceWinner(symbol);
                return;
            }
        }

        if (isBoardFull()) {
            announceDraw();
        }
    }

    private boolean isBoardFull() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getLabel().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void announceWinner(String symbol) {
        showMessage(symbol + " wins!");
        isGameOver = true;
    }

    private void announceDraw() {
        showMessage("It's a draw!");
        isGameOver = true;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void performAiMove() {
        if (!isGameOver && !isPlayerX) {
            int[] bestMove = findBestMove();
            buttons[bestMove[0]][bestMove[1]].setLabel("O");
            isPlayerX = true;
            checkForWin();
        }
    }

    private int[] findBestMove() {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getLabel().isEmpty()) {
                    buttons[i][j].setLabel("O");
                    int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    buttons[i][j].setLabel("");
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        int score = evaluate();

        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (isBoardFull()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getLabel().isEmpty()) {
                        buttons[i][j].setLabel("O");
                        int currentScore = minimax(depth + 1, false, alpha, beta);
                        buttons[i][j].setLabel("");
                        bestScore = Math.max(bestScore, currentScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getLabel().isEmpty()) {
                        buttons[i][j].setLabel("X");
                        int currentScore = minimax(depth + 1, true, alpha, beta);
                        buttons[i][j].setLabel("");
                        bestScore = Math.min(bestScore, currentScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    private int evaluate() {
        String[][] board = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getLabel();
            }
        }

        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals("X") && board[row][1].equals("X") && board[row][2].equals("X")) {
                return -10;
            }
            if (board[row][0].equals("O") && board[row][1].equals("O") && board[row][2].equals("O")) {
                return 10;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals("X") && board[1][col].equals("X") && board[2][col].equals("X")) {
                return -10;
            }
            if (board[0][col].equals("O") && board[1][col].equals("O") && board[2][col].equals("O")) {
                return 10;
            }
        }

        if (board[0][0].equals("X") && board[1][1].equals("X") && board[2][2].equals("X")) {
            return -10;
        }
        if (board[0][0].equals("O") && board[1][1].equals("O") && board[2][2].equals("O")) {
            return 10;
        }

        if (board[0][2].equals("X") && board[1][1].equals("X") && board[2][0].equals("X")) {
            return -10;
        }
        if (board[0][2].equals("O") && board[1][1].equals("O") && board[2][0].equals("O")) {
            return 10;
        }

        return 0;
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void actionPerformed(ActionEvent e) {
            if (!isGameOver && isPlayerX && buttons[row][col].getLabel().isEmpty()) {
                buttons[row][col].setLabel("X");
                isPlayerX = false;
                checkForWin();
                performAiMove();
            }
        }
    }

    public static void main(String[] args) {
        new TicTacToeWithAI();
    }
}
