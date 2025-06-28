module cl.ufsm.dpoo.sistemaparking {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens cl.ufsm.dpoo.sistemaparking to javafx.fxml;
    exports cl.ufsm.dpoo.sistemaparking;
}