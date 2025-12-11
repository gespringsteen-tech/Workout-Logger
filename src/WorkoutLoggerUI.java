import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutLoggerUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WorkoutLoggerUI().createAndShow());
    }
    private void createAndShow() {
        JFrame frame = new JFrame("Workout Logger - Roger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(980, 640));

        // Root
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        frame.setContentPane(root);
    }
}
