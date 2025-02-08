package poo.model;

public class Locomotive extends RailVehicles {

    // Atributos
    private MotorType motorType;

    // Constructor por defecto
    public Locomotive() {
        this("Undefined", "Undfined", 0, CouplerType.AUTOMATIC, MotorType.DIESEL);
    }

    // Constructor parametrizado
    public Locomotive(String id, String producerName, int wheelAmount, CouplerType acoplatorType, MotorType motorType) {
        super(id, producerName, wheelAmount, acoplatorType);
        setMotorType(motorType);
    }

    // Accesores y Mutadores
    public MotorType getMotorType() {
        return motorType;
    }

    public final void setMotorType(MotorType motorType) {
        this.motorType = motorType;
    }
}
