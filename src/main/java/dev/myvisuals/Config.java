package dev.myvisuals;

/** In-memory settings (reset on restart). */
public final class Config {
    public static boolean watermark = true;
    public static boolean info = true;       // coords / FPS / ping
    public static boolean keystrokes = true;
    public static boolean vignette = true;
    public static boolean tint = false;
    public static boolean titleOverlay = true;
    public static int hudScale = 1;          // 1..3

    private Config() {}
}
