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

        col = 0;
        gc.gridy = 1; gc.gridx = col++; form.add(lblWeight, gc);
        gc.gridx = col++; form.add(spWeight, gc);
        gc.gridx = col++; form.add(lblBodyWeight, gc);
        gc.gridx = col++; form.add(spBodyWeight, gc);

        root.add(form, BorderLayout.NORTH);

        // making the center table
        String[] cols = {"Date", "Exercise", "Reps", "Weight (kg)", "Body weight (kg)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(table.getFont().deriveFont(15f));
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 15f));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Workout log"));
        root.add(scroll, BorderLayout.CENTER);

        //adding the bottom buttons to add, edit, delete, export and trend graph
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        JButton btnAdd = bigButton("Add");
        JButton btnEdit = bigButton("Edit");
        JButton btnDelete = bigButton("Delete");
        JButton btnExport = bigButton("Export CSV");
        JButton btnTrends = bigButton("Trends");

        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(new JSeparator(SwingConstants.VERTICAL) {{
            setPreferredSize(new Dimension(18, 28));
        }});
        buttons.add(btnExport);
        buttons.add(btnTrends);

        root.add(buttons, BorderLayout.SOUTH);

        // making the date column
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //how the buttons will work and making a new row
        btnAdd.addActionListener(e -> {
            String exercise = tfExercise.getText().trim();
            if (exercise.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter an exercise name.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int reps = ((Number) spReps.getValue()).intValue();
            double weight = ((Number) spWeight.getValue()).doubleValue();
            double bodyWeight = ((Number) spBodyWeight.getValue()).doubleValue();

            if (reps <= 0) {
                JOptionPane.showMessageDialog(frame,
                        "Reps must be greater than zero.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (weight < 0) {
                JOptionPane.showMessageDialog(frame,
                        "Weight cannot be negative.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String now = LocalDateTime.now().format(dtf);
            model.addRow(new Object[]{now, exercise, reps, weight, bodyWeight});

            // clear exercise field
            tfExercise.setText("");
        });
    }
}
