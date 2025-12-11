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
    //making the trend graph with simple line chart
    static class TrendDialog extends JDialog {

        private final DefaultTableModel model;
        private int metricCol = 2; // 2 = Reps, 3 = Weight, 4 = Body weight

        private TrendDialog(Frame owner, DefaultTableModel model) {
            super(owner, "Trends", true);
            this.model = model;

            setMinimumSize(new Dimension(780, 520));
            JPanel root = new JPanel(new BorderLayout(10, 10));
            root.setBorder(new EmptyBorder(12, 12, 12, 12));
            setContentPane(root);

            // Top controls
            JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
            controls.add(new JLabel("Metric:"));
            JComboBox<String> cbMetric = new JComboBox<>(new String[]{
                    "Reps", "Weight (kg)", "Body weight (kg)"
            });
            cbMetric.setFont(cbMetric.getFont().deriveFont(15f));
            controls.add(cbMetric);

            controls.add(new JLabel("Range:"));
            JComboBox<String> cbRange = new JComboBox<>(new String[]{
                    "All time"   // you can extend to "Last 30 days", etc. later
            });
            cbRange.setFont(cbRange.getFont().deriveFont(15f));
            controls.add(cbRange);

            JButton btnRefresh = new JButton("Refresh");
            btnRefresh.setFont(btnRefresh.getFont().deriveFont(Font.BOLD, 15f));
            controls.add(btnRefresh);

            root.add(controls, BorderLayout.NORTH);

            // Chart panel that draws from table model
            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int w = getWidth();
                    int h = getHeight();
                    int margin = 50;

                    // Background
                    g2.setColor(new Color(245, 245, 245));
                    g2.fillRoundRect(8, 8, w - 16, h - 16, 18, 18);

                    int rowCount = model.getRowCount();
                    if (rowCount == 0) {
                        g2.setColor(Color.GRAY.darker());
                        g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
                        String msg = "No data to display yet";
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
                        return;
                    }

                    // Collect values
                    double[] values = new double[rowCount];
                    int n = 0;
                    for (int r = 0; r < rowCount; r++) {
                        Object val = model.getValueAt(r, metricCol);
                        if (val == null) continue;
                        try {
                            double d = Double.parseDouble(val.toString());
                            values[n++] = d;
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    if (n == 0) {
                        g2.setColor(Color.GRAY.darker());
                        g2.setFont(getFont().deriveFont(Font.BOLD, 16f));
                        String msg = "No numeric data to plot";
                        FontMetrics fm = g2.getFontMetrics();
                        g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
                        return;
                    }

                    // Resize array if needed
                    if (n < values.length) {
                        double[] tmp = new double[n];
                        System.arraycopy(values, 0, tmp, 0, n);
                        values = tmp;
                    }

                    // Compute min/max
                    double min = values[0];
                    double max = values[0];
                    for (double v : values) {
                        if (v < min) min = v;
                        if (v > max) max = v;
                    }
                    if (max == min) {
                        // avoid flat line at border
                        max = min + 1;
                    }

                    int x0 = margin;
                    int y0 = h - margin;
                    int x1 = w - margin;
                    int y1 = margin;

                    // Axes
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawLine(x0, y0, x1, y0); // x-axis
                    g2.drawLine(x0, y0, x0, y1); // y-axis

                    // Labels
                    g2.setFont(getFont().deriveFont(12f));
                    String labelMin = String.format("%.1f", min);
                    String labelMax = String.format("%.1f", max);
                    g2.drawString(labelMin, x0 - 40, y0);
                    g2.drawString(labelMax, x0 - 40, y1 + 5);

                    String metricName = switch (metricCol) {
                        case 2 -> "Reps";
                        case 3 -> "Weight (kg)";
                        case 4 -> "Body weight (kg)";
                        default -> "";
                    };
                    g2.drawString(metricName, x0 + 5, y1 - 10);

                    // Plot line
                    int pointCount = values.length;
                    if (pointCount == 1) {
                        int x = x0 + (x1 - x0) / 2;
                        int y = y0 - (int) ((values[0] - min) / (max - min) * (y0 - y1));
                        g2.fillOval(x - 4, y - 4, 8, 8);
                        return;
                    }

                    int[] xPoints = new int[pointCount];
                    int[] yPoints = new int[pointCount];

                    for (int i = 0; i < pointCount; i++) {
                        double t = (double) i / (pointCount - 1); // 0..1
                        int x = x0 + (int) (t * (x1 - x0));
                        double norm = (values[i] - min) / (max - min); // 0..1
                        int y = y0 - (int) (norm * (y0 - y1));
                        xPoints[i] = x;
                        yPoints[i] = y;
                    }

                    g2.setStroke(new BasicStroke(2f));
                    g2.setColor(Color.BLUE);
                    g2.drawPolyline(xPoints, yPoints, pointCount);

                    // Points
                    g2.setColor(Color.RED);
                    for (int i = 0; i < pointCount; i++) {
                        g2.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
                    }
                }
            };
            chartPanel.setPreferredSize(new Dimension(720, 380));
            root.add(chartPanel, BorderLayout.CENTER);

            // Metric selection and refresh
            cbMetric.addActionListener(e -> {
                String selected = (String) cbMetric.getSelectedItem();
                if ("Reps".equals(selected)) metricCol = 2;
                else if ("Weight (kg)".equals(selected)) metricCol = 3;
                else if ("Body weight (kg)".equals(selected)) metricCol = 4;
                chartPanel.repaint();
            });
            btnRefresh.addActionListener(e -> chartPanel.repaint());

            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(e -> dispose());
            JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            south.add(btnClose);
            root.add(south, BorderLayout.SOUTH);
        }
    }
}
