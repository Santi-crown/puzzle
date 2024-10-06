import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class PuzzleContest {

    // Método para determinar si el puzzle puede ser resuelto
    public boolean solve(char[][] starting, char[][] ending) {
        // Verificar si los tableros tienen la misma cantidad de fichas de cada color
        if (!tileCountsMatch(starting, ending)) {
            JOptionPane.showMessageDialog(null,"No");
            return false;
        }
        
        JOptionPane.showMessageDialog(null,"Yes", "Icono de Octopocto", 0, new ImageIcon("img/checkMark.png"));
        // Implementar BFS para encontrar si existe una secuencia de inclinaciones que transforme starting en ending
        return bfsSolve(starting, ending) != null;
    }

    // Método para simular los movimientos y mostrar paso a paso
    public void simulate(char[][] starting, char[][] ending) {
        // Primero, verificar si hay solución
        List<Character> moves = bfsSolve(starting, ending);
        if (moves == null) {
            JOptionPane.showMessageDialog(null,"No hay solución posible");
            return;
        }

        // Crear una instancia de Puzzle con la configuración inicial
        Puzzle puzzle = new Puzzle(starting, ending);
        puzzle.makeVisible();

        // Mostrar la configuración inicial
        System.out.println("Configuración inicial:");
        printBoard(puzzle);

        // Aplicar los movimientos y mostrar paso a paso
        int step = 1;
        for (char move : moves) {
            puzzle.tilt(move);
            System.out.println("Paso " + step + ": Inclinando hacia " + directionToString(move));
            printBoard(puzzle);
            step++;
        }
        System.out.println("Finalizado");
        // Verificar si se alcanzó la configuración final
        if (puzzle.isGoal()) {
            JOptionPane.showMessageDialog(null,"Se ha alcanzado la configuración final\n starting = ending", "Icono de Octopocto", 0, new ImageIcon("img/matrixEqual.png"));
        } else {
            JOptionPane.showMessageDialog(null,"No se ha podido alcanzar la configuración final con las inclinaciones realizadas.");
        }
    }

    // Implementación de BFS para encontrar la secuencia de inclinaciones
    private List<Character> bfsSolve(char[][] starting, char[][] ending) {
        int h = starting.length;
        int w = starting[0].length;

        // Representación del estado del tablero
        class State {
            char[][] board;
            List<Character> moves;

            State(char[][] board, List<Character> moves) {
                this.board = board;
                this.moves = moves;
            }

            // Generar una clave única para el estado del tablero
            String getKey() {
                StringBuilder sb = new StringBuilder();
                for (char[] row : board) {
                    sb.append(row);
                }
                return sb.toString();
            }
        }

        // Direcciones de las inclinaciones
        char[] directions = { 'u', 'd', 'l', 'r' };

        // Inicializar la cola para BFS y el conjunto de estados visitados
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Estado inicial
        State initialState = new State(copyBoard(starting), new ArrayList<>());
        queue.add(initialState);
        visited.add(initialState.getKey());

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            // Verificar si hemos alcanzado el estado final
            if (boardsEqual(currentState.board, ending)) {
                return currentState.moves;
            }

            // Generar estados vecinos
            for (char dir : directions) {
                char[][] newBoard = tiltBoard(currentState.board, dir);
                State newState = new State(newBoard, new ArrayList<>(currentState.moves));
                newState.moves.add(dir);

                String key = newState.getKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.add(newState);
                }
            }
        }

        // No se encontró solución
        return null;
    }

    // Métodos auxiliares

    // Verificar si dos tableros son iguales
    private boolean boardsEqual(char[][] board1, char[][] board2) {
        int h = board1.length;
        for (int i = 0; i < h; i++) {
            if (!Arrays.equals(board1[i], board2[i])) {
                return false;
            }
        }
        return true;
    }

    // Copiar un tablero
    private char[][] copyBoard(char[][] board) {
        int h = board.length;
        char[][] newBoard = new char[h][];
        for (int i = 0; i < h; i++) {
            newBoard[i] = board[i].clone();
        }
        return newBoard;
    }

    // Verificar si los recuentos de fichas coinciden
    private boolean tileCountsMatch(char[][] starting, char[][] ending) {
        Map<Character, Integer> startingCounts = countTiles(starting);
        Map<Character, Integer> endingCounts = countTiles(ending);
        return startingCounts.equals(endingCounts);
    }

    // Contar las fichas de cada color en una matriz
    private Map<Character, Integer> countTiles(char[][] board) {
        Map<Character, Integer> counts = new HashMap<>();
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != '*') {
                    counts.put(cell, counts.getOrDefault(cell, 0) + 1);
                }
            }
        }
        return counts;
    }

    // Aplicar una inclinación a un tablero y devolver el nuevo tablero
    private char[][] tiltBoard(char[][] board, char direction) {
        int h = board.length;
        int w = board[0].length;
        char[][] newBoard = copyBoard(board);

        switch (direction) {
            case 'u':
                for (int col = 0; col < w; col++) {
                    int insertPos = 0;
                    for (int row = 0; row < h; row++) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[insertPos][col] = temp;
                            insertPos++;
                        }
                    }
                }
                break;
            case 'd':
                for (int col = 0; col < w; col++) {
                    int insertPos = h - 1;
                    for (int row = h - 1; row >= 0; row--) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[insertPos][col] = temp;
                            insertPos--;
                        }
                    }
                }
                break;
            case 'l':
                for (int row = 0; row < h; row++) {
                    int insertPos = 0;
                    for (int col = 0; col < w; col++) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[row][insertPos] = temp;
                            insertPos++;
                        }
                    }
                }
                break;
            case 'r':
                for (int row = 0; row < h; row++) {
                    int insertPos = w - 1;
                    for (int col = w - 1; col >= 0; col--) {
                        if (newBoard[row][col] != '*') {
                            char temp = newBoard[row][col];
                            newBoard[row][col] = '*';
                            newBoard[row][insertPos] = temp;
                            insertPos--;
                        }
                    }
                }
                break;
        }

        return newBoard;
    }

    // Convertir la dirección a una cadena legible
    private String directionToString(char direction) {
        switch (direction) {
            case 'u':
                return "arriba";
            case 'd':
                return "abajo";
            case 'l':
                return "la izquierda";
            case 'r':
                return "la derecha";
            default:
                return "";
        }
    }

    // Imprimir el tablero actual
    private void printBoard(Puzzle puzzle) {
        int h = puzzle.getHeight();
        int w = puzzle.getWidth();
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                Tile tile = puzzle.getTileAtPosition(row, col);
                System.out.print(tile.getLabel());
            }
            System.out.println();
        }
        System.out.println();
    }

    // Método principal para pruebas
    public static void main(String[] args) {
        PuzzleContest contest = new PuzzleContest();

        // Sample Input 1
        char[][] starting1 = {
            { '*', 'r', '*', '*' },
            { 'r', 'g', 'y', 'b' },
            { '*', 'b', '*', '*' },
            { '*', 'y', 'r', '*' }
        };

        char[][] ending1 = {
            { 'y', 'r', 'b', 'r' },
            { '*', '*', 'y', 'r' },
            { '*', '*', '*', 'g' },
            { '*', '*', '*', 'b' }
        };

        // Probar solve
        boolean canSolve = contest.solve(starting1, ending1);
        System.out.println("¿Es posible resolver el puzzle? " + (canSolve ? "Sí" : "No"));

        // Probar simulate
        contest.simulate(starting1, ending1);
    }
}
