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
    private Rectangle endingBoard;
    private Color color;
    private char[][] starting;
    private char[][] ending;
    private List<List<Tile>> tiles;
    private List<List<Tile>> referingTiles;
    private boolean visible = true;
    private boolean ok = true;

    Color lightBrown = new Color(207, 126, 60);

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
        this.referingTiles = new ArrayList<>();

        // Crear el tablero inicial
        startingBoard = new Rectangle(cols * (tileSize + margin), rows * (tileSize + margin), lightBrown, 100, 50);

        // Crear las baldosas del puzzle inicial
        for (int row = 0; row < starting.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < starting[row].length; col++) {
                char label = starting[row][col];
                int xPosition = 105 + (col * (tileSize + margin));
                int yPosition = 55 + (row * (tileSize + margin));

                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding, row, col);
                rowList.add(tile);
            }
            tiles.add(rowList);
        }

        // Crear el tablero de referencia (opcional)
        // Si no necesitas el tablero de referencia, puedes comentar o eliminar este bloque
        /*
        endingBoard = new Rectangle(cols * (tileSize + margin), rows * (tileSize + margin), lightBrown, cols * (tileSize + margin) + 350, 50);

        for (int row = 0; row < ending.length; row++) {
            List<Tile> rowList = new ArrayList<>();
            for (int col = 0; col < ending[row].length; col++) {
                char label = ending[row][col];
                int xPosition = cols * (tileSize + margin) + 355 + (col * (tileSize + margin));
                int yPosition = 55 + (row * (tileSize + margin));

                Tile tile = new Tile(tileSize, label, xPosition, yPosition, padding, row, col);
                rowList.add(tile);
            }
            referingTiles.add(rowList);
        }
        */
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
        if (newRow < 0) newRow = 0; // Asegurar que no se salga del tablero
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
            if (newRow < 0) newRow = 0; // Asegurar que no se salga del tablero
            tile.moveVertical(-steps * (tileSize + margin));
            tiles.get(newRow).set(tile.getCol(), tile);
            tile.setRow(newRow);
        }
    }

    // Implementa métodos similares para abajo, izquierda y derecha, asegurando límites

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
        if (newRow >= rows) newRow = rows - 1; // Asegurar que no se salga del tablero
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
            if (newRow >= rows) newRow = rows - 1; // Asegurar que no se salga del tablero
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
        if (newCol >= cols) newCol = cols - 1; // Asegurar que no se salga del tablero
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
            if (newCol >= cols) newCol = cols - 1; // Asegurar que no se salga del tablero
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
        if (newCol < 0) newCol = 0; // Asegurar que no se salga del tablero
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
            if (newCol < 0) newCol = 0; // Asegurar que no se salga del tablero
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
        Tile emptyTile = new Tile(tileSize, '*', xPosition, yPosition, padding, row, col);
        return emptyTile;
    }

    // Método para obtener una baldosa en una posición específica
    private Tile getTileAtPosition(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return tiles.get(row).get(col);
        }
        return null;
    }

    // Método para verificar si una baldosa está vacía
    private boolean isTileEmpty(Tile tile) {
        char label = tile.getLabel();
        return label == '*' || label == 'n' || label == '\0';
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

    // Método principal para pruebas
    public static void main(String[] args) {
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
            {'r', 'r', 'y', 'b', 'g', 'r', 'y', 'b', 'g', 'r'}
        };

        char[][] ending1 = {
            // Tu matriz ending1 (puedes dejarla vacía si no la necesitas)
        };

        Puzzle pz4 = new Puzzle(starting1, ending1);

        // Ejemplo de prueba
        pz4.addGlue(9, 0);
        pz4.deleteGlue(9, 1);
        pz4.tilt('u');

        // Otra prueba
        pz4.addGlue(3, 1);
        pz4.tilt('d');
    }
}
