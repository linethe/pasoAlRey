import java.io.*;
import java.util.*;

public class Main {
    static final int N = 5;

    enum TipoFigura {
        REY, TORRE, ALFIL, MURO, HUECO, OBJETIVO, VACIO
    }

    static class Posicion {
        int x, y;
        Posicion(int x, int y) { this.x = x; this.y = y; }
        public boolean equals(Object o) {
            if (o instanceof Posicion p) return this.x == p.x && this.y == p.y;
            return false;
        }
    }

    static class Figura {
        TipoFigura tipo;
        Posicion pos;

        Figura(TipoFigura tipo, int x, int y) {
            this.tipo = tipo;
            this.pos = new Posicion(x, y);
        }

        boolean puedeMoverA(Posicion destino, Figura[][] tablero) {
            if (tablero[destino.x][destino.y].tipo != TipoFigura.HUECO) return false;
            int dx = destino.x - pos.x;
            int dy = destino.y - pos.y;

            return switch (tipo) {
                case REY -> Math.abs(dx) <= 1 && Math.abs(dy) <= 1;
                case TORRE -> (dx == 0 || dy == 0) && sinObstaculos(pos, destino, tablero);
                case ALFIL -> Math.abs(dx) == Math.abs(dy) && sinObstaculos(pos, destino, tablero);
                default -> false;
            };
        }

        private boolean sinObstaculos(Posicion origen, Posicion destino, Figura[][] tablero) {
            int dx = Integer.compare(destino.x, origen.x);
            int dy = Integer.compare(destino.y, origen.y);
            int x = origen.x + dx;
            int y = origen.y + dy;
            while (x != destino.x || y != destino.y) {
                if (tablero[x][y].tipo != TipoFigura.VACIO && tablero[x][y].tipo != TipoFigura.HUECO)
                    return false;
                x += dx;
                y += dy;
            }
            return true;
        }
    }

    static class Tablero {
        Figura[][] matriz = new Figura[N][N];
        Posicion posRey, posHueco, posObjetivo;
        double heuristica;
        int costo;
        List<String> movimientos = new ArrayList<>();

        void cargarDesdeArchivo(String ruta) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            for (int i = 0; i < N; i++) {
                String[] linea = br.readLine().split(", ");
                for (int j = 0; j < N; j++) {
                    char c = linea[j].charAt(0);
                    TipoFigura tipo;
                    switch (c) {
                        case 'M' -> tipo = TipoFigura.MURO;
                        case 'H' -> tipo = TipoFigura.HUECO;
                        case 'R' -> {
                            tipo = TipoFigura.REY;
                            posRey = new Posicion(i, j);
                        }
                        case 'O' -> {
                            tipo = TipoFigura.OBJETIVO;
                            posObjetivo = new Posicion(i, j);
                        }
                        case 'A' -> tipo = TipoFigura.ALFIL;
                        case 'T' -> tipo = TipoFigura.TORRE;
                        default -> tipo = TipoFigura.VACIO;
                    }
                    if (tipo == TipoFigura.HUECO) posHueco = new Posicion(i, j);
                    matriz[i][j] = new Figura(tipo, i, j);
                }
            }
            calcularHeuristica();
        }

        void calcularHeuristica() {
            int dx = posObjetivo.x - posRey.x;
            int dy = posObjetivo.y - posRey.y;
            heuristica = Math.sqrt(dx * dx + dy * dy);
        }

        boolean esObjetivo() {
            return posRey.equals(posObjetivo);
        }

