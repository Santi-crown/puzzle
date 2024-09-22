import java.awt.Color;

public class Tile {
    private int size;
    private char label;
    private int x;
    private int y;
    private int padding;
    private int row;
    private int col;
    private boolean hasGlue = false;
    private boolean isStuck = false;
    private boolean visited = false;
    private Color tileColor;
    private Color originalColor;
    private Rectangle rectangle;
    private final Color lightBrown;

    public Tile(int size, char label, int x, int y, int padding, int row, int col, Color lightBrown) {
        this.size = size;
        this.label = label;
        this.x = x;
        this.y = y;
        this.padding = padding;
        this.row = row;
        this.col = col;
        this.lightBrown = lightBrown;
        this.tileColor = getColorFromLabel(label);
        this.originalColor = tileColor;
        if (label != '*' && label != 'n' && label != '\0') {
            rectangle = new Rectangle(size, size, tileColor, x, y);
        }
    }

    private Color getColorFromLabel(char label) {
        switch (label) {
            case 'r':
                return Color.RED;
            case 'g':
                return Color.GREEN;
            case 'b':
                return Color.BLUE;
            case 'y':
                return Color.YELLOW;
            case '*':
            case 'n':
            case '\0':
                return lightBrown;
            default:
                return Color.GRAY;
        }
    }

    // Métodos getters y setters

    public char getLabel() {
        return label;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public void setTileColor(Color color) {
        this.tileColor = color;
        if (rectangle != null) {
            rectangle.changeColor(color);
        }
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    // Métodos para mover la baldosa

    public void moveVertical(int distance) {
        if (rectangle != null) {
            rectangle.moveVertical(distance);
        }
        this.y += distance;
    }

    public void moveHorizontal(int distance) {
        if (rectangle != null) {
            rectangle.moveHorizontal(distance);
        }
        this.x += distance;
    }
}
