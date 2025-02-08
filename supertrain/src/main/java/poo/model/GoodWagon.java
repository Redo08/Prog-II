package poo.model;

public class GoodWagon extends RailroadCar {

    // Atributos
    protected ComercialGood comercialGood;

    // Constructor por defecto
    public GoodWagon() {
        this("Undefined", "Undfined", 0, CouplerType.AUTOMATIC, VelocityType.B, ComercialGood.ABIERTO);
    }

    // Constructor Parametrizado
    public GoodWagon(String id, String producerName, int wheelAmount, CouplerType acoplatorType, VelocityType velocityType, ComercialGood comercialGood) {
        super(id, producerName, wheelAmount, acoplatorType, velocityType);
        setComercialGood(comercialGood);
    }

    // Accesores y mutadores
    public ComercialGood getComercialGood() {
        return comercialGood;
    }

    public final void setComercialGood(ComercialGood comercialGood) {
        this.comercialGood = comercialGood;
    }
}
