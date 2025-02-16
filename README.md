# PASO AL REY
El proyecto consiste en implementar un sistema relacionado con el movimiento de las figuras de ajedrez situadas en el tablero.

Objetivo: conseguir los movimientos de las figuras hasta llevar a la figura del rey al objetivo.

# Entrada
Diferentes tableros 5x5 en formato texto, uno por cada ejecución

# Salida
1. **Solución:** Indicar la secuencia de movimientos
2. **Nº nodos:** nº de movimientos que ha probado
3. **Tiempo:** en segundos

# Requisitos
- Tablero de 5x5
- Piezas: 
  - M: Muro
  - H: Hueco
  - R: Rey
  - O: Objetivo
  - A: Alfil
  - T: Torre
  
- Las fichas se mueven al hueco, no se mueven entre ellos.
- Si hay un Muro no se puede transpasar
- Puede haber solución o puede que no haya solución.
- Las figuras se mueven como en el Ajedrez

# Movimiento
- El que se mueve es el hueco

# Ejemplo
Entrada:
1.    1, 2, 3, 4, 5
2. a: T, T, T, A, T,
2. b: R, H, T, A, T,
3. c: A, A, M, M, O,
4. d: M, M, M, M, M,
5. e: T, T, T, T, T,

Salida:  
1. Solución: b1, c2, b2, b3, c2, ..   
2. Nº nodos del árbol de búsqueda:
3. Tiempo: 60 segundos



# Primeros Pasos
1. Leer fichero
2. Pasarlo a una matriz
3. Implementar el movimiento
