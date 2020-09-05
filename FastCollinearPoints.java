/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] lineSegments;

    /**
     * Finds all line segments containing 4 points or more points.
     */
    public FastCollinearPoints(Point[] points) {

        checkNull(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicate(sortedPoints);

        final int N = points.length;
        final List<LineSegment> maxLineSegments = new LinkedList<>();

        for (int i = 0; i < N; i++) {

            Point p = sortedPoints[i];
            Point[] pointsBySlope = sortedPoints.clone();
            Arrays.sort(pointsBySlope, p.slopeOrder());

            // Notice the difference between "sortedPoints" & "pointsBySlope":
            // the below points are taken from "pointsBySlope".
            int x = 1;
            while (x < N) {

                LinkedList<Point> candidates = new LinkedList<>();
                final double SLOPE_REF = p.slopeTo(pointsBySlope[x]);
                do {
                    candidates.add(pointsBySlope[x++]);
                } while (x < N && p.slopeTo(pointsBySlope[x]) == SLOPE_REF);

                // Candidates have a max line segment if ...
                // 1. Candidates are collinear: At least 4 points are located
                //    at the same line, so at least 3 without "p".
                // 2. The max line segment is created by the point "p" and the
                //    last point in candidates: so "p" must be the smallest
                //    point having this slope comparing to all candidates.
                if (candidates.size() >= 3
                        && p.compareTo(candidates.peek()) < 0) {
                    Point min = p;
                    Point max = candidates.removeLast();
                    maxLineSegments.add(new LineSegment(min, max));
                }
            }
        }
        lineSegments = maxLineSegments.toArray(new LineSegment[0]);
    }

    private void checkNull(Point[] points) {
        if (points == null) {
            throw new NullPointerException("The array \"Points\" is null.");
        }
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException(
                        "The array \"Points\" contains null element.");
            }
        }
    }

    private void checkDuplicate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate(s) found.");
            }
        }
    }

    /**
     * The number of line segments.
     */
    public int numberOfSegments() {
        return lineSegments.length;
    }

    /**
     * The line segments.
     */
    public LineSegment[] segments() {
        return lineSegments.clone();
    }
}

    /*    private int numSegments;
    private final Point[] intPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // final int numPoints;

        if (points == null) throw new IllegalArgumentException("Points[] == null !!!");
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException("Point[" + i + "] == null !!!");

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Repeated Point at index: " + i);

        numSegments = 0;
        // numPoints = points.length;
        // intPoints = new Point[numPoints];
        // for (int i = 0; i < numPoints; i++) intPoints[i] = points[i];

        intPoints = Arrays.copyOf(points, points.length);
    }


    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        ArrayList<LineSegment> res = new ArrayList<LineSegment>();
        ArrayList<Point> maxPoints = new ArrayList<Point>();
        ArrayList<Point> minPoints = new ArrayList<Point>();


        for (int i = 0; i < intPoints.length; i++) {
            Arrays.sort(intPoints, intPoints[i].slopeOrder());

            int j = 1;
            while (j < intPoints.length) {
                ArrayList<Point> a = new ArrayList<Point>();
                a.add(intPoints[0]);
                a.add(intPoints[j]);
                double curSlope = intPoints[0].slopeTo(intPoints[j]);

                while ((j + 1 < intPoints.length)
                        && Double.compare(curSlope, intPoints[0].slopeTo(intPoints[j + 1])) == 0) {
                    j++;
                    a.add(intPoints[j]);
                    curSlope = intPoints[0].slopeTo(intPoints[j]);
                }
                if (a.size() >= 4) {
                    Point max = getMax(a);
                    Point min = getMin(a);
                    if (!hasDuplicateInLineSegment(maxPoints, minPoints, max, min)) {
                        maxPoints.add(max);
                        minPoints.add(min);
                        res.add(new LineSegment(max, min));
                        numSegments++;
                    }
                }
                j++;
            }
        }


        int index = 0;
        LineSegment[] lineSeg = new LineSegment[res.size()];
        for (LineSegment temp : res) {
            lineSeg[index++] = temp;
        }
        return lineSeg;
    }

    private boolean hasDuplicateInLineSegment(ArrayList<Point> maxPoints,
                                              ArrayList<Point> minPoints, Point max, Point min) {
        for (int i = 0; i < maxPoints.size(); i++) {
            if (max.compareTo(maxPoints.get(i)) == 0 && min.compareTo(minPoints.get(i)) == 0)
                return true;
        }
        return false;
    }

    private Point getMin(ArrayList<Point> points) {
        if (points.isEmpty()) return null;
        Point min = points.get(0);
        for (Point p : points) if (min.compareTo(p) > 0) min = p;

        return min;
    }

    private Point getMax(ArrayList<Point> points) {
        if (points.isEmpty()) return null;
        Point max = points.get(0);
        for (Point p : points) if (max.compareTo(p) < 0) max = p;

        return max;
    }

}
*/
/*

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private int numSegments;
    private final Point[] intPoints;

    private class Segment {
        public ArrayList<Point> points;
        public double slope;

        public Segment(ArrayList<Point> ppoints, double sslope) {
            points = ppoints;
            slope = sslope;
        }
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        final int numPoints;

        if (points == null) throw new IllegalArgumentException("Points[] == null !!!");
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException("Point[" + i + "] == null !!!");

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Repeated Point at index: " + i);

        numSegments = 0;
        numPoints = points.length;
        // intPoints = new Point[numPoints];
        // for (int i = 0; i < numPoints; i++) intPoints[i] = points[i];
        intPoints = points.clone();

        Arrays.sort(intPoints);
    }


    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] ls;
        ArrayList<Segment> segments = new ArrayList<Segment>();

        for (int i = 0; i < intPoints.length; i++) {
            Arrays.sort(intPoints, intPoints[i].slopeOrder());

            for (int j = 0; j < intPoints.length - 1; j++) {
                double slope = intPoints[j].slopeTo(intPoints[j + 1]);
                int numOfPoints = 1;
                ArrayList<Point> segPoints = new ArrayList<Point>();
                segPoints.add(0, intPoints[j]);

                while ((j + numOfPoints) < intPoints.length) {
                    if (
                            Double.compare(slope, intPoints[j].slopeTo(intPoints[j + numOfPoints]))
                                    == 0) {

                        segPoints.add(numOfPoints, intPoints[j + numOfPoints]);
                        numOfPoints++;
                    }
                    else break;
                }

                if (numOfPoints > 3) {

                    boolean segToAdd = true;
                    for (Segment sS : segments) {
                        for (Point pP : segPoints) {
                            if (sS.points.contains(pP) && Double.compare(sS.slope, slope) == 0) {
                                if (sS.points.size() < segPoints.size()) sS.points = segPoints;
                                segToAdd = false;
                                break;
                            }
                            if (!segToAdd) break;
                        }
                    }
                    if (segToAdd)
                        segments.add(segments.size(), new Segment(segPoints, slope));

                }
            }
        }

        numSegments = segments.size();

        ls = new LineSegment[numSegments];
        int i = 0;

        for (Segment s : segments) {
            ls[i] = new LineSegment(getMin(s.points), getMax(s.points));
            i++;
        }

        return ls;
    }

    private Point getMin(ArrayList<Point> points) {
        if (points.size() < 1) return null;
        Point min = points.get(0);
        for (Point p : points) if (min.compareTo(p) > 0) min = p;

        return min;
    }

    private Point getMax(ArrayList<Point> points) {
        if (points.size() < 1) return null;
        Point max = points.get(0);
        for (Point p : points) if (max.compareTo(p) < 0) max = p;

        return max;
    }

}

*/
