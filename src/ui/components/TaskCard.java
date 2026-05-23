package ui.components;

import model.Task;
import ui.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A single animated task row rendered in the task list.
 */
public class TaskCard extends JPanel {

    public interface TaskCardListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onStatusChange(Task task);
    }

    private final Task task;
    private boolean hovered = false;
    private float alpha = 0f;  // fade-in

    public TaskCard(Task task, TaskCardListener listener) {
        this.task = task;
        setLayout(new BorderLayout(12, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Left colour bar (priority indicator)
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(priorityColor(task.getPriority()));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 3, 3));
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(4, 0));
        bar.setOpaque(false);

        // Centre content
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();

        // Title + badges row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titleRow.setOpaque(false);
        JLabel titleLabel = new JLabel(task.isCompleted()
                ? "<html><strike>" + task.getTitle() + "</strike></html>"
                : task.getTitle());
        titleLabel.setFont(Theme.FONT_HEAD);
        titleLabel.setForeground(task.isCompleted() ? Theme.TEXT_MUTED : Theme.TEXT_PRIMARY);
        titleRow.add(titleLabel);
        titleRow.add(statusBadge(task.getStatus()));
        if (task.isOverdue()) {
            Widgets.Badge overdue = new Widgets.Badge("OVERDUE", Theme.RED, Theme.RED_BG);
            titleRow.add(overdue);
        }

        // Sub-info row
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        infoRow.setOpaque(false);
        infoRow.add(iconLabel("\u2022 " + task.getPriority().getLabel() + " Priority",
                priorityColor(task.getPriority())));
        infoRow.add(iconLabel("\uD83D\uDCC5  Due: " + task.getFormattedDueDate(),
                task.isOverdue() ? Theme.RED : Theme.TEXT_SECONDARY));

        gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.WEST; gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        centre.add(titleRow, gc);
        gc.gridy = 1;
        centre.add(infoRow, gc);

        // Right buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        actions.setOpaque(false);

        Widgets.GhostButton statusBtn = new Widgets.GhostButton(cycleStatusLabel(task.getStatus()), Theme.CYAN);
        statusBtn.setFont(Theme.FONT_SMALL);
        statusBtn.setPreferredSize(new Dimension(110, 28));
        statusBtn.addActionListener(e -> listener.onStatusChange(task));

        Widgets.GhostButton editBtn = new Widgets.GhostButton("Edit", Theme.ACCENT);
        editBtn.setPreferredSize(new Dimension(60, 28));
        editBtn.addActionListener(e -> listener.onEdit(task));

        Widgets.GhostButton delBtn = new Widgets.GhostButton("Delete", Theme.RED);
        delBtn.setPreferredSize(new Dimension(65, 28));
        delBtn.addActionListener(e -> listener.onDelete(task));

        actions.add(statusBtn);
        actions.add(editBtn);
        actions.add(delBtn);

        add(bar,     BorderLayout.WEST);
        add(centre,  BorderLayout.CENTER);
        add(actions, BorderLayout.EAST);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
        });

        // Fade-in timer
        Timer timer = new Timer(16, null);
        timer.addActionListener(e -> {
            alpha = Math.min(1f, alpha + 0.08f);
            repaint();
            if (alpha >= 1f) timer.stop();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Color bg = hovered ? Theme.BG_HOVER : Theme.BG_CARD;
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), Theme.RADIUS, Theme.RADIUS));
        g2.setColor(Theme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, Theme.RADIUS, Theme.RADIUS));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 72);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Widgets.Badge statusBadge(Task.Status s) {
        if (s == Task.Status.COMPLETED)   return new Widgets.Badge("DONE",        Theme.GREEN,  Theme.GREEN_BG);
        if (s == Task.Status.IN_PROGRESS) return new Widgets.Badge("IN PROGRESS", Theme.CYAN,   Theme.CYAN_BG);
        return                                   new Widgets.Badge("PENDING",      Theme.YELLOW, Theme.YELLOW_BG);
    }

    private Color priorityColor(Task.Priority p) {
        if (p == Task.Priority.HIGH)   return Theme.RED;
        if (p == Task.Priority.MEDIUM) return Theme.YELLOW;
        return Theme.TEXT_MUTED;
    }

    private String cycleStatusLabel(Task.Status s) {
        if (s == Task.Status.PENDING)     return "→ Mark In Progress";
        if (s == Task.Status.IN_PROGRESS) return "→ Mark Complete";
        return                                   "↺ Mark Pending";
    }

    private JLabel iconLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(color);
        return l;
    }
}
