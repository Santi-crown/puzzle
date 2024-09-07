import java.awt.Color;
// To memorize
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class Puzzle here.
 *
 * @author Cristian Santiago Pedraza
 * @version (2024)
 */
public class Puzzle
{
    private int h;
    private int w;
    private Rectangle startingBoard;
    private Rectangle endingBoard;
    private Color color;
    private char[][] starting;
    private char[][] ending;
    private List<Tile> tiles;
    private List<Glue> glues;
    
    // Board color
    Color lightBrown = new Color(207, 126, 60);
    
    // Create the initial puzzle board and tiles
    public Puzzle(int h, int w){
        color = lightBrown;
        
        startingBoard = new Rectangle();        
        startingBoard.changeSize(h * 50, w * 50);        
        startingBoard.changeColor(color);
        startingBoard.makeVisible();
        startingBoard.moveHorizontal(100);
        startingBoard.moveVertical(50);
                
        endingBoard = new Rectangle();        
        endingBoard.changeSize(h * 50, w * 50);     
        endingBoard.changeColor(color);
        endingBoard.makeVisible();
        endingBoard.moveHorizontal((h*50)+ 360);
        endingBoard.moveVertical(50);
    }        
    
    // Create the starting and ending puzzle boards    
    public Puzzle(char[][] starting, char[][] ending){
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        this.glues = new ArrayList<>();      
    }
    
    
}
