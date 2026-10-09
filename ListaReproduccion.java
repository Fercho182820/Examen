/**
 * TAD Lista de reproducción implementado con una lista doblemente enlazada circular.
 *
 * Invariantes (se verifican en verificarIntegridad()):
 *  - Vacía: inicio == fin == actual == null y tamanio == 0.
 *  - Un nodo: su anterior y su siguiente son él mismo.
 *  - No vacía: fin.siguiente == inicio e inicio.anterior == fin.
 *  - Para todo nodo n: n.siguiente.anterior == n y n.anterior.siguiente == n.
 *  - actual pertenece a la lista.
 */
public class ListaReproduccion {
    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int tamanio;
    private int siguienteId = 1; // contador incremental: evita buscar duplicados (inserción O(1))

    // ---------------------------------------------------------------- consultas O(1)

    public boolean estaVacia() { return tamanio == 0; }

    public int cantidad() { return tamanio; }

    /** @return la canción actual, o null si la lista está vacía. */
    public Cancion getActual() { return actual == null ? null : actual.cancion; }

    // ---------------------------------------------------------------- inserción O(1)

    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        insertarEntreFinEInicio(nuevo);
        inicio = nuevo; // el nuevo nodo pasa a ser la cabeza
        return nuevo.cancion;
    }

    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        insertarEntreFinEInicio(nuevo);
        fin = nuevo; // el nuevo nodo pasa a ser la cola
        return nuevo.cancion;
    }

    /**
     * Crea el nodo ANTES de tocar la lista y antes de consumir un id:
     * si los datos son inválidos se lanza la excepción y la lista queda intacta.
     */
    private Nodo crearNodo(String titulo, String artista, int duracion) {
        Cancion c = new Cancion(siguienteId, titulo, artista, duracion);
        siguienteId++;
        return new Nodo(c);
    }

    /**
     * Enlaza 'nuevo' entre fin e inicio (el hueco "circular"). Actualiza inicio/fin
     * solo en el caso de lista vacía; el llamador mueve inicio o fin según corresponda.
     */
    private void insertarEntreFinEInicio(Nodo nuevo) {
        if (tamanio == 0) {
            nuevo.siguiente = nuevo;
            nuevo.anterior = nuevo;
            inicio = nuevo;
            fin = nuevo;
            actual = nuevo; // la primera canción insertada es la actual
        } else {
            nuevo.anterior = fin;
            nuevo.siguiente = inicio;
            fin.siguiente = nuevo;
            inicio.anterior = nuevo;
        }
        tamanio++;
    }

    // ---------------------------------------------------------------- navegación O(1)

    /** Avanza a la siguiente canción. @return la nueva actual, o null si está vacía. */
    public Cancion avanzar() {
        if (actual == null) return null;
        actual = actual.siguiente;
        return actual.cancion;
    }

    /** Retrocede a la canción anterior. @return la nueva actual, o null si está vacía. */
    public Cancion retroceder() {
        if (actual == null) return null;
        actual = actual.anterior;
        return actual.cancion;
    }

    // ---------------------------------------------------------------- búsqueda O(n)

    /**
     * Localiza un nodo. Se recorre exactamente 'tamanio' pasos: en una lista circular
     * no existe null que marque el final, por eso la condición de término es el contador.
     */
    private Nodo localizar(int id) {
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (p.cancion.getId() == id) return p;
            p = p.siguiente;
        }
        return null;
    }

    public Cancion buscar(int id) {
        Nodo n = localizar(id);
        return n == null ? null : n.cancion;
    }

    /** @return true si existía y ahora es la actual. */
    public boolean seleccionar(int id) {
        Nodo n = localizar(id);
        if (n == null) return false;
        actual = n;
        return true;
    }

    // ---------------------------------------------------------------- eliminación

    /** O(n): búsqueda O(n) + desenlace O(1). */
    public boolean eliminarPorId(int id) {
        Nodo n = localizar(id);
        if (n == null) return false;
        desenlazar(n);
        return true;
    }

    /** O(1): ya tenemos la referencia, no hay búsqueda. @return la eliminada, o null si vacía. */
    public Cancion eliminarActual() {
        if (actual == null) return null;
        Cancion eliminada = actual.cancion;
        desenlazar(actual);
        return eliminada;
    }

    /**
     * Quita un nodo YA LOCALIZADO. Orden importante: primero se guardan/ajustan las
     * referencias de la lista (inicio, fin, actual) y recién al final se rompen los
     * enlaces del nodo, para no perder el "siguiente" que necesitamos.
     */
    private void desenlazar(Nodo n) {
        if (tamanio == 1) {
            inicio = null;
            fin = null;
            actual = null;
        } else {
            Nodo ant = n.anterior;
            Nodo sig = n.siguiente;
            ant.siguiente = sig;
            sig.anterior = ant;
            if (n == inicio) inicio = sig;
            if (n == fin) fin = ant;
            if (n == actual) actual = sig; // la nueva actual es la que le seguía
        }
        n.anterior = null; // el nodo queda inalcanzable → recolectable por el GC
        n.siguiente = null;
        tamanio--;
    }

    // ---------------------------------------------------------------- recorridos O(n)

    /** Cada canción se visita una sola vez (tamanio pasos), marcando la actual con ▶. */
    public String mostrarAdelante() {
        if (tamanio == 0) return "La lista está vacía.";
        StringBuilder sb = new StringBuilder();
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            sb.append(p == actual ? " ▶ " : "   ").append(p.cancion).append('\n');
            p = p.siguiente;
        }
        return sb.toString();
    }

    public String mostrarAtras() {
        if (tamanio == 0) return "La lista está vacía.";
        StringBuilder sb = new StringBuilder();
        Nodo p = fin;
        for (int i = 0; i < tamanio; i++) {
            sb.append(p == actual ? " ▶ " : "   ").append(p.cancion).append('\n');
            p = p.anterior;
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------- reproducción O(k)

    /**
     * Muestra la actual y avanza, exactamente k veces. Su costo depende de k, no de n:
     * si k > n simplemente da varias vueltas.
     */
    public String reproducir(int k) {
        if (k <= 0) throw new IllegalArgumentException("k debe ser un entero positivo.");
        if (actual == null) return "La lista está vacía: no hay nada que reproducir.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append("♪ Reproduciendo: ").append(actual.cancion).append('\n');
            actual = actual.siguiente;
        }
        return sb.toString();
    }

    // ---------------------------------------------------------------- verificación (para pruebas)

    /** Revisa todos los invariantes de la estructura. O(n). */
    public boolean verificarIntegridad() {
        if (tamanio == 0)
            return inicio == null && fin == null && actual == null;
        if (inicio == null || fin == null || actual == null) return false;
        if (fin.siguiente != inicio || inicio.anterior != fin) return false;

        boolean actualEncontrada = false;
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (p == null || p.siguiente == null || p.anterior == null) return false;
            if (p.siguiente.anterior != p || p.anterior.siguiente != p) return false;
            if (p == actual) actualEncontrada = true;
            if (i == tamanio - 1 && p != fin) return false; // el último paso debe caer en fin
            p = p.siguiente;
        }
        if (p != inicio) return false; // tras 'tamanio' pasos se debe cerrar el círculo
        if (tamanio == 1 && (inicio.siguiente != inicio || inicio.anterior != inicio)) return false;
        return actualEncontrada;
    }

    /** IDs en orden de inicio a fin, ej. "3,1,2". Útil para pruebas. */
    public String idsAdelante() {
        if (tamanio == 0) return "(vacía)";
        StringBuilder sb = new StringBuilder();
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) sb.append(',');
            sb.append(p.cancion.getId());
            p = p.siguiente;
        }
        return sb.toString();
    }

    /** IDs de fin a inicio, siguiendo los enlaces 'anterior'. */
    public String idsAtras() {
        if (tamanio == 0) return "(vacía)";
        StringBuilder sb = new StringBuilder();
        Nodo p = fin;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) sb.append(',');
            sb.append(p.cancion.getId());
            p = p.anterior;
        }
        return sb.toString();
    }
}
