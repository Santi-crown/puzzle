package puzzle;
import shapes.*;
import java.util.Random;
import java.awt.Color;

// WildTile es un tipo de baldosa comodín que al hacer tilt, cambia su color de forma aleatoria, esto se hará para cada vez que se haga tilt. 
// Ademas, las wildTiles no se pueden eliminar
public class WildTile extends BaseTile{
	private Rectangle verticalRectangle; // Rectángulo interior de color blanco
	private Rectangle horizontalRectangle; // Rectángulo interior de color blanco
	
	
	public WildTile (char label, int xPosition, int yPosition,int row, int column) {		
		super(label, xPosition, yPosition, row, column);
	        
	    int withd = Tile.SIZE; // Reducimos 5 píxeles en cada lado para dejar el borde
	    int height = 10;
	    int innerXPosition = this.getXPos() + 20;
	    int innerYPosition = this.getYPos();
	    
	    int verticalInnerXPosition = this.getXPos();
	    
	    //this.makeVisible();       
	    verticalRectangle = new Rectangle(withd, height, new Color(75, 0, 130), innerXPosition, innerYPosition);
	    horizontalRectangle = new Rectangle(height, withd, new Color(75, 0, 130), verticalInnerXPosition, innerYPosition);
        this.makeVisible();
        verticalRectangle.makeVisible();
        horizontalRectangle.makeVisible();
	}
	
	@Override
    public void makeVisible(){
        super.makeVisible();
        if (verticalRectangle != null) verticalRectangle.makeVisible();
        if (horizontalRectangle != null) horizontalRectangle.makeVisible();
    }

    @Override
    public void makeInvisible(){
        super.makeInvisible();
        if (verticalRectangle != null) verticalRectangle.makeInvisible();
        if (horizontalRectangle != null) horizontalRectangle.makeInvisible();
    }

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if (verticalRectangle != null) verticalRectangle.moveVertical(distance);
        if (horizontalRectangle != null) horizontalRectangle.moveVertical(distance);
    }

    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if (verticalRectangle != null) verticalRectangle.moveHorizontal(distance);
        if (horizontalRectangle != null) horizontalRectangle.moveHorizontal(distance);
    }
    
    public void setRandowColor() {
    	
    	char[] colors = {'r', 'y', 'b', 'g'};
    	
    	Random randomColor = new Random();
    	
    	int index = randomColor.nextInt(colors.length);
    	char ramdomColorChoice = colors[index];
    	
    	this.setTileColor(ramdomColorChoice);    
    	this.makeVisible();
    }
}