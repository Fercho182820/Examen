public class Cancion {
    private int id;
    private String titulo;
    private String artista;
    private int duracion; // en segundos

    public Cancion(int id, String titulo, String artista, int duracion) {
        if (titulo == null || titulo.trim().equals("")) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if (artista == null || artista.trim().equals("")) {
            throw new IllegalArgumentException("El artista no puede estar vacio");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duracion debe ser mayor que cero");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracion = duracion;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getDuracion() {
        return duracion;
    }

    public String toString() {
        return "#" + id + " | " + titulo + " | " + artista + " | " + duracion + " seg";
    }
}
