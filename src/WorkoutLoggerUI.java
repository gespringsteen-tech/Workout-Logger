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

        //ability to edit row
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame,
                        "Please select a row to edit.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

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
            // Update cells
            model.setValueAt(exercise, row, 1);
            model.setValueAt(reps, row, 2);
            model.setValueAt(weight, row, 3);
            model.setValueAt(bodyWeight, row, 4);
        });

        //make the ability to delete a row
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame,
                        "Please select a row to delete.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to delete this entry?",
                    "Confirm delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                model.removeRow(row);
            }
        });
        //exporting the table out of the application
        btnExport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save workout log as CSV");
            int choice = chooser.showSaveDialog(frame);
            if (choice != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            // simple: add .csv if not present
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getParentFile(), file.getName() + ".csv");
            }

            try (FileWriter fw = new FileWriter(file)) {
                // header
                for (int c = 0; c < model.getColumnCount(); c++) {
                    fw.write(model.getColumnName(c));
                    if (c < model.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");

                // rows
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        String text = val == null ? "" : val.toString();
                        // basic CSV escaping (wrap in quotes if needed)
                        if (text.contains(",") || text.contains("\"")) {
                            text = "\"" + text.replace("\"", "\"\"") + "\"";
                        }
                        fw.write(text);
                        if (c < model.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(frame,
                        "Exported to:\n" + file.getAbsolutePath(),
                        "Export complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Error writing file:\n" + ex.getMessage(),
                        "Export error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        //make it possible to present the trend graph
        btnTrends.addActionListener(e -> {
            TrendDialog.show(frame, model);
        });

        //make it possible so that when a row is pressed, show its values in the center form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    tfExercise.setText(String.valueOf(model.getValueAt(row, 1)));
                    spReps.setValue(model.getValueAt(row, 2));
                    spWeight.setValue(model.getValueAt(row, 3));
                    spBodyWeight.setValue(model.getValueAt(row, 4));
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
    //to make labels and buttons easy for my grandpa
    private static JLabel bigLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 16f));
        return l;
    }
    private static JTextField bigTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(tf.getFont().deriveFont(16f));
        return tf;
    }
    private static JButton bigButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 16f));
        b.setMargin(new Insets(10, 16, 10, 16));
        return b;
    }
    private static void makeBig(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setFont(de.getTextField().getFont().deriveFont(16f));
            de.getTextField().setColumns(6);
        }
        spinner.setPreferredSize(new Dimension(120, 34));
    }
}
