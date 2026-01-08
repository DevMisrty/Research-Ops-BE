package CollectionFramework;

import java.util.*;
import java.util.ArrayList;

public class Point implements Comparable<Point> {
     int x;
     int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int compareTo(Point p){
        if(p.x==this.x)return this.y-p.y;
        return this.x-p.x;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Set<Point> list = new TreeSet<>((p,q)-> {
            if(p.x==q.x)return p.y-q.y;
            return p.x-q.x;
        });
        list.add(new Point(2,3));
        list.add(new Point(2,4));
        list.add(new Point(1,1));
        list.add(new Point(2,2));
        list.add(new Point(3,4));
        list.add(new Point(10,10));
        System.out.println(list);

    }
}
