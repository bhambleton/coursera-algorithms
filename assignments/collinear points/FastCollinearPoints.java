/* *****************************************************************************
 *  Name:              Brian Hambleton
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private final int numSegments;
    private final LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] p) {
        if (p == null) throw new IllegalArgumentException();
        Arrays.sort(p);
        checkPoints(p);

        Point[] pSlopeOrder = Arrays.copyOf(p, p.length);

        // create linked list to store segments
        List<LineSegment> segments = new LinkedList<>();
        List<Point> usedPoints = new LinkedList<>();

        // iterate through array of points
        for (int i = 0; i < p.length; i++) {

            // use this point as current origin
            Point origin = p[i];

            if (usedPoints.contains(origin)) continue;

            // sort based on slope made between current origin and each point
            Arrays.sort(pSlopeOrder, origin.slopeOrder());
            int count = 1;
            int segmentIndex = -1;

            // loop through points, comparing slope they make
            for (int j = 1; j < p.length - 1; j++) {
                if (pSlopeOrder[j].slopeTo(origin) == pSlopeOrder[j + 1]
                        .slopeTo(origin)) {
                    // same slope with origin, increment segment count
                    count++;
                    if (segmentIndex < 0) segmentIndex = j;

                }
                else if (segmentIndex > 0) break;
            }

            if (count >= 3) {
                // Array of points to hold points in segment
                Point[] collinearSegment = new Point[count + 1];
                collinearSegment[0] = origin;
                for (int iter = 0; iter < count; iter++)
                    collinearSegment[iter + 1] = pSlopeOrder[iter + segmentIndex];

                Arrays.sort(collinearSegment);
                segments.add(new LineSegment(collinearSegment[0], collinearSegment[count]));

                usedPoints.addAll(Arrays.asList(collinearSegment).subList(0, count + 1));

            }
        }

        this.numSegments = segments.size();
        this.lineSegments = segments.toArray(new LineSegment[this.numSegments]);
    }

    public int numberOfSegments() {
        return this.numSegments;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, numberOfSegments());
    }

    private void checkPoints(Point[] p) {
        for (int i = 0; i < p.length; i++) {
            if (p[i] == null) {
                throw new IllegalArgumentException("Null point.");
            }
            for (int j = 0; j < i; j++) {
                if (p[j].compareTo(p[i]) == 0) {
                    throw new IllegalArgumentException("Duplicate points.");
                }
            }
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
