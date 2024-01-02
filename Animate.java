import uk.ac.warwick.dcs.maze.logic.IRobot;
import uk.ac.warwick.dcs.maze.logic.Maze;

import java.awt.*;
import java.util.ArrayList;

public class Animate
{
    Maze maze;
    int frameCount = 0;
    private static final int ANIMATION_FRAMES = 64;
    private Point center;

    // Maze must be set to a blank maze beforehand with size 16, 16
    public void controlRobot(IRobot robot)
    {
        maze = robot.getMaze();
        // Set the maze origin
        center = new Point(maze.getWidth() / 2, maze.getHeight() / 2);

        // Initially called with all 4 points at the center, that way, the calculated vertices will be N, E, S and W of the origin position.
        doAnimation(getPointVertices(new Point[] { center, center, center, center }));
    }

    private void setCell(Point point)
    {
        this.maze.setCellType(point.x, point.y, Maze.WALL);
    }

    private void doAnimation(Point[] oldPointsOnCircumference)
    {
        // Start with a central circle by setting the square at half of the width and height of the maze to be an active cell. Execute this only on the first frame.
        if(frameCount == 0)
        {
            setCell(center);
        }

        Point[] pointsOnCircumference = getPointVertices(oldPointsOnCircumference);
        // Now we have the vertices, fill in the gaps diagonally until we reach another vertex.

        ArrayList<Point> diagonals = new ArrayList<>();

        // From the leftmost to right most point, fill in the gaps for +y and -y.
        for(int x = -(pointsOnCircumference[3].x) + 1; x < pointsOnCircumference[1].x; x++)
        {

        }

        for(Point p : pointsOnCircumference)
        {
            setCell(p);
        }

        frameCount++;
        if(!stopAnimation()) doAnimation(pointsOnCircumference);
    }

    private Point[] getPointVertices(Point[] oldPointsOnCircumference)
    {
        // Four point elements in this array, each representing the north, east. south and west-most locations within the circle.
        Point[] pointsOnCircumference = new Point[4];
        for(int i = Direction.NORTH; i <= Direction.WEST; i++)
        {
            // Gets the old location from last frame
            Point currPoint = oldPointsOnCircumference[i];

            // Dependent on which vertex of the circle we are looking at, increment the position of the point in that direction
            switch(i) {
                case Direction.NORTH:
                    pointsOnCircumference[i] = new Point(currPoint.x, currPoint.y--);
                    break;
                case Direction.SOUTH:
                    pointsOnCircumference[i] = new Point(currPoint.x, currPoint.y++);
                    break;
                case Direction.EAST:
                    pointsOnCircumference[i] = new Point(currPoint.x++, currPoint.y);
                    break;
                case Direction.WEST:
                    pointsOnCircumference[i] = new Point(currPoint.x--, currPoint.y);
                    break;
                default:
                    break;
            }
        }

        return pointsOnCircumference;
    }

    private boolean stopAnimation()
    {
        return frameCount > ANIMATION_FRAMES;
    }

    enum Direction
    {;
        static final int NORTH = 0;
        static final int EAST = 1;
        static final int SOUTH = 2;
        static final int WEST = 3;
    }

}