package cl.ufsm.dpoo.sistemaparking;
/**
 * Espacio de aparcamiento en un estacionamiento.
 * */
public class EspacioParking {
    /** Numero de espacio.*/
    private int numero;
    /** Estado del espacio.*/
    private boolean ocupado;
    /** Vehiculo el cual esta ocupando el espacio.*/
    private Vehiculo vehiculo;

    /**
     * Metodo constructor, inicializa con un numero y con estado ocupado y sin vehiculo.
     * @param numero numero del espacio.
     * */
    public EspacioParking(int numero) {
        this.numero = numero;
        this.ocupado = false;
        this.vehiculo = null;
    }
    /** Metodo para conocer el estado
     * @return True si el espacio esta libre.
     * */
    public boolean isLibre() {
        return !ocupado;
    }

    /**
     * Metodo para ocupar el espacio.
     * @param vehiculo Vehiculo el cual ocupara el espacio.
     * */
    public void ocupar(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        this.ocupado = true;
    }
    /**
     * Metodo para liberar un espacio.
     * */
    public void liberar() {
        this.vehiculo = null;
        this.ocupado = false;
    }
    /**
     * Metodo para obtener el numero del espacio.
     * @return Numero del espacio.
     * */
    public int getNumero() {
        return numero;
    }

    /**
     * Metodo para obtener el vehiculo aparcado.
     * @return Vehiculo aparcado.
     * */

    public Vehiculo getVehiculo() {
        return vehiculo;
    }
}
