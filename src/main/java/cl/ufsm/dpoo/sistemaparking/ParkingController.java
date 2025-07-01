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

            Label labelNumero = new Label(String.valueOf(e.getNumero()));
            labelNumero.setTextFill(Color.BLACK);

            StackPane contenedorRectangulo = new StackPane(rect, labelNumero);
            contenedorRectangulo.setPrefSize(60, 60);

            Label labelPatente = new Label();
            if (!e.isLibre()){
                labelPatente.setText(e.getVehiculo().getPatente());
            }

            VBox box = new VBox(contenedorRectangulo, labelPatente);
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
        String antiguo = "^[A-Z]{2}[0-9]{4}$";
        String nuevo = "^[BCDFGHJKLPRSTVWXZ]{4}[1-9]{1}[0-9]{1}$";
        return Pattern.matches(antiguo, patente) || Pattern.matches(nuevo, patente);
    }

    private boolean validarPatenteMoto(String patente) {
        String antiguo = "^[A-Z]{2}[0-9]{3,4}$";
        String nuevo = "^[A-Z]{3}[0-9]{2}$";
        return Pattern.matches(antiguo, patente) || Pattern.matches(nuevo, patente);
    }
}

