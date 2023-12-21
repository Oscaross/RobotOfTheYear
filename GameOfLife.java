import uk.ac.warwick.dcs.maze.logic.IRobot;
import uk.ac.warwick.dcs.maze.logic.Maze;

import java.awt.*;
import java.util.ArrayList;
/*


Robot of the Year 2023
Author: Oscar Horner

This is an implementation of Conway's Game of Life (https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) with a robot-related twist (treat it as a 2 for 1 game)!

 */

public class GameOfLife
{
    ArrayList<Cell> cells;
    private int iter = 0;

    // Entry point for the program (treat it as main method) that immediately hands over control to our custom implementation with the robot object:
    public void controlRobot(IRobot robot)
    {
        if(iter == 0) this.cells = initializeStartCells(robot.getMaze());

        tick(robot.getMaze());
        iter++;
    }

    public void reset()
    {
        this.iter = 0;
    }

    private ArrayList<Cell> initializeStartCells(Maze maze)
    {
        ArrayList<Cell> cells = new ArrayList<>();

        // Get each cell that exists in the current maze and create a Cell object for each of them by iterating over them.
        Point start = maze.getStart();
        Point finish = maze.getFinish();

        for(int x = start.x; x <= finish.x; x++)
        {
            for(int y = start.y; y <= finish.y; y++)
            {
                Point pos = new Point(x, y);
                cells.add(new Cell(pos, maze.getCellType(pos), maze));
            }
        }

        return cells;
    }

    // A "tick" is called in conjunction with the maze environment's "move robot" function. Each time tick is called, a new generation will occur. The number of times tick() is called is the number of generations.
    private void tick(Maze maze)
    {
        // Set each cell to alive or dead based on the number of neighbours that it contains.

        // Get the maze size from the number of cells in the current iteration.
        for(int i = 0; i < cells.size(); i++)
        {
            Cell c = cells.get(i);

            int neighbours = c.liveNeighbours.size();
            // Rule 4: Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
            if(!c.alive)
            {
                if(neighbours == 3) c.setAlive(true, maze);
            }
            else
            {
                // Rule 1: Any live cell with fewer than two live neighbours dies, as if by underpopulation
                if(neighbours < 2) c.setAlive(false, maze);

                // Rule 3: Any live cell with more than three live neighbours dies, as if by overpopulation.
                else if(neighbours > 3) c.setAlive(false, maze);

                // Rule 2: Any live cell with two or three live neighbours lives on to the next generation.
                // Rule 2 requires no additional code, if no statements fire above then alive remains true and the cell lives on.
            }
        }
    }
}

class Cell
{
    Point pos;
    boolean alive;
    ArrayList<Point> liveNeighbours = new ArrayList<>();

    public Cell(Point pos, int alive, Maze maze)
    {
        this.pos = pos;
        // If the cell type is a wall then it's dead, if it's a passage it is alive.
        setAlive(alive != IRobot.WALL, maze);

        // On creation of a cell, generate an ArrayList containing the co-ordinates of all living neighbours.
        this.liveNeighbours = getLiveNeighbours(maze, pos);
    }

    private ArrayList<Point> getLiveNeighbours(Maze maze, Point pos)
    {
        ArrayList<Point> liveNeighbours = new ArrayList<>();
        // Iterates 3 times, first checks left, then center, then right with respect to the original cell position.
        for(int x = pos.x - 1; x <= pos.x + 1; x++)
        {
            // Same logic as the loop it is nested within, but for y coordinates.
            for(int y = pos.y - 1; y <= pos.y + 1; y++)
            {
                // Current coordinates being analyzed as a Point.
                Point curr = new Point(x, y);

                System.out.println(curr);
                // If the Point(x,y) is equal to the position of the cell then Point(x,y) is the position of the cell and hence is not its neighbour, so skip over the iteration.
                if(curr.equals(pos)) continue;

                if(maze.getCellType(curr) != IRobot.WALL) liveNeighbours.add(curr);
            }
        }

        return liveNeighbours;
    }

    /**
     * Sets the value of the class member "alive" to either true or false depending on the square it was passed.
     * @param alive whether the cell should be alive or dead.
     * @param maze a reference to the current maze in order to actually convert the cell to an alive or dead type.
     */
    protected void setAlive(boolean alive, Maze maze)
    {
        this.alive = alive;
        // Set the cell to a PASSAGE if the cell is alive, otherwise set the cell to a WALL.
        maze.setCellType(pos.x, pos.y, (alive) ? IRobot.PASSAGE : IRobot.WALL);
    }


}
