package com.flechazo.nekoration.utils;

public record PixelPos(int x, int y) {

    public PixelPos up() {
        return new PixelPos(x, y + 1);
    }

    public PixelPos down() {
        return new PixelPos(x, y - 1);
    }

    public PixelPos left() {
        return new PixelPos(x - 1, y);
    }

    public PixelPos right() {
        return new PixelPos(x + 1, y);
    }

    public PixelPos offset(int dx, int dy) {
        return new PixelPos(x + dx, y + dy);
    }
}