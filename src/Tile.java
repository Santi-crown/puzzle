import java.awt.Color;

public class Tile extends Rectangle {
    private Color color;
    private char label;
    private int size;
    private int padding;
    private int row;
    private int col;
    private boolean hasGlue = false;
    private boolean isStuck = false;
    private boolean visited = false; // For tracking during tilt
    private Color originalColor;

    public Tile(int size, char label, int xPosition, int yPosition, int padding, int row, int col) {
        super(size, size, Color.WHITE, xPosition, yPosition);
        this.size = size;
        this.padding = padding;
        this.label = label;
        this.row = row;
        this.col = col;

        this.setTileColor(label);
        this.makeVisible();
    }

    public void setTileColor(char label) {
        Color lightBrown = new Color(207, 126, 60);

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
            case 'n':
            case '*':
                color = lightBrown;
                break;
            default:
                color = lightBrown;
        }

        this.originalColor = color;
        this.changeColor(this.color);
    }

    public Color getTileColor() {
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
        this.col = newCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
}
