package br.com.josedev.main;

import java.awt.event.KeyEvent;

public class Input {
    private static final int KeyLeft = KeyEvent.VK_LEFT;
    private static final int KeyRight = KeyEvent.VK_RIGHT;
    private static final int KeyUp = KeyEvent.VK_UP;
    private static final int KeyDown = KeyEvent.VK_DOWN;

    private static final int KeyALeft = KeyEvent.VK_A;
    private static final int KeyDRight = KeyEvent.VK_D;
    private static final int KeyWUp = KeyEvent.VK_W;
    private static final int KeySDown = KeyEvent.VK_S;

    private static final int KeySpace = KeyEvent.VK_SPACE;
    private static final int KeyEscape = KeyEvent.VK_ESCAPE;
    private static final int KeyEnter = KeyEvent.VK_ENTER;
    private static final int KeyShift = KeyEvent.VK_SHIFT;

    private static boolean up = false,
            down = false,
            left = false,
            right = false,
            space = false,
            enter = false,
            escape = false,
            shift = false;

    public static void setPressed(int keyCode) {

        if (keyCode == KeyUp || keyCode == KeyWUp) {
            up = true;
        }

        if (keyCode == KeyDown || keyCode == KeySDown) {
            down = true;
        }

        if (keyCode == KeyLeft || keyCode == KeyALeft) {
            left = true;
        }

        if (keyCode == KeyRight || keyCode == KeyDRight) {
            right = true;
        }

        if (keyCode == KeyEnter) {
            enter = true;
        }

        if (keyCode == KeyShift) {
            shift = true;
        }

        if (keyCode == KeySpace) {
            space = true;
        }

        if (keyCode == KeyEscape) {
            escape = true;
        }
    }
    public static void setReleased(int keyCode) {

        if (keyCode == KeyUp || keyCode == KeyWUp) {
            up = false;
        }

        if (keyCode == KeyDown || keyCode == KeySDown) {
            down = false;
        }

        if (keyCode == KeyLeft || keyCode == KeyALeft) {
            left = false;
        }

        if (keyCode == KeyRight || keyCode == KeyDRight) {
            right = false;
        }

        if (keyCode == KeyEnter) {
            enter = false;
        }

        if (keyCode == KeyShift) {
            shift = false;
        }

        if (keyCode == KeySpace) {
            space = false;
        }

        if (keyCode == KeyEscape) {
            escape = false;
        }
    }

    public static boolean Right() {
        return right;
    }

    public static boolean Left() {
        return left;
    }

    public static boolean Up() {
        return up;
    }

    public static boolean Down() {
        return down;
    }

    public static boolean Space() {
        return space;
    }

    public static boolean Enter() {
        return enter;
    }

    public static  boolean Escape() {
        return escape;
    }

    public static boolean Shift() {
        return shift;
    }
}
