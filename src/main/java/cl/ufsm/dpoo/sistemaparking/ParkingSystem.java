package cl.ufsm.dpoo.sistemaparking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;

public class ParkingSystem {

    private List<EspacioParking> espacios;
    private List<Ticket> tickets;
    private int contadorTickets;
    private int cantidadAutos, cantidadCamiones, cantidadMotos, cantidadCamionetas;
    private double totalRecaudado;
    public ParkingSystem(int cantidadEspacios) {
        espacios = new ArrayList<>();
        tickets = new ArrayList<>();
        contadorTickets = 1;
        cantidadAutos = 0;
        cantidadCamiones = 0;
        cantidadMotos = 0;
        cantidadCamionetas = 0;

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
        if(tipo.toLowerCase().equals("auto")) cantidadAutos++;
        if(tipo.toLowerCase().equals("moto")) cantidadMotos++;
        if(tipo.toLowerCase().equals("camioneta")) cantidadCamionetas++;
        if(tipo.toLowerCase().equals("camión")) cantidadCamiones++;
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

    public boolean generarReporte(){
        String nombre_archivo = "reporte.txt";
        double ocupados = 0;
        String nl = System.lineSeparator();
        for(EspacioParking espacio : espacios){
            if(!espacio.isLibre()){
                ocupados++;
            }
        }
        ocupados = ocupados/espacios.size() * 100;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombre_archivo, false))) {
            writer.write("La cantidad de autos registrados es: " + cantidadAutos + nl);
            writer.write("La cantidad de motos registradas es: " + cantidadMotos + nl);
            writer.write("La cantidad de camionetas registradas es: " + cantidadCamionetas + nl);
            writer.write("La cantidad de camiones registrados es: " + cantidadCamiones + nl);
            writer.write("------------------------" + nl);
            writer.write("Ocupacion actual: " + ocupados + "%" + nl);
            writer.write("Total recaudado: "+ totalRecaudado + nl);
            System.out.println("✅ Reporte generado correctamente.");
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error al generar el reporte: " + e.getMessage());
        }
        return false;
    }


    public void anadirPago(double valor){
        totalRecaudado = totalRecaudado+valor;
    }
}
