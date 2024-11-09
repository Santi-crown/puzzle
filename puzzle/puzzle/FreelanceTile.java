package puzzle;
import shapes.*;

public class FreelanceTile extends BaseTile{
    private Rectangle innerRectangle; // Inside light brown rectangle 
    
    public FreelanceTile(char label, int xPosition, int yPosition,int row, int column) {
        super(label, xPosition, yPosition, row, column);
        int innerSize = Tile.SIZE - 10; // To reduce 5 pxs for each side to leave the edge
        int innerXPosition = this.getXPos() + 5;
        int innerYPosition = this.getYPos() + 5;
        innerRectangle = new Rectangle(innerSize, innerSize, Puzzle.lightBrown, innerXPosition, innerYPosition);
        this.makeVisible();
        innerRectangle.makeVisible();                              
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

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if (innerRectangle != null) innerRectangle.moveVertical(distance);
    }

    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if (innerRectangle != null) innerRectangle.moveHorizontal(distance);
    }
}