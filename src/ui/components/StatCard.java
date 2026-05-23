package ui.components;

import ui.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A coloured statistics card showing a number + label.
 */
public class StatCard extends JPanel {

    private final String label;
    private final Color  accent;
    private int value;
    private final JLabel numLabel;

    public StatCard(String label, int value, Color accent) {
        this.label = label;
        this.value = value;
        this.accent = accent;
        setOpaque(false);
        setLayout(new BorderLayout(0, 4));
        setBorder(new EmptyBorder(18, 20, 18, 20));

        numLabel = new JLabel(String.valueOf(value));
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        numLabel.setForeground(accent);

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_SMALL);
        lbl.setForeground(Theme.TEXT_SECONDARY);

        // Coloured top accent strip
        JPanel strip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 3, 3));
                g2.dispose();
            }
        };
        strip.setPreferredSize(new Dimension(0, 3));
        strip.setOpaque(false);

        add(strip,    BorderLayout.NORTH);
        add(numLabel, BorderLayout.CENTER);
        add(lbl,      BorderLayout.SOUTH);
    }

    public void setValue(int v) {
        this.value = v;
        numLabel.setText(String.valueOf(v));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Glow effect behind card
        g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 12));
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), Theme.RADIUS, Theme.RADIUS));
        g2.setColor(Theme.BG_CARD);
        g2.fill(new RoundRectangle2D.Float(1, 3, getWidth()-2, getHeight()-3, Theme.RADIUS, Theme.RADIUS));
        g2.setColor(Theme.BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(1, 3, getWidth()-2, getHeight()-4, Theme.RADIUS, Theme.RADIUS));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() { return new Dimension(140, 100); }
}
