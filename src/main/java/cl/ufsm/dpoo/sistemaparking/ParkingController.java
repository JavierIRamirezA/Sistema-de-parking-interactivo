// ParkingController.java
package cl.ufsm.dpoo.sistemaparking;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class ParkingController {

    @FXML private TextField txtPatente;
    @FXML private ComboBox<String> comboTipoVehiculo;
    @FXML private Button btnIngresar;

    @FXML private TextField txtBuscarPatente;
    @FXML private Button btnBuscar;
    @FXML private Button btnCalcularPago;
    @FXML private Button btnAbrirBarrera;

    @FXML private Label lblEstado;
    @FXML private GridPane gridParking;
    @FXML private TextField txtDescuento;

    private ParkingSystem sistema;

    @FXML
    public void initialize() {
        sistema = new ParkingSystem(36);
        comboTipoVehiculo.getItems().addAll("Auto", "Moto", "Camioneta", "Camión");

        btnIngresar.setOnAction(e -> ingresarVehiculo());
        btnBuscar.setOnAction(e -> buscarVehiculo());
        btnCalcularPago.setOnAction(e -> calcularPago());
        btnAbrirBarrera.setOnAction(e -> abrirBarrera());

        actualizarVista();
    }

    private void ingresarVehiculo() {
        String patente = txtPatente.getText().trim().toUpperCase();
        String tipo = comboTipoVehiculo.getValue();

        if (tipo == null || (!validarPatenteVehiculo(patente) && !("Moto".equals(tipo) && validarPatenteMoto(patente)))) {
            lblEstado.setText("Ingrese patente válida y tipo de vehículo.");
            return;
        }

        // Obtener lista de espacios disponibles
        var espaciosLibres = sistema.getEspacios().stream()
                .filter(EspacioParking::isLibre)
                .map(EspacioParking::getNumero)
                .toList();

        if (espaciosLibres.isEmpty()) {
            lblEstado.setText("Estacionamiento lleno.");
            return;
        }

        // Diálogo para seleccionar el espacio
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(espaciosLibres.get(0), espaciosLibres);
        dialog.setTitle("Seleccionar Espacio");
        dialog.setHeaderText("Asignación manual de espacio");
        dialog.setContentText("Seleccione el número de espacio:");

        dialog.showAndWait().ifPresent(numeroSeleccionado -> {
            Ticket t = sistema.ingresarVehiculo(patente, tipo, numeroSeleccionado);
            if (t != null) {
                lblEstado.setText("Vehículo ingresado en espacio " + numeroSeleccionado + ". Ticket #" + t.getNumero());
                actualizarVista();
                txtPatente.clear();
                comboTipoVehiculo.getSelectionModel().clearSelection();
            } else {
                lblEstado.setText("El espacio seleccionado ya está ocupado.");
            }
        });
    }


    private void buscarVehiculo() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        EspacioParking e = sistema.buscarVehiculo(patente);

        if (e != null) {
            long minutos = sistema.calcularMinutos(patente);
            lblEstado.setText("Vehículo en espacio " + e.getNumero() + ". Tiempo de estadía: " + minutos + " min.");
        } else {
            lblEstado.setText("Vehículo no encontrado.");
        }
    }


    private void calcularPago() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        EspacioParking e = sistema.buscarVehiculo(patente);

        if (e != null) {
            double monto = sistema.calcularMonto(patente);
            double descuento = 0;
            try {
                descuento = Double.parseDouble(txtDescuento.getText().trim());
            } catch (NumberFormatException ignored) { }

            if (descuento > 0 && descuento <= 100) {
                monto -= (monto * descuento / 100);
            }
            lblEstado.setText("Monto a pagar: $" + monto);
        } else {
            lblEstado.setText("Vehículo no encontrado.");
        }
    }


    private void abrirBarrera() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        EspacioParking e = sistema.buscarVehiculo(patente);

        if (e != null) {
            sistema.retirarVehiculo(patente);
            lblEstado.setText("Pago recibido. Barrera abierta. Espacio liberado.");

            // Recuperar la casilla correctamente
            VBox box = (VBox) gridParking.getChildren().get(e.getNumero() - 1);
            StackPane contenedor = (StackPane) box.getChildren().get(0);
            contenedor.setStyle("-fx-background-color: yellow; -fx-border-color: #555;");

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(this::actualizarVista);
            }).start();
        } else {
            lblEstado.setText("Vehículo no encontrado.");
        }
    }




    private void actualizarVista() {
        gridParking.getChildren().clear();
        int columnas = 6; // 6 columnas x 6 filas = 36 espacios

        for (int i = 0; i < sistema.getEspacios().size(); i++) {
            EspacioParking e = sistema.getEspacios().get(i);

            StackPane contenedor = new StackPane();
            contenedor.setStyle(e.isLibre()
                    ? "-fx-background-color: lightgreen; -fx-border-color: #555; -fx-border-radius: 5;"
                    : "-fx-background-color: indianred; -fx-border-color: #555; -fx-border-radius: 5;");

            Label labelNumero = new Label(String.valueOf(e.getNumero()));
            labelNumero.setTextFill(Color.BLACK);

            contenedor.getChildren().add(labelNumero);
            contenedor.setPrefSize(70, 70);
            contenedor.setMaxSize(70, 70);


            if (!e.isLibre()) {
                contenedor.setOnMouseClicked(event -> {
                    txtBuscarPatente.setText(e.getVehiculo().getPatente());
                    lblEstado.setText("Vehículo seleccionado: " + e.getVehiculo().getPatente());
                });
            }

            VBox box = new VBox(contenedor);
            box.setAlignment(Pos.CENTER);
            box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            int fila = i / columnas;
            int col = i % columnas;

            gridParking.add(box, col, fila);
            GridPane.setHgrow(box, Priority.ALWAYS);
            GridPane.setVgrow(box, Priority.ALWAYS);
        }
    }




    private boolean validarPatenteVehiculo(String patente) {
        // Formato antiguo: 2 letras + 4 números (Ej: AB1234)
        String antiguo = "^[A-Z]{2}[0-9]{4}$";

        // Formato nuevo: 4 letras + 2 números (Ej: FBXA86)
        String nuevo = "^[A-Z]{4}[0-9]{2}$";

        return Pattern.matches(antiguo, patente) || Pattern.matches(nuevo, patente);
    }


    private boolean validarPatenteMoto(String patente) {
        String antiguo = "^[A-Z]{2}[0-9]{3,4}$";
        String nuevo = "^[A-Z]{3}[0-9]{2}$";
        return Pattern.matches(antiguo, patente) || Pattern.matches(nuevo, patente);
    }
}

