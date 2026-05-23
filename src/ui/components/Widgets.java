package ui.components;

import ui.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Factory & base classes for all custom-painted Swing widgets.
 */
public class Widgets {

    // ── Rounded Panel ─────────────────────────────────────────────────────────
    public static class RoundPanel extends JPanel {
        private final int radius;
        private Color borderColor;

        public RoundPanel(int radius) {
            this(radius, Theme.BG_CARD, Theme.BORDER);
        }
        public RoundPanel(int radius, Color bg, Color border) {
            this.radius = radius;
            this.borderColor = border;
            setBackground(bg);
            setOpaque(false);
        }
        public void setBorderColor(Color c) { this.borderColor = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, radius, radius));
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Accent Button ─────────────────────────────────────────────────────────
    public static class AccentButton extends JButton {
        private boolean hovered;
        private Color base, hover;

        public AccentButton(String text) {
            this(text, Theme.ACCENT, Theme.ACCENT_DIM);
        }
        public AccentButton(String text, Color base, Color hover) {
            super(text);
            this.base = base; this.hover = hover;
            setFont(Theme.FONT_HEAD);
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? base : hover);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), Theme.RADIUS, Theme.RADIUS));
            if (hovered) {
                g2.setColor(base.brighter());
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f, 0.75f, getWidth()-1.5f, getHeight()-1.5f, Theme.RADIUS, Theme.RADIUS));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Ghost Button ──────────────────────────────────────────────────────────
    public static class GhostButton extends JButton {
        private boolean hovered;
        private final Color accent;

        public GhostButton(String text, Color accent) {
            super(text);
            this.accent = accent;
            setFont(Theme.FONT_BODY);
            setForeground(accent);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            if (hovered) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 25));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    // ── Styled Text Field ─────────────────────────────────────────────────────
    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setFont(Theme.FONT_BODY);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.ACCENT);
        f.setBackground(Theme.BG_INPUT);
        f.setOpaque(false);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        f.setPreferredSize(new Dimension(f.getPreferredSize().width, 38));
        // Placeholder
        f.putClientProperty("placeholder", placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { f.repaint(); }
            public void focusLost(FocusEvent e)   { f.repaint(); }
        });
        return f;
    }

    // ── Styled TextArea ───────────────────────────────────────────────────────
    public static JTextArea styledArea() {
        JTextArea a = new JTextArea();
        a.setFont(Theme.FONT_BODY);
        a.setForeground(Theme.TEXT_PRIMARY);
        a.setBackground(Theme.BG_INPUT);
        a.setCaretColor(Theme.ACCENT);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(8, 12, 8, 12));
        return a;
    }

    // ── Styled ComboBox ───────────────────────────────────────────────────────
    public static <T> JComboBox<T> styledCombo(T[] items) {
        JComboBox<T> c = new JComboBox<>(items);
        c.setFont(Theme.FONT_BODY);
        c.setForeground(Theme.TEXT_PRIMARY);
        c.setBackground(Theme.BG_INPUT);
        c.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? Theme.ACCENT_DIM : Theme.BG_CARD);
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(6, 12, 6, 12));
                return this;
            }
        });
        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 38));
        return c;
    }

    // ── Badge label ───────────────────────────────────────────────────────────
    public static class Badge extends JLabel {
        public Badge(String text, Color fg, Color bg) {
            super(text);
            setFont(Theme.FONT_BADGE);
            setForeground(fg);
            setOpaque(false);
            setHorizontalAlignment(CENTER);
            setBorder(new EmptyBorder(3, 8, 3, 8));
            putClientProperty("bg", bg);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor((Color) getClientProperty("bg"));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Section label ─────────────────────────────────────────────────────────
    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(Theme.FONT_BADGE);
        l.setForeground(Theme.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 0, 6, 0));
        return l;
    }

    public static JLabel bodyLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BODY);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }

    // ── Divider ───────────────────────────────────────────────────────────────
    public static JSeparator divider() {
        JSeparator s = new JSeparator();
        s.setForeground(Theme.BORDER);
        s.setBackground(Theme.BORDER);
        return s;
    }
}
