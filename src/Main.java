import ui.MainWindow;
import javax.swing.*;

/**
 * Entry point — launches the Swing GUI on the Event Dispatch Thread.
 *
 * Compile:  javac -d out -sourcepath src src/Main.java
 * Run:      java -cp out Main
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system L&F as base, then override with custom colours
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}
