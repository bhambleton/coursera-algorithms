/* *****************************************************************************
 *  Name:              Brian Hambleton
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private int numSegments;
    private final LineSegment[] lineSegments;
    private final Point[] points;

    public BruteCollinearPoints(Point[] pArray) {
        if (pArray == null) throw new IllegalArgumentException();
        this.points = new Point[pArray.length];

        checkDuplicatePoints(pArray);

        Arrays.sort(this.points);

        // create linked list to store segments
        List<LineSegment> segments = new LinkedList<>();

        int numPoints = this.points.length;
        for (int p = 0; p < numPoints; p++) {
            for (int q = p + 1; q < numPoints; q++) {
                for (int r = q + 1; r < numPoints; r++) {
                    for (int s = r + 1; s < numPoints; s++) {
                        if (this.points[p].slopeTo(this.points[q]) == this.points[p]
                                .slopeTo(this.points[r]) &&
                                this.points[p].slopeTo(this.points[r]) == this.points[p]
                                        .slopeTo(this.points[s])) {
                            // if points i, j, k, and l form a line,
                            //      add segment between i and l to linkedList
                            segments.add(new LineSegment(this.points[p], this.points[s]));
                            this.numSegments++;
                        }
                    }
                }
            }
        }

        // copy linkedList to array
        this.lineSegments = segments.toArray(new LineSegment[this.numSegments]);
    }

    // compare points
    private void checkDuplicatePoints(Point[] p) {
        for (int i = 0; i < p.length; i++) {
            if (p[i] == null) throw new IllegalArgumentException("Point " + i + " == null");

            for (int j = 0; j < i; j++) {
                if (p[j].compareTo(p[i]) == 0) {
                    throw new IllegalArgumentException("Points equal.");
                }
            }

            this.points[i] = p[i];
        }
    }

    public int numberOfSegments() {
        return this.numSegments;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, numberOfSegments());
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
