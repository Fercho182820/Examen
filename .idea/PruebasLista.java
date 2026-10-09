public class PruebasLista {
    static int total = 0;
    static int correctas = 0;

    public static void main(String[] args) {
        // ---------- lista vacia ----------
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        probar("Vacia: estado inicial", ini, "ninguna", "cant=0 | actual=null | lista=(vacia)", estado(l), l);
        probar("Vacia: avanzar", ini, "avanzar()", "null", "" + l.avanzar(), l);
        probar("Vacia: retroceder", ini, "retroceder()", "null", "" + l.retroceder(), l);
        probar("Vacia: eliminarActual", ini, "eliminarActual()", "null", "" + l.eliminarActual(), l);
        probar("Vacia: buscar", ini, "buscar(1)", "null", "" + l.buscar(1), l);
        probar("Vacia: eliminarPorId", ini, "eliminarPorId(1)", "false", "" + l.eliminarPorId(1), l);
        probar("Vacia: reproducir", ini, "reproducir(3)", "La lista esta vacia, no hay nada que reproducir\n"
                .trim(), l.reproducir(3).trim(), l);
        probar("Vacia: mostrar", ini, "mostrarAdelante()", "La lista esta vacia", l.mostrarAdelante(), l);

        // ---------- primera insercion ----------
        l = new ListaReproduccion();
        ini = estado(l);
        l.agregarAlFinal("Uno", "X", 60);
        probar("Primera cancion", ini, "agregarAlFinal(Uno)", "cant=1 | actual=1 | lista=1", estado(l), l);

        // ---------- un solo nodo ----------
        ini = estado(l);
        l.avanzar();
        probar("Un nodo: avanzar", ini, "avanzar()", "cant=1 | actual=1 | lista=1", estado(l), l);
        l.retroceder();
        probar("Un nodo: retroceder", ini, "retroceder()", "cant=1 | actual=1 | lista=1", estado(l), l);

        // ---------- inserciones inicio y final ----------
        l = lista(1);
        ini = estado(l);
        l.agregarAlFinal("T2", "A", 10);
        l.agregarAlInicio("T3", "A", 10);
        probar("Inserciones inicio y final", ini, "agregarAlFinal(T2), agregarAlInicio(T3)",
                "cant=3 | actual=1 | lista=3,1,2", estado(l), l);

        // ---------- recorridos con 5 canciones ----------
        l = lista(3);
        l.agregarAlInicio("T4", "A", 10);
        l.agregarAlInicio("T5", "A", 10);
        ini = estado(l);
        probar("Recorrido hacia adelante", ini, "idsAdelante()", "5,4,1,2,3", l.idsAdelante(), l);
        probar("Recorrido hacia atras", ini, "idsAtras()", "3,2,1,4,5", l.idsAtras(), l);
        probar("Mostrar visita cada cancion una vez", ini, "mostrarAdelante() cuenta lineas",
                "5", "" + l.mostrarAdelante().split("\n").length, l);

        // ---------- navegacion circular ----------
        l = lista(5);
        l.seleccionar(5);
        ini = estado(l);
        l.avanzar();
        probar("Avanzar desde la ultima", ini, "avanzar()", "cant=5 | actual=1 | lista=1,2,3,4,5", estado(l), l);
        ini = estado(l);
        l.retroceder();
        probar("Retroceder desde la primera", ini, "retroceder()", "cant=5 | actual=5 | lista=1,2,3,4,5", estado(l), l);

        // ---------- eliminaciones ----------
        l = lista(5);
        l.seleccionar(3);
        ini = estado(l);
        l.eliminarPorId(1);
        probar("Eliminar el primero (la actual se conserva)", ini, "eliminarPorId(1)",
                "cant=4 | actual=3 | lista=2,3,4,5", estado(l), l);

        l = lista(5);
        ini = estado(l);
        l.eliminarPorId(5);
        probar("Eliminar el ultimo", ini, "eliminarPorId(5)", "cant=4 | actual=1 | lista=1,2,3,4", estado(l), l);

        l = lista(5);
        ini = estado(l);
        l.eliminarPorId(3);
        probar("Eliminar uno intermedio", ini, "eliminarPorId(3)", "cant=4 | actual=1 | lista=1,2,4,5", estado(l), l);

        // ---------- eliminar la actual ----------
        l = lista(5);
        l.seleccionar(3);
        ini = estado(l);
        l.eliminarActual();
        probar("Eliminar actual intermedia", ini, "eliminarActual()", "cant=4 | actual=4 | lista=1,2,4,5", estado(l), l);

        l = lista(5);
        l.seleccionar(5);
        ini = estado(l);
        l.eliminarActual();
        probar("Eliminar actual que es la ultima", ini, "eliminarActual()", "cant=4 | actual=1 | lista=1,2,3,4", estado(l), l);

        l = lista(5);
        ini = estado(l);
        l.eliminarActual();
        probar("Eliminar actual que es la primera", ini, "eliminarActual()", "cant=4 | actual=2 | lista=2,3,4,5", estado(l), l);

        // ---------- unica cancion ----------
        l = lista(1);
        ini = estado(l);
        l.eliminarActual();
        probar("Eliminar la unica cancion", ini, "eliminarActual()", "cant=0 | actual=null | lista=(vacia)", estado(l), l);

        l = lista(1);
        l.eliminarPorId(1);
        probar("Eliminar la unica cancion por id", ini, "eliminarPorId(1)", "cant=0 | actual=null | lista=(vacia)", estado(l), l);

        l.agregarAlInicio("Nueva", "Z", 30);
        probar("Usar la lista despues de vaciarla", "lista vacia", "agregarAlInicio(Nueva)",
                "cant=1 | actual=2 | lista=2", estado(l), l);

        // ---------- id inexistente ----------
        l = lista(3);
        ini = estado(l);
        probar("Buscar id inexistente", ini, "buscar(99)", "null", "" + l.buscar(99), l);
        probar("Seleccionar id inexistente", ini, "seleccionar(99)", "false", "" + l.seleccionar(99), l);
        probar("Eliminar id inexistente", ini, "eliminarPorId(99)", "false y " + ini, l.eliminarPorId(99) + " y " + estado(l), l);

        // ---------- reproduccion con k mayor que n ----------
        l = lista(5);
        ini = estado(l);
        String salida = l.reproducir(12);
        probar("Reproducir k=12 con 5 canciones: orden", ini, "reproducir(12)", "1,2,3,4,5,1,2,3,4,5,1,2", sacarIds(salida), l);
        probar("Reproducir k=12: actual final", ini, "reproducir(12)", "cant=5 | actual=3 | lista=1,2,3,4,5", estado(l), l);

        // ---------- entradas invalidas ----------
        ListaReproduccion m = lista(2);
        ini = estado(m);
        String bien = "excepcion y " + ini;
        try {
            m.agregarAlFinal("a", "b", 0);
            probar("Duracion 0", ini, "agregarAlFinal(a,b,0)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("Duracion 0", ini, "agregarAlFinal(a,b,0)", bien, "excepcion y " + estado(m), m);
        }
        try {
            m.agregarAlInicio("a", "b", -5);
            probar("Duracion negativa", ini, "agregarAlInicio(a,b,-5)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("Duracion negativa", ini, "agregarAlInicio(a,b,-5)", bien, "excepcion y " + estado(m), m);
        }
        try {
            m.agregarAlFinal("  ", "b", 10);
            probar("Titulo vacio", ini, "agregarAlFinal('  ',b,10)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("Titulo vacio", ini, "agregarAlFinal('  ',b,10)", bien, "excepcion y " + estado(m), m);
        }
        try {
            m.agregarAlFinal("a", null, 10);
            probar("Artista nulo", ini, "agregarAlFinal(a,null,10)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("Artista nulo", ini, "agregarAlFinal(a,null,10)", bien, "excepcion y " + estado(m), m);
        }
        try {
            m.reproducir(0);
            probar("k = 0", ini, "reproducir(0)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("k = 0", ini, "reproducir(0)", bien, "excepcion y " + estado(m), m);
        }
        try {
            m.reproducir(-3);
            probar("k negativo", ini, "reproducir(-3)", bien, "sin excepcion", m);
        } catch (IllegalArgumentException e) {
            probar("k negativo", ini, "reproducir(-3)", bien, "excepcion y " + estado(m), m);
        }
        // los ids fallidos no se gastan: el siguiente valido debe ser el 3
        m.agregarAlFinal("ok", "ok", 10);
        probar("Los ids no se gastan con datos invalidos", ini, "agregarAlFinal valido",
                "cant=3 | actual=1 | lista=1,2,3", estado(m), m);

        System.out.println();
        System.out.println("RESULTADO: " + correctas + " de " + total + " pruebas correctas");
    }

    // crea una lista con n canciones (ids 1..n) agregadas al final
    static ListaReproduccion lista(int n) {
        ListaReproduccion l = new ListaReproduccion();
        for (int i = 1; i <= n; i++) {
            l.agregarAlFinal("T" + i, "A" + i, 100 + i);
        }
        return l;
    }

    static String estado(ListaReproduccion l) {
        String actual = "null";
        if (l.getActual() != null) {
            actual = "" + l.getActual().getId();
        }
        return "cant=" + l.getCantidad() + " | actual=" + actual + " | lista=" + l.idsAdelante();
    }

    // saca los ids de las lineas "Reproduciendo: #3 | ..."
    static String sacarIds(String texto) {
        String ids = "";
        String[] lineas = texto.split("\n");
        for (int i = 0; i < lineas.length; i++) {
            int a = lineas[i].indexOf('#');
            int b = lineas[i].indexOf(' ', a);
            if (i > 0) {
                ids += ",";
            }
            ids += lineas[i].substring(a + 1, b);
        }
        return ids;
    }

    static void probar(String nombre, String inicial, String operacion, String esperado, String obtenido, ListaReproduccion l) {
        total++;
        boolean enlaces = l.verificarEnlaces();
        boolean paso = esperado.equals(obtenido) && enlaces;
        if (paso) {
            correctas++;
        }
        System.out.println();
        System.out.println("[" + (paso ? "OK" : "FALLA") + "] " + nombre);
        System.out.println("  Estado inicial: " + inicial);
        System.out.println("  Operacion:      " + operacion);
        System.out.println("  Esperado:       " + esperado);
        System.out.println("  Obtenido:       " + obtenido);
        System.out.println("  Enlaces bien:   " + enlaces + " | recorrido atras: " + l.idsAtras());
    }
}
