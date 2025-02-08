package poo.model;

public abstract class RailroadCar extends RailVehicles {

    // Atributos
    private VelocityType velocityType;

    // Constructor por defecto
    public RailroadCar() {
        this("Undefined", "Undfined", 0, CouplerType.AUTOMATIC, VelocityType.B);
    }

    // Constructor parametrizado
    public RailroadCar(String id, String producerName, int wheelAmount, CouplerType acoplatorType, VelocityType velocityType) {
        super(id, producerName, wheelAmount, acoplatorType);
        setVelocityType(velocityType);
    }

    // Accesores y mutadores
    public VelocityType getVelocityType() {
        return velocityType;
    }

    public final void setVelocityType(VelocityType velocityType) {
        this.velocityType = velocityType;
    }

}
