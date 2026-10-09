// Lista doblemente enlazada circular
public class ListaReproduccion {
    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int cantidad;
    private int contadorId = 1; // para generar los ids solos

    public boolean estaVacia() {
        return cantidad == 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Cancion getActual() {
        if (actual == null) {
            return null;
        }
        return actual.cancion;
    }

    // O(1)
    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        // se crea primero la cancion: si los datos son malos lanza excepcion y la lista no cambia
        Cancion c = new Cancion(contadorId, titulo, artista, duracion);
        contadorId++;
        Nodo nuevo = new Nodo(c);

        if (cantidad == 0) {
            nuevo.sig = nuevo;
            nuevo.ant = nuevo;
            inicio = nuevo;
            fin = nuevo;
            actual = nuevo;
        } else {
            nuevo.sig = inicio;
            nuevo.ant = fin;
            inicio.ant = nuevo;
            fin.sig = nuevo;
            inicio = nuevo;
        }
        cantidad++;
        return c;
    }

    // O(1)
    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Cancion c = new Cancion(contadorId, titulo, artista, duracion);
        contadorId++;
        Nodo nuevo = new Nodo(c);

        if (cantidad == 0) {
            nuevo.sig = nuevo;
            nuevo.ant = nuevo;
            inicio = nuevo;
            fin = nuevo;
            actual = nuevo;
        } else {
            nuevo.sig = inicio;
            nuevo.ant = fin;
            fin.sig = nuevo;
            inicio.ant = nuevo;
            fin = nuevo;
        }
        cantidad++;
        return c;
    }

    // O(1)
    public Cancion avanzar() {
        if (actual == null) {
            return null;
        }
        actual = actual.sig;
        return actual.cancion;
    }

    // O(1)
    public Cancion retroceder() {
        if (actual == null) {
            return null;
        }
        actual = actual.ant;
        return actual.cancion;
    }

    // O(n): se recorren maximo "cantidad" nodos (no se usa null porque es circular)
    private Nodo buscarNodo(int id) {
        Nodo p = inicio;
        for (int i = 0; i < cantidad; i++) {
            if (p.cancion.getId() == id) {
                return p;
            }
            p = p.sig;
        }
        return null;
    }

    public Cancion buscar(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) {
            return null;
        }
        return n.cancion;
    }

    public boolean seleccionar(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) {
            return false;
        }
        actual = n;
        return true;
    }

    // O(n) por la busqueda
    public boolean eliminarPorId(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) {
            return false;
        }
        quitar(n);
        return true;
    }

    // O(1), ya tenemos el nodo
    public Cancion eliminarActual() {
        if (actual == null) {
            return null;
        }
        Cancion c = actual.cancion;
        quitar(actual);
        return c;
    }

    // Saca un nodo de la lista. O(1)
    private void quitar(Nodo n) {
        if (cantidad == 1) {
            inicio = null;
            fin = null;
            actual = null;
        } else {
            // guardar los vecinos antes de cambiar enlaces
            Nodo anterior = n.ant;
            Nodo siguiente = n.sig;
            anterior.sig = siguiente;
            siguiente.ant = anterior;

            if (n == inicio) {
                inicio = siguiente;
            }
            if (n == fin) {
                fin = anterior;
            }
            if (n == actual) {
                actual = siguiente; // la que le seguia
            }
        }
        // soltar el nodo para que no quede referenciado
        n.ant = null;
        n.sig = null;
        cantidad--;
    }

    // O(n)
    public String mostrarAdelante() {
        if (cantidad == 0) {
            return "La lista esta vacia";
        }
        String texto = "";
        Nodo p = inicio;
        for (int i = 0; i < cantidad; i++) {
            if (p == actual) {
                texto += "-> ";
            } else {
                texto += "   ";
            }
            texto += p.cancion + "\n";
            p = p.sig;
        }
        return texto;
    }

    // O(n)
    public String mostrarAtras() {
        if (cantidad == 0) {
            return "La lista esta vacia";
        }
        String texto = "";
        Nodo p = fin;
        for (int i = 0; i < cantidad; i++) {
            if (p == actual) {
                texto += "-> ";
            } else {
                texto += "   ";
            }
            texto += p.cancion + "\n";
            p = p.ant;
        }
        return texto;
    }

    // O(k): muestra la actual y avanza, k veces
    public String reproducir(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k debe ser un entero positivo");
        }
        if (actual == null) {
            return "La lista esta vacia, no hay nada que reproducir";
        }
        String texto = "";
        for (int i = 0; i < k; i++) {
            texto += "Reproduciendo: " + actual.cancion + "\n";
            actual = actual.sig;
        }
        return texto;
    }

    // Para las pruebas: revisa que los enlaces esten bien
    public boolean verificarEnlaces() {
        if (cantidad == 0) {
            return inicio == null && fin == null && actual == null;
        }
        if (fin.sig != inicio || inicio.ant != fin) {
            return false;
        }
        boolean actualEsta = false;
        Nodo p = inicio;
        for (int i = 0; i < cantidad; i++) {
            if (p.sig.ant != p || p.ant.sig != p) {
                return false;
            }
            if (p == actual) {
                actualEsta = true;
            }
            if (i == cantidad - 1 && p != fin) {
                return false;
            }
            p = p.sig;
        }
        return p == inicio && actualEsta;
    }

    // Para las pruebas: ids de inicio a fin, ejemplo "3,1,2"
    public String idsAdelante() {
        if (cantidad == 0) {
            return "(vacia)";
        }
        String texto = "";
        Nodo p = inicio;
        for (int i = 0; i < cantidad; i++) {
            if (i > 0) {
                texto += ",";
            }
            texto += p.cancion.getId();
            p = p.sig;
        }
        return texto;
    }

    // Para las pruebas: ids de fin a inicio usando los enlaces "ant"
    public String idsAtras() {
        if (cantidad == 0) {
            return "(vacia)";
        }
        String texto = "";
        Nodo p = fin;
        for (int i = 0; i < cantidad; i++) {
            if (i > 0) {
                texto += ",";
            }
            texto += p.cancion.getId();
            p = p.ant;
        }
        return texto;
    }
}
