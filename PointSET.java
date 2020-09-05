/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> mPoints;

    // construct an empty set of points
    public PointSET() {
        mPoints = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return mPoints.isEmpty();
    }

    // number of points in the set
    public int size() {
        return mPoints.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No Point to insert!!!");

        mPoints.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No Point to check!!!");

        return mPoints.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("No Rect to check!!!");

        Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());

        List<Point2D> pointsInRect = new LinkedList<>();

        for (Point2D p : mPoints.subSet(minPoint, true, maxPoint, true)) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()) {
                pointsInRect.add(p);
            }
        }
        return pointsInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No Point to check !!!");

        if (isEmpty()) {
            return null;
        }

        Point2D next = mPoints.ceiling(p);
        Point2D prev = mPoints.floor(p);

        if (next == null && prev == null) {
            return null;
        }

        double distNext = (next == null ? Double.POSITIVE_INFINITY : p.distanceTo(next));
        double distPrev = (prev == null ? Double.POSITIVE_INFINITY : p.distanceTo(prev));
        double d = Math.min(distNext, distPrev);

        Point2D minPoint = new Point2D(p.x(), p.y() - d);
        Point2D maxPoint = new Point2D(p.x(), p.y() + d);
        Point2D nearest = (next == null ? prev : next);

        for (Point2D candidate : mPoints.subSet(minPoint, true, maxPoint, true)) {
            if (p.distanceTo(candidate) < p.distanceTo(nearest)) {
                nearest = candidate;
            }
        }
        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
