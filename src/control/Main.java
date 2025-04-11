package control;

import model.Tablero;

import java.io.IOException;

public class Main {

    public static void escaladaSimple(Tablero t){

        // Ejecutar algoritmo de Escalada Simple
        System.out.println("\n--- Algoritmo de Escalada Simple ---");
        Tablero tablero2 = t.clonar(); // Crear una copia para el segundo algoritmo

        long tiempoInicio = System.currentTimeMillis(); //Comienza el contador
        Solucionador.escaladaSimple(tablero2);
        long tiempoFin = System.currentTimeMillis();

        System.out.println("\nTiempo empleado (Escalada Simple): " + (tiempoFin - tiempoInicio) + " ms");//Anoto el tiempo
    }


    public static void aEstrella(Tablero t){
        // Ejecutar algoritmo A*
        System.out.println("\n--- Algoritmo A* ---");
        Tablero tablero2 = t.clonar(); // Crear una copia para el segundo algoritmo

        long tiempoInicio = System.currentTimeMillis(); //Comienza el contador
        Solucionador.aEstrella(tablero2);
        long tiempoFin = System.currentTimeMillis();

        System.out.println("\nTiempo empleado (A*): " + (tiempoFin - tiempoInicio) + " ms");//Anoto el tiempo
    }



    public static void main(String[] args) throws IOException {

        //VERSION SENCILLA PARA PRUEBAS


        for(int i=1;i<11;i++){
            Tablero tablero = new Tablero();
            tablero.cargarDesdeArchivo("./files/PASOALREY"+i+".TXT");
            System.out.println("----------------------------------------------------");
            System.out.println("Tablero PASOALREY"+i);
            escaladaSimple(tablero);
            aEstrella(tablero);
            //tablero.mostrar();
        }

        //Cargo el archivo y lo muestro
//        tablero.cargarDesdeArchivo("./files/PASOALREY3.TXT");
//        System.out.println("Tablero inicial:");
//        tablero.mostrar();





    }
}