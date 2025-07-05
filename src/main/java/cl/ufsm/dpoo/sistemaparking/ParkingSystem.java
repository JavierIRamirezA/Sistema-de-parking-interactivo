package cl.ufsm.dpoo.sistemaparking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.Duration;
/**
 * Clase para manejar el sistema de Parking.
 * */
public class ParkingSystem {
/**
 *Lista que guarda cada espacio del estacionamiento.
 * */
    private List<EspacioParking> espacios;
    /** Lista que almacena los tickets entregados*/
    private List<Ticket> tickets;
    /**
     * Variable que almacena el numero de ticket que se debe entregar al siguiente vehiculo.
     * */
    private int contadorTickets;
    /**
     *  Cantidad de vehiculos tipo auto que han ingresado.
     * */
    private int cantidadAutos;
    /** Cantidad de vehiculos tipo camion que han ingresado.*/
    private int cantidadCamiones;
    /** Cantidad de vehiculos tipo moto que han ingresado.*/
    private int cantidadMotos;
    /** Cantidad de vehiculos tipo camioneta que han ingresado.*/
    private int cantidadCamionetas;
    /** Cantidad total de dinero recaudado*/
    private double totalRecaudado;

    /**
     * Metodo constructor, inicializa el sistema de parking
     * @param cantidadEspacios Cantidad de espacios que posee le estacionamiento
     * */
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
    /**
     * Ingresar un vehiculo al sistema.
     * @param patente   Patente del vehiculo a ingresar.
     * @param tipo      Tipo del vehiculo a ingresar.
     * @param numeroEspacio Numero de estacionamiento correspondiente.
     * @return Ticket correspondiente al vehiculo.
     * */

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
    /**
     * Retirar un vehiculo del sistema
     * @param patente Patente del vehiculo a retirar.
     * @return True si el vehiculo es retirado correctamente.
     * */

    public boolean retirarVehiculo(String patente) {
        for (EspacioParking espacio : espacios) {
            if (!espacio.isLibre() && espacio.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                espacio.liberar();
                return true;
            }
        }
        return false;
    }
    /**
     * Busca el espacio de aparcamiento del vehiculo.
     * @param patente Patente del vehiculo a buscar.
     * @return Espacio de aparcamiento del vehiculo.
     * */

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
    /**
     * Busca la tarifa por minuto dependiendo del tipo.
     * @param tipo Tipo de auto a buscar.
     * @return Valor de tarifa por minuto.
     * */

    private double getTarifaPorTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "auto" -> 100;
            case "moto" -> 50;
            case "camioneta" -> 150;
            case "camión" -> 200;
            default -> 100;
        };
    }

    /**
     * Calcula los minutos entre el ingreso de un vehiculo y la hora actual.
     * @param patente Patente del vehiculo correspondiente al calculo.
     * @return Cantidad de minutos.
     * */
    public long calcularMinutos(String patente) {
        for (Ticket t : tickets) {
            if (t.getVehiculo().getPatente().equalsIgnoreCase(patente)) {
                return Duration.between(t.getHoraEntrada(), LocalDateTime.now()).toMinutes();
            }
        }
        return 0;
    }

    /**
     * Obtener la lista de espacios.
     * @return Lista de espacios.
     * */
    public List<EspacioParking> getEspacios() {
        return espacios;
    }

    /** Genera un reporte de extension .txt
     * @return True si el reporte se genera correctamente.
     * */
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

    /**
     * Suma un valor al total recaudado.
     * @param valor Valor a sumar.
     * */

    public void anadirPago(double valor){
        totalRecaudado = totalRecaudado+valor;
    }
}
