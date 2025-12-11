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

        // The top entry form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(8, 8, 8, 8));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;

        JLabel lblExercise = bigLabel("Exercise");
        JTextField tfExercise = bigTextField(24);

        JLabel lblReps = bigLabel("Reps");
        JSpinner spReps = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1));
        makeBig(spReps);

        JLabel lblWeight = bigLabel("Weight (kg)");
        JSpinner spWeight = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 500.0, 0.5));
        makeBig(spWeight);

        JLabel lblBodyWeight = bigLabel("Body weight (kg)");
        JSpinner spBodyWeight = new JSpinner(new SpinnerNumberModel(78.0, 20.0, 300.0, 0.5));
        makeBig(spBodyWeight);

        // Layout (2 rows x 4 columns: label/field pairs)
        int col = 0;
        gc.gridy = 0; gc.gridx = col++; form.add(lblExercise, gc);
        gc.gridx = col++; form.add(tfExercise, gc);
        gc.gridx = col++; form.add(lblReps, gc);
        gc.gridx = col++; form.add(spReps, gc);

    }
}
