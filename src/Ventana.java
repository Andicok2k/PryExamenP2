import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Ventana {

    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    // Componentes de Registro
    private JTextField txtCodigo;
    private JTextField txtTitulo;
    private JTextField txtDirector;
    private JComboBox cmbGenero;
    private JTextField txtAnio;
    private JButton btnRegistrar;
    private JList listaPeliculas;
    private JLabel lblCantidad;

    // Componentes de Busqueda
    private JTextField txtBuscarTexto;
    private JButton btnBuscarTexto;
    private JTextArea txtResultados;
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JTextField txtEditarTitulo;
    private JTextField txtEditarDirector;
    private JComboBox cmbEditarGenero;
    private JTextField txtEditarAnio;
    private JLabel lblMeGusta;
    private JButton btnActualizar;
    private JButton btnBorrar;
    private JButton btnMeGusta;

    // Componentes de Ranking
    private JList listaRanking;

    private SistemaPeliculas sistema;
    private DefaultListModel<String> modeloPeliculas;
    private DefaultListModel<String> modeloRanking;

    private int codigoSeleccionado = -1;
    private String ultimaBusqueda = "";

    public Ventana() {

        sistema = new SistemaPeliculas();

        modeloPeliculas = new DefaultListModel<>();
        modeloRanking = new DefaultListModel<>();

        listaPeliculas.setModel(modeloPeliculas);
        listaRanking.setModel(modeloRanking);

        txtResultados.setEditable(false);

        actualizarLista();
        actualizarRanking();

        // Boton Registrar
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    int codigo = Integer.parseInt(txtCodigo.getText());
                    String titulo = txtTitulo.getText();
                    String director = txtDirector.getText();
                    String genero = (String) cmbGenero.getSelectedItem();
                    int anio = Integer.parseInt(txtAnio.getText());

                    if (sistema.registrarPelicula(codigo, titulo,
                            director, genero, anio)) {

                        JOptionPane.showMessageDialog(null,
                                "Pelicula registrada correctamente");

                        actualizarLista();
                        actualizarRanking();
                        actualizarResultados();
                        limpiarRegistro();

                    } else {

                        JOptionPane.showMessageDialog(null,
                                "Datos incorrectos o codigo repetido");
                    }

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(null,
                            "Ingrese un codigo y anio validos");
                }
            }
        });

        // Boton Buscar Texto
        btnBuscarTexto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String texto = txtBuscarTexto.getText();

                if (texto.trim().isEmpty()) {

                    ultimaBusqueda = "";
                    txtResultados.setText("");

                    JOptionPane.showMessageDialog(null,
                            "Ingrese un titulo o director");

                } else {

                    ultimaBusqueda = texto;
                    actualizarResultados();

                    if (sistema.busquedaLineal(texto).isEmpty()) {

                        JOptionPane.showMessageDialog(null,
                                "No se encontraron coincidencias");
                    }
                }
            }
        });

        // Boton Buscar Codigo
        btnBuscarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                limpiarSeleccion();

                try {

                    int codigo = Integer.parseInt(
                            txtBuscarCodigo.getText());

                    if (codigo <= 0) {

                        JOptionPane.showMessageDialog(null,
                                "El codigo debe ser positivo");

                    } else {

                        int indice = sistema.busquedaBinaria(codigo);

                        if (indice != -1) {

                            codigoSeleccionado = codigo;
                            mostrarPelicula();

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "No existe la pelicula");
                        }
                    }

                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(null,
                            "Ingrese un codigo valido");
                }
            }
        });

        // Boton Actualizar
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (codigoSeleccionado == -1) {

                    JOptionPane.showMessageDialog(null,
                            "Primero debe buscar una pelicula");

                } else {

                    try {

                        String titulo = txtEditarTitulo.getText();
                        String director = txtEditarDirector.getText();
                        String genero = (String)
                                cmbEditarGenero.getSelectedItem();

                        int anio = Integer.parseInt(
                                txtEditarAnio.getText());

                        if (sistema.editarPelicula(codigoSeleccionado,
                                titulo, director, genero, anio)) {

                            JOptionPane.showMessageDialog(null,
                                    "Pelicula actualizada correctamente");

                            actualizarLista();
                            actualizarRanking();
                            actualizarResultados();

                            limpiarSeleccion();
                            txtBuscarCodigo.setText("");

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "Los datos ingresados no son validos");
                        }

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(null,
                                "Ingrese un anio valido");
                    }
                }
            }
        });

        // Boton Borrar
        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (codigoSeleccionado == -1) {

                    JOptionPane.showMessageDialog(null,
                            "Primero debe buscar una pelicula");

                } else {

                    int respuesta = JOptionPane.showConfirmDialog(
                            null,
                            "Desea eliminar la pelicula?",
                            "Eliminar pelicula",
                            JOptionPane.YES_NO_OPTION);

                    if (respuesta == JOptionPane.YES_OPTION) {

                        if (sistema.eliminarPelicula(
                                codigoSeleccionado)) {

                            JOptionPane.showMessageDialog(null,
                                    "Pelicula eliminada correctamente");

                            limpiarSeleccion();
                            txtBuscarCodigo.setText("");

                            actualizarLista();
                            actualizarRanking();
                            actualizarResultados();

                        } else {

                            JOptionPane.showMessageDialog(null,
                                    "No se pudo eliminar la pelicula");

                            limpiarSeleccion();
                        }
                    }
                }
            }
        });

        // Boton Me gusta
        btnMeGusta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (codigoSeleccionado == -1) {

                    JOptionPane.showMessageDialog(null,
                            "Primero debe buscar una pelicula");

                } else {

                    if (sistema.darMeGusta(codigoSeleccionado)) {

                        int indice = sistema.busquedaBinaria(
                                codigoSeleccionado);

                        Pelicula p = sistema.getPeliculas().get(indice);

                        lblMeGusta.setText("Me gusta: "
                                + p.getMeGusta());

                        actualizarLista();
                        actualizarRanking();
                        actualizarResultados();

                    } else {

                        JOptionPane.showMessageDialog(null,
                                "No existe la pelicula");

                        limpiarSeleccion();
                    }
                }
            }
        });
    }

    // Actualizar listado de la primera pestana
    private void actualizarLista() {

        modeloPeliculas.clear();

        ArrayList<Pelicula> peliculas = sistema.getPeliculas();

        for (int i = 0; i < peliculas.size(); i++) {

            modeloPeliculas.addElement(
                    peliculas.get(i).toString());
        }

        lblCantidad.setText("Total de peliculas: "
                + sistema.cantidadPeliculas());
    }

    // Actualizar ranking de popularidad
    private void actualizarRanking() {

        modeloRanking.clear();

        ArrayList<Pelicula> ranking = sistema.obtenerRanking();

        for (int i = 0; i < ranking.size(); i++) {

            Pelicula p = ranking.get(i);

            modeloRanking.addElement(
                    p.getNumeroID() + " | "
                            + p.getTitulo() + " | Me gusta: "
                            + p.getMeGusta());
        }
    }

    // Mostrar resultados de la busqueda lineal
    private void actualizarResultados() {

        txtResultados.setText("");

        if (!ultimaBusqueda.trim().isEmpty()) {

            ArrayList<Pelicula> resultados =
                    sistema.busquedaLineal(ultimaBusqueda);

            for (int i = 0; i < resultados.size(); i++) {

                txtResultados.append(
                        resultados.get(i).toString() + "\n");
            }

            if (resultados.isEmpty()) {

                txtResultados.setText(
                        "No se encontraron coincidencias");
            }
        }
    }

    // Mostrar pelicula encontrada por codigo
    private void mostrarPelicula() {

        int indice = sistema.busquedaBinaria(codigoSeleccionado);

        if (indice != -1) {

            Pelicula p = sistema.getPeliculas().get(indice);

            txtEditarTitulo.setText(p.getTitulo());
            txtEditarDirector.setText(p.getDirector());
            cmbEditarGenero.setSelectedItem(p.getGenero());
            txtEditarAnio.setText(String.valueOf(p.getAnio()));

            lblMeGusta.setText("Me gusta: " + p.getMeGusta());
        }
    }

    // Limpiar formulario de registro
    private void limpiarRegistro() {

        txtCodigo.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        txtAnio.setText("");
        cmbGenero.setSelectedIndex(0);
    }

    // Limpiar pelicula seleccionada
    private void limpiarSeleccion() {

        codigoSeleccionado = -1;

        txtEditarTitulo.setText("");
        txtEditarDirector.setText("");
        txtEditarAnio.setText("");
        cmbEditarGenero.setSelectedIndex(-1);

        lblMeGusta.setText("Me gusta: 0");
    }

    // Ejecutar programa
    public static void main(String[] args) {

        JFrame frame = new JFrame("CineLikes");

        frame.setContentPane(new Ventana().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(950, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
