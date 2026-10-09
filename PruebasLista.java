/**
 * Pruebas reproducibles sin dependencias externas.
 * Cada caso imprime: estado inicial, operación, esperado y obtenido, y además
 * verifica automáticamente la integridad de los enlaces tras la operación.
 */
public class PruebasLista {
    static int total = 0, ok = 0;

    public static void main(String[] args) {
        vacia();
        primeraCancion();
        unSoloNodo();
        insercionesInicioFinal();
        recorridoCincoCanciones();
        navegacionCircular();
        eliminaciones();
        eliminarActual();
        unicaCancion();
        idInexistente();
        reproduccionMayorQueN();
        entradasInvalidas();
        System.out.println("\n==============================");
        System.out.println("RESULTADO: " + ok + " / " + total + " pruebas correctas");
        if (ok != total) System.exit(1);
    }

    // ------------------------------------------------------------ utilidades

    static ListaReproduccion crear(int n) { // ids 1..n insertados al final; actual = 1
        ListaReproduccion l = new ListaReproduccion();
        for (int i = 1; i <= n; i++) l.agregarAlFinal("T" + i, "A" + i, 100 + i);
        return l;
    }

    static String estado(ListaReproduccion l) {
        Cancion a = l.getActual();
        return "lista=" + l.idsAdelante() + " | cant=" + l.cantidad()
                + " | actual=" + (a == null ? "null" : a.getId());
    }

    static void caso(String nombre, String inicial, String operacion,
                     String esperado, String obtenido, ListaReproduccion l) {
        total++;
        boolean integra = l.verificarIntegridad();
        boolean paso = esperado.equals(obtenido) && integra;
        if (paso) ok++;
        System.out.println("\n[" + (paso ? "PASA" : "FALLA") + "] " + nombre);
        System.out.println("  Estado inicial : " + inicial);
        System.out.println("  Operación      : " + operacion);
        System.out.println("  Esperado       : " + esperado);
        System.out.println("  Obtenido       : " + obtenido);
        System.out.println("  Enlaces íntegros: " + integra
                + " | atrás=" + l.idsAtras());
    }

