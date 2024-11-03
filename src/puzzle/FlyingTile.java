package puzzle;
import shapes.*;

public class FlyingTile extends BaseTile{

    private Circle innerCircle;

    public FlyingTile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        int innerSize = Tile.SIZE - 10; // Reducimos 5 píxeles en cada lado para dejar el borde
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        this.makeVisible();       
        innerCircle = new Circle(innerSize, innerXPosition, innerYPosition,Puzzle.lightBrown);
    }

    @Override
    public void makeVisible(){
        super.makeVisible();
        if (innerCircle != null) innerCircle.makeVisible();
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (innerCircle != null) innerCircle.makeInvisible();
    }

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if (innerCircle != null) innerCircle.moveVertical(distance);
    }

    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if (innerCircle != null) innerCircle.moveHorizontal(distance);
    }
}