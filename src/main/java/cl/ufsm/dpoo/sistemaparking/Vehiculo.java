package cl.ufsm.dpoo.sistemaparking;

public class Vehiculo {
    private String patente;
    private String tipo;

    public Vehiculo(String patente, String tipo) {
        this.patente = patente;
        this.tipo = tipo;
    }

    public String getPatente() {
        return patente;
    }

    public String getTipo() {
        return tipo;
    }
}
