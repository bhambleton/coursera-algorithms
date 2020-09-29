/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last modified:     27 September 2020
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    // constructor
    public PointSET() {
        this.points = new TreeSet<>();
    }

    // check if tree is empty
    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.points.size();
    }

    // add the given point to the tree (if it does not already exist)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        // add point p, returns false if point already exists
        // if (!this.points.add(p)) StdOut.println("Point already exists in set");
        this.points.add(p);
    }

    // does the tree contain point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.points)
            p.draw();
    }

    // return iterable of all points that are inside or on the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (this.isEmpty()) return null;
        List<Point2D> items = new LinkedList<>();

        for (Point2D p : this.points)
            if (rect.contains(p)) items.add(p);

        return items;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.isEmpty()) return null;

        double nearestDist = Double.POSITIVE_INFINITY;
        Point2D nearest = p;

        for (Point2D point : this.points) {
            // if distance from current point to original point p
            //  is < current minimum distance, update nearestDist
            //  and save point in nearest
            if (p.distanceSquaredTo(point) < nearestDist) {
                nearestDist = p.distanceSquaredTo(point);
                nearest = point;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        // empty main
    }
}
