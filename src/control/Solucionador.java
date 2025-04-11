package control;

import model.Tablero;

import java.util.*;

public class Solucionador {

    static void escaladaSimple(Tablero inicial) {
        Tablero actual = inicial;
        boolean mejora = true;
        int nodosExplorados = 0; // Contador de nodos explorados
        int iteraciones = 0;

        System.out.println("\nIniciando búsqueda por Escalada Simple...");

        while (mejora && !actual.esObjetivo() && iteraciones < 1000) {
            mejora = false;
            iteraciones++;
            nodosExplorados++; // Incrementar contador


            List<Tablero> sucesores = actual.generarSucesores();
            Tablero mejor = null;
            double mejorHeuristica = Double.MAX_VALUE;

            for (Tablero sucesor : sucesores) {
                if (sucesor.getHeuristica() < mejorHeuristica) {
                    mejorHeuristica = sucesor.getHeuristica();
                    mejor = sucesor;
                }
            }

            if (mejor != null && mejor.getHeuristica() < actual.getHeuristica()) {
                actual = mejor;
                mejora = true;
                System.out.println("Iteración " + iteraciones + ": h = " + actual.getHeuristica());
            }
        }

        if (actual.esObjetivo()) {
            System.out.println("\n¡Objetivo alcanzado con Escalada Simple!");
            System.out.println("Pasos: " + actual.getCosto());
            System.out.println("Nodos explorados: " + nodosExplorados);
            System.out.println("Movimientos: " + String.join(", ", actual.getMovimientos()));
            actual.mostrar();
        } else {
            System.out.println("\nEscalada Simple no encontró solución.");
            System.out.println("Nodos explorados: " + nodosExplorados);
            System.out.println("Mejor solución parcial encontrada (h = " + actual.getHeuristica() + "):");
            actual.mostrar();
        }
    }





    public static void aEstrella(Tablero inicial) {
        PriorityQueue<Tablero> cola = new PriorityQueue<>(
                Comparator.comparingDouble(t -> t.getHeuristica() + t.getCosto())
        );
        Set<String> visitados = new HashSet<>();
        cola.add(inicial);
        int nodosExplorados = 0; // Contador de nodos explorados


        while (!cola.isEmpty()) {
            Tablero actual = cola.poll();
            nodosExplorados++; // Incrementar contador

            if (actual.esObjetivo()) {
                System.out.println("\n¡Objetivo alcanzado con A*!\nPasos: " + actual.getCosto());
                System.out.println("Nodos explorados: " + nodosExplorados);
                System.out.println("Movimientos: " + String.join(", ", actual.getMovimientos()));
                actual.mostrar();
                return;
            }

            String hash = actual.getPosRey().getX() + "," + actual.getPosRey().getY() + "," +
                    actual.getPosHueco().getX() + "," + actual.getPosHueco().getY();

            if (visitados.contains(hash)) continue;
            visitados.add(hash);

            for (Tablero sucesor : actual.generarSucesores()) {
                cola.add(sucesor);
            }
        }

        System.out.println("\nNo se encontró solución con A*.");
    }
}
