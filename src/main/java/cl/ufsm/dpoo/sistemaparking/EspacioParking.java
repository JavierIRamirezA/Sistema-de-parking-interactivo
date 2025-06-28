package cl.ufsm.dpoo.sistemaparking;

public class EspacioParking {
    private int numero;
    private boolean ocupado;
    private Vehiculo vehiculo;

    public EspacioParking(int numero) {
        this.numero = numero;
        this.ocupado = false;
        this.vehiculo = null;
    }

    public boolean isLibre() {
        return !ocupado;
    }

    public void ocupar(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.ocupado = true;
    }

    public void liberar() {
        this.vehiculo = null;
        this.ocupado = false;
    }

    public int getNumero() {
        return numero;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
}
