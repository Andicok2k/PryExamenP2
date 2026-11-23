import java.util.ArrayList;
import java.time.Year;

public class SistemaPeliculas {

    private ArrayList<Pelicula> peliculas;

    public SistemaPeliculas() {
        peliculas = new ArrayList<>();
        cargarPeliculas();
        ordenarPorNumero();
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }


    private void cargarPeliculas() {
        peliculas.add(new Pelicula(2001, "Coco", "Lee Unkrich", "Animación", 2017, 15));
        peliculas.add(new Pelicula(2002, "Titanic", "James Cameron", "Drama", 1997, 25));
        peliculas.add(new Pelicula(2003, "Avatar", "James Cameron", "Aventura", 2009, 30));
        peliculas.add(new Pelicula(2004, "Shrek", "Andrew Adamson", "Animación", 2001, 20));
        peliculas.add(new Pelicula(2005, "Cars", "John Lasseter", "Animación", 2006, 10));
        peliculas.add(new Pelicula(2006, "Toy Story", "John Lasseter", "Animación", 1995, 20));
        peliculas.add(new Pelicula(2007, "Up", "Pete Docter", "Animación", 2009, 15));
        peliculas.add(new Pelicula(2008, "Interstellar", "Christopher Nolan", "Aventura", 2014, 35));
        peliculas.add(new Pelicula(2009, "Inception", "Christopher Nolan", "Aventura", 2010, 30));
        peliculas.add(new Pelicula(2010, "Gladiador", "Ridley Scott", "Drama", 2000, 25));
        peliculas.add(new Pelicula(2011, "Forrest Gump", "Robert Zemeckis", "Drama", 1994, 18));
        peliculas.add(new Pelicula(2012, "Mi pobre angelito", "Chris Columbus", "Comedia", 1990, 12));
        peliculas.add(new Pelicula(2013, "La mascara", "Chuck Russell", "Comedia", 1994, 10));
        peliculas.add(new Pelicula(2014, "Buscando a Nemo", "Andrew Stanton", "Animación", 2003, 22));
        peliculas.add(new Pelicula(2015, "Ratatouille", "Brad Bird", "Animación", 2007, 22));
        peliculas.add(new Pelicula(2016, "Free Solo", "Jimmy Chin", "Documental", 2018, 5));
        peliculas.add(new Pelicula(2017, "Icarus", "Bryan Fogel", "Documental", 2017, 8));
        peliculas.add(new Pelicula(2018, "Superbad", "Greg Mottola", "Comedia", 2007, 12));
        peliculas.add(new Pelicula(2019, "Jurassic Park", "Steven Spielberg", "Aventura", 1993, 28));
        peliculas.add(new Pelicula(2020, "El pianista", "Roman Polanski", "Drama", 2002, 18));
    }


    private boolean validarDatos(String titulo, String director,
                                 String genero, int anio) {

        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }

        if (director == null || director.trim().isEmpty()) {
            return false;
        }

        if (genero == null) {
            return false;
        }

        if (!genero.equals("Drama") &&
                !genero.equals("Aventura") &&
                !genero.equals("Comedia") &&
                !genero.equals("Animación") &&
                !genero.equals("Documental")) {
            return false;
        }

        if (anio < 1930 || anio > Year.now().getValue()) {
            return false;
        }

        return true;
    }


    public void ordenarPorNumero() {

        for (int i = 0; i < peliculas.size() - 1; i++) {

            for (int j = 0; j < peliculas.size() - 1 - i; j++) {

                if (peliculas.get(j).getNumeroID() <
                        peliculas.get(j + 1).getNumeroID()) {

                    Pelicula aux = peliculas.get(j);
                    peliculas.set(j, peliculas.get(j + 1));
                    peliculas.set(j + 1, aux);
                }
            }
        }
    }


    public boolean registrarPelicula(int numeroID, String titulo,
                                     String director, String genero,
                                     int anio) {

        if (numeroID <= 0) {
            return false;
        }

        if (busquedaBinaria(numeroID) != -1) {
            return false;
        }

        if (!validarDatos(titulo, director, genero, anio)) {
            return false;
        }

        Pelicula p = new Pelicula(numeroID, titulo,
                director, genero, anio);

        peliculas.add(p);
        ordenarPorNumero();

        return true;
    }


    public ArrayList<Pelicula> busquedaLineal(String texto) {

        ArrayList<Pelicula> resultados = new ArrayList<>();

        if (texto == null || texto.trim().isEmpty()) {
            return resultados;
        }

        for (int i = 0; i < peliculas.size(); i++) {

            Pelicula p = peliculas.get(i);

            if (p.getTitulo().contains(texto) ||
                    p.getDirector().contains(texto)) {

                resultados.add(p);
            }
        }

        return resultados;
    }


    public int busquedaBinaria(int numeroID) {

        if (numeroID <= 0) {
            return -1;
        }

        int inicio = 0;
        int fin = peliculas.size() - 1;

        while (inicio <= fin) {

            int medio = (inicio + fin) / 2;

            if (peliculas.get(medio).getNumeroID() == numeroID) {
                return medio;
            }

            if (numeroID > peliculas.get(medio).getNumeroID()) {
                fin = medio - 1;
            } else {
                inicio = medio + 1;
            }
        }

        return -1;
    }


    public boolean editarPelicula(int numeroID, String titulo,
                                  String director, String genero,
                                  int anio) {

        int indice = busquedaBinaria(numeroID);

        if (indice == -1) {
            return false;
        }

        if (!validarDatos(titulo, director, genero, anio)) {
            return false;
        }

        Pelicula p = peliculas.get(indice);

        p.setTitulo(titulo);
        p.setDirector(director);
        p.setGenero(genero);
        p.setAnio(anio);

        return true;
    }


    public boolean eliminarPelicula(int numeroID) {

        int indice = busquedaBinaria(numeroID);

        if (indice == -1) {
            return false;
        }

        peliculas.remove(indice);

        return true;
    }


    public boolean darMeGusta(int numeroID) {

        int indice = busquedaBinaria(numeroID);

        if (indice == -1) {
            return false;
        }

        peliculas.get(indice).darMeGusta();

        return true;
    }


    public ArrayList<Pelicula> obtenerRanking() {

        ArrayList<Pelicula> ranking = new ArrayList<>(peliculas);

        for (int i = 0; i < ranking.size() - 1; i++) {

            for (int j = 0; j < ranking.size() - 1 - i; j++) {

                if (ranking.get(j).getMeGusta() <
                        ranking.get(j + 1).getMeGusta()) {

                    Pelicula aux = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, aux);
                }
            }
        }

        return ranking;
    }

    public int cantidadPeliculas() {
        return peliculas.size();
    }
}