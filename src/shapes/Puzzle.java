import java.awt.Color;
// To memorize
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
/**
 * Write a description of class Puzzle here.
 *
 * @author Cristian Santiago Pedraza
 * @version (2024)
 */
public class Puzzle
{
    private int h;
    private int w;
    private Rectangle startingBoard;
    private Rectangle endingBoard;
    private Color color;
    private char[][] starting;
    private char[][] ending;
    private List<Tile> tiles;
    private List<Glue> glues;
    
    // Board color
    Color lightBrown = new Color(207, 126, 60);
    
    // Create the initial puzzle board and tiles
    public Puzzle(int h, int w){        
        this.h = h;
        this.w = w;
        color = lightBrown;
        
        startingBoard = new Rectangle();        
        startingBoard.changeSize(h * 50, w * 50);        
        startingBoard.changeColor(color);
        startingBoard.makeVisible();
        startingBoard.moveHorizontal(100);
        startingBoard.moveVertical(50);
                
        endingBoard = new Rectangle();        
        endingBoard.changeSize(h * 50, w * 50);     
        endingBoard.changeColor(color);
        endingBoard.makeVisible();
        // original size + more distance
        endingBoard.moveHorizontal((h*50)+ 360);
        endingBoard.moveVertical(50);
    }        
    
    // Create the starting and ending puzzle boards    
    public Puzzle(char[][] starting, char[][] ending, int h, int w) {
        this.h = h*10; // Ajuste del tamaño de las piezas
        this.w = w*10;
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        
        // Creamos las piezas del puzzle inicial
        int heightCountSpace = 0;
        for (char[] row : starting) {
            int widthCountSpace = 0;
            for (char column : row) {
                // Calculamos la posición de cada pieza
                int xPosition = 100 + (widthCountSpace * this.w);  // Espaciado en el eje x
                int yPosition = 50 + (heightCountSpace * this.h);  // Espaciado en el eje y
                
                // Creamos la pieza y la agregamos a la lista
                Tile tile = new Tile(this.h, this.w, column, xPosition, yPosition);
                tiles.add(tile);
                
                // Aumentamos el espaciado horizontal
                widthCountSpace++;                
            }
            // Aumentamos el espaciado vertical después de cada fila
            heightCountSpace++;            
        }
        
                // Creamos las piezas del puzzle inicial
        int heightCountSpace2 = 0;
        for (char[] row : ending) {
            int widthCountSpace2 = 0;
            for (char column : row) {
                // Calculamos la posición de cada pieza
                int xPosition = (h*12)+ 450 + (widthCountSpace2 * this.w);  // Espaciado en el eje x
                int yPosition = 50 + (heightCountSpace2 * this.h);  // Espaciado en el eje y
                
                // Creamos la pieza y la agregamos a la lista
                Tile tile = new Tile(this.h, this.w, column, xPosition, yPosition);
                tiles.add(tile);
                
                // Aumentamos el espaciado horizontal
                widthCountSpace2++;                
            }
            // Aumentamos el espaciado vertical después de cada fila
            heightCountSpace2++;
            
        }

    }

    
        public static void main(String[] args) {
        // Crear una matriz de caracteres de ejemplo
            char[][] starting = {
                {'r', 'r'},
                {'*', '*'},
                {'r', 'y'}
            };
            
            char[][] ending = {
                {'*', 'r'},
                {'r', 'r'},
                {'*', 'y'}
            };
            Puzzle pz1 = new Puzzle(3,2);
            Puzzle pz2 = new Puzzle(starting, ending, 5,5);
        
        

        // Convertir la matriz de caracteres en una lista de Tiles
        
        // Imprimir los Tiles para verifica
    }
    
    
}
