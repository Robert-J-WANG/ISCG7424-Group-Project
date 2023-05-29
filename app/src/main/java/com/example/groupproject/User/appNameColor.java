package com.example.groupproject.User;

import android.graphics.Color;

public class appNameColor {
    public static int getColorForCharacter(char character) {
        switch (Character.toLowerCase(character)) {
            case 'n':
                return Color.BLACK;
            case 'u':
                return Color.RED;
            case 'm':
                return Color.BLUE;
            case 'b':
                return Color.GREEN;
            case '0':
                return Color.MAGENTA;
            default:
                return Color.BLACK;
        }
    }
}