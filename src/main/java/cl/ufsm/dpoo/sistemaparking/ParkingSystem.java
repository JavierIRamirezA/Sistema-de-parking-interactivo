package cl.ufsm.dpoo.sistemaparking;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;

public class ParkingSystem {

    private List<EspacioParking> espacios;
    private List<Ticket> tickets;
    private int contadorTickets;

    public ParkingSystem(int cantidadEspacios) {
        espacios = new ArrayList<>();
        tickets = new ArrayList<>();
        contadorTickets = 1;

        for (int i = 1; i <= cantidadEspacios; i++) {
            espacios.add(new EspacioParking(i));
        }
    }

    public Ticket ingresarVehiculo(String patente, String tipo, int numeroEspacio) {
        if (numeroEspacio < 1 || numeroEspacio > espacios.size()) {
            return null; // espacio fuera de rango
        }

        EspacioParking espacio = espacios.get(numeroEspacio - 1);

        if (!espacio.isLibre()) {
            return null; // el espacio ya está ocupado
        }

        Vehiculo v = new Vehiculo(patente, tipo);
        espacio.ocupar(v);
        Ticket t = new Ticket(contadorTickets++, v);
        tickets.add(t);
        return t;
    }


    public boolean retirarVehiculo(String patente) {
        for (EspacioParking espacio : espacios) {
            if (!espacio.isLibre() && espacio.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                espacio.liberar();
                return true;
            }
        }
        return false;
    }

    public EspacioParking buscarVehiculo(String patente) {
        for (EspacioParking espacio : espacios) {
            if (!espacio.isLibre() && espacio.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                return espacio;
            }
        }
        return null;
    }

    public double calcularMonto(String patente) {
        for (Ticket t : tickets) {
            if (t.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                long minutos = Duration.between(t.getHoraEntrada(), LocalDateTime.now()).toMinutes();
                if (minutos == 0) minutos = 1; // Cobrar al menos un minuto

                String tipo = t.getVehiculo().getTipo();
                double tarifaPorMinuto = getTarifaPorTipo(tipo);

                return minutos * tarifaPorMinuto;
            }
        }
        return 0;
    }

    private double getTarifaPorTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "auto" -> 100;
            case "moto" -> 50;
            case "camioneta" -> 150;
            case "camión" -> 200;
            default -> 100;
        };
    }
    public long calcularMinutos(String patente) {
        for (Ticket t : tickets) {
            if (t.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                return Duration.between(t.getHoraEntrada(), LocalDateTime.now()).toMinutes();
            }
        }
        return 0;
    }


    public List<EspacioParking> getEspacios() {
        return espacios;
    }


}
