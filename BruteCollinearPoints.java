/* *****************************************************************************
 *  Name:              Branko Milosevic
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private int numSegments;
    private final int numPoints;
    private final Point[] intPoints;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

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

        intPoints = Arrays.copyOf(points, points.length);

        Arrays.sort(intPoints);
        // for (Point p : intPoints) System.out.println(p.toString());
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {

        LineSegment[] ls;
        ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

        for (int i1 = 0; i1 < numPoints; i1++) {
            for (int i2 = i1 + 1; i2 < numPoints; i2++) {
                for (int i3 = i2 + 1; i3 < numPoints; i3++) {
                    for (int i4 = i3 + 1; i4 < numPoints; i4++) {
                        double a;
                        double b;
                        double c;
                        a = intPoints[i1].slopeTo(intPoints[i2]);
                        b = intPoints[i1].slopeTo(intPoints[i3]);
                        c = intPoints[i1].slopeTo(intPoints[i4]);
                        if (Double.compare(a, b) == 0 && Double.compare(b, c) == 0) {
                            segments.add(new LineSegment(intPoints[i1], intPoints[i4]));
                            numSegments++;

                            // System.out.println("i1: " + i1 + "   i2: " + i2 + "   i3: " + i3 + "   i4: " + i4);
                        }
                    }
                }
            }
        }

        ls = new LineSegment[numSegments];
        for (int i = 0; i < numSegments; i++) ls[i] = segments.get(i);

        return ls;

    }
}