    static String idsDe(String texto) { // extrae los #id de la salida de reproducir()
        StringBuilder sb = new StringBuilder();
        for (String linea : texto.split("\n")) {
            int i = linea.indexOf('#');
            int j = linea.indexOf(' ', i);
            if (i >= 0) { if (sb.length() > 0) sb.append(','); sb.append(linea, i + 1, j); }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------ casos

    static void vacia() {
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        caso("Lista vacía: estado", ini, "(ninguna)", "lista=(vacía) | cant=0 | actual=null", estado(l), l);
        caso("Lista vacía: avanzar", ini, "avanzar()", "null", String.valueOf(l.avanzar()), l);
        caso("Lista vacía: retroceder", ini, "retroceder()", "null", String.valueOf(l.retroceder()), l);
        caso("Lista vacía: eliminarActual", ini, "eliminarActual()", "null", String.valueOf(l.eliminarActual()), l);
        caso("Lista vacía: buscar(1)", ini, "buscar(1)", "null", String.valueOf(l.buscar(1)), l);
        caso("Lista vacía: reproducir(3)", ini, "reproducir(3)",
                "La lista está vacía: no hay nada que reproducir.", l.reproducir(3), l);
        caso("Lista vacía: mostrar", ini, "mostrarAdelante()", "La lista está vacía.", l.mostrarAdelante(), l);
    }

    static void primeraCancion() {
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        l.agregarAlFinal("Uno", "X", 60);
        caso("Primera inserción", ini, "agregarAlFinal(Uno)",
                "lista=1 | cant=1 | actual=1", estado(l), l);
    }

    static void unSoloNodo() {
        ListaReproduccion l = crear(1);
        String ini = estado(l);
        l.avanzar();
        caso("1 nodo: avanzar", ini, "avanzar()", "lista=1 | cant=1 | actual=1", estado(l), l);
        l.retroceder();
        caso("1 nodo: retroceder", ini, "retroceder()", "lista=1 | cant=1 | actual=1", estado(l), l);
    }

    static void insercionesInicioFinal() {
        ListaReproduccion l = crear(1);
        String ini = estado(l);
        l.agregarAlFinal("T2", "A", 10);
        l.agregarAlInicio("T3", "A", 10);
        caso("Inserciones inicio/final (actual no cambia)", ini,
                "agregarAlFinal(T2); agregarAlInicio(T3)",
                "lista=3,1,2 | cant=3 | actual=1", estado(l), l);
    }

    static void recorridoCincoCanciones() {
        ListaReproduccion l = crear(3);                // 1,2,3
        l.agregarAlInicio("T4", "A", 10);              // 4,1,2,3
        l.agregarAlInicio("T5", "A", 10);              // 5,4,1,2,3
        String ini = estado(l);
        caso("Recorrido adelante (5 canciones)", ini, "idsAdelante()", "5,4,1,2,3", l.idsAdelante(), l);
        caso("Recorrido atrás (5 canciones)", ini, "idsAtras()", "3,2,1,4,5", l.idsAtras(), l);
        String txt = l.mostrarAdelante();
        caso("mostrarAdelante visita cada canción una vez", ini, "mostrarAdelante() → nº de líneas",
                "5", String.valueOf(txt.split("\n").length), l);
    }

    static void navegacionCircular() {
        ListaReproduccion l = crear(5);
        l.seleccionar(5);
        String ini = estado(l);
        l.avanzar();
        caso("Avance desde la última hacia la primera", ini, "avanzar()",
                "lista=1,2,3,4,5 | cant=5 | actual=1", estado(l), l);
        ini = estado(l);
        l.retroceder();
        caso("Retroceso desde la primera hacia la última", ini, "retroceder()",
                "lista=1,2,3,4,5 | cant=5 | actual=5", estado(l), l);
    }

    static void eliminaciones() {
        ListaReproduccion l = crear(5); l.seleccionar(3);
        String ini = estado(l);
        l.eliminarPorId(1);
        caso("Eliminar el primero (actual distinta se conserva)", ini, "eliminarPorId(1)",
                "lista=2,3,4,5 | cant=4 | actual=3", estado(l), l);

        l = crear(5);
        ini = estado(l);
        l.eliminarPorId(5);
        caso("Eliminar el último", ini, "eliminarPorId(5)",
                "lista=1,2,3,4 | cant=4 | actual=1", estado(l), l);

        l = crear(5);
        ini = estado(l);
        l.eliminarPorId(3);
        caso("Eliminar un intermedio", ini, "eliminarPorId(3)",
                "lista=1,2,4,5 | cant=4 | actual=1", estado(l), l);
    }

    static void eliminarActual() {
        ListaReproduccion l = crear(5); l.seleccionar(3);
        String ini = estado(l);
        l.eliminarActual();
        caso("Eliminar la actual (intermedia): pasa a la que le seguía", ini, "eliminarActual()",
                "lista=1,2,4,5 | cant=4 | actual=4", estado(l), l);

        l = crear(5); l.seleccionar(5);
        ini = estado(l);
        l.eliminarActual();
        caso("Eliminar la actual siendo la última: da la vuelta al inicio", ini, "eliminarActual()",
                "lista=1,2,3,4 | cant=4 | actual=1", estado(l), l);

        l = crear(5);
        ini = estado(l);
        l.eliminarActual();
        caso("Eliminar la actual siendo la primera", ini, "eliminarActual()",
                "lista=2,3,4,5 | cant=4 | actual=2", estado(l), l);
    }

    static void unicaCancion() {
        ListaReproduccion l = crear(1);
        String ini = estado(l);
        l.eliminarActual();
        caso("Eliminar la única canción (eliminarActual)", ini, "eliminarActual()",
                "lista=(vacía) | cant=0 | actual=null", estado(l), l);
        l = crear(1);
        l.eliminarPorId(1);
        caso("Eliminar la única canción (por id)", ini, "eliminarPorId(1)",
                "lista=(vacía) | cant=0 | actual=null", estado(l), l);
        // y se puede volver a usar
        l.agregarAlInicio("Nueva", "Z", 30);
        caso("Reutilizar la lista tras vaciarla", "lista=(vacía)", "agregarAlInicio(Nueva)",
                "lista=2 | cant=1 | actual=2", estado(l), l); // el id 1 ya se usó: los ids nunca se reutilizan
    }

    static void idInexistente() {
        ListaReproduccion l = crear(3);
        String ini = estado(l);
        caso("Buscar id inexistente", ini, "buscar(99)", "null", String.valueOf(l.buscar(99)), l);
        caso("Seleccionar id inexistente", ini, "seleccionar(99)", "false", String.valueOf(l.seleccionar(99)), l);
        caso("Eliminar id inexistente (lista sin cambios)", ini, "eliminarPorId(99)",
                "false | " + ini, l.eliminarPorId(99) + " | " + estado(l), l);
    }

    static void reproduccionMayorQueN() {
        ListaReproduccion l = crear(5);
        String ini = estado(l);
        String salida = l.reproducir(12);
        caso("Reproducción k=12 con n=5 (más de una vuelta): canciones mostradas", ini, "reproducir(12)",
                "1,2,3,4,5,1,2,3,4,5,1,2", idsDe(salida), l);
        caso("Reproducción k=12: actual final", ini, "reproducir(12)",
                "lista=1,2,3,4,5 | cant=5 | actual=3", estado(l), l);
    }

    static void entradasInvalidas() {
        ListaReproduccion l = crear(2);
        String ini = estado(l);
        caso("Duración 0", ini, "agregarAlFinal(\"a\",\"b\",0)", "IllegalArgumentException | " + ini,
                intentar(() -> l.agregarAlFinal("a", "b", 0)) + " | " + estado(l), l);
        caso("Duración negativa", ini, "agregarAlInicio(\"a\",\"b\",-5)", "IllegalArgumentException | " + ini,
                intentar(() -> l.agregarAlInicio("a", "b", -5)) + " | " + estado(l), l);
        caso("Título vacío", ini, "agregarAlFinal(\"  \",\"b\",10)", "IllegalArgumentException | " + ini,
                intentar(() -> l.agregarAlFinal("  ", "b", 10)) + " | " + estado(l), l);
        caso("Artista nulo", ini, "agregarAlFinal(\"a\",null,10)", "IllegalArgumentException | " + ini,
                intentar(() -> l.agregarAlFinal("a", null, 10)) + " | " + estado(l), l);
        caso("k = 0", ini, "reproducir(0)", "IllegalArgumentException | " + ini,
                intentar(() -> l.reproducir(0)) + " | " + estado(l), l);
        caso("k negativo", ini, "reproducir(-3)", "IllegalArgumentException | " + ini,
                intentar(() -> l.reproducir(-3)) + " | " + estado(l), l);
        // Un fallo de validación no debe consumir ids: la próxima inserción válida recibe el id 3
        l.agregarAlFinal("ok", "ok", 10);
        caso("Los ids no se consumen en inserciones inválidas", ini, "agregarAlFinal válido",
                "lista=1,2,3 | cant=3 | actual=1", estado(l), l);
    }

    static String intentar(Runnable r) {
        try { r.run(); return "sin excepción"; }
        catch (IllegalArgumentException e) { return "IllegalArgumentException"; }
    }
}