        void mostrar() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(simb(matriz[i][j].tipo) + " ");
                }
                System.out.println();
            }
        }

        String simb(TipoFigura tipo) {
            return switch (tipo) {
                case MURO -> "M";
                case HUECO -> "H";
                case REY -> "R";
                case TORRE -> "T";
                case ALFIL -> "A";
                case OBJETIVO -> "O";
                default -> ".";
            };
        }

        List<Tablero> generarSucesores() {
            List<Tablero> sucesores = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Figura figura = matriz[i][j];
                    if (figura.tipo == TipoFigura.REY || figura.tipo == TipoFigura.TORRE || figura.tipo == TipoFigura.ALFIL) {
                        if (figura.puedeMoverA(posHueco, matriz)) {
                            Tablero copia = this.clonar();
                            copia.intercambiar(figura.pos, posHueco);
                            copia.movimientos.addAll(this.movimientos);
                            copia.movimientos.add(posToCoord(figura.pos));
                            copia.costo = this.costo + 1;
                            copia.calcularHeuristica();
                            sucesores.add(copia);
                        }
                    }
                }
            }
            return sucesores;
        }

        void intercambiar(Posicion p1, Posicion p2) {
            Figura temp = matriz[p1.x][p1.y];
            matriz[p1.x][p1.y] = matriz[p2.x][p2.y];
            matriz[p2.x][p2.y] = temp;

            TipoFigura tipo1 = matriz[p1.x][p1.y].tipo;
            TipoFigura tipo2 = matriz[p2.x][p2.y].tipo;
            if (tipo1 == TipoFigura.REY) posRey = new Posicion(p1.x, p1.y);
            if (tipo2 == TipoFigura.REY) posRey = new Posicion(p2.x, p2.y);
            if (tipo1 == TipoFigura.HUECO) posHueco = new Posicion(p1.x, p1.y);
            if (tipo2 == TipoFigura.HUECO) posHueco = new Posicion(p2.x, p2.y);
        }

        Tablero clonar() {
            Tablero copia = new Tablero();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Figura f = matriz[i][j];
                    copia.matriz[i][j] = new Figura(f.tipo, i, j);
                }
            }
            copia.posRey = new Posicion(posRey.x, posRey.y);
            copia.posHueco = new Posicion(posHueco.x, posHueco.y);
            copia.posObjetivo = new Posicion(posObjetivo.x, posObjetivo.y);
            copia.costo = this.costo;
            return copia;
        }

        static String posToCoord(Posicion pos) {
            return "" + (char)('a' + pos.y) + (pos.x + 1);
        }
    }

    static void escaladaSimple(Tablero inicial) {
        Tablero actual = inicial;
        int pasos = 0;
        while (!actual.esObjetivo()) {
            List<Tablero> sucesores = actual.generarSucesores();
            if (sucesores.isEmpty()) break;
            sucesores.sort(Comparator.comparingDouble(t -> t.heuristica));
            Tablero mejor = sucesores.get(0);
            if (mejor.heuristica < actual.heuristica) {
                actual = mejor;
                pasos++;
                System.out.println("Paso " + pasos + " - h': " + actual.heuristica);
                actual.mostrar();
                System.out.println();
            } else {
                break;
            }
        }
        if (actual.esObjetivo()) {
            System.out.println("\n¡Objetivo alcanzado en " + pasos + " pasos!");
        } else {
            System.out.println("\nNo se pudo alcanzar el objetivo con escalada simple.");
        }
    }

    static void aEstrella(Tablero inicial) {
        PriorityQueue<Tablero> cola = new PriorityQueue<>(Comparator.comparingDouble(t -> t.heuristica + t.costo));
        Set<String> visitados = new HashSet<>();
        cola.add(inicial);

        while (!cola.isEmpty()) {
            Tablero actual = cola.poll();
            if (actual.esObjetivo()) {
                System.out.println("\n¡Objetivo alcanzado con A*!\nPasos: " + actual.costo);
                System.out.println("Movimientos: " + String.join(", ", actual.movimientos));
                actual.mostrar();
                return;
            }

            String hash = actual.posRey.x + "," + actual.posRey.y + "," + actual.posHueco.x + "," + actual.posHueco.y;
            if (visitados.contains(hash)) continue;
            visitados.add(hash);

            for (Tablero sucesor : actual.generarSucesores()) {
                cola.add(sucesor);
            }
        }

        System.out.println("\nNo se encontró solución con A*.");
    }

    public static void main(String[] args) throws IOException {
        Tablero tablero = new Tablero();
        long tiempoInicio = System.currentTimeMillis();

        tablero.cargarDesdeArchivo("./files/PASOALREY2.TXT");
        System.out.println("Tablero inicial:");
        tablero.mostrar();

        // escaladaSimple(tablero);
        aEstrella(tablero);

        long tiempoFin = System.currentTimeMillis();
        System.out.println("\nTiempo empleado: " + (tiempoFin - tiempoInicio) + " ms");
    }
}