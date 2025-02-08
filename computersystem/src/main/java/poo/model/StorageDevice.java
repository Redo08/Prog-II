package poo.model;

public abstract class StorageDevice extends ComputerDevice{

    //Atributos
    protected int usedCapacity;
    protected int freeCapacity;
    protected int speed;

    // Constructores
    public StorageDevice(){
        usedCapacity = 0;
        freeCapacity = 0;
        speed = 0;
    }

    public StorageDevice(int usedCapacity, int freeCapacity, int speed, String model, boolean wireless){
        super(model, wireless);
        setUsedCapacity(usedCapacity);
        setFreeCapacity(freeCapacity);
        setSpeed(speed);
       
    }

    // Accesores y mutadores
    public int getCapacity(){
        return usedCapacity + freeCapacity;
    }
    public int getUsedCapacity(){
        return usedCapacity;
    }
    public final void setUsedCapacity(int usedCapacity){
        this.usedCapacity = usedCapacity;
    }
    public int getFreeCapacity(){
        return freeCapacity;
    }
    public final void setFreeCapacity(int freeCapacity){
        this.freeCapacity = freeCapacity;
    }
    public int getSpeed(){
        return speed;
    }
    public final void setSpeed(int speed){
        this.speed = speed;
    }


    
}
