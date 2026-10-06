import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * GUI frame for the Rock Paper Scissors game.
 */
public class RockPaperScissorsFrame extends JFrame
{
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JButton quitButton;

    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;

    private JTextArea resultsArea;

    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    private int playerRockCount = 0;
    private int playerPaperCount = 0;
    private int playerScissorsCount = 0;

    private String lastPlayerMove = "";

    private Random random = new Random();

    private Strategy cheatStrategy = new Cheat();
    private Strategy randomStrategy = new RandomStrategy();

    private Strategy leastUsedStrategy = new LeastUsed();
    private Strategy mostUsedStrategy = new MostUsed();
    private Strategy lastUsedStrategy = new LastUsed();

    /**
     * Creates the Rock Paper Scissors GUI.
     */
    public RockPaperScissorsFrame()
    {
        setTitle("Rock Paper Scissors Game");

        setSize(700, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        createButtonPanel();

        createStatsPanel();

        createResultsPanel();
    }

    /**
     * Creates the Rock, Paper, Scissors and Quit buttons.
     */
    private void createButtonPanel()
    {
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));

        buttonPanel.setBorder(
                new TitledBorder("Choose Your Move"));

        ImageIcon rockIcon = new ImageIcon("src/rock.png");
        ImageIcon paperIcon = new ImageIcon("src/paper.png");
        ImageIcon scissorsIcon = new ImageIcon("src/scissors.png");

        Image rockImage = rockIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image paperImage = paperIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image scissorsImage = scissorsIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        rockButton = new JButton("Rock", new ImageIcon(rockImage));
        paperButton = new JButton("Paper", new ImageIcon(paperImage));
        scissorsButton = new JButton("Scissors", new ImageIcon(scissorsImage));

        rockButton.setHorizontalTextPosition(JButton.CENTER);
        rockButton.setVerticalTextPosition(JButton.BOTTOM);

        paperButton.setHorizontalTextPosition(JButton.CENTER);
        paperButton.setVerticalTextPosition(JButton.BOTTOM);

        scissorsButton.setHorizontalTextPosition(JButton.CENTER);
        scissorsButton.setVerticalTextPosition(JButton.BOTTOM);

        quitButton = new JButton("Quit");
        // One ActionListener for Rock, Paper, and Scissors
        ActionListener gameListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                String playerMove = "";

                if (ae.getSource() == rockButton)
                {
                    playerMove = "R";
                }
                else if (ae.getSource() == paperButton)
                {
                    playerMove = "P";
                }
                else if (ae.getSource() == scissorsButton)
                {
                    playerMove = "S";
                }

