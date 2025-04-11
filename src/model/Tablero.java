package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private static final int N = 5;
    private Figura[][] matriz = new Figura[N][N];
    private Posicion posRey, posHueco, posObjetivo;
    private double heuristica;
    private int costo;
    private List<String> movimientos = new ArrayList<>();

    public void cargarDesdeArchivo(String ruta) throws IOException {
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

                if (tipo == TipoFigura.HUECO)
                    posHueco = new Posicion(i, j);

                matriz[i][j] = new Figura(tipo, i, j);
            }
        }
        br.close();
        calcularHeuristica();
    }

    public void calcularHeuristica() {
        int dx = posObjetivo.x - posRey.x;
        int dy = posObjetivo.y - posRey.y;
        heuristica = Math.sqrt(dx * dx + dy * dy);
    }

    //Si el rey esta en el objetivo
    public boolean esObjetivo() {
        return posRey.equals(posObjetivo);
    }

    public void mostrar() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(simb(matriz[i][j].getTipo()) + " ");
            }
            System.out.println();
        }
    }

    //Transforma el Tipo de Figura al símbolo en carácter
    private String simb(TipoFigura tipo) {
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

    public List<Tablero> generarSucesores() {
        List<Tablero> sucesores = new ArrayList<>();
        int[][] direcciones = {{-1,0},{1,0},{0,-1},{0,1},{-1,-1},{-1,1},{1,-1},{1,1}};

        // Movimiento normal hacia el hueco
        for (int[] d : direcciones) {
            int i = posHueco.x + d[0];
            int j = posHueco.y + d[1];

            if (i >= 0 && i < N && j >= 0 && j < N) {
                Figura figura = matriz[i][j];
                if (figura.getTipo() == TipoFigura.REY ||
                        figura.getTipo() == TipoFigura.TORRE ||
                        figura.getTipo() == TipoFigura.ALFIL) {

                    if (figura.puedeMoverA(posHueco, matriz)) {
                        Tablero copia = this.clonar();
                        copia.intercambiar(figura.getPos(), posHueco);
                        copia.movimientos.addAll(this.movimientos);
                        copia.movimientos.add(posToCoord(figura.getPos()));
                        copia.costo = this.costo + 1;
                        copia.calcularHeuristica();
                        sucesores.add(copia);
                    }
                }
            }
        }

        // Movimiento especial: Rey al objetivo si están adyacentes y el hueco también está junto al Rey
        for (int[] d : direcciones) {
            int rx = posRey.x + d[0];
            int ry = posRey.y + d[1];

            if (rx == posObjetivo.x && ry == posObjetivo.y) {
                for (int[] dh : direcciones) {
                    int hx = posRey.x + dh[0];
                    int hy = posRey.y + dh[1];

                    if (hx == posHueco.x && hy == posHueco.y) {
                        Tablero copia = this.clonar();
                        copia.matriz[posRey.x][posRey.y] = new Figura(TipoFigura.HUECO, posRey.x, posRey.y);
                        copia.matriz[posObjetivo.x][posObjetivo.y] = new Figura(TipoFigura.REY, posObjetivo.x, posObjetivo.y);
                        copia.posRey = new Posicion(posObjetivo.x, posObjetivo.y);
                        copia.posHueco = new Posicion(posRey.x, posRey.y);
                        copia.movimientos.addAll(this.movimientos);
                        copia.movimientos.add(posToCoord(posRey));
                        copia.costo = this.costo + 1;
                        copia.calcularHeuristica();
                        sucesores.add(copia);
                    }
                }
            }
        }

        return sucesores;
    }

    public void intercambiar(Posicion p1, Posicion p2) {
        Figura temp = matriz[p1.x][p1.y];
        matriz[p1.x][p1.y] = matriz[p2.x][p2.y];
        matriz[p2.x][p2.y] = temp;

        TipoFigura tipo1 = matriz[p1.x][p1.y].getTipo();
        TipoFigura tipo2 = matriz[p2.x][p2.y].getTipo();

        if (tipo1 == TipoFigura.REY) posRey = new Posicion(p1.x, p1.y);
        if (tipo2 == TipoFigura.REY) posRey = new Posicion(p2.x, p2.y);
        if (tipo1 == TipoFigura.HUECO) posHueco = new Posicion(p1.x, p1.y);
        if (tipo2 == TipoFigura.HUECO) posHueco = new Posicion(p2.x, p2.y);
    }

    public Tablero clonar() {
        Tablero copia = new Tablero();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Figura f = matriz[i][j];
                copia.matriz[i][j] = new Figura(f.getTipo(), i, j);
            }
        }

        copia.posRey = new Posicion(posRey.x, posRey.y);
        copia.posHueco = new Posicion(posHueco.x, posHueco.y);
        copia.posObjetivo = new Posicion(posObjetivo.x, posObjetivo.y);
        copia.costo = this.costo;
        return copia;
    }

    public static String posToCoord(Posicion pos) {
        return "" + (char)('a' + pos.y) + (pos.x + 1);
    }

    public double getHeuristica() {
        return heuristica;
    }

    public int getCosto() {
        return costo;
    }

    public List<String> getMovimientos() {
        return movimientos;
    }

    public Figura[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(Figura[][] matriz) {
        this.matriz = matriz;
    }

    public Posicion getPosRey() {
        return posRey;
    }

    public void setPosRey(Posicion posRey) {
        this.posRey = posRey;
    }

    public Posicion getPosHueco() {
        return posHueco;
    }

    public void setPosHueco(Posicion posHueco) {
        this.posHueco = posHueco;
    }

    public Posicion getPosObjetivo() {
        return posObjetivo;
    }

    public void setPosObjetivo(Posicion posObjetivo) {
        this.posObjetivo = posObjetivo;
    }

    public void setHeuristica(double heuristica) {
        this.heuristica = heuristica;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public void setMovimientos(List<String> movimientos) {
        this.movimientos = movimientos;
    }
}
