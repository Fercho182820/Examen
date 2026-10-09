/**
 * Nodo de la lista doblemente enlazada circular.
 * Los enlaces son de paquete (sin modificador) para que solo ListaReproduccion
 * pueda manipularlos: el menú nunca toca referencias directamente.
 */
class Nodo {
    final Cancion cancion;
    Nodo anterior;
    Nodo siguiente;

    Nodo(Cancion cancion) {
        this.cancion = cancion;
    }
}
