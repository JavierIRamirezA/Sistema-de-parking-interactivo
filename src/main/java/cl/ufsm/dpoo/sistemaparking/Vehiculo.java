package cl.ufsm.dpoo.sistemaparking;

/** Clase que representa un vehiculo dentro del sistema.*/
public class Vehiculo {
    /** Patente asociada al vehiculo*/
    private String patente;
    /** Tipo del vehiculo.*/
    private String tipo;

    /**
     * Metodo constructor, inicializacon una patente y un tipo.
     * @param patente Patente del vehiculo.
     * @param tipo Tipo de vehiculo.
     * */
    public Vehiculo(String patente, String tipo) {
        this.patente = patente;
        this.tipo = tipo;
    }

    /** Metodo que obtiene la patente del vehiculo.
     * @return Patente del vehiculo.*/
    public String getPatente() {
        return patente;
    }
    /** Metodo que obtiene el tipo del vehiculo.
     * @return Tipo del vehiculo.*/
    public String getTipo() {
        return tipo;
    }
}
