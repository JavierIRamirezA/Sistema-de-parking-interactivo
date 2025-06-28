package cl.ufsm.dpoo.sistemaparking;
import java.time.LocalDateTime;

public class Ticket {
    private int numero;
    private Vehiculo vehiculo;
    private LocalDateTime horaEntrada;

    public Ticket(int numero, Vehiculo vehiculo) {
        this.numero = numero;
        this.vehiculo = vehiculo;
        this.horaEntrada = LocalDateTime.now();
    }

    public int getNumero() {
        return numero;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }
}

