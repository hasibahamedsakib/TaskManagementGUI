package ui;

import java.awt.*;

/**
 * Centralised design tokens — one place to change the entire look.
 */
public class Theme {

    // ── Palette ───────────────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(0x0F, 0x11, 0x17);
    public static final Color BG_PANEL     = new Color(0x17, 0x1A, 0x23);
    public static final Color BG_CARD      = new Color(0x1E, 0x22, 0x2E);
    public static final Color BG_HOVER     = new Color(0x25, 0x2A, 0x3A);
    public static final Color BG_INPUT     = new Color(0x12, 0x15, 0x1E);
    public static final Color BG_SELECTED  = new Color(0x1A, 0x35, 0x5A);

    public static final Color ACCENT       = new Color(0x4D, 0x9D, 0xFF);
    public static final Color ACCENT_DIM   = new Color(0x2A, 0x5A, 0xA0);
    public static final Color ACCENT_GLOW  = new Color(0x4D, 0x9D, 0xFF, 60);

    public static final Color TEXT_PRIMARY  = new Color(0xE8, 0xEC, 0xF4);
    public static final Color TEXT_SECONDARY= new Color(0x8A, 0x92, 0xAA);
    public static final Color TEXT_MUTED    = new Color(0x50, 0x58, 0x70);

    public static final Color BORDER        = new Color(0x28, 0x2E, 0x42);
    public static final Color BORDER_LIGHT  = new Color(0x35, 0x3D, 0x58);

    public static final Color GREEN         = new Color(0x3D, 0xD6, 0x8C);
    public static final Color GREEN_BG      = new Color(0x3D, 0xD6, 0x8C, 30);
    public static final Color YELLOW        = new Color(0xF5, 0xC5, 0x42);
    public static final Color YELLOW_BG     = new Color(0xF5, 0xC5, 0x42, 30);
    public static final Color RED           = new Color(0xFF, 0x5A, 0x5A);
    public static final Color RED_BG        = new Color(0xFF, 0x5A, 0x5A, 30);
    public static final Color CYAN          = new Color(0x42, 0xD4, 0xF4);
    public static final Color CYAN_BG       = new Color(0x42, 0xD4, 0xF4, 30);

    // ── Typography ────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    public static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO   = new Font("Consolas",  Font.PLAIN, 12);
    public static final Font FONT_BADGE  = new Font("Segoe UI", Font.BOLD,  10);

    // ── Sizes ─────────────────────────────────────────────────────────────────
    public static final int  RADIUS      = 10;
    public static final int  SIDEBAR_W   = 220;

    private Theme() {}
}
