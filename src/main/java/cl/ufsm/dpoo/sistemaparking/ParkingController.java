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

    private ParkingSystem sistema;

    @FXML
    public void initialize() {
        sistema = new ParkingSystem(12);
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

        if (!validarPatente(patente) || tipo == null) {
            lblEstado.setText("Ingrese patente válida y tipo de vehículo.");
            return;
        }

        Ticket t = sistema.ingresarVehiculo(patente, tipo);
        if (t != null) {
            lblEstado.setText("Vehículo ingresado. Ticket #" + t.getNumero());
            actualizarVista();
            txtPatente.clear();
            comboTipoVehiculo.getSelectionModel().clearSelection();
        } else {
            lblEstado.setText("Estacionamiento lleno.");
        }
    }

    private void buscarVehiculo() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        EspacioParking e = sistema.buscarVehiculo(patente);
        lblEstado.setText(e != null ? "Vehículo en espacio " + e.getNumero() : "Vehículo no encontrado.");
    }

    private void calcularPago() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        EspacioParking e = sistema.buscarVehiculo(patente);
        if (e != null) {
            double monto = sistema.calcularMonto(patente);
            lblEstado.setText("Monto a pagar: $" + monto);
        } else {
            lblEstado.setText("Vehículo no encontrado.");
        }
    }

    private void abrirBarrera() {
        String patente = txtBuscarPatente.getText().trim().toUpperCase();
        if (sistema.retirarVehiculo(patente)) {
            lblEstado.setText("Pago recibido. Barrera abierta.");
            actualizarVista();
        } else {
            lblEstado.setText("Vehículo no encontrado.");
        }
    }

    private void actualizarVista() {
        gridParking.getChildren().clear();
        int columnas = 4;

        for (int i = 0; i < sistema.getEspacios().size(); i++) {
            EspacioParking e = sistema.getEspacios().get(i);
            Rectangle rect = new Rectangle();
            rect.setWidth(60);
            rect.setHeight(60);
            rect.setArcWidth(10);
            rect.setArcHeight(10);
            rect.setFill(e.isLibre() ? Color.LIGHTGREEN : Color.INDIANRED);

            Label num = new Label("Espacio " + e.getNumero());
            VBox box = new VBox(rect, num);
            box.setAlignment(Pos.CENTER);
            box.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            int fila = i / columnas;
            int col = i % columnas;

            gridParking.add(box, col, fila);
            GridPane.setHgrow(box, Priority.ALWAYS);
            GridPane.setVgrow(box, Priority.ALWAYS);
        }
    }

    private boolean validarPatente(String patente) {
        return Pattern.matches("^[A-Z0-9]{1,6}$", patente);
    }
}