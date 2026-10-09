import java.util.Scanner;

/** Capa de aplicación: solo interactúa con la lista mediante su interfaz pública. */
public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final ListaReproduccion lista = new ListaReproduccion();

    public static void main(String[] args) {
        int opcion;
        do {
            imprimirMenu();
            opcion = leerEntero("Opción: ");
            ejecutar(opcion);
        } while (opcion != 13);
    }

    private static void imprimirMenu() {
        System.out.println("\n===== GESTOR DE LISTA DE REPRODUCCIÓN =====");
        System.out.println(" 1. Agregar canción al inicio");
        System.out.println(" 2. Agregar canción al final");
        System.out.println(" 3. Buscar canción por id");
        System.out.println(" 4. Seleccionar canción por id (hacerla actual)");
        System.out.println(" 5. Eliminar canción por id");
        System.out.println(" 6. Eliminar la canción actual");
        System.out.println(" 7. Consultar canción actual");
        System.out.println(" 8. Avanzar a la siguiente");
        System.out.println(" 9. Retroceder a la anterior");
        System.out.println("10. Mostrar lista completa (ambos sentidos)");
        System.out.println("11. Cantidad de canciones");
        System.out.println("12. Simular reproducción de k canciones");
        System.out.println("13. Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1: agregar(true); break;
            case 2: agregar(false); break;
            case 3: {
                int id = leerEntero("Id a buscar: ");
                Cancion c = lista.buscar(id);
                System.out.println(c == null ? "No existe una canción con id " + id + "." : "Encontrada: " + c);
                break;
            }
            case 4: {
                int id = leerEntero("Id a seleccionar: ");
                System.out.println(lista.seleccionar(id)
                        ? "Canción actual: " + lista.getActual()
                        : "No existe una canción con id " + id + ". La lista no cambió.");
                break;
            }
            case 5: {
                int id = leerEntero("Id a eliminar: ");
                System.out.println(lista.eliminarPorId(id)
                        ? "Canción eliminada."
                        : "No existe una canción con id " + id + ". La lista no cambió.");
                mostrarActualTrasCambio();
                break;
            }
            case 6: {
                Cancion eliminada = lista.eliminarActual();
                System.out.println(eliminada == null ? "La lista está vacía." : "Eliminada: " + eliminada);
                mostrarActualTrasCambio();
                break;
            }
            case 7: {
                Cancion c = lista.getActual();
                System.out.println(c == null ? "La lista está vacía." : "Canción actual: " + c);
                break;
            }
            case 8: {
                Cancion c = lista.avanzar();
                System.out.println(c == null ? "La lista está vacía." : "Ahora: " + c);
                break;
            }
            case 9: {
                Cancion c = lista.retroceder();
                System.out.println(c == null ? "La lista está vacía." : "Ahora: " + c);
                break;
            }
            case 10:
                System.out.println("--- Adelante (inicio → fin) ---");
                System.out.print(lista.mostrarAdelante());
                System.out.println("--- Atrás (fin → inicio) ---");
                System.out.print(lista.mostrarAtras());
                break;
            case 11:
                System.out.println("Cantidad de canciones: " + lista.cantidad());
                break;
            case 12: {
                int k = leerEntero("Cantidad k (entero positivo): ");
                if (k <= 0) System.out.println("k debe ser un entero positivo.");
                else System.out.print(lista.reproducir(k));
                break;
            }
            case 13:
                System.out.println("¡Hasta luego!");
                break;
            default:
                System.out.println("Opción inválida. Elige un número del 1 al 13.");
        }
    }

    private static void agregar(boolean alInicio) {
        String titulo = leerTexto("Título: ");
        String artista = leerTexto("Artista: ");
        int duracion = leerEntero("Duración en segundos (>0): ");
        try {
            Cancion c = alInicio ? lista.agregarAlInicio(titulo, artista, duracion)
                                 : lista.agregarAlFinal(titulo, artista, duracion);
            System.out.println("Agregada: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("No se agregó la canción: " + e.getMessage());
        }
    }

    private static void mostrarActualTrasCambio() {
        Cancion c = lista.getActual();
        System.out.println(c == null ? "(La lista quedó vacía)" : "Canción actual: " + c);
    }

    /** Nunca lanza excepción por texto no numérico: repite hasta recibir un entero. */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!in.hasNextLine()) return 13; // fin de entrada → salir limpio
            String linea = in.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: escribe un número entero.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return in.hasNextLine() ? in.nextLine() : "";
    }
}
