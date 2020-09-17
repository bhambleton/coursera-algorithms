# Collinear Points

Write a program to recognize line patterns in a given set of points.

**The problem.** Given a set of n distinct points in the plane, find every (maximal) line segment that connects a subset of 4 or more of the points.

**Brute force.** Write a program BruteCollinearPoints.java that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments

*Performance requirement.* The order of growth of the running time of your program should be n4 in the worst case and it should use space proportional to n plus the number of line segments returned.

**A faster, sorting-based solution.** Write a program FastCollinearPoints.java that implements the following algorithm:
Given a point p, the following method for each of the n points to determine whether p participates in a set of 4 or more collinear points.

* Think of p as the origin.
* For each other point q, determine the slope it makes with p.
* Sort the points according to the slopes they makes with p.
* Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.

*Performance requirement.* The order of growth of the running time of your program should be n2 log n in the worst case and it should use space proportional to n plus the number of line segments returned. FastCollinearPoints should work properly even if the input has 5 or more collinear points.
