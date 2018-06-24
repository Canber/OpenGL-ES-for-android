/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.airhockey.android.util;

public class Geometry {        
    public static class Point {
        public final float x, y, z;
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }        
        
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
    }
    
    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }                      
        
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }
    
    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;
        
        public Cylinder(Point center, float radius, float height) {        
            this.center = center;
            this.radius = radius;
            this.height = height;
        }                                    
    }
}
