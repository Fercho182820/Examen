import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ListaReproduccion lista = new ListaReproduccion();

    public static void main(String[] args) {
        int opcion = 0;
        while (opcion != 13) {
            menu();
            opcion = leerEntero("Opcion: ");

            if (opcion == 1) {
                agregar(true);
            } else if (opcion == 2) {
                agregar(false);
            } else if (opcion == 3) {
                int id = leerEntero("Id a buscar: ");
                Cancion c = lista.buscar(id);
                if (c == null) {
                    System.out.println("No existe una cancion con ese id");
                } else {
                    System.out.println("Encontrada: " + c);
                }
            } else if (opcion == 4) {
                int id = leerEntero("Id a seleccionar: ");
                if (lista.seleccionar(id)) {
                    System.out.println("Cancion actual: " + lista.getActual());
                } else {
                    System.out.println("No existe una cancion con ese id");
                }
            } else if (opcion == 5) {
                int id = leerEntero("Id a eliminar: ");
                if (lista.eliminarPorId(id)) {
                    System.out.println("Cancion eliminada");
                    mostrarActual();
                } else {
                    System.out.println("No existe una cancion con ese id");
                }
            } else if (opcion == 6) {
                Cancion c = lista.eliminarActual();
                if (c == null) {
                    System.out.println("La lista esta vacia");
                } else {
                    System.out.println("Eliminada: " + c);
                    mostrarActual();
                }
            } else if (opcion == 7) {
                mostrarActual();
            } else if (opcion == 8) {
                Cancion c = lista.avanzar();
                if (c == null) {
                    System.out.println("La lista esta vacia");
                } else {
                    System.out.println("Ahora: " + c);
                }
            } else if (opcion == 9) {
                Cancion c = lista.retroceder();
                if (c == null) {
                    System.out.println("La lista esta vacia");
                } else {
                    System.out.println("Ahora: " + c);
                }
            } else if (opcion == 10) {
                System.out.println("--- De inicio a fin ---");
                System.out.println(lista.mostrarAdelante());
                System.out.println("--- De fin a inicio ---");
                System.out.println(lista.mostrarAtras());
            } else if (opcion == 11) {
                System.out.println("Cantidad de canciones: " + lista.getCantidad());
            } else if (opcion == 12) {
                int k = leerEntero("Cuantas canciones reproducir (k): ");
                if (k <= 0) {
                    System.out.println("k debe ser mayor que cero");
                } else {
                    System.out.println(lista.reproducir(k));
                }
            } else if (opcion == 13) {
                System.out.println("Adios");
            } else {
                System.out.println("Opcion invalida, elige de 1 a 13");
            }
        }
    }

    static void menu() {
        System.out.println();
        System.out.println("===== LISTA DE REPRODUCCION =====");
        System.out.println("1. Agregar al inicio");
        System.out.println("2. Agregar al final");
        System.out.println("3. Buscar por id");
        System.out.println("4. Seleccionar por id");
        System.out.println("5. Eliminar por id");
        System.out.println("6. Eliminar la cancion actual");
        System.out.println("7. Consultar la cancion actual");
        System.out.println("8. Avanzar");
        System.out.println("9. Retroceder");
        System.out.println("10. Mostrar lista completa");
        System.out.println("11. Cantidad de canciones");
        System.out.println("12. Simular reproduccion");
        System.out.println("13. Salir");
    }

    static void agregar(boolean alInicio) {
        System.out.print("Titulo: ");
        String titulo = sc.nextLine();
        System.out.print("Artista: ");
        String artista = sc.nextLine();
        int duracion = leerEntero("Duracion en segundos: ");
        try {
            Cancion c;
            if (alInicio) {
                c = lista.agregarAlInicio(titulo, artista, duracion);
            } else {
                c = lista.agregarAlFinal(titulo, artista, duracion);
            }
            System.out.println("Agregada: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("No se agrego: " + e.getMessage());
        }
    }

    static void mostrarActual() {
        Cancion c = lista.getActual();
        if (c == null) {
            System.out.println("La lista esta vacia");
        } else {
            System.out.println("Cancion actual: " + c);
        }
    }

    // repite hasta que escriban un numero entero
    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!sc.hasNextLine()) {
                return 13; // se acabo la entrada, salir
            }
            String texto = sc.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero entero valido");
            }
        }
    }
}
