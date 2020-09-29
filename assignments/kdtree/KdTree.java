/* *****************************************************************************
 *  Name:              Brian Hambleton
 *  Last modified:     27 September 2020
 *
 *  2-d tree implementation
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;
    private Point2D closest;
    private double minDist;

    public KdTree() {
        this.root = null;
        this.closest = null;
        this.size = 0;
        this.minDist = Double.POSITIVE_INFINITY;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (this.root == null) { // empty tree, insert first node
            this.root = new Node(p, 0, null, null, new RectHV(0, 0, 1, 1));
            this.size++;
        }
        else if (!contains(p)) {
            insert(root, p, true);
        }
    }

    private void insert(Node r, Point2D p, boolean axis) {
        // if axis true, compare by x-coord, otherwise compare by y-coord
        Comparator<Point2D> c = axis ? Point2D.X_ORDER : Point2D.Y_ORDER;

        // p is < root go left
        if (c.compare(p, r.data) < 0) {
            if (r.left == null) {
                if (axis) { // comparing x-coordinates
                    // insert node with a rect left of this node's rect (xmax == this point's x-coord)
                    r.left = new Node(p, r.nodeLevel + 1, null, null,
                                      new RectHV(r.rect.xmin(), r.rect.ymin(), r.data.x(),
                                                 r.rect.ymax()));
                }
                else { // comparing y-coordinates
                    // insert node using rect below this node's rect (ymax == this point's y-coord)
                    r.left = new Node(p, r.nodeLevel + 1, null, null,
                                      new RectHV(r.rect.xmin(), r.rect.ymin(), r.rect.xmax(),
                                                 r.data.y()));
                }
                this.size++;
            }
            else
                insert(r.left, p, !axis); // continue search, move down a level
        }
        else { // p >= root, go right
            if (r.right == null) {
                if (axis) { // comparing x-coordinates
                    // insert node with a rect right of this node's rect (xmin == this point's x-coord)
                    r.right = new Node(p, r.nodeLevel + 1, null, null,
                                       new RectHV(r.data.x(), r.rect.ymin(), r.rect.xmax(),
                                                  r.rect.ymax()));
                }
                else { // comparing y-coordinates
                    // insert node using rect above this node's rect (ymin == this point's y-coord)
                    r.right = new Node(p, r.nodeLevel + 1, null, null,
                                       new RectHV(r.rect.xmin(), r.data.y(), r.rect.xmax(),
                                                  r.rect.ymax()));
                }
                this.size++;
            }
            else
                insert(r.right, p, !axis); // continue search, move down a level
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;

        return contains(root, p, true);
    }

    private boolean contains(Node r, Point2D p, boolean axis) {
        // if axis true, compare by x-coord, otherwise compare by y-coord
        Comparator<Point2D> c = axis ? Point2D.X_ORDER : Point2D.Y_ORDER;

        if (r.data.equals(p)) return true;
        if (c.compare(r.data, p) > 0) {
            if (r.left == null) return false;
            else return contains(r.left, p, !axis);
        }
        else {
            if (r.right == null) return false;
            else return contains(r.right, p, !axis);
        }
    }

    public void draw() {
        // call recursive fx using rectangle the size of drawing board
        draw(this.root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV rect) {
        if (node == null) return;

        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.setPenRadius(0.02);
        node.data.draw();

        StdDraw.setPenRadius(0.01);
        if (node.nodeLevel % 2 == 0) {
            // even level, divide plane with vert line
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.line(node.data.x(), rect.ymin(), node.data.x(), rect.ymax());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), node.data.x(), rect.ymax()));
            draw(node.right, new RectHV(node.data.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        }
        else {
            // odd level, horizontal line
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.line(rect.xmin(), node.data.y(), rect.xmax(), node.data.y());
            draw(node.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.data.y()));
            draw(node.right, new RectHV(rect.xmin(), node.data.y(), rect.xmax(), rect.ymax()));
        }
    }

    // find all points contained in a given query rectangle (rect)
    // recursively searches subtrees using the pruning rule:
    //      - if query rectangle does not intersect the rectangle corresponding to a node, stop
    //      - subtree is searched only if it might contain a point contained in the query rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        if (this.isEmpty()) return null; // empty tree

        List<Point2D> points = new LinkedList<>();
        range(this.root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, List<Point2D> items) {
        if (node == null) return;

        if (rect.contains(node.data)) items.add(node.data);

        if (node.nodeLevel % 2 == 0) { // compare x-coordinates for left/right of vertical segment
            if (rect.xmin() <= node.data.x() && node.data.x() <= rect.xmax()) {
                // intersection, search right (right child) and left (left child)
                range(node.left, rect, items);
                range(node.right, rect, items);
            }
            else if (rect.xmin() > node.data.x())
                range(node.right, rect, items); // search right
            else
                range(node.left, rect, items); // search left
        }
        else { // compare y-coordinates for above/below horizontal segment
            if (rect.ymin() <= node.data.y() && node.data.y() <= rect.ymax()) {
                // intersection, search above (right child) and below (left child)
                range(node.left, rect, items);
                range(node.right, rect, items);
            }
            else if (rect.ymin() > node.data.y())
                range(node.right, rect, items); // search above
            else
                range(node.left, rect, items); // search below
        }
    }

    // nearest-neighbor search to find a closest point to a given query point
    // begins at root and recursively searches subtrees using the rule:
    //      - if the closest point discovered so far is closer than the distance b/w the query point
    //          and the rectangle corresponding to the current node, stop.
    //          Only search a node if it might contain a point closer than the best one found so far
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (this.isEmpty()) return null;
        else {
            this.minDist = Double.POSITIVE_INFINITY;
            nearest(p, this.root);
        }

        return this.closest;
    }

    private void nearest(Point2D p, Node n) {
        if (n == null) return;

        double nDist = n.data.distanceSquaredTo(p);
        if (nDist < this.minDist) {
            this.minDist = nDist;
            this.closest = n.data;
        }

        Comparator<Point2D> c = n.nodeLevel % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;

        if (c.compare(p, n.data) < 0) { // check left & right, query < curr node
            nearest(p, n.left);
            if (n.right != null && n.right.rect.distanceSquaredTo(p) < this.minDist)
                nearest(p, n.right);
        }
        else { // check right & left, query > curr node
            nearest(p, n.right);
            if (n.left != null && n.left.rect.distanceSquaredTo(p) < this.minDist)
                nearest(p, n.left);
        }


        // if (n.nodeLevel % 2 == 0) {
        //     // vertical separator
        //     if (p.x() < n.data.x()) { // check left & right, query < curr node
        //         nearest(p, n.left);
        //         if (n.right != null && n.right.rect.distanceSquaredTo(p) < this.minDist)
        //             nearest(p, n.right);
        //     }
        //     else { // check right & left, query > curr node
        //         nearest(p, n.right);
        //         if (n.left != null && n.left.rect.distanceSquaredTo(p) < this.minDist)
        //             nearest(p, n.left);
        //     }
        // }
        // else {
        //     // horizontal separator
        //     if (p.y() < n.data.y()) { // check above & below
        //         nearest(p, n.left);
        //         if (n.right != null && n.right.rect.distanceSquaredTo(p) < this.minDist)
        //             nearest(p, n.right);
        //     }
        //     else {
        //         nearest(p, n.right);
        //         if (n.left != null && n.left.rect.distanceSquaredTo(p) < this.minDist)
        //             nearest(p, n.left);
        //     }
        // }
    }


    private static class Node {
        private final Point2D data;     // point
        private Node left, right;       // children
        private final int nodeLevel;    // level in tree
        private RectHV rect;            // rect for this node

        private Node(Point2D p, int nodeLevel, Node left, Node right, RectHV rect) {
            this.data = p;
            this.left = left;
            this.right = right;
            this.nodeLevel = nodeLevel;
            this.rect = rect;
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        // tree.insert(new Point2D(0.7, 0.2));
        // tree.insert(new Point2D(0.5, 0.4));
        // tree.insert(new Point2D(0.2, 0.3));
        // tree.insert(new Point2D(0.4, 0.7));
        // tree.insert(new Point2D(0.9, 0.6));
        // Point2D query = new Point2D(0.701, 0.945);
        // Point2D myAns = tree.nearest(query);

        tree.insert(new Point2D(0.875, 0.125));
        tree.insert(new Point2D(0.625, 0.5625));
        tree.insert(new Point2D(0.3125, 0.59375));
        tree.insert(new Point2D(0.125, 0.375));
        tree.insert(new Point2D(0.6875, 0.1875));
        tree.insert(new Point2D(0.4375, 0.03125));
        tree.insert(new Point2D(0.1875, 0.09375));
        tree.insert(new Point2D(0.15625, 0.0));
        tree.insert(new Point2D(1.0, 0.625));
        tree.insert(new Point2D(0.375, 0.65625));
        tree.insert(new Point2D(0.5, 0.875));
        tree.insert(new Point2D(0.46875, 0.0625));
        tree.insert(new Point2D(0.34375, 0.28125));
        tree.insert(new Point2D(0.96875, 0.90625));
        tree.insert(new Point2D(0.90625, 0.96875));
        tree.insert(new Point2D(0.65625, 0.6875));
        tree.insert(new Point2D(0.25, 0.71875));
        tree.insert(new Point2D(0.28125, 0.5));
        tree.insert(new Point2D(0.21875, 0.46875));
        tree.insert(new Point2D(0.03125, 0.25));

        Point2D query = new Point2D(0.84375, 0.21875);
        Point2D myAns = tree.nearest(query);

        StdOut.println("Nearest: " + myAns.toString());
        StdOut.println("Distance: " + myAns.distanceSquaredTo(query));
    }
}
