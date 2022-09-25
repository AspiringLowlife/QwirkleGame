package com.example.qwirklegame;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Tile {

    public Shape Shape;
    public Color Color;
    public ImageView imageView=null;

    public Tile(Color color, Shape shape) {
        this.Color = color;
        this.Shape = shape;

    }

    public enum Shape {
        square(1), circle(2), star(3), diamond(4), cross(5), club(6);

        public int code;

        Shape(int i) {
            this.code = i;
        }
    }

    public enum Color {
        blue(1), green(2), red(3), yellow(4), purple(5), orange(6);

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

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn += this.getColor().name() + "" + this.getShape().name();
        for (int i = toReturn.length(); i < 14; i++) {
            toReturn += "";
        }
        return toReturn;
    }
}
