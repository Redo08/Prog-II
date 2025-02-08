package poo.model;

import org.json.JSONObject;

public abstract class RailVehicles {

    // Atributos
    protected String id;
    protected String producerName;
    protected int wheelAmount;
    protected CouplerType acoplatorType;

    // Constructor por defecto
    public RailVehicles() {
        this("Undefined", "Undefined", 0, CouplerType.AUTOMATIC);
    }

    // Constructor parametrizado
    public RailVehicles(String id, String producerName, int wheelAmount, CouplerType acoplatorType) {
        setId(id);
        setProducerName(producerName);
        setWheelAmount(wheelAmount);
        setAcoplatorType(acoplatorType);
    }

    // Accesores y mutadores
    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return this.getClass().getSimpleName();
    }

    public String getProducerName() {
        return producerName;
    }

    public final void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public int getWheelAmount() {
        return wheelAmount;
    }

    public final void setWheelAmount(int wheelAmount) {
        this.wheelAmount = wheelAmount;
    }

    public CouplerType getAcoplatorType() {
        return acoplatorType;
    }

    public final void setAcoplatorType(CouplerType acoplatorType) {
        this.acoplatorType = acoplatorType;
    }

    // Override toString
    @Override
    public String toString() {
        return (new JSONObject(this)).toString(2);
    }

}
