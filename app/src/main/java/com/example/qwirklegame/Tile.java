package com.example.qwirklegame;

public class Tile {

    public Shape Shape;
    public Color Color;

    public Tile(Color color, Shape shape) {
        this.Color = color;
        this.Shape = shape;
    }

    public enum Shape {
        SQUARE(1), CIRCLE(2), STAR(3), DIAMOND(4), CROSS(5), CLUB(6);

        public int code;

        Shape(int i) {
            this.code = i;
        }
    }

    public enum Color {
        BLUE(1), GREEN(2), RED(3), YELLOW(4), PURPLE(5), ORANGE(6);

        public int code;

        Color(int i) {
            this.code = i;
        }
    }

    public Shape getShape() {
        return Shape;
    }

    public Color getColor() {
        return Color;
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn += this.getColor().name() + " " + this.getShape().name();
        for (int i = toReturn.length(); i < 14; i++) {
            toReturn += " ";
        }
        return toReturn;
    }
}
