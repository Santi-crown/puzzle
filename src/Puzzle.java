import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Comparator;

public class Puzzle {    
    private int h;
    private int w;
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
        this.h = h;
        this.w = w;
        this.color = lightBrown;
        this.tiles = new ArrayList<>();
        this.referingTiles = new ArrayList<>();
        
        // Inicializar la matriz de agujeros y la lista de círculos        
        holeCircles = new ArrayList<>();
        
        if (h > 0 && w > 0){
            holes = new boolean[h][w];
            //startingBoard = new Rectangle(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN), color,100,50);
            //endingBoard = new Rectangle(h * (Tile.SIZE + Tile.MARGIN), w * (Tile.SIZE + Tile.MARGIN),color, h * (Tile.SIZE + Tile.MARGIN) + 350, 50);
            
            startingBoard = new Rectangle(h,w,100,50,"starting");
            endingBoard = new Rectangle(h,w,350,50,"ending"); 
            
            // Crear baldosas vacías en el tablero inicial y baldosas en el tablero final
            createEmptyTiles();
            createEmptyreferingTiles();
            
        }
        else {
            showMessage("You cannot create the two boards with negative or zero h,w", "Error");
            this.ok = false; //Acción no exitosa
        }
        
    }

    // Constructor para inicializar los tableros con matrices
    public Puzzle(char[][] starting, char[][] ending) {    
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
        createTiles(ending, referingTiles, w * (Tile.SIZE + Tile.MARGIN) + 355, 55);
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

    
    public void addTile(int row,int column, char label) {

        // Allowed list for tile
        char[] validLabels = {'r', 'g', 'b', 'y'};

        // Initialize if the label is valid
        boolean isValidLabel = false;

        // Moving into the char []
        for (char validLabel : validLabels) {
            if (label == validLabel) {
                isValidLabel = true; // Change the condition
                break;
            }
        }

        // If label is invalid, show the error
        if (!isValidLabel) {
            showMessage("Invalid label. Accepted labels are: r, g, b, y.", "Error");
            this.ok = false; // Error message
            return; // Leaves the method
        }

        // Other validations
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error message
        } else if (row < 0 || column < 0) {
            showMessage("You're searching for a non-existent tile with negative position.", "Error");
            this.ok = false; // Error message
        } else {
            Tile previousTile = tiles.get(row).get(column);

            // Verificar si la baldosa tiene un agujero
            if (previousTile.getLabel() == 'h') {
                showMessage("You cannot add a tile on a hole.", "Error");
                this.ok = false; // Error message
            }
            // Verificar si la baldosa es una celda vacía
            else if (isTileEmpty(previousTile)) {
                previousTile.setTileColor(label); // Cambia el color de la baldosa
                previousTile.setLabel(label);
                previousTile.makeVisible();
                this.ok = true; // Acción exitosa

            } else {
                showMessage("There is already a tile here.", "Error");
                this.ok = false; // Error message
            }
        }
    }

    public void deleteTile(int row,int column) {
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error message

        } else if (row < 0 || column < 0) {
            showMessage("You're searching for a non-existent tile with negative position.", "Error");
            this.ok = false; // Error message
        } else {
            Tile previousTile = tiles.get(row).get(column);

            // Verificar si la baldosa tiene un agujero
            if (previousTile.getLabel() == 'h') {
                showMessage("You cannot delete a tile that is a hole.", "Error");
                this.ok = false; // Error message
            }

            else if (!isTileEmpty(previousTile)) {
                previousTile.setTileColor('n');
                previousTile.setLabel('*');
                previousTile.makeInvisible();
                this.ok = true; // Acción exitosa
            } else {
                showMessage("You're trying to delete a non-existent tile.", "Error");
                this.ok = false; // Error message
            }
        }

    }

    // Método para relocalizar una baldosa
    public void relocateTile(int[] from, int[] to) {
        // Validar las coordenadas de entrada
        if (!areValidCoordinates(from) || !areValidCoordinates(to)) {
            showMessage("Invalid coordinates.", "Error");
            this.ok = false;
            return;
        }

        Tile fromTile = tiles.get(from[0]).get(from[1]);
        Tile toTile = tiles.get(to[0]).get(to[1]);

        // Validar existencia de la baldosa de origen y disponibilidad de la baldosa de destino
        if (fromTile.getLabel() == 'h') {
            showMessage("You cannot move a hole tile.", "Error");
            this.ok = false;

        } else if (toTile.getLabel() == 'h') {
            showMessage("You cannot relocate a tile to a position that has a hole.", "Error");
            this.ok = false;

        } else if (isTileEmpty(fromTile)) {
            showMessage("You cannot move a non-existent tile.", "Error");
            this.ok = false;

        } else if (!isTileEmpty(toTile)) {
            showMessage("There is already a tile in the destination position.", "Error");
            this.ok = false;

        } else if (fromTile.hasGlue() || fromTile.isStuck()) {
            showMessage("You cannot move a tile that has glue or is stuck.", "Error");
            this.ok = false;

        } else {
            // Realizar el movimiento
            this.relocateTileMovement(fromTile, toTile, from, to);
            this.ok = true;
        }
    }

    // Método auxiliar para realizar el movimiento visual y actualizar la lista de baldosas
    private void relocateTileMovement(Tile fromTile, Tile toTile, int[] from, int[] to) {
        // Mover la instancia de la baldosa visualmente
        fromTile.moveHorizontal((to[1] - from[1]) * (Tile.SIZE + Tile.MARGIN));
        fromTile.moveVertical((to[0] - from[0]) * (Tile.SIZE + Tile.MARGIN));
        // Actualizar la lista de baldosas: mover la baldosa a la nueva posición
        tiles.get(to[0]).set(to[1], fromTile);
        // Actualizar la posición interna de la baldosa
        fromTile.setRow(to[0]);
        fromTile.setCol(to[1]);
        // Crear una nueva baldosa vacía en la posición original
        Tile emptyTile = createEmptyTile(from[0], from[1]);
        tiles.get(from[0]).set(from[1], emptyTile);
    }
    
    // Método para validar si las coordenadas son correctas
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] >= 0 && coords[0] < h && coords[1] >= 0 && coords[1] < w;
    }    

    // Método para aplicar pegamento a una baldosa
    public void addGlue(int row,int column) {
        Tile tile = getTileAtPosition(row, column);
        if (tile == null) {
            showMessage("Invalid position.", "Error");
            this.ok = false;
        } else if (tile.getLabel() == 'h') {
            showMessage("You cannot add glue on a hole tile.", "Error");
            this.ok = false;

        } else if (isTileEmpty(tile)) {
            showMessage("Cannot apply glue to an empty tile.", "Error");
            this.ok = false;
        } else if (tile.hasGlue()) {
            showMessage("This tile already has glue applied.", "Error");
            this.ok = false;

        } else {
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
            tile.setLabel('p');
            this.ok = true;
        }

    }

    // Método para eliminar el pegamento de una baldosa
    public void deleteGlue(int row,int column) {
        Tile tile = getTileAtPosition(row, column);
        if (tile == null) {
            showMessage("Invalid position.", "Error");
            this.ok = false;
        } else if (tile.getLabel() == 'h') {
            showMessage("You cannot delete glue on a hole tile.", "Error");
            this.ok = false;
        
        } else if (isTileEmpty(tile)) {
            showMessage("Cannot delete glue to an empty tile.", "Error");
            this.ok = false;
            
        }else if (!tile.hasGlue()) {
            showMessage("There is no glue to remove on this tile.", "Error");
            this.ok = false;

        } else {
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

    }

    // Método para actualizar las baldosas adyacentes después de aplicar pegamento
    private void updateAdjacentTiles(Tile tile) {
        int row = tile.getRow();
       int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.isStuck() && !adjacentTile.getIsHole()) {
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
       int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);

            if (adjacentTile != null && !isTileEmpty(adjacentTile) && !adjacentTile.getIsHole()) {
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
       int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
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
    
    // Método de inclinación que llama a tiltImplementation una sola vez
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
                showMessage("Invalid direction.", "Error");
                this.ok = false;
        }
        resetVisitedFlags(); // Resetear las banderas de visitado después de la inclinación
    }

    // Métodos de inclinación ajustados para manejar agujeros

    // Inclinación hacia arriba
    private void tiltUpWithGlue(int col) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int row = 0; row < h; row++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Resetear las banderas de visitado
        resetVisitedFlags();

        // Ordenar los grupos por la fila mínima (los superiores primero)
        groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(Tile::getRow).min().orElse(h)));

        // Mover los grupos
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveUpGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Eliminar baldosas libres que caen en el agujero
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Las baldosas pegadas o con pegamento no se mueven
            } else {
                moveGroupUp(group, maxMove);
            }
        }
    }

    // Inclinación hacia abajo
    private void tiltDownWithGlue(int col) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int row = h - 1; row >= 0; row--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Resetear las banderas de visitado
        resetVisitedFlags();

        // Ordenar los grupos por la fila máxima (los inferiores primero)
        groups.sort((g1, g2) -> {
            int maxRow1 = g1.stream().mapToInt(Tile::getRow).max().orElse(-1);
            int maxRow2 = g2.stream().mapToInt(Tile::getRow).max().orElse(-1);
            return Integer.compare(maxRow2, maxRow1);
        });

        // Mover los grupos
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveDownGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Eliminar baldosas libres que caen en el agujero
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Las baldosas pegadas o con pegamento no se mueven
            } else {
                moveGroupDown(group, maxMove);
            }
        }
    }

    // Inclinación hacia la izquierda
    private void tiltLeftWithGlue(int row) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int col = 0; col < w; col++) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Resetear las banderas de visitado
        resetVisitedFlags();

        // Ordenar los grupos por la columna mínima (los más a la izquierda primero)
        groups.sort(Comparator.comparingInt(g -> g.stream().mapToInt(Tile::getCol).min().orElse(w)));

        // Mover los grupos
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveLeftGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Eliminar baldosas libres que caen en el agujero
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Las baldosas pegadas o con pegamento no se mueven
            } else {
                moveGroupLeft(group, maxMove);
            }
        }
    }

    // Inclinación hacia la derecha
    private void tiltRightWithGlue(int row) {
        List<List<Tile>> groups = new ArrayList<>();

        for (int col = w - 1; col >= 0; col--) {
            Tile tile = getTileAtPosition(row, col);
            if (!isTileEmpty(tile) && !tile.isVisited() && !tile.getIsHole()) {
                List<Tile> group = new ArrayList<>();
                boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
                if (isGluedOrStuck) {
                    collectStuckGroup(tile, group);
                } else {
                    tile.setVisited(true);
                    group.add(tile);
                }
                groups.add(group);
            }
        }

        // Resetear las banderas de visitado
        resetVisitedFlags();

        // Ordenar los grupos por la columna máxima (los más a la derecha primero)
        groups.sort((g1, g2) -> {
            int maxCol1 = g1.stream().mapToInt(Tile::getCol).max().orElse(-1);
            int maxCol2 = g2.stream().mapToInt(Tile::getCol).max().orElse(-1);
            return Integer.compare(maxCol2, maxCol1);
        });

        // Mover los grupos
        for (List<Tile> group : groups) {
            boolean isGluedOrStuck = group.get(0).isStuck() || group.get(0).hasGlue();
            int maxMove = calculateMaxMoveRightGroup(group, isGluedOrStuck);
            if (maxMove == -1) {
                if (!isGluedOrStuck) {
                    // Eliminar baldosas libres que caen en el agujero
                    for (Tile tile : group) {
                        tiles.get(tile.getRow()).set(tile.getCol(), createEmptyTile(tile.getRow(), tile.getCol()));
                        tile.makeInvisible();
                        tile.setLabel('*');
                    }
                }
                // Las baldosas pegadas o con pegamento no se mueven
            } else {
                moveGroupRight(group, maxMove);
            }
        }
    }

    // Métodos de cálculo de movimiento máximo ajustados para manejar agujeros

    // Movimiento hacia arriba
    private int calculateMaxMoveUp(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = row - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(i, column);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    // Baldosa pegada: no puede moverse al agujero, detenerse antes
                    break;
                } else {
                    // Baldosa libre: caería en el agujero
                    return -1; // Indica que la baldosa libre caerá en el agujero
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveUpGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                // Una de las baldosas libres del grupo caería en un agujero
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Movimiento hacia abajo
    private int calculateMaxMoveDown(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = row + 1; i < h; i++) {
            Tile nextTile = getTileAtPosition(i, column);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveDownGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = h;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Movimiento hacia la izquierda
    private int calculateMaxMoveLeft(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = column - 1; i >= 0; i--) {
            Tile nextTile = getTileAtPosition(row, i);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveLeftGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Movimiento hacia la derecha
    private int calculateMaxMoveRight(int row,int column, List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = 0;
        for (int i = column + 1; i < w; i++) {
            Tile nextTile = getTileAtPosition(row, i);
            if (nextTile.getIsHole()) {
                if (isGluedOrStuck) {
                    break;
                } else {
                    return -1;
                }
            } else if (isTileEmpty(nextTile) || (group != null && group.contains(nextTile))) {
                maxMove++;
            } else {
                break;
            }
        }
        return maxMove;
    }

    private int calculateMaxMoveRightGroup(List<Tile> group, boolean isGluedOrStuck) {
        int maxMove = w;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group, isGluedOrStuck);
            if (tileMaxMove == -1) {
                return -1;
            }
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    // Métodos de movimiento de grupos

    // Movimiento hacia arriba
    private void moveGroupUp(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas superiores se muevan primero
        group.sort(Comparator.comparingInt(Tile::getRow));
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

    // Movimiento hacia abajo
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

    // Movimiento hacia la izquierda
    private void moveGroupLeft(List<Tile> group, int steps) {
        if (steps == 0) return;
        // Ordenar el grupo para que las baldosas con columnas más bajas se muevan primero
        group.sort(Comparator.comparingInt(Tile::getCol));
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

    // Movimiento hacia la derecha
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

    // Método para recolectar todas las baldosas en un grupo pegado
    private void collectStuckGroup(Tile tile, List<Tile> group) {
        if (tile.isVisited()) return;
        tile.setVisited(true);
        group.add(tile);
        int row = tile.getRow();
       int column = tile.getCol();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = column + dir[1];
            Tile adjacentTile = getTileAtPosition(adjRow, adjCol);
            if (adjacentTile != null && !adjacentTile.isVisited() && !isTileEmpty(adjacentTile) &&
                    (adjacentTile.isStuck() || adjacentTile.hasGlue()) && !adjacentTile.getIsHole()) {
                collectStuckGroup(adjacentTile, group);
            }
        }
    }

    // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row,int column) {
        if (row >= 0 && row < h && column >= 0 && column < w) {
            return tiles.get(row).get(column);
        }
        return null;
    }

    // Método para verificar si una baldosa está vacía
    private boolean isTileEmpty(Tile tile) {
        if (tile == null) return false;
        return !tile.getIsHole() && tile.getLabel() == '*';
    }

    // Resetear las banderas de visitado después de la inclinación
    private void resetVisitedFlags() {
        for (List<Tile> rowList : tiles) {
            for (Tile tile : rowList) {
                tile.setVisited(false);
            }
        }
    }

    // Método para crear una baldosa vacía
    private Tile createEmptyTile(int row,int column) {
        int xPosition = 105 + (column * (Tile.SIZE + Tile.MARGIN));
        int yPosition = 55 + (row * (Tile.SIZE + Tile.MARGIN));
        Tile emptyTile;
        if (holes[row][column]) {
            emptyTile = new Tile('h', xPosition, yPosition, row, column);
            emptyTile.setIsHole(true);
            createHoleCircle(emptyTile);
        } else {
            emptyTile = new Tile('*', xPosition, yPosition, row, column);
        }
        return emptyTile;
    }
    
    // Muestra un mensaje de error si el simulador es visible y cambia el estado de ok a false
    private void showMessage(String message, String title){
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
                
                if (currentLabel == 'h' || targetLabel == 'h'){
                    continue;
                }
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
        
        System.out.println();
        
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
        // Intercambiar matrices de caracteres
        char[][] temp = starting;
        starting = ending;
        ending = temp;
    
        // Intercambiar listas de baldosas
        List<List<Tile>> tempTiles = tiles;
        tiles = referingTiles;
        referingTiles = tempTiles;
        
        // Intercambiar visualmente las posiciones de las baldosas
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile startingTile = tiles.get(row).get(col);
                int xPositionStartingTile = startingTile.getXPos();
                int yPositionStartingTile = startingTile.getYPos();
    
                Tile endingTile = referingTiles.get(row).get(col);
                int xPositionEndingTile = endingTile.getXPos();
                int yPositionEndingTile = endingTile.getYPos();
    
                // Calcular la diferencia en posiciones
                int deltaXStarting = xPositionEndingTile - xPositionStartingTile;
                int deltaYStarting = yPositionEndingTile - yPositionStartingTile;
    
                int deltaXEnding = xPositionStartingTile - xPositionEndingTile;
                int deltaYEnding = yPositionStartingTile - yPositionEndingTile;
    
                // Mover las baldosas a sus nuevas posiciones
                startingTile.moveHorizontal(deltaXStarting);
                startingTile.moveVertical(deltaYStarting);
                startingTile.setXPos(xPositionEndingTile);
                startingTile.setYPos(yPositionEndingTile);
    
                endingTile.moveHorizontal(deltaXEnding);
                endingTile.moveVertical(deltaYEnding);
                endingTile.setXPos(xPositionStartingTile);
                endingTile.setYPos(yPositionStartingTile);
            }
        }
    
        System.out.println("Boards have been exchanged. Now, you're editing the board that was the reference board before.");
    }
    
    // Método para crear un agujero en una posición dada
    public void makeHole(int row,int column) {
        // Validar las coordenadas
        if (row >= h || column >= w) {
            showMessage("You have exceeded the puzzle space.", "Error");
            this.ok = false; // Error Message

        } else if (row < 0 || column < 0) {
            showMessage("You cannot make a hole in a non-existent tile with negative position.", "Error");
            this.ok = false; // Error Message

        } else {

            Tile targetTile = tiles.get(row).get(column);

            if (targetTile.getLabel() == 'h') {
                showMessage("This tile already has a hole.", "Error");
                this.ok = false; // Error message
            } else if (isTileEmpty(targetTile) && !targetTile.getIsHole()) {

                // Marcar la baldosa como agujero
                targetTile.setLabel('h');
                targetTile.setIsHole(true);
                holes[row][column] = true;
                createHoleCircle(targetTile);
                this.ok = true; // Acción exitosa

            } else {
                showMessage("You can only make a hole in an empty tile.", "Error");
                this.ok = false; // Error message
            }
        }
    }
    
    // Método para crear un agujero visualmente
    private void createHoleCircle(Tile tile) {
        int xPos = tile.getXPos();
        int yPos = tile.getYPos();
        int diameter = Tile.SIZE;

        // Calcular la posición centrada del círculo
        int circleX = xPos;
        int circleY = yPos;

        // Crear y hacer visible el círculo (agujero)
        Circle hole = new Circle(diameter, circleX, circleY, Color.WHITE);
        hole.makeVisible();
        holeCircles.add(hole);
    }

  // <----------------------------------- IMPLEMENTING FIXEDTILES METHOD ----------------------------------->
    
    public int[][] fixedTiles() {
        int[][] fixedTilesMatrix = new int[h][w];
        
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = getTileAtPosition(row, col);
                
                // Skip if tile is empty or a hole
                if (isTileEmpty(tile) || tile.getIsHole()) {
                    fixedTilesMatrix[row][col] = 0;
                    continue;
                }
                
                boolean canMoveUp = canTileMove(tile, 'u');
                boolean canMoveDown = canTileMove(tile, 'd');
                boolean canMoveLeft = canTileMove(tile, 'l');
                boolean canMoveRight = canTileMove(tile, 'r');
                
                if (!canMoveUp && !canMoveDown && !canMoveLeft && !canMoveRight) {
                    fixedTilesMatrix[row][col] = 1;
                    if (visible) {
                        tiles.get(row).get(col).blink();
                    }
                } else {
                    fixedTilesMatrix[row][col] = 0;
                }
            }
        }
        
        // Print the fixed tiles matrix to the console
        System.out.println("Fixed Tiles Matrix:");
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                System.out.print(fixedTilesMatrix[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
        
        return fixedTilesMatrix;
    }
    
    
    private boolean canTileMove(Tile tile, char direction) {
        // Reset visited flags before checking
        resetVisitedFlags();
        
        List<Tile> group = new ArrayList<>();
        boolean isGluedOrStuck = tile.isStuck() || tile.hasGlue();
        
        if (isGluedOrStuck) {
            collectStuckGroup(tile, group);
        } else {
            group.add(tile);
        }
        
        int maxMove = 0;
        switch (direction) {
            case 'u':
                maxMove = calculateMaxMoveUpGroup(group, isGluedOrStuck);
                break;
            case 'd':
                maxMove = calculateMaxMoveDownGroup(group, isGluedOrStuck);
                break;
            case 'l':
                maxMove = calculateMaxMoveLeftGroup(group, isGluedOrStuck);
                break;
            case 'r':
                maxMove = calculateMaxMoveRightGroup(group, isGluedOrStuck);
                break;
        }
        
        if (maxMove > 0 || (maxMove == -1 && !isGluedOrStuck)) {
            return true;
        } else {
            return false;
        }
    }
    
    public void printFixedTilesMatrix() {
        int[][] fixedTilesMatrix = fixedTiles(); // Llamar a tu método que devuelve la matriz
        
        for (int row = 0; row < fixedTilesMatrix.length; row++) {
            for (int col = 0; col < fixedTilesMatrix[0].length; col++) {
                // Imprimir el valor en la posición (row, col)
                System.out.print(fixedTilesMatrix[row][col] + " ");
            }
            System.out.println(); // Salto de línea para la siguiente fila
        }
    }
    
    
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
        
        
         //SECOND TEST
        char[][] starting1 = {
        {'y', 'g', 'y', 'b', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'b', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'g'},
        {'g', 'b', '*', 'y', 'b', 'g', 'r', 'y', 'b', 'r'},
        {'r', '*', 'g', 'b', 'r', '*', '*', 'b', 'r', 'g'},
        {'b', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'r'},
        {'y', '*', 'r', '*', 'y', 'b', 'r', 'g', 'y', 'b'},
        {'*', 'r', 'y', 'b', 'g', '*', '*', 'b', 'g', 'r'},
        {'*', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'*', 'b', 'g', 'r', 'y', '*', 'g', 'r', 'y', 'g'},
        {'*', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'}
    };
        
        char[][] ending1 = {
        {'y', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'g', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'b', 'g', 'y', 'r', 'y', 'b', 'g', 'r', 'y', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'},
        {'y', 'b', 'g', 'r', 'y', 'b', 'g', '*', 'y', 'b'},
        {'g', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
        {'y', 'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r'},
        {'g', 'b', 'y', 'r', 'g', 'b', 'y', '*', 'g', 'b'},
        {'r', 'g', 'b', 'y', 'r', 'g', 'b', 'y', 'r', 'g'}
    };
        
        Puzzle pz3 = new Puzzle(10, 10); // Tablero sin matrices
        Puzzle pz4 = new Puzzle(starting1, ending1); // Tablero con matrices
        
        pz4.addTile(9,0,'r');
        pz4.addGlue(9,1);
        pz4.tilt('u');
        pz4.tilt('r');
        
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
        pz4.addTile(6,0,'r');
        
        int[] from4 = {6,0};
        int[] to4   = {3,1};
        pz4.relocateTile(from4,to4);
    }
}
