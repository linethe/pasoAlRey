package model;

enum TipoFigura {
    REY, TORRE, ALFIL, MURO, HUECO, OBJETIVO, VACIO
}

public class Figura {
    private TipoFigura tipo;
    private Posicion pos;

    public Figura(TipoFigura tipo, int x, int y) {
        this.tipo = tipo;
        this.pos = new Posicion(x, y);
    }

    public TipoFigura getTipo() {
        return tipo;
    }

    public Posicion getPos() {
        return pos;
    }

    public boolean puedeMoverA(Posicion destino, Figura[][] tablero) {
        if (tablero[destino.x][destino.y].tipo != TipoFigura.HUECO)
            return false;

        int dx = destino.x - pos.x;
        int dy = destino.y - pos.y;

        return switch (tipo) {
            case REY -> Math.abs(dx) <= 1 && Math.abs(dy) <= 1;
            case TORRE -> (dx == 0 || dy == 0);
            case ALFIL -> Math.abs(dx) == Math.abs(dy);
            default -> false;
        };
    }


}
