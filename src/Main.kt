import kotlin.math.sqrt

// CONSTANTES
const val CANTIDAD = 100      // Cantidad exacta de números que se almacenan
const val VALOR_MIN = 1       // Mínimo para números aleatorios
const val VALOR_MAX = 100     // Máximo para números aleatorios

// PROGRAMA PRINCIPAL
fun main() {
    var numeros: List<Int> = emptyList()
    var salir = false

    while (!salir) {
        mostrarMenu()
        // Si la entrada se cierra (null), se sale para evitar un bucle infinito
        when (readlnOrNull()?.trim() ?: "8") {
            "1" -> {
                numeros = generarNumerosAleatorios()
                println("Se han generado $CANTIDAD números aleatorios entre $VALOR_MIN y $VALOR_MAX.")
            }
            "2" -> {
                val introducidos = introducirNumerosManualmente()
                if (introducidos != null) {
                    numeros = introducidos
                    println("Se han almacenado $CANTIDAD números correctamente.")
                } else {
                    println("Introducción cancelada. Se mantienen los datos anteriores.")
                }
            }
            "3" -> if (hayDatos(numeros)) mostrarNumeros(numeros)
            "4" -> if (hayDatos(numeros)) mostrarMedia(numeros)
            "5" -> if (hayDatos(numeros)) mostrarModa(numeros)
            "6" -> if (hayDatos(numeros)) mostrarDesviacionTipica(numeros)
            "7" -> if (hayDatos(numeros)) mostrarTodasLasEstadisticas(numeros)
            "8" -> {
                println("Fin del programa. ¡Hasta pronto!")
                salir = true
            }
            else -> println("Opción no válida. Introduzca un número del 1 al 8.")
        }
    }
}

// MENÚ
fun mostrarMenu() {
    println()
    println("===== ESTADÍSTICAS CON KOTLIN =====")
    println("1. Generar $CANTIDAD números aleatorios")
    println("2. Introducir $CANTIDAD números manualmente")
    println("3. Mostrar números")
    println("4. Calcular media")
    println("5. Calcular moda")
    println("6. Calcular desviación típica")
    println("7. Calcular todas las estadísticas")
    println("8. Salir")
    print("Seleccione una opción: ")
}

// OBTENCIÓN DE DATOS
fun generarNumerosAleatorios(): List<Int> =
    List(CANTIDAD) { (VALOR_MIN..VALOR_MAX).random() }

/**
 * Pide los 100 números uno a uno. Si el usuario escribe algo no numérico
 * se le vuelve a pedir. Escribiendo "cancelar" se aborta y devuelve null.
 */

fun introducirNumerosManualmente(): List<Int>? {
    val lista = mutableListOf<Int>()
    println("Introduzca $CANTIDAD números enteros (escriba 'cancelar' para abortar).")
    while (lista.size < CANTIDAD) {
        print("Número ${lista.size + 1}/$CANTIDAD: ")
        val entrada = readlnOrNull()?.trim() ?: return null
        if (entrada.equals("cancelar", ignoreCase = true)) return null

        val numero = entrada.toIntOrNull()
        if (numero == null) {
            println("  Error: '$entrada' no es un número entero válido. Inténtelo de nuevo.")
        } else {
            lista.add(numero)
        }
    }
    return lista.toList()
}

// VALIDACIONES
fun hayDatos(numeros: List<Int>): Boolean {
    if (numeros.size != CANTIDAD) {
        println("No hay datos cargados. Use la opción 1 o la 2 primero.")
        return false
    }
    return true
}

// CÁLCULOS
fun calcularMedia(numeros: List<Int>): Double =
    numeros.sum().toDouble() / numeros.size

/** Devuelve todos los valores con mayor frecuencia (puede haber varias modas).
 *  Si todos los valores aparecen una sola vez devuelve una lista vacía (amodal). */

fun calcularModa(numeros: List<Int>): List<Int> {
    val frecuencias = numeros.groupingBy { it }.eachCount()
    val maxFrecuencia = frecuencias.values.max()
    if (maxFrecuencia == 1) return emptyList()
    return frecuencias.filter { it.value == maxFrecuencia }.keys.sorted()
}

/** Desviación típica poblacional: raíz de la varianza (dividiendo entre N). */
fun calcularDesviacionTipica(numeros: List<Int>): Double {
    val media = calcularMedia(numeros)
    val varianza = numeros.sumOf { (it - media) * (it - media) } / numeros.size
    return sqrt(varianza)
}

// PRESENTACIÓN DE RESULTADOS
fun mostrarNumeros(numeros: List<Int>) {
    println("Números almacenados (${numeros.size}):")
    numeros.chunked(10).forEach { fila ->
        println(fila.joinToString(" ") { it.toString().padStart(4) })
    }
}

fun mostrarMedia(numeros: List<Int>) {
    println("Media: %.2f".format(calcularMedia(numeros)))
}

fun mostrarModa(numeros: List<Int>) {
    val moda = calcularModa(numeros)
    if (moda.isEmpty()) {
        println("Moda: no existe (todos los valores aparecen una sola vez).")
    } else {
        val frecuencia = numeros.count { it == moda.first() }
        val etiqueta = if (moda.size == 1) "Moda" else "Modas (distribución multimodal)"
        println("$etiqueta: ${moda.joinToString(", ")}  [aparece $frecuencia veces]")
    }
}

fun mostrarDesviacionTipica(numeros: List<Int>) {
    println("Desviación típica: %.2f".format(calcularDesviacionTipica(numeros)))
}

fun mostrarTodasLasEstadisticas(numeros: List<Int>) {
    println("----- ESTADÍSTICAS COMPLETAS -----")
    println("Cantidad de datos: ${numeros.size}")
    println("Mínimo: ${numeros.min()}   Máximo: ${numeros.max()}")
    mostrarMedia(numeros)
    mostrarModa(numeros)
    mostrarDesviacionTipica(numeros)
}