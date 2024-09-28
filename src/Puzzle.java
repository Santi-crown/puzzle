import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Puzzle {
    //private int Tile.SIZE;
    private int h;
    private int w;
    //private int Tile.MARGIN; // Margen entre cada baldosas - Tile
    //private int padding; // Padding interno - Tile
    private Rectangle startingBoard;
    private Rectangle endingBoard;
    private Color color;
    private char[][] starting;
    private char[][] ending;
    private List<List<Tile>> tiles; // Lista de listas
    private List<List<Tile>> referingTiles; // Tiles de referencia
    private boolean visible = true; //Determina si el simulador está visible
    private boolean ok = true; //Rastrea si la última acción fue exitosa
    private Circle circle;
    
    // Board color
    public static Color lightBrown = new Color(207, 126, 60);
    
    //Holes
    private boolean[][] holes; // Matriz para rastrear agujeros
    private List<Circle> holeCircles; // Lista para almacenar círculos de agujeros
    
    
    // Constructor para inicializar los tableros sin las matrices
    public Puzzle(int h, int w) {
        //this.Tile.SIZE = 50;  // Tamaño de cada tile
        //this.Tile.MARGIN = 10;    // Margen entre tiles
        //this.padding = 5;    // Padding interno
        this.h = h;
        this.w = w;
        this.color = lightBrown;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        
        
        // Inicializar la matriz de agujeros y la lista de círculos        
        holeCircles = new ArrayList<>();
        
        /**
        startingBoard = new Rectangle();
        startingBoard.changeTile.SIZE(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN));
        startingBoard.changeColor(color);
        startingBoard.makeVisible();
        startingBoard.moveHorizontal(100);
        startingBoard.moveVertical(50);

        endingBoard = new Rectangle();
        endingBoard.changeTile.SIZE(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN));
        endingBoard.changeColor(color);
        endingBoard.makeVisible();
        endingBoard.moveHorizontal(h * (Tile.SIZE + Tile.MARGIN) + 350);
        endingBoard.moveVertical(50);
        **/ 
        if (h > 0 && w > 0){
            holes = new boolean[h][w];
            //startingBoard = new Rectangle(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN), color,100,50);
            //endingBoard = new Rectangle(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN),color, h * (Tile.SIZE + Tile.MARGIN) + 350, 50);
            
            startingBoard = new Rectangle(h,w,100,50,"starting");
            endingBoard = new Rectangle(h,w,350,50,"ending"); 
            
        }
        else{
            showMessage("You cannot create the two boards with negative or zero h,w", "Error");
            this.ok = false; //Acción no exitosa
        }
        
        // Crear baldosas vacías en el tablero inicial y baldosas en el tablero final
        createEmptyTiles();
        createEmptyreferingTiles();
    }

    // Constructor para inicializar los tableros con matrices
    public Puzzle(char[][] starting, char[][] ending) {
        
        //this.Tile.SIZE = 50;  // Tamaño de cada tile
        //this.Tile.MARGIN = 10;    // Margen entre tiles
        //this.padding = 10;    // Padding interno
        this.h = starting.length;
        this.w = starting[0].length;
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        this.color = lightBrown;
        
        // Inicializar la matriz de agujeros y la lista de círculos
        holes = new boolean[h][w];
        holeCircles = new ArrayList<>();
        
        // Crear los tableros    
        startingBoard = new Rectangle(h,w,100,50,"starting");
        endingBoard = new Rectangle(h,w,350,50,"ending"); 
        
        // Crear baldosas
        
        createTiles(starting, tiles, 105, 55); // Posición inicial de las baldosas iniciales
        createTiles(ending, referingTiles, w * (Tile.SIZE+ Tile.MARGIN) + 355, 55); // Posición inicial del tablero de referencia
        
    }
    
    // Constructor para inicializar vacio starting y con baldosas ending
    public Puzzle(char [][] ending){    
    
        //this.Tile.SIZE = 50;  // Tamaño de cada tile
        //this.Tile.MARGIN = 10;    // Margen entre tiles
        //this.padding = 10;    // Padding interno
        this.h = ending.length;
        this.w = ending[0].length;
        //this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        this.color = lightBrown;
       
        // Crear los tableros    
        startingBoard = new Rectangle(h,w,100,50,"starting");
        endingBoard = new Rectangle(h,w,350,50,"ending");
        
        // Crear baldosas vacías en el tablero inicial y baldosas en el tablero final
        createEmptyTiles();
        createTiles(ending, referingTiles, h * (Tile.SIZE + Tile.MARGIN) + 355, 55);
    }
    
    
    // Método para crear baldosas en una lista de listas, dado el tablero de referencia
    private void createTiles(char[][] board, List<List<Tile>> tileList, int xOffset, int yOffset) {
        for (int row = 0; row < board.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < board[row].length; col++) {
                char label = board[row][col];
                int xPosition = xOffset + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = yOffset + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition,row, col);
                rowList.add(tile);
            }
            tileList.add(rowList);
        }
    }

    // Método para crear baldosas vacías en el tablero inicial
    private void createEmptyTiles() {
        for (int row = 0; row < h; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < w; col++) {
                char label = '*';  // Baldosa vacía
                int xPosition = 105 + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition,row, col);
                rowList.add(tile);
            }
            tiles.add(rowList);
        }
    }
    
    private void createEmptyreferingTiles() {
        for (int row = 0; row < h; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < w; col++) {
                char label = '*';  // Baldosa vacía
                int xPosition = (w * (Tile.SIZE + Tile.MARGIN)) + 355 + (col * (Tile.SIZE + Tile.MARGIN));
                int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
                Tile tile = new Tile(label, xPosition, yPosition,row, col);
                rowList.add(tile);
            }
            referingTiles.add(rowList);
        }
    }    

    
    public void addTile(int row, int column, char label) {                
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error"); 
            this.ok = false; // Error message
        } else if (row < 0 || column < 0){
            showMessage("You're searching for a non-exist tile, with negative position.", "Error"); 
            this.ok = false; // Error message
        } else {
            Tile previousTile = tiles.get(row).get(column);
    
            // Verificar si la baldosa tiene un agujero
            if (previousTile.getIsHole()) {
                showMessage("You cannot add a tile in a tile with a hole.", "Error");
                this.ok = false; // Error message
            } 
            // Verificar si la baldosa es una celda vacía (lightBrown)
            else if (previousTile.getTileColor().equals(lightBrown)) {
                previousTile.setTileColor(label); // Cambia el color de la baldosa
                this.ok = true; // Acción exitosa
            } else {
                showMessage("There is already a tile here.", "Error");
                this.ok = false; // Error message
            }                                               
        }
    }



    public void deleteTile(int row, int column){
        if (row >= h || column >= w){
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; //Error messag
            
        } else if (row < 0 || column <0 ){
            showMessage("You're searching for a non-exist tile, with negative position.", "Error"); 
            this.ok = false; // Error message
        }
        else{
            Tile previousTile = tiles.get(row).get(column);

            // Usa equals para comparar colores
            if(!previousTile.getTileColor().equals(lightBrown)) {
                previousTile.setTileColor('n');  
                this.ok = true; //Acciòn exitosa
            } else {
                showMessage("There is a tile here now.", "Error");
                this.ok = false; //Error message
            }
        }
        
    }
    
    // Método para relocalizar una baldosa
    public void relocateTile(int[] from, int[] to) {
        // Validar las coordenadas de entrada
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            showMessage("Coordenadas inválidas.", "Error");
            this.ok = false;
            return;
        }

        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);

        // Validar existencia de la baldosa de origen y disponibilidad de la baldosa de destino
        if (isTileEmpty(fromTile)) {
            showMessage("No puedes mover una baldosa inexistente.", "Error");
            this.ok = false;
        } else if (!isTileEmpty(toTile)) {
            showMessage("Ya hay una baldosa en la posición de destino.", "Error");
            this.ok = false;
        } else if (fromTile.hasGlue() || fromTile.isStuck()) {
            showMessage("No puedes mover una baldosa que tiene pegamento o está pegada.", "Error");
            this.ok = false;
        } else {
            // Realizar el movimiento
            this.relocateTileMovement(fromTile, toTile, from, to);
            this.ok = true;
        }
    }

    // Método auxiliar para realizar el movimiento visual y actualizar la lista de baldosas
    public void relocateTileMovement(Tile fromTile, Tile toTile, int[] from, int[] to) {
        // Mover la instancia de la baldosa visualmente
        fromTile.moveHorizontal((to[1] - from[1]) * (Tile.SIZE + Tile.MARGIN));
        fromTile.moveVertical((to[0] - from[0]) * (Tile.SIZE + Tile.MARGIN));
        // Actualizar la lista de baldosas: mover la baldosa a la nueva posición
        tiles.get(to[0]).set(to[1], fromTile);
        // Crear una nueva baldosa vacía en la posición original
        Tile emptyTile = createEmptyTile(from[0], from[1]);
        tiles.get(from[0]).set(from[1], emptyTile);
    }

    // Método para validar si las coordenadas son correctas
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] >= 0 && coords[0] < h && coords[1] >= 0 && coords[1] < w;
    }    

    // Método para aplicar pegamento a una baldosa
    public void addGlue(int row, int col) {
        Tile tile = getTileAtPosition(row, col);
        if (tile == null || isTileEmpty(tile) || holes[row][col]) {
            showMessage("No se puede aplicar pegamento a una baldosa vacía.", "Error");
            this.ok = false;
            return;
        }
        if (tile.hasGlue()) {
            showMessage("Esta baldosa ya tiene pegamento aplicado.", "Error");
            this.ok = false;
            return;
        }

        tile.setHasGlue(true);

        // Cambiar el color de la baldosa a una versión más pálida
        Color evenPalerColor = getPaleColor(tile.getOriginalColor(), 150);
        tile.setTileColor(evenPalerColor);

        // Actualizar las baldosas adyacentes
        updateAdjacentTiles(tile);

        // Recolectar el grupo de baldosas pegadas
        List<Tile> group = new ArrayList<>();
        collectStuckGroup(tile, group);

        // Resetear las banderas de visitado
        resetVisitedFlags();

        this.ok = true;
    }

    // Método para eliminar el pegamento de una baldosa
    public void deleteGlue(int row, int col) {
        Tile tile = getTileAtPosition(row, col);
        if (tile == null || !tile.hasGlue()) {
            showMessage("No hay pegamento para eliminar en esta baldosa.", "Error");
            this.ok = false;
            return;
        }

        tile.setHasGlue(false);

        // Si la baldosa ya no está pegada a ninguna otra, ajustar el color
        if (!tile.isStuck()) {
            // Cambiar el color a una versión ligeramente más clara
            Color slightlyPalerColor = getPaleColor(tile.getOriginalColor(), 50);
            tile.setTileColor(slightlyPalerColor);
        }

        // Actualizar las baldosas adyacentes
        updateAdjacentTilesAfterGlueRemoval(tile);

        // Recolectar el grupo de baldosas pegadas para actualizar estados
        List<Tile> group = new ArrayList<>();
        collectStuckGroup(tile, group);

        // Resetear las banderas de visitado
        resetVisitedFlags();

        this.ok = true;
    }

    // Método para actualizar las baldosas adyacentes después de aplicar pegamento
    private void updateAdjacentTiles(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.isStuck()) {
                adjacentTile.setIsStuck(true);
                // Cambiar el color a una versión pálida
                Color paleColor = getPaleColor(adjacentTile.getOriginalColor(), 100);
                adjacentTile.setTileColor(paleColor);
            }
        }
    }

    // Método para actualizar las baldosas adyacentes después de eliminar pegamento
    private void updateAdjacentTilesAfterGlueRemoval(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile)) {
                if (isAdjacentToGlue(adjacentTile)) {
                    // Si aún está adyacente a otra baldosa con pegamento, se mantiene pegada
                    continue;
                } else {
                    adjacentTile.setIsStuck(false);
                    if (!adjacentTile.hasGlue()) {
                        // Cambiar el color a una versión ligeramente más clara
                        Color slightlyPalerColor = getPaleColor(adjacentTile.getOriginalColor(), 50);
                        adjacentTile.setTileColor(slightlyPalerColor);
                    }
                }
            }
        }
    }

    // Método auxiliar para verificar si una baldosa está adyacente a alguna baldosa con pegamento
    private boolean isAdjacentToGlue(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && adjacentTile.hasGlue()) {
                return true;
            }
        }
        return false;
    }

    // Método para obtener una versión más pálida de un color
    private Color getPaleColor(Color color, int palenessFactor) {
        int r = Math.min(255, color.getRed() + palenessFactor);
        int g = Math.min(255, color.getGreen() + palenessFactor);
        int b = Math.min(255, color.getBlue() + palenessFactor);
        return new Color(r, g, b);
    }

    // Método para inclinar el puzzle en una dirección
    public void tilt(char direction) {
        switch (direction) {
            case 'd':
                for (int col = 0; col < w; col++) {
                    tiltDownWithGlue(col);
                }
                break;
            case 'u':
                for (int col = 0; col < w; col++) {
                    tiltUpWithGlue(col);
                }
                break;
            case 'r':
                for (int row = 0; row < h; row++) {
                    tiltRightWithGlue(row);
                }
                break;
            case 'l':
                for (int row = 0; row < h; row++) {
                    tiltLeftWithGlue(row);
                }
                break;
            default:
                showMessage("Dirección inválida.", "Error");
                this.ok = false;
        }
        resetVisitedFlags(); // Resetear las banderas de visitado después de la inclinación
    }

    // Métodos de inclinación considerando pegamento y baldosas pegadas

    // Inclinación hacia arriba
    private void tiltUpWithGlue(int col) {
        for (int row = 0; row < h; row++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveUp(row, col, null);
                    moveTileUp(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveUpGroup(group);
                    moveGroupUp(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia abajo
    private void tiltDownWithGlue(int col) {
        for (int row = h - 1; row >= 0; row--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveDown(row, col, null);
                    moveTileDown(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveDownGroup(group);
                    moveGroupDown(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia la derecha
    private void tiltRightWithGlue(int row) {
        for (int col = w - 1; col >= 0; col--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveRight(row, col, null);
                    moveTileRight(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveRightGroup(group);
                    moveGroupRight(group, maxMove);
                }
            }
        }
    }

    // Inclinación hacia la izquierda
    private void tiltLeftWithGlue(int row) {
        for (int col = 0; col < w; col++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile)) {
                if (!tile.isStuck() && !tile.hasGlue()) {
                    int maxMove = calculateMaxMoveLeft(row, col, null);
                    moveTileLeft(tile, maxMove);
                } else if (!tile.isVisited()) {
                    List<Tile> group = new ArrayList<>();
                    collectStuckGroup(tile, group);
                    int maxMove = calculateMaxMoveLeftGroup(group);
                    moveGroupLeft(group, maxMove);
                }
            }
        }
    }

    // Métodos auxiliares para calcular el movimiento máximo y mover baldosas/grupos

    // Movimiento hacia arriba
    private int calculateMaxMoveUp(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = row - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(i, col);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveUpGroup(List<Tile> group) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileUp(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() - steps;
        tile.moveVertical(-steps * (Tile.SIZE + Tile.MARGIN));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(newRow).set(tile.getCol(), tile);
        tile.setRow(newRow);
    }

    private void moveGroupUp(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas superiores se muevan primero
        group.sort((t1, t2) -> Integer.compare(t1.getRow(), t2.getRow()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newRow = tile.getRow() - steps;
            tile.moveVertical(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Métodos similares para movimientos hacia abajo, izquierda y derecha...

    // Movimiento hacia abajo
    private int calculateMaxMoveDown(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = row + 1; i < h; i++) {
            Tile nextTile = getTileAtPosition(i, col);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveDownGroup(List<Tile> group) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileDown(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() + steps;
        tile.moveVertical(steps * (Tile.SIZE + Tile.MARGIN));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(newRow).set(tile.getCol(), tile);
        tile.setRow(newRow);
    }

    private void moveGroupDown(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas inferiores se muevan primero
        group.sort((t1, t2) -> Integer.compare(t2.getRow(), t1.getRow()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newRow = tile.getRow() + steps;
            tile.moveVertical(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Movimiento hacia la derecha
    private int calculateMaxMoveRight(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = col + 1; i < w; i++) {
            Tile nextTile = getTileAtPosition(row, i);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveRightGroup(List<Tile> group) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileRight(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() + steps;
        tile.moveHorizontal(steps * (Tile.SIZE + Tile.MARGIN));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(tile.getRow()).set(newCol, tile);
        tile.setCol(newCol);
    }

    private void moveGroupRight(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas con columnas más altas se muevan primero
        group.sort((t1, t2) -> Integer.compare(t2.getCol(), t1.getCol()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newCol = tile.getCol() + steps;
            tile.moveHorizontal(steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    // Movimiento hacia la izquierda
    private int calculateMaxMoveLeft(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = col - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(row, i);
            if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveLeftGroup(List<Tile> group) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileLeft(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() - steps;
        tile.moveHorizontal(-steps * (Tile.SIZE + Tile.MARGIN));
        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        tiles.get(tile.getRow()).set(newCol, tile);
        tile.setCol(newCol);
    }

    private void moveGroupLeft(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas con columnas más bajas se muevan primero
        group.sort((t1, t2) -> Integer.compare(t1.getCol(), t2.getCol()));
        // Eliminar baldosas de sus posiciones antiguas
        for (Tile tile : group) {
            tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
        }
        // Mover baldosas a sus nuevas posiciones
        for (Tile tile : group) {
            int newCol = tile.getCol() - steps;
            tile.moveHorizontal(-steps * (Tile.SIZE + Tile.MARGIN));
            tiles.get(tile.getRow()).set(newCol, tile);
            tile.setCol(newCol);
        }
    }

    // Método para recolectar todas las baldosas en un grupo pegado
    private void collectStuckGroup(Tile tile, List<Tile> group) {
        if (tile.isVisited()) return;
        tile.setVisited(true);
        group.add(tile);
        int row = tile.getRow();
        int col = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && !adjacentTile.isVisited() && !isTileEmpty(adjacentTile) &&
                (adjacentTile.isStuck() || adjacentTile.hasGlue())) {
                collectStuckGroup(adjacentTile, group);
            }
        }
    }

    // Método para crear una baldosa vacía
    private Tile createEmptyTile(int row, int col) {
        int xPosition = 105 + (col * (Tile.SIZE + Tile.MARGIN));
        int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
        Tile emptyTile = new Tile('*', xPosition, yPosition,row, col);
        return emptyTile;
    }

    // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row, int col) {
        if (row >= 0 && row < h && col >= 0 && col < w) {
            return tiles.get(row).get(col);
        }
        return null;
    }

    // Método para verificar si una baldosa está vacía (basado en el color lightBrown)
    private boolean isTileEmpty(Tile tile) {
        return tile.getTileColor().equals(lightBrown);
    }

    // Resetear las banderas de visitado después de la inclinación
    private void resetVisitedFlags() {
        for (List<Tile> rowList : tiles) {
            for (Tile tile : rowList) {
                tile.setVisited(false);
            }
        }
    }
    
    // Muestra un mensaje de error si el simulador es visible y cambia el estado de ok a false
    public void showMessage(String message, String title){
        if(this.visible){
            JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
            this.ok = false;
        }
    }
    
    public boolean isGoal() {
        // Recorrer el tablero actual (tiles) y compararlo con el tablero de referencia (ending)
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile currentTile = tiles.get(row).get(col);
                char currentLabel = currentTile.getLabel();
    
                Tile targetTile = referingTiles.get(row).get(col);
                char targetLabel = targetTile.getLabel();
    
                // Comparar la baldosa actual con la baldosa en el estado objetivo
                if (currentLabel != targetLabel) {
                    return false;  // Si no coinciden, el estado final aún no se ha alcanzado
                }
            }
        }
        
        // Si todas las baldosas coinciden con las de la referencia, entonces hemos alcanzado el estado final
        return true;
    }



    
    // Hace visible el simulador
    //This method consists in two parts cuz there are two different constructors to disappear rectangle or tiles(visible)
    
    public void makeVisible(){
        this.visible = true;
        
        // Verificar si los tableros han sido inicializados
        if (startingBoard != null) {
            startingBoard.makeVisible();  // Hace visible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeVisible();    // Hace visible el tablero final
        }
        
        
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
        
    }
    /**
    public void makeVisibleTiles() {
        this.visible = true;
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeVisible();
            }
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
            
    }
    
    public void makeVisibleRectangle(){
        // Verificar si los tableros han sido inicializados
        if (startingBoard != null) {
            startingBoard.makeVisible();  // Hace visible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeVisible();    // Hace visible el tablero final
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
    }
    
    **/
    
    // Hace invisible el simulador
    //This method consists in two parts cuz there are two different constructors to disappear rectangle or tiles(invisible)
    
    public void makeInvisible(){
        this.visible = false;
        
        // Hacer invisibles las baldosas
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        // Hacer invisibles los tableros (rectángulos)
        if (startingBoard != null) {
            startingBoard.makeInvisible();  // Hace invisible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeInvisible();    // Hace invisible el tablero final
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
    }

    
    /**
    public void makeInvisibleTiles() {
        this.visible = false;
        
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        for (List<Tile> row : referingTiles) {
            for (Tile tile : row) {
                tile.makeInvisible();
            }
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
    }
    
    public void makeInvisibleRectangle(){
        // Verificar si los tableros han sido inicializados
        
        if (startingBoard != null) {
            startingBoard.makeInvisible();  // Hace invisible el tablero inicial
        }
        
        if (endingBoard != null) {
            endingBoard.makeInvisible();    // Hace invisible el tablero final
        }
        
        this.ok = true;  // Indicar que la acción fue exitosa
    }
    
    **/
    
    // Termina el simulador
    public void finish() {
        System.out.println("El simulador ha finalizado.");
        System.exit(0);
    }

    /**
     * Devuelve una copia de la matriz actual de edición (starting),
     * representando el estado actual del puzzle y pinta las baldosas.
     * @return Una copia de la matriz starting.
     */
    
    public char[][] actualArrangement() {
        // Crear una copia de la matriz starting
        char[][] currentArrangement = new char[h][w];
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = getTileAtPosition(row, col);
                char label = tile.getLabel();
                System.out.print(label + " ");
            }
            System.out.println();
        }
        
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                currentArrangement[row][col] = starting[row][col]; // Copia el valor actual
    
                // Simulación de pintar o mostrar la baldosa
                Tile tile = tiles.get(row).get(col);
                System.out.println("Baldosa en (" + row + ", " + col + "): " + tile.getLabel());
            }
        }
        
        return currentArrangement; // Retornar la copia de la matriz
        
        
    }
    
    
    // Retorna si la última acción fue exitosa
    public boolean ok() {
        return this.ok;
    }
    
    public void exchange() {
        // Intercambia las referencias entre el tablero de edición (starting) y el tablero de referencia (ending)
        char[][] temp = starting;  // Guarda la matriz de edición temporalmente
        starting = ending;         // Asigna la matriz de referencia como la nueva matriz de edición
        ending = temp;             // Asigna la matriz temporal (original de edición) como la nueva matriz de referencia
    
        // Intercambia las listas de baldosas entre tiles y referingTiles sin alterar el estado de las baldosas
        List<List<Tile>> tempTiles = tiles;
        tiles = referingTiles;
        referingTiles = tempTiles;
    
        // No se alteran los colores ni el estado de pegamento de las baldosas. Simplemente intercambiamos los tableros activos.
        System.out.println("Boards have been exchanged. Now, you're editing the board that was the reference board before.");
    }
    
    public void makeHole(int row, int column) {
        // Validar las coordenadas
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error Message
            
        } else if (row < 0 || column < 0){
            showMessage("You cannot make a hole in a non-exist tile with negative position.", "Error");
            this.ok = false; // Error Message
            
        } else{
            
            Tile targetTile = tiles.get(row).get(column);
    
            // Verificar si la celda está vacía y no tiene ya un agujero
            if (isTileEmpty(targetTile) && !targetTile.getIsHole()) {
                int xPos = targetTile.getXPos();
                int yPos = targetTile.getYPos();
                int diameter = Tile.SIZE;
        
                // Calcular la posición centrada del círculo
                int circleX = xPos + (Tile.SIZE - diameter) / 2;
                int circleY = yPos + (Tile.SIZE - diameter) / 2;
        
                // Crear y hacer visible el círculo (agujero)
                Circle hole = new Circle(diameter, circleX, circleY, Color.WHITE);
                hole.makeVisible();
        
                // Marcar la baldosa como agujereada
                targetTile.setLabel('h');
                targetTile.setIsHole(true);
                this.ok = true; // Acción exitosa
                
            } else if (targetTile.getIsHole()) {
                showMessage("This tile already has a hole.", "Error");
                this.ok = false; // Error message
            } else {
                showMessage("You can only make a hole in an empty tile.", "Error");
                this.ok = false; // Error message
            }
        }
          
    }

    /**
    public int [][] fixedTiles(){
        List<int[]> fixedTilesPositions = new ArrayList<>();


        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = getTileAtPosition(row, col);

                if (!isTileEmpty(tile) && (tile.hasGlue() || tile.isStuck())) {
                    // Agregar la posición a la lista
                    fixedTilesPositions.add(new int[]{row, col});

                    // Hacer que la baldosa parpadee si el simulador está visible
                    if (visible) {
                        tile.blink();
                    }
                }
            }
        }

        // Convertir la lista a un arreglo bidimensional
        int[][] result = new int[fixedTilesPositions.Tile.SIZE()][2];
        for (int i = 0; i < fixedTilesPositions.Tile.SIZE(); i++) {
            result[i] = fixedTilesPositions.get(i);
        }

        return result;
    }
    **/
    
    // I used the same logic that method isGoal about comparing and to get the position on the tile with the label.
    public int misplacedTiles(){
        
        int cont = 0;
        
        for (int row = 0; row < h;row++){
            for (int col = 0; col < w;col++){
                Tile currentTile = tiles.get(row).get(col);
                char currentLabel = currentTile.getLabel();
    
                Tile referingTile = referingTiles.get(row).get(col);
                char referenceLabel = referingTile.getLabel();
                
                if (currentLabel != referenceLabel && currentLabel != '*'){
                    cont++;
                }
            }
        }
        
        return cont;
    }

    
    public static void main(String[] args) {
        
        /**
         * FIRST TEST
         */
        
        
        // Crear matrices de caracteres de ejemplo con 8 filas y 4 columnas
        char[][] starting = {
            {'b', 'b','y','y'},
            {'y', '*','r','y'},
            {'g','*','g','y'} 
        };

        char[][] ending = {
            {'b', 'b','y'},
            {'y', '*','r'},
            {'g','*','g'}
        };
        Puzzle pz1 = new Puzzle(3, 4); // Tablero sin matrices
        Puzzle pz2 = new Puzzle(starting, ending); // Tablero con matrices
        /**

        // Instanciar los objetos de Puzzle
        Puzzle pz1 = new Puzzle(3, 4); // Tablero sin matrices
        Puzzle pz2 = new Puzzle(starting, ending); // Tablero con matrices
        
        //pz2.addTile(77,81,'b');
        //pz2.addTile(0,1,'r');
        //pz2.addTile(2,3,'b');
        //pz2.addTile(1,1,'g');
        
        //pz2.addTile(0,0,Color.BLACK);
        
        //pz2.addGlue(0,0);
        //pz2.addGlue(1,2);
        
        //pz2.removeGlue(1,2);
        
        //pz2.deleteTile(8,0);
        //pz2.deleteTile(1,1);
        
        int[] from = {0,1};
        int[] to   = {1,2};  
        //pz2.relocateTile(from, to);
        
        int[] from1 = {2,3};
        int[] to1   = {0,1};  
        //pz2.relocateTile(from1, to1);
        
        int[] from2 = {4124,414};
        int[] to2   = {0,131};  
        //pz2.relocateTile(from2, to2);
        
        int[] from3 = {2,0};
        int[] to3   = {0,2};  
        //pz2.relocateTile(from3, to3);
        
        //pz2.tilt('r');
        //pz2.tilt('l');
        //pz2.tilt('u');
        //pz2.tilt('d');
        
        **/
        
        
     /**   
         //SECOND TEST
        char[][] starting1 = {
        {'y', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'g', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'b', 'g', 'y', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'y', 'b', 'g', 'r', 'y', 'b', 'g', '*', 'y', 'b'},
        {'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
    };
        
        char[][] ending1 = {
        {'y', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'g', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'b', 'g', 'y', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'y', 'b', 'g', 'r', 'y', 'b', 'g', '*', 'y', 'b'},
        {'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
    };
        **/
        /**
        Puzzle pz3 = new Puzzle(7, 10); // Tablero sin matrices
        Puzzle pz4 = new Puzzle(starting1, ending1); // Tablero con matrices
        
        // pz4.addTile(9,0,'r');
        // pz4.addGlue(9,1);
        // pz4.tilt('u');
        // pz4.tilt('r');
        
        //pz4.addTile(5,1,'b');
        //pz4.deleteTile(5,1);
        //pz4.deleteTile(9,8);
        
        int[] from1 = {9,9};
        int[] to1   = {3,1};
        //pz4.relocateTile(from1,to1);
        
        int[] from3 = {3,1};
        int[] to3   = {9,9};
        //pz4.relocateTile(from3,to3);
        
        int[] from2 = {1,9};
        int[] to2   = {3,2};
        //pz4.relocateTile(from2,to2);
    
        //pz4.tilt('l');
        //pz4.tilt('g');
        // pz4.addTile(6,0,'r');
        
        int[] from4 = {6,0};
        int[] to4   = {3,1};
        // pz4.relocateTile(from4,to4);
        // caso problema 1.1
        // pz4.addGlue(4,3);
        //pz4.addGlue(9,1);    
        //pz4.addGlue(9,5);
        //pz4.addGlue(0,8);
        //pz4.addGlue(0,4);
        //pz4.tilt('u');
        //pz4.tilt('u');
        
        // pz4.tilt('d');
        // pz4.tilt('d');
        // pz4.tilt('d');
        
        // pz4.tilt('u');
        // pz4.tilt('u');
        // pz4.tilt('u');
        pz4.addGlue(9,1);
        pz4.addGlue(4,3);
        pz4.addGlue(9,6);
        pz4.tilt('d');
        pz4.tilt('u');
        pz4.tilt('u');
        // no deberían quedar todas en la fila 1, deberían moverse más las que pueden moverse. 
        
        **/
        
        //pz4.printBoard();
    }
}
