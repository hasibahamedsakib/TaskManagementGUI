package ui;

import model.Task;
import service.InMemoryTaskRepository;
import service.TaskService;
import ui.components.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.List;

/**
 * Main application window — dark-themed, sidebar layout.
 */
public class MainWindow extends JFrame {

    private final TaskService taskService;

    // ── Sidebar nav state ─────────────────────────────────────────────────────
    private static final String VIEW_ALL        = "All Tasks";
    private static final String VIEW_PENDING    = "Pending";
    private static final String VIEW_PROGRESS   = "In Progress";
    private static final String VIEW_DONE       = "Completed";
    private static final String VIEW_OVERDUE    = "Overdue";

    private String currentView = VIEW_ALL;
    private String searchQuery = "";

    // ── Live panels ───────────────────────────────────────────────────────────
    private JPanel  taskListPanel;
    private JPanel  statsRow;
    private JLabel  viewTitleLabel;
    private JLabel  taskCountLabel;
    private JTextField searchField;

    private StatCard statTotal, statPending, statProgress, statDone, statOverdue;
    private final java.util.Map<String, JButton> navButtons = new java.util.LinkedHashMap<>();

    public MainWindow() {
        taskService = new TaskService(new InMemoryTaskRepository());
        seedData();
        buildUI();
    }

    // ── Build main UI ─────────────────────────────────────────────────────────
    private void buildUI() {
        setTitle("Task Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setPreferredSize(new Dimension(1100, 700));

        // Dark title bar (best-effort)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMain(),    BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(Theme.SIDEBAR_W, 0));
        sidebar.setBackground(Theme.BG_PANEL);
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));

        // Logo area
        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(Theme.BG_PANEL);
        logo.setBorder(new EmptyBorder(24, 20, 20, 20));
        JLabel appName = new JLabel("TaskBoard");
        appName.setFont(Theme.FONT_TITLE);
        appName.setForeground(Theme.ACCENT);
        JLabel appSub = new JLabel("Project Manager");
        appSub.setFont(Theme.FONT_SMALL);
        appSub.setForeground(Theme.TEXT_MUTED);
        logo.add(appName, BorderLayout.CENTER);
        logo.add(appSub,  BorderLayout.SOUTH);
        sidebar.add(logo, BorderLayout.NORTH);

        // Nav items
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(Theme.BG_PANEL);
        nav.setBorder(new EmptyBorder(8, 12, 12, 12));

        addNavLabel(nav, "VIEWS");
        addNavBtn(nav, VIEW_ALL,      "  All Tasks",     true);
        addNavBtn(nav, VIEW_PENDING,  "  Pending",       false);
        addNavBtn(nav, VIEW_PROGRESS, "  In Progress",   false);
        addNavBtn(nav, VIEW_DONE,     "  Completed",     false);
        addNavBtn(nav, VIEW_OVERDUE,  "  Overdue  ⚠",   false);

        sidebar.add(nav, BorderLayout.CENTER);