                playGame(playerMove);
            }
        };

        rockButton.addActionListener(gameListener);
        paperButton.addActionListener(gameListener);
        scissorsButton.addActionListener(gameListener);

        quitButton.addActionListener(e ->
        {
            JOptionPane.showMessageDialog(
                    this,
                    "Thanks for playing!");

            System.exit(0);
        });

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the statistics panel.
     */
    private void createStatsPanel()
    {
        JPanel statsPanel = new JPanel();

        statsPanel.setLayout(new GridLayout(3, 2, 5, 5));

        statsPanel.setBorder(
                new TitledBorder("Game Statistics"));

        JLabel playerWinsLabel =
                new JLabel("Player Wins:");

        JLabel computerWinsLabel =
                new JLabel("Computer Wins:");

        JLabel tiesLabel =
                new JLabel("Ties:");

        playerWinsField = new JTextField("0");
        computerWinsField = new JTextField("0");
        tiesField = new JTextField("0");

        playerWinsField.setEditable(false);
        computerWinsField.setEditable(false);
        tiesField.setEditable(false);

        statsPanel.add(playerWinsLabel);
        statsPanel.add(playerWinsField);

        statsPanel.add(computerWinsLabel);
        statsPanel.add(computerWinsField);

        statsPanel.add(tiesLabel);
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.WEST);
    }

    /**
     * Creates the results text area.
     */
    private void createResultsPanel()
    {
        resultsArea = new JTextArea();

        resultsArea.setEditable(false);

        resultsArea.setLineWrap(true);

        resultsArea.setWrapStyleWord(true);

        JScrollPane scrollPane =
                new JScrollPane(resultsArea);

        scrollPane.setBorder(
                new TitledBorder("Game Results"));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Plays one round of Rock Paper Scissors.
     *
     * @param playerMove player's move
     */
    private void playGame(String playerMove)
    {
        int probability = random.nextInt(100) + 1;

        Strategy selectedStrategy;
        String strategyName;

        if (probability <= 10)
        {
            selectedStrategy = cheatStrategy;
            strategyName = "Cheat";
        }
        else if (probability <= 30)
        {
            selectedStrategy = leastUsedStrategy;
            strategyName = "Least Used";
        }
        else if (probability <= 50)
        {
            selectedStrategy = mostUsedStrategy;
            strategyName = "Most Used";
        }
        else if (probability <= 70)
        {
            // Last Used cannot be used on the first round
            if (lastPlayerMove.equals(""))
            {
                selectedStrategy = randomStrategy;
                strategyName = "Random";
            }
            else
            {
                selectedStrategy = lastUsedStrategy;
                strategyName = "Last Used";
            }
        }
        else
        {
            selectedStrategy = randomStrategy;
            strategyName = "Random";
        }

        String computerMove =
                selectedStrategy.getMove(playerMove);

        String result =
                determineWinner(
                        playerMove,
                        computerMove,
                        strategyName);

        resultsArea.append(result + "\n");

        // Record current player's move
        if (playerMove.equals("R"))
        {
            playerRockCount++;
        }
        else if (playerMove.equals("P"))
        {
            playerPaperCount++;
        }
        else if (playerMove.equals("S"))
        {
            playerScissorsCount++;
        }

        lastPlayerMove = playerMove;

        updateStats();
    }

    /**
     * Determines the winner of the game.
     *
     * @param playerMove player's move
     * @param computerMove computer's move
     * @param strategyName strategy used
     * @return result message
     */
    private String determineWinner(
            String playerMove,
            String computerMove,
            String strategyName)
    {
        if (playerMove.equals(computerMove))
        {
            ties++;

            return moveName(playerMove)
                    + " ties "
                    + moveName(computerMove)
                    + ". (Tie! Computer: "
                    + strategyName + ")";
        }

        if (playerMove.equals("R")
                && computerMove.equals("S"))
        {
            playerWins++;

            return "Rock breaks Scissors. "
                    + "(Player wins! Computer: "
                    + strategyName + ")";
        }

        if (playerMove.equals("P")
                && computerMove.equals("R"))
        {
            playerWins++;

            return "Paper covers Rock. "
                    + "(Player wins! Computer: "
                    + strategyName + ")";
        }

        if (playerMove.equals("S")
                && computerMove.equals("P"))
        {
            playerWins++;

            return "Scissors cuts Paper. "
                    + "(Player wins! Computer: "
                    + strategyName + ")";
        }

        computerWins++;

        if (computerMove.equals("R")
                && playerMove.equals("S"))
        {
            return "Rock breaks Scissors. "
                    + "(Computer wins! Computer: "
                    + strategyName + ")";
        }

        if (computerMove.equals("P")
                && playerMove.equals("R"))
        {
            return "Paper covers Rock. "
                    + "(Computer wins! Computer: "
                    + strategyName + ")";
        }

        return "Scissors cuts Paper. "
                + "(Computer wins! Computer: "
                + strategyName + ")";
    }

    /**
     * Changes R, P, or S into the full word.
     *
     * @param move R, P, or S
     * @return full move name
     */
    private String moveName(String move)
    {
        if (move.equals("R"))
        {
            return "Rock";
        }
        else if (move.equals("P"))
        {
            return "Paper";
        }
        else
        {
            return "Scissors";
        }
    }

    /**
     * Updates the statistics fields.
     */
    private void updateStats()
    {
        playerWinsField.setText(
                String.valueOf(playerWins));

        computerWinsField.setText(
                String.valueOf(computerWins));

        tiesField.setText(
                String.valueOf(ties));
    }

    /**
     * Inner strategy that assumes the least-used
     * player move will be used next.
     */
    private class LeastUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            String expectedMove;

            if (playerRockCount <= playerPaperCount
                    && playerRockCount <= playerScissorsCount)
            {
                expectedMove = "R";
            }
            else if (playerPaperCount <= playerRockCount
                    && playerPaperCount <= playerScissorsCount)
            {
                expectedMove = "P";
            }
            else
            {
                expectedMove = "S";
            }

            return moveThatBeats(expectedMove);
        }
    }

    /**
     * Inner strategy that assumes the most-used
     * player move will be used next.
     */
    private class MostUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            String expectedMove;

            if (playerRockCount >= playerPaperCount
                    && playerRockCount >= playerScissorsCount)
            {
                expectedMove = "R";
            }
            else if (playerPaperCount >= playerRockCount
                    && playerPaperCount >= playerScissorsCount)
            {
                expectedMove = "P";
            }
            else
            {
                expectedMove = "S";
            }

            return moveThatBeats(expectedMove);
        }
    }

    /**
     * Inner strategy that copies the move
     * the player used during the previous round.
     */
    private class LastUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            return lastPlayerMove;
        }
    }

    /**
     * Returns the move that beats another move.
     *
     * @param move move to beat
     * @return winning move
     */
    private String moveThatBeats(String move)
    {
        if (move.equals("R"))
        {
            return "P";
        }
        else if (move.equals("P"))
        {
            return "S";
        }
        else
        {
            return "R";
        }
    }
}