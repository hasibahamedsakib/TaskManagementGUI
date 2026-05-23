package ui.components;

import model.Task;
import ui.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Modal dialog for creating or editing a task.
 */
public class TaskDialog extends JDialog {

    private boolean confirmed = false;

    private final JTextField titleField;
    private final JTextArea  descArea;
    private final JComboBox<String> priorityCombo;
    private final JTextField dueDateField;

    public TaskDialog(Frame parent, String dialogTitle, Task existing) {
        super(parent, dialogTitle, true);
        setBackground(Theme.BG_PANEL);
        getContentPane().setBackground(Theme.BG_PANEL);

        // ── Layout ────────────────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(Theme.BG_PANEL);
        root.setBorder(new EmptyBorder(28, 32, 24, 32));

        // Title
        JLabel header = new JLabel(dialogTitle);
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_PRIMARY);
        header.setBorder(new EmptyBorder(0, 0, 24, 0));
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(4, 0, 4, 0);
        gc.weightx = 1;

        titleField    = Widgets.styledField("Enter task title...");
        priorityCombo = Widgets.styledCombo(new String[]{"High", "Medium", "Low"});
        dueDateField  = Widgets.styledField("dd-MM-yyyy  (optional)");
        descArea      = Widgets.styledArea();
        descArea.setRows(3);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBackground(Theme.BG_INPUT);
        descScroll.setBorder(new LineBorder(Theme.BORDER, 1, true));
        descScroll.setPreferredSize(new Dimension(0, 80));

        int row = 0;
        addField(form, gc, row++, "Task Title *", titleField);
        addField(form, gc, row++, "Description", descScroll);
        addField(form, gc, row++, "Priority", priorityCombo);
        addField(form, gc, row,   "Due Date", dueDateField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(20, 0, 0, 0));

        Widgets.GhostButton cancel = new Widgets.GhostButton("Cancel", Theme.TEXT_SECONDARY);
        cancel.setPreferredSize(new Dimension(90, 38));
        cancel.addActionListener(e -> dispose());

        Widgets.AccentButton save = new Widgets.AccentButton(existing == null ? "Add Task" : "Save Changes");
        save.setPreferredSize(new Dimension(120, 38));
        save.addActionListener(e -> submit());

        btnRow.add(cancel);
        btnRow.add(save);
        root.add(btnRow, BorderLayout.SOUTH);

        // Pre-fill if editing
        if (existing != null) {
            titleField.setText(existing.getTitle());
            descArea.setText(existing.getDescription());
            priorityCombo.setSelectedItem(existing.getPriority().getLabel());
            if (existing.getDueDate() != null)
                dueDateField.setText(existing.getFormattedDueDate());
        }

        getRootPane().setDefaultButton(save);
        getRootPane().setBackground(Theme.BG_PANEL);
        setContentPane(root);
        setPreferredSize(new Dimension(480, 420));
        pack();
        setLocationRelativeTo(parent);
    }

    private void addField(JPanel form, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridy = row * 2;
        form.add(Widgets.sectionLabel(label), gc);
        gc.gridy = row * 2 + 1;
        gc.insets = new Insets(2, 0, 12, 0);
        form.add(field, gc);
        gc.insets = new Insets(4, 0, 4, 0);
    }

    private void submit() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Task title is required.");
            return;
        }
        String dateText = dueDateField.getText().trim();
        if (!dateText.isEmpty() && !dateText.equals("dd-MM-yyyy  (optional)")) {
            try {
                LocalDate.parse(dateText, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeParseException e) {
                showError("Invalid date format. Use dd-MM-yyyy");
                return;
            }
        }
        confirmed = true;
        dispose();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public boolean isConfirmed()  { return confirmed; }
    public String  getTaskTitle() { return titleField.getText().trim(); }
    public String  getTaskDesc()  { return descArea.getText().trim(); }

    public Task.Priority getTaskPriority() {
        String s = (String) priorityCombo.getSelectedItem();
        if ("High".equals(s))   return Task.Priority.HIGH;
        if ("Medium".equals(s)) return Task.Priority.MEDIUM;
        return Task.Priority.LOW;
    }

    public LocalDate getTaskDueDate() {
        String s = dueDateField.getText().trim();
        if (s.isEmpty() || s.startsWith("dd")) return null;
        try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd-MM-yyyy")); }
        catch (DateTimeParseException e) { return null; }
    }
}
