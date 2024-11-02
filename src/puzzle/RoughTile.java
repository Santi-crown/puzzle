package puzzle;
import shapes.*;
import java.awt.Color;

public class RoughTile extends BaseTile{
    private Rectangle outerBorder; // Rectangulo que sirve de borde
    public RoughTile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        this.changeColor(Color.GRAY);

        int innerSize = Tile.SIZE - 10; // Reducimos 5 píxeles en cada lado para dejar el borde
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        outerBorder = new Rectangle(innerSize, innerSize, Color.WHITE, innerXPosition, innerYPosition);
        this.makeVisible();
        innerRectangle.makeVisible(); // Aseguramos que se dibuje después del rectángulo rojo                              
    }

    private Color ColorTochangeInsideRectangleColor(char label){
        Color color = Color.GRAY;
        switch (label) {
            case 'r':
                color = Color.RED;
                break;
            case 'b':
                color = Color.BLUE;
                break;
            case 'y':
                color = Color.YELLOW;
                break;
            case 'g':
                color = Color.GREEN;
                break;
            case 'h':
                color = Color.WHITE;
                break;
        }

        return color;
    }
}