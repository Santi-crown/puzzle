package puzzle;
import java.awt.Color;

import shapes.Circle;
import shapes.Rectangle;
import shapes.Triangle;

public abstract class BaseTile extends Rectangle{
    // Attributes
    protected Color color;
    protected char label;    
    protected int row; // Tile row
    protected int column; // Tile Column
    protected boolean hasGlue = false;
    protected boolean isStuck = false;
    protected boolean visited = false; // For tracking during tilt
    protected Color originalColor;
    protected int xPos;
    protected int yPos;
    protected boolean isHole = false;
    protected boolean isFixed = true; // It is true because if after validate col and row it still is true, it's because it is really fixed. 
    protected boolean superGlue = false; // Define super glue type
    protected boolean fragileGlue = false; // Define fragile glue type
    protected Triangle glueTriangle;
    
    public BaseTile(char label, int xPosition, int yPosition,int row, int column) {
    	
        xPos = xPosition;
        yPos = yPosition;
        this.row = row;
        this.column = column;
        this.label = label;
        this.isFixed = false; // Valor inicial por defecto
        this.changeSize(Tile.SIZE, Tile.SIZE);                
        setTileColor(label);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        //this.makeVisible();
    }

     /**
     * Sets the color of the tile based on its label.
     *
     * @param label The label character to determine the tile color.
     */
    public void setTileColor(char label){
        Color lightBrown = Puzzle.lightBrown; 
        this.label = label;

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
            case 'n':
                color = lightBrown;
                break;
            case 'f':
            	color = Color.GRAY;
            	break;
            case 'l':
            	color = Color.WHITE;
            	break;
            case 'x':
            	color = Color.GREEN;
            	break;
            case 'o':
            	color = lightBrown;
            	break;
            default:
                color = lightBrown;
        }
        this.originalColor = color;
        this.changeColor(this.color);
    }

        
    /**
     * Gets the current color of the tile.
     *
     * @return The color of the tile.
     */
    public Color getTileColor(){
        return color;
    }
    
    /**
     * Sets the tile color to a specific value, in this case, for pale color when a tile hasGlue or isStuck.
     *
     * @param newColor The new paler color for the tile.
     */
    public void setTileColor(Color newColor) {
        color = newColor;
        this.changeColor(newColor);
    }

    // Methods to manage glue and stuck states

     /**
     * Checks if the tile has glue.
     *
     * @return True if the tile has glue, false otherwise.
     */    
    public boolean hasGlue() {
        return hasGlue;
    }

    /**
     * Sets whether the tile has glue or not.
     *
     * @param hasGlue True if the tile should have glue, false otherwise.
     */
    public void setHasGlue(boolean hasGlue) {
        this.hasGlue = hasGlue;        
    }
    
    
    public boolean isStuck() {
        return isStuck;
    }

    /**
     * Sets whether the tile is stuck or not.
     *
     * @param isStuck True if the tile should be stuck, false otherwise.
     */
    public void setIsStuck(boolean isStuck) {
        this.isStuck = isStuck;
    }

    /**
     * Gets the original color of the tile.
     *
     * @return The original color of the tile.
     */
    public Color getOriginalColor() {
        return originalColor;
    }

    /**
     * Gets the row index of the tile.
     *
     * @return The row index of the tile.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row index of the tile.
     *
     * @param newRow The new row index for the tile.
     */
    public void setRow(int newRow) {
        this.row = newRow;
    }

    /**
     * Gets the column index of the tile.
     *
     * @return The column index of the tile.
     */
    public int getCol() {
        return column;
    }

    /**
     * Sets the column index of the tile.
     *
     * @param newCol The new column index for the tile.
     */
    public void setCol(int newCol) {
        this.column = newCol;
    }

    /**
     * Gets the label of the tile.
     *
     * @return The label character of the tile.
     */
    public char getLabel() {
        return label;
    }

    /**
     * Sets the label of the tile.
     *
     * @param newLabel The new label character for the tile.
     */
    public void setLabel(char newLabel) {
        this.label = newLabel;
    }

    /**
     * Checks if the tile has been visited.
     *
     * @return True if the tile has been visited, false otherwise.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets whether the tile has been visited or not.
     *
     * @param visited True if the tile has been visited, false otherwise.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Gets the x-coordinate position of the tile.
     *
     * @return The x-coordinate position.
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * Sets the x-coordinate position of the tile.
     *
     * @param xPos The new x-coordinate position.
     */
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Gets the y-coordinate position of the tile.
     *
     * @return The y-coordinate position.
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * Sets the y-coordinate position of the tile.
     *
     * @param yPos The new y-coordinate position.
     */
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Checks if the tile is a hole.
     *
     * @return True if the tile is a hole, false otherwise.
     */
    public boolean getIsHole() {
        return isHole;
    }

    /**
     * Sets whether the tile is a hole.
     *
     * @param isHole True if the tile is a hole, false otherwise.
     */
    public void setIsHole(boolean isHole) {
        this.isHole = isHole;
    }

    /**
     * Gets the size of the tile.
     *
     * @return The size of the tile.
     */
    public int getSize() {
        return Tile.SIZE;
    }

    /**
     * Makes the tile blink to indicate that it is a fixed tile.
     */
    public void blink() {
        Color originalColor = this.color;
        for (int i = 0; i < 4; i++) {
            changeColorMultipleTimes(Puzzle.lightBrown, 5);
            changeColorMultipleTimes(originalColor, 5);
        }
    }

    /**
     * Changes the tile color multiple times to create a blinking effect.
     *
     * @param color The color to change to.
     * @param times The number of times to change the color.
     */
    private void changeColorMultipleTimes(Color color, int times) {
        for (int i = 0; i < times; i++) {
            this.changeColor(color);
            this.changeColor(color);
        }
    }
    
    /**
     * Sets a tile as not fixed
     *     
     */    
    public void setIsNotFixed(){
        isFixed = false;
    }
    
    
    /**
     * Gets the fixed status of the tile.
     * 
     * @return true if the tile is fixed (cannot move), false otherwise.
     */
    public boolean getFixedStatus(){
        return isFixed;
    }

    
 // Getter y Setter para superGlue
    public boolean hasSuperGlue() {
        return superGlue;
    }
    
    public boolean hasFragileGlue()
    {
    	return fragileGlue;
    }
    
    public void setSuperGlue(boolean superGlue) {
    	
        this.superGlue = superGlue;
        if (superGlue) {
        	glueTriangle = new Triangle();
            glueTriangle.changeSize(10, 10); // Tamaño pequeño
            glueTriangle.changeColor(Color.BLACK); // Color del triángulo
            //glueTriangle.makeInvisible();
            // Posicionar el triángulo en el centro de la baldosa
            int centerX = this.getXPos() + Tile.SIZE / 2 - glueTriangle.getWidth() / 2 + 4;
            int centerY = this.getYPos() + Tile.SIZE / 2 - glueTriangle.getHeight() / 2 + 1;
            glueTriangle.setPosition(centerX, centerY);
            glueTriangle.makeVisible();
            
            // Depuración
            //System.out.println("Triángulo de superGlue posicionado en: (" + centerX + ", " + centerY + ")");
        } else {
            glueTriangle.makeInvisible();
        }
    }
    
    
    public void setFragileGlue(boolean fragileGlue) {
    	this.fragileGlue = fragileGlue;
    	if (fragileGlue) {
        	glueTriangle = new Triangle();
            glueTriangle.changeSize(40, 40); // Tamaño pequeño
            glueTriangle.changeColor(new Color(200, 162, 200)); // Color del triángulo
            //glueTriangle.makeInvisible();
            // Posicionar el triángulo en el centro de la baldosa
            int centerX = this.getXPos() + Tile.SIZE / 2 - glueTriangle.getWidth() / 2 + 20;
            int centerY = this.getYPos() + Tile.SIZE / 2 - glueTriangle.getHeight() / 2;
            glueTriangle.setPosition(centerX, centerY);
            glueTriangle.makeVisible();            

        } else {
            glueTriangle.makeInvisible();
        }
    }

    // Sobrescribir los métodos de movimiento si superGlue está activo
    @Override
    public void moveHorizontal(int distance){
        super.moveHorizontal(distance);
        if ((superGlue || fragileGlue) && glueTriangle != null) {
            glueTriangle.moveHorizontal(distance);
        }
    }

    @Override
    public void moveVertical(int distance){
        super.moveVertical(distance);
        if ((superGlue || fragileGlue) && glueTriangle != null) {
            glueTriangle.moveVertical(distance);
        }
    }
    
    
}