import java.awt.Color;

public class Tile extends Rectangle{
    private Color color;
    private char label;
    public static final int SIZE = 50;
    public static final int MARGIN = 10;
    public static final int PADDING = 10; // Padding interno
    private int row; // Fila de la baldosa
    private int column; // Columna de la baldosa
    private boolean hasGlue = false;
    private boolean isStuck = false;
    private boolean visited = false; // For tracking during tilt
    private Color originalColor;
    private int xPos;
    private int yPos;
    private boolean isHole = false;
    public static Color fixedTile = new Color(139, 0, 0); 
    
    public Tile(char label, int xPosition, int yPosition,int row, int column) {
        this.label = label;
        this.row = row;
        this.column = column;
        xPos = xPosition;
        yPos = yPosition;
      
        int effectiveSize = SIZE - 2 * PADDING; // Tamaño efectivo después de aplicar padding

        // Cambia el tamaño del rectángulo para reflejar el padding
        this.changeSize(SIZE, SIZE);
        
        // Cambia el color y mueve el rectángulo visible
        this.setTileColor(label);
        this.moveHorizontal(xPosition);
        this.moveVertical(yPosition);
        this.makeVisible();
    }
    
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
            default:
                color = lightBrown;
        }

        this.originalColor = color;
        this.changeColor(this.color);
    }
    
    public Color getTileColor(){
        return this.color;
    }
    
    // New method to set the color directly
    public void setTileColor(Color newColor) {
        this.color = newColor;
        this.changeColor(newColor);
    }

    // Methods to manage glue and stuck states
    public boolean hasGlue() {
        return hasGlue;
    }

    public void setHasGlue(boolean hasGlue) {
        this.hasGlue = hasGlue;
    }

    public boolean isStuck() {
        return isStuck;
    }

    public void setIsStuck(boolean isStuck) {
        this.isStuck = isStuck;
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    public void setRow(int newRow) {
        this.row = newRow;
    }

    public void setCol(int newCol) {
        this.column = newCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return column;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char newLabel) {
        this.label = newLabel;
    }

    // For tracking during tilt
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    public int getXPos(){
        return xPos;
    }
    
    public int getYPos(){
        return yPos;
    }
    
    public void setXPos(int xPos){
        this.xPos = xPos;
    }
    
    public void setYPos(int yPos){
        this.yPos = yPos;
    }
    
    public boolean getIsHole(){
        return this.isHole;
    }
    
    public void setIsHole(boolean isHole){
        this.isHole = isHole;
    }
    
    public int getSize(){
        return SIZE;
    }
      
     // // <----------------------------------- IMPLEMENTING FIXEDTILES METHOD ----------------------------------->
    public void blink() {
        // Store the original color
         
        Color originalColor = this.color;
            for (int i = 0; i < 4; i++) { // Blink 4 times
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    //Thread.sleep(200); // 200 milliseconds
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);
                    //Thread.sleep(200);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    this.changeColor(fixedTile);
                    
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);
                    this.changeColor(originalColor);    
                    this.changeColor(originalColor);    
                    this.changeColor(originalColor);    
                    
                }

    
    }
}
