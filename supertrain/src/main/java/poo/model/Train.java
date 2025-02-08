package poo.model;

import org.json.JSONObject;
import java.util.ArrayList;

public class Train {

    // Atributos
    private String id;
    private ArrayList<RailVehicles> railVehicle;

    // Constructor por defecto
    public Train() {
        this("Undefined", new ArrayList<>());
    }

    // Constructor Parametrizado 
    public Train(String id, ArrayList<RailVehicles> railVehicle) {
        setId(id);
        setRailVehicle(railVehicle);
    }

    // Accesores y mutadores
    public ArrayList<RailVehicles> getRailVehicle() {
        return railVehicle;
    }

    public final void setRailVehicle(ArrayList<RailVehicles> railVehicle) {
        this.railVehicle = railVehicle;
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    /**
     * Recorre la colección de RailVehicles para totalizar el total de asientos
     * de pasajero de tipo PassangerWagon
     *
     * @return La suma total de la cantidad de asientos para pasajeros
     */
    public int getTotalPassangerSeats() {
        int seatsPerRow = 0;
        for (RailVehicles railVehicles : railVehicle) {
            if (railVehicles instanceof PassengerWagon) {
               seatsPerRow += (((PassengerWagon) railVehicles).getPassengerSeats());
            }
        }
        return seatsPerRow;
    }

    /**
     * Recorre la colección de RailVehicles para totalizar el total de ruedas de
     * las instancias
     *
     * @return La suma total de la cantidad de ruedas de los vehiculos
     */
    public int getTotalWheelAmount() {
        int wheelAmount = 0;
        for (RailVehicles railVehicles : railVehicle) {
            wheelAmount += railVehicles.getWheelAmount();
        }
        return wheelAmount;
    }

    /**
     * Recorre la colección de RailVehicles para ver si cumple con la cantidad
     * minima de instancias requeridas
     *
     * @return Un boolean para ver si la instancia de Train cumple con los
     * filtros.
     */
    public boolean getTotalAmountRestriction() {
        int amountLocomotives = 0;
        int amountRailRoadCar = 0;
        for (RailVehicles railVehicles : railVehicle) {
            if (railVehicles instanceof Locomotive) {
                amountLocomotives++;
            } else if (railVehicles instanceof RailroadCar) {
                amountRailRoadCar++;
            }
        }
        if ((amountLocomotives > 2 || amountLocomotives < 1) || amountRailRoadCar < 1) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * Recorre la colección de RailVehicles para ver si cumple con los
     * requisitos puestos por el filtro.
     * @return Un boolean que comprueba si la instancia de Train contiene sus componentes necesarios 
     * para cumplir el filtro.
     */
    public boolean trainList() {
        boolean electricLocomotive = false;
        boolean goodWagon = false;
        boolean passengerWagon = false;
        int cisternaAmount = 0;
        int jaulaAmount = 0;
        int goodWagonAmount = 0;
        
        for (RailVehicles railVehicles : railVehicle) {
            if (railVehicles instanceof Locomotive) {
                if (((Locomotive) railVehicles).getMotorType() == MotorType.ELECTRIC) {
                    electricLocomotive = true;
                }
            }
            if (railVehicles instanceof GoodWagon) {
                if (((GoodWagon) railVehicles).getComercialGood() == ComercialGood.JAULA) {
                    goodWagon = true;
                    jaulaAmount++;
                }
                if (((GoodWagon) railVehicles).getComercialGood() == ComercialGood.CISTERNA) {
                    cisternaAmount++;
                }
                goodWagonAmount++;
            }
            if ((railVehicles instanceof PassengerWagon)) {
                passengerWagon = true;
            }
        }
        if (goodWagon == true && cisternaAmount >= 1 && (jaulaAmount + cisternaAmount == goodWagonAmount)) {
            goodWagon = true;
        } else {
            goodWagon = false;
        }
        if (electricLocomotive == true && goodWagon == true && passengerWagon == false) {
            return true;
        } else {
            return false;
        }
    }
    //Override toString

    @Override
    public String toString() {
        return (new JSONObject(this)).toString(2);
    }

}
