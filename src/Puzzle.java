import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Puzzle {
    private int tileSize;
    private int rows;
    private int cols;
    private int margin;
    private int padding;
    private Rectangle startingBoard;
    private char[][] starting;
    private char[][] ending;
    private List<List<Tile>> tiles;
    private boolean visible = true;
    private boolean ok = true;

    // Definición del color lightBrown
    private final Color lightBrown = new Color(207, 126, 60);

    // Constructor para inicializar el puzzle con matrices
    public Puzzle(char[][] starting, char[][] ending) {
        this.tileSize = 50;  // Tamaño de cada baldosa
        this.margin = 10;    // Margen entre baldosas
        this.padding = 5;    // Padding interno
        this.rows = starting.length;
        this.cols = starting[0].length;
        this.starting = starting;
        this.ending = ending;
        this.tiles = new ArrayList<>();

        // Crear el tablero inicial
        startingBoard = new Rectangle(cols * (tileSize + margin), rows * (tileSize + margin), lightBrown, 100, 50);

        // Crear las baldosas del puzzle inicial
        for (int row = 0; row < starting.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < starting[row].length; col++) {
                char label = starting[row][col];
                int xPosition = 105 + (col * (tileSize + margin));
                int yPosition = 55 + (row * (tileSize + margin));

                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding, row, col, lightBrown);
                rowList.add(tile);
            }
            tiles.add(rowList);
        }
    }

    // Método para imprimir el estado del tablero
    public void printBoardState() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = getTileAtPosition(row, col);
                char label = tile.getLabel();
                System.out.print(label + " ");
            }
            System.out.println();
        }
    }

    // Método para aplicar pegamento a una baldosa
    public void addGlue(int row, int col) {
        Tile tile = getTileAtPosition(row, col);
        if (tile == null || isTileEmpty(tile)) {
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
                for (int col = 0; col < cols; col++) {
                    tiltDownWithGlue(col);
                }
                break;
            case 'u':
                for (int col = 0; col < cols; col++) {
                    tiltUpWithGlue(col);
                }
                break;
            case 'r':
                for (int row = 0; row < rows; row++) {
                    tiltRightWithGlue(row);
                }
                break;
            case 'l':
                for (int row = 0; row < rows; row++) {
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
        for (int row = 0; row < rows; row++) {
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
        for (int row = rows - 1; row >= 0; row--) {
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
        for (int col = cols - 1; col >= 0; col--) {
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
        for (int col = 0; col < cols; col++) {
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
        int maxMove = rows;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveUp(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileUp(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() - steps;
        tile.moveVertical(-steps * (tileSize + margin));
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
            tile.moveVertical(-steps * (tileSize + margin));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Métodos similares para movimientos hacia abajo, izquierda y derecha...

    // Movimiento hacia abajo
    private int calculateMaxMoveDown(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = row + 1; i < rows; i++) {
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
        int maxMove = rows;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveDown(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileDown(Tile tile, int steps) {
        if (steps == 0) return;
        int newRow = tile.getRow() + steps;
        tile.moveVertical(steps * (tileSize + margin));
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
            tile.moveVertical(steps * (tileSize + margin));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Movimiento hacia la derecha
    private int calculateMaxMoveRight(int row, int col, List<Tile> group) {
        int maxMove = 0;
        for (int i = col + 1; i < cols; i++) {
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
        int maxMove = cols;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveRight(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileRight(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() + steps;
        tile.moveHorizontal(steps * (tileSize + margin));
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
            tile.moveHorizontal(steps * (tileSize + margin));
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
        int maxMove = cols;
        for (Tile tile : group) {
            int tileMaxMove = calculateMaxMoveLeft(tile.getRow(), tile.getCol(), group);
            maxMove = Math.min(maxMove, tileMaxMove);
        }
        return maxMove;
    }

    private void moveTileLeft(Tile tile, int steps) {
        if (steps == 0) return;
        int newCol = tile.getCol() - steps;
        tile.moveHorizontal(-steps * (tileSize + margin));
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
            tile.moveHorizontal(-steps * (tileSize + margin));
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
        int xPosition = 105 + (col * (tileSize + margin));
        int yPosition = 55 + (row * (tileSize + margin));
        Tile emptyTile = new Tile(tileSize, '*', xPosition, yPosition, padding, row, col, lightBrown);
        return emptyTile;
    }

    // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
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

    // Mostrar mensaje
    public void showMessage(String message, String title) {
        if (this.visible) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
            this.ok = false;
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
        fromTile.moveHorizontal((to[1] - from[1]) * (tileSize + margin));
        fromTile.moveVertical((to[0] - from[0]) * (tileSize + margin));
        // Actualizar la lista de baldosas: mover la baldosa a la nueva posición
        tiles.get(to[0]).set(to[1], fromTile);
        // Crear una nueva baldosa vacía en la posición original
        Tile emptyTile = createEmptyTile(from[0], from[1]);
        tiles.get(from[0]).set(from[1], emptyTile);
    }

    // Método para validar si las coordenadas son correctas
    private boolean areValidCoordinates(int[] coords) {
        return coords.length == 2 && coords[0] >= 0 && coords[0] < rows && coords[1] >= 0 && coords[1] < cols;
    }

    // Método principal para pruebas
    public static void main(String[] args) {
        char[][] starting = {
            {'b', '*', 'b', 'g', 'b', 'r', 'g', 'b', 'y', 'r'},
            {'*', 'y', 'g', 'g', 'b', 'y', 'r', 'g', 'b', 'b'},
            {'*', '*', 'r', 'b', 'y', 'b', 'g', 'r', 'y', 'y'},
            {'*', 'g', 'b', 'g', 'b', 'r', 'b', 'r', 'g', 'b'},
            {'*', 'g', 'r', 'g', 'r', 'y', 'b', 'g', 'r', 'y'},
            {'*', 'r', 'r', 'y', 'b', 'r', 'g', 'y', 'b', 'g'},
            {'*', '*', 'b', 'y', 'b', 'g', 'b', 'g', 'r', 'r'},
            {'*', '*', 'r', 'y', 'r', 'g', 'b', 'y', 'r', 'b'},
            {'*', '*', 'y', 'y', 'g', 'r', 'y', 'g', 'b', 'r'},
            {'g', '*', '*', 'b', 'g', 'r', 'y', 'b', 'g', 'r'}
        };

        char[][] ending = {
            // Tu matriz ending (puedes dejarla vacía si no la necesitas)
        };

        Puzzle pz4 = new Puzzle(starting, ending);

        // Imprimir estado inicial
        System.out.println("Estado inicial del tablero:");
        pz4.printBoardState();

        // Realizar inclinación hacia abajo
        pz4.tilt('u');

        // Imprimir estado después del movimiento
        System.out.println("Estado del tablero después de tilt('d'):");
        pz4.printBoardState();
    }
}
