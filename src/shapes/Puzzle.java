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
    private Rectangle board;
    private Color color;
    
    // Board color
    Color lightBrown = new Color(207, 126, 60);
    
    
    public Puzzle(int h, int w){        
        board = new Rectangle();
        color = lightBrown;
        board.changeSize(h, w);        
        board.changeColor(color);
        board.makeVisible();
        board.moveHorizontal(100);
        board.moveVertical(50);
    }
    
    
    
    
    
}
