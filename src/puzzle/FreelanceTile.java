package puzzle;
import shapes.*;

public class FreelanceTile extends BaseTile{
    private Rectangle innerRectangle; // Rectángulo interior de color blanco
    
    public FreelanceTile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        int innerSize = Tile.SIZE - 10; // Reducimos 5 píxeles en cada lado para dejar el borde
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        innerRectangle = new Rectangle(innerSize, innerSize, Puzzle.lightBrown, innerXPosition, innerYPosition);
        this.makeVisible();
        innerRectangle.makeVisible(); // Aseguramos que se dibuje después del rectángulo rojo                              
    }

    @Override
    public void makeVisible(){
        super.makeVisible();
        if (innerRectangle != null) innerRectangle.makeVisible();
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (innerRectangle != null) innerRectangle.makeInvisible();
    }
}