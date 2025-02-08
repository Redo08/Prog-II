package poo.model;

public class FlashMemory extends StorageDevice {
    // Atributos
    private String portType;

    // Constructores
    public FlashMemory(){
        portType = "Undefined";
    }
    public FlashMemory(String model, boolean wireless, int usedCapacity, int freeCapacity, int speed, String portType){
        super(usedCapacity,freeCapacity,speed, model, wireless);
        setPortType(portType);
    }

    // Accesores y mutadores
    public String getPortType(){
        return portType;
    }
    public final void setPortType(String portType){
        this.portType = portType;
    }
}
