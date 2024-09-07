import java.awt.Color;

/**
 * Write a description of class Tile here.
 *
 * @author Cristian Santiago Pedraza
 * @version (2024)
 */
public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;

    public Tile(int h, int w, char label, int xPosition, int yPosition) {        
        this.size = size;
        this.label = label;
        this.changeSize(h, w);
        Color lightBrown = new Color(207, 126, 60);
        
        switch (label) {
            case 'r':
                this.color = Color.RED;
                break;
            case 'b':
                this.color = Color.BLUE;
                break;
            case 'y':
                this.color = Color.YELLOW;
                break;
            case 'g':
                this.color = Color.GREEN;
                break;
            default:
                this.color = lightBrown;
        }
        
        this.changeColor(this.color);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.makeVisible();
    }
}