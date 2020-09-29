# KD-Trees



**The problem.** Write a data type to represent a set of points in the unit square (all points have x- and y-coordinates between 0 and 1) using a 2d-tree to support efficient range search (find all of the points contained in a query rectangle) and nearest-neighbor search (find a closest point to a query point).


**Brute-force implementation.** Write a mutable data type PointSET.java that represents a set of points in the unit square.

**2d-tree implementation.** Write a mutable data type KdTree.java that uses a 2d-tree to implement the same API (but replace PointSET with KdTree). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.

The prime advantage of a 2d-tree over a BST is that it supports efficient implementation of range search and nearest-neighbor search. Each node corresponds to an axis-aligned rectangle in the unit square, which encloses all of the points in its subtree. The root corresponds to the unit square; the left and right children of the root corresponds to the two rectangles split by the x-coordinate of the point at the root; and so forth.

**Test clients.**
[KdTreeVisualizer.java](https://github.com/bhambleton/coursera-algorithms/blob/master/assignments/kdtree/KdTreeVisualizer.java) computes and draws the 2d-tree that results from the sequence of points clicked by the user in the standard drawing window.
[RangeSearchVisualizer.java](https://github.com/bhambleton/coursera-algorithms/blob/master/assignments/kdtree/RangeSearchVisualizer.java) reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs range searches on the axis-aligned rectangles dragged by the user in the standard drawing window.
[NearestNeighborVisualizer.java](https://github.com/bhambleton/coursera-algorithms/blob/master/assignments/kdtree/NearestNeighborVisualizer.java) reads a sequence of points from a file (specified as a command-line argument) and inserts those points into a 2d-tree. Then, it performs nearest-neighbor queries on the point corresponding to the location of the mouse in the standard drawing window.