        // Bottom group info
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Theme.BG_PANEL);
        bottom.setBorder(new EmptyBorder(16, 16, 20, 16));
        JTextArea members = new JTextArea(
                "Group Members:\n" +
                "Hasib Ahmed Sakib\n" +
                "Fatema Akter Reya\n" +
                "Dipantor Chandra\n" +
                "Sumiya Akter\n" +
                "Ema  ·  Kawser Islam");
        members.setFont(Theme.FONT_SMALL);
        members.setForeground(Theme.TEXT_MUTED);
        members.setBackground(Theme.BG_PANEL);
        members.setEditable(false);
        members.setOpaque(false);
        bottom.add(members, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavLabel(JPanel nav, String text) {
        JLabel l = Widgets.sectionLabel(text);
        l.setBorder(new EmptyBorder(16, 8, 4, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        nav.add(l);
    }

    private void addNavBtn(JPanel nav, String viewName, String label, boolean selected) {
        JButton btn = new JButton(label) {
            boolean hov = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hov = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hov = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = viewName.equals(currentView);
                if (active) {
                    g2.setColor(Theme.BG_SELECTED);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                    g2.setColor(Theme.ACCENT);
                    g2.fill(new RoundRectangle2D.Float(0, 4, 3, getHeight()-8, 3, 3));
                } else if (hov) {
                    g2.setColor(Theme.BG_HOVER);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(viewName.equals(currentView) ? Theme.ACCENT : Theme.TEXT_SECONDARY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> switchView(viewName));
        navButtons.put(viewName, btn);
        nav.add(btn);
        nav.add(Box.createVerticalStrut(2));
    }

    // ── Main content area ─────────────────────────────────────────────────────
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(Theme.BG_DARK);

        main.add(buildTopBar(),   BorderLayout.NORTH);
        main.add(buildContent(),  BorderLayout.CENTER);

        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(Theme.BG_PANEL);
        bar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(14, 24, 14, 24)));

        // Left: view title + count
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        viewTitleLabel = new JLabel(currentView);
        viewTitleLabel.setFont(Theme.FONT_TITLE);
        viewTitleLabel.setForeground(Theme.TEXT_PRIMARY);
        taskCountLabel = new JLabel();
        taskCountLabel.setFont(Theme.FONT_SMALL);
        taskCountLabel.setForeground(Theme.TEXT_MUTED);
        left.add(viewTitleLabel);
        left.add(taskCountLabel);
        bar.add(left, BorderLayout.WEST);

        // Centre: search
        JPanel searchWrap = new JPanel(new BorderLayout());
        searchWrap.setOpaque(false);
        searchWrap.setPreferredSize(new Dimension(260, 36));
        searchField = Widgets.styledField("Search tasks...");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchQuery = searchField.getText().trim().toLowerCase();
                refreshTaskList();
            }
        });
        searchWrap.add(searchField, BorderLayout.CENTER);
        bar.add(searchWrap, BorderLayout.CENTER);

        // Right: Add button
        Widgets.AccentButton addBtn = new Widgets.AccentButton("+ Add Task");
        addBtn.setPreferredSize(new Dimension(120, 38));
        addBtn.addActionListener(e -> showAddDialog());
        bar.add(addBtn, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 0));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Stats row
        statsRow = new JPanel(new GridLayout(1, 5, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(new EmptyBorder(0, 0, 20, 0));

        statTotal    = new StatCard("Total Tasks",  taskService.getTotalCount(),      Theme.ACCENT);
        statPending  = new StatCard("Pending",      taskService.getPendingCount(),     Theme.YELLOW);
        statProgress = new StatCard("In Progress",  taskService.getInProgressCount(),  Theme.CYAN);
        statDone     = new StatCard("Completed",    taskService.getCompletedCount(),   Theme.GREEN);
        statOverdue  = new StatCard("Overdue",      taskService.getOverdueCount(),     Theme.RED);

        statsRow.add(statTotal);
        statsRow.add(statPending);
        statsRow.add(statProgress);
        statsRow.add(statDone);
        statsRow.add(statOverdue);
        content.add(statsRow, BorderLayout.NORTH);

        // Scrollable task list
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBackground(Theme.BG_DARK);
        taskListPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

        JScrollPane scroll = new JScrollPane(taskListPanel);
        scroll.setBackground(Theme.BG_DARK);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(Theme.BG_PANEL);
        content.add(scroll, BorderLayout.CENTER);

        refreshTaskList();
        return content;
    }

    // ── Task list rendering ───────────────────────────────────────────────────
    private void refreshTaskList() {
        taskListPanel.removeAll();

        List<Task> tasks = getFilteredTasks();

        // Apply search filter
        if (!searchQuery.isEmpty()) {
            java.util.List<Task> filtered = new java.util.ArrayList<>();
            for (Task t : tasks)
                if (t.getTitle().toLowerCase().contains(searchQuery)
                        || t.getDescription().toLowerCase().contains(searchQuery))
                    filtered.add(t);
            tasks = filtered;
        }

        // Update counters
        viewTitleLabel.setText(currentView);
        taskCountLabel.setText(tasks.size() + " task" + (tasks.size() != 1 ? "s" : ""));

        if (tasks.isEmpty()) {
            JPanel empty = new JPanel(new BorderLayout());
            empty.setOpaque(false);
            JLabel msg = new JLabel("No tasks found", SwingConstants.CENTER);
            msg.setFont(Theme.FONT_HEAD);
            msg.setForeground(Theme.TEXT_MUTED);
            msg.setBorder(new EmptyBorder(60, 0, 0, 0));
            empty.add(msg, BorderLayout.CENTER);
            taskListPanel.add(empty);
        } else {
            TaskCard.TaskCardListener listener = new TaskCard.TaskCardListener() {
                public void onEdit(Task task)         { showEditDialog(task); }
                public void onDelete(Task task)       { confirmDelete(task); }
                public void onStatusChange(Task task) { cycleStatus(task); }
            };
            for (Task task : tasks) {
                TaskCard card = new TaskCard(task, listener);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
                taskListPanel.add(card);
                taskListPanel.add(Box.createVerticalStrut(6));
            }
        }

        updateStats();
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private List<Task> getFilteredTasks() {
        if (VIEW_PENDING.equals(currentView))  return taskService.getTasksByStatus(Task.Status.PENDING);
        if (VIEW_PROGRESS.equals(currentView)) return taskService.getTasksByStatus(Task.Status.IN_PROGRESS);
        if (VIEW_DONE.equals(currentView))     return taskService.getTasksByStatus(Task.Status.COMPLETED);
        if (VIEW_OVERDUE.equals(currentView))  return taskService.getOverdueTasks();
        return taskService.getAllTasks();
    }

    private void updateStats() {
        statTotal.setValue(taskService.getTotalCount());
        statPending.setValue(taskService.getPendingCount());
        statProgress.setValue(taskService.getInProgressCount());
        statDone.setValue(taskService.getCompletedCount());
        statOverdue.setValue(taskService.getOverdueCount());
        statsRow.repaint();
    }

    // ── Navigation ────────────────────────────────────────────────────────────
    private void switchView(String view) {
        currentView = view;
        searchField.setText("");
        searchQuery = "";
        navButtons.forEach((name, btn) -> {
            boolean active = name.equals(view);
            btn.setForeground(active ? Theme.ACCENT : Theme.TEXT_SECONDARY);
            btn.repaint();
        });
        refreshTaskList();
    }

    // ── Add / Edit dialogs ────────────────────────────────────────────────────
    private void showAddDialog() {
        TaskDialog dlg = new TaskDialog(this, "New Task", null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            taskService.createTask(dlg.getTaskTitle(), dlg.getTaskDesc(),
                    dlg.getTaskPriority(), dlg.getTaskDueDate());
            refreshTaskList();
        }
    }

    private void showEditDialog(Task task) {
        TaskDialog dlg = new TaskDialog(this, "Edit Task", task);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            taskService.updateTask(task.getId(),
                    dlg.getTaskTitle(), dlg.getTaskDesc(),
                    dlg.getTaskPriority(), dlg.getTaskDueDate());
            refreshTaskList();
        }
    }

    // ── Status cycling ────────────────────────────────────────────────────────
    private void cycleStatus(Task task) {
        if (task.getStatus() == Task.Status.PENDING)          taskService.markAsInProgress(task.getId());
        else if (task.getStatus() == Task.Status.IN_PROGRESS) taskService.markAsCompleted(task.getId());
        else                                                   taskService.markAsPending(task.getId());
        refreshTaskList();
    }

    // ── Delete confirmation ───────────────────────────────────────────────────
    private void confirmDelete(Task task) {
        int result = JOptionPane.showConfirmDialog(this,
                "Delete task \"" + task.getTitle() + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            taskService.deleteTask(task.getId());
            refreshTaskList();
        }
    }

    // ── Seed data ─────────────────────────────────────────────────────────────
    private void seedData() {
        taskService.createTask("Design database schema",
                "Create ER diagram and define all tables",
                Task.Priority.HIGH, LocalDate.now().plusDays(3));
        taskService.createTask("Implement login module",
                "User authentication with hashed passwords",
                Task.Priority.HIGH, LocalDate.now().plusDays(7));
        taskService.createTask("Write project report",
                "Document all features and OOP design decisions",
                Task.Priority.MEDIUM, LocalDate.now().plusDays(14));
        taskService.createTask("Set up GitHub repository",
                "Create repo, add .gitignore and README",
                Task.Priority.LOW, LocalDate.now().minusDays(2));
        taskService.createTask("Prepare demo presentation",
                "Slides and live demo walkthrough",
                Task.Priority.MEDIUM, LocalDate.now().plusDays(10));
        taskService.markAsInProgress(1);
        taskService.markAsCompleted(4);
    }
}
