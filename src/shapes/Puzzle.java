import java.awt.Color;
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
    // Create the ending puzzle board    
    
}
