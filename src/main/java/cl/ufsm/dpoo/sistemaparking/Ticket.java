package cl.ufsm.dpoo.sistemaparking;
import java.time.LocalDateTime;

/** Ticket entregado a los vehiculos que ingresan al sistema.*/

public class Ticket {
    /** Numero de ticket.*/
    private int numero;
    /** Vehiculo al que le corresponde el ticket.*/
    private Vehiculo vehiculo;
    /** Fecha de entrada del vehiculo.*/
    private LocalDateTime horaEntrada;

    /** Metodo constructor, inicializa con un numero, vehiculo y fecha.
     * @param numero Numero de ticket.
     * @param vehiculo Vehiculo al que le corresponde el ticket.*/
    public Ticket(int numero, Vehiculo vehiculo) {
        this.numero = numero;
        this.vehiculo = vehiculo;
        this.horaEntrada = LocalDateTime.now();
    }

    /** Metodo que obtiene el numero del ticket.
     * @return Numero del ticket.*/
    public int getNumero() {
        return numero;
    }

    /** Metodo que obitene el vehiculo asociado al ticket.
     * @return Vehiculo asociado al ticket.*/
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /** Metodo que obtiene la fecha de entrada del vehiculo.
     * @return Fecha de entrada.*/
    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }
}

