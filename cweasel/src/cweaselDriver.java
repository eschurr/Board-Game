import java.awt.BorderLayout;
import javax.swing.*;

public class cweaselDriver extends JFrame {

    private JMenuBar menubar;

    public cweaselDriver() {
        initializeUI();
    }

    private void initializeUI() {

        menubar = new JMenuBar();
        add(menubar, BorderLayout.NORTH);

        JButton start = new JButton("Start");
        JMenu settings = new JMenu("Settings");
        JMenu difficulty = new JMenu("Difficulty");
        JMenu traps = new JMenu("Number of Traps");

        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem("Hard");
        JMenuItem numTraps = new JMenuItem();

        difficulty.add(easy);
        difficulty.add(medium);
        difficulty.add(hard);
        settings.add(difficulty);
        JMenu help = new JMenu("Help");
        JMenuItem helpDialogue = new JMenuItem("Welcome to Caldera Weasel! " +
                "Avoid clicking the thermal traps around the board. The number on a revealed " +
                "square tells you how many traps surround it! Good luck!");
        JButton quit = new JButton("Quit");
        help.add(helpDialogue);

        JLabel trapCounter = new JLabel("");
        JLabel timeCounter = new JLabel("");

        for (int i = 0; i < 75; i++) {
            String trapName = "" + i;
            numTraps = new JMenuItem(trapName);
            traps.add(numTraps);
        }

        add(new cweasel(menubar, start, settings, difficulty, traps, numTraps,
                easy, medium, hard,
                help, helpDialogue, quit, trapCounter, timeCounter));

        setResizable(false);
        pack();

        setTitle("Caldera Weasel");
        setLocationRelativeTo(null); // center board
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        var dr = new cweaselDriver();
        dr.setVisible(true);
    }
}

