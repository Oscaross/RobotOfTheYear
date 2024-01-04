import uk.ac.warwick.dcs.maze.logic.IRobot;
import uk.ac.warwick.dcs.maze.logic.Maze;

import java.awt.*;
import java.util.ArrayList;

public class Animate
{
    Maze maze;
    int frameCount = 0;
    private static final int ANIMATION_FRAMES = 15;
    private Point center;

    // Maze must be set to a blank maze beforehand with size 16, 16
    public void controlRobot(IRobot robot)
    {
        maze = robot.getMaze();
        // Set the maze origin
        center = new Point(maze.getWidth() / 2, maze.getHeight() / 2);

        // Initially called with all 4 points at the center, that way, the calculated vertices will be N, E, S and W of the origin position.
        doAnimation(new Point[] { center, center, center, center });
    }

    public void reset()
    {
        this.frameCount = 0;
    }

    private void setCell(Point point)
    {
        this.maze.setCellType(point.x, point.y, Maze.WALL);
    }

    private void doAnimation(Point[] oldPointsOnCircumference)
    {
        Point[] pointsOnCircumference = new Point[4];
        // Start with a central circle by setting the square at half of the width and height of the maze to be an active cell. Execute this only on the first frame.
        if(frameCount == 0)
        {
            setCell(center);
        }
        else
        {
            pointsOnCircumference = getPointVertices(oldPointsOnCircumference);
        }

        // Now we have the vertices, fill in the gaps diagonally until we reach another vertex.

        ArrayList<Point> diagonals = new ArrayList<>();

        for(Point p : pointsOnCircumference)
        {
            setCell(p);
        }
        for(Point d : diagonals)
        {
            setCell(d);
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
            Point currPoint = (Point)oldPointsOnCircumference[i].clone();

            // Dependent on which vertex of the circle we are looking at, increment the position of the point in that direction
            switch(i) {
                case Direction.NORTH:
                    currPoint.translate(0, -1);
                    break;
                case Direction.SOUTH:
                    currPoint.translate(0, 1);
                    break;
                case Direction.EAST:
                    currPoint.translate(1, 0);
                    break;
                case Direction.WEST:
                    currPoint.translate(-1, 0);
                    break;
                default:
                    break;
            }

            pointsOnCircumference[i] = currPoint;
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
