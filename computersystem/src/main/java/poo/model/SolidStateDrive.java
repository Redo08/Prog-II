package poo.model;

public class SolidStateDrive extends StorageDevice {
    // Atributos
    private String interfaceType;

    // Constructores
    public SolidStateDrive(){
        interfaceType = "Undefined";
    }
    public SolidStateDrive(String model, boolean wireless, int usedCapacity, int freeCapacity, int speed, String interfaceType){
        super(usedCapacity, freeCapacity, speed, model, wireless);
        setInterfaceType(interfaceType);
    }

    // Accesores y mutadores
    public String getInterfaceType(){
        return interfaceType;
    }
    public final void setInterfaceType(String interfaceType){
        this.interfaceType = interfaceType;
    }
    
}
