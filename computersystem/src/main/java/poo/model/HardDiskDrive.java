package poo.model;

public class HardDiskDrive extends StorageDevice {
    // Atributos
    private int rpm;
    private double inches;

    // Constructores
    public HardDiskDrive(){
        rpm = 0;
        inches = 0;
    }
    public HardDiskDrive(String model, boolean wireless, int usedCapacity, int freeCapacity, int speed,int rpm, double inches){
        super(usedCapacity,freeCapacity,speed, model, wireless);
        setRpm(rpm);
        setInches(inches);
    }
    
    // Accesores y  mutadores
    public int getRpm(){
        return rpm;
    }
    public final void setRpm(int rpm){
        this.rpm = rpm;
    }
    public double getInches(){
        return inches;
    }
    public final void setInches(double inches){
        this.inches = inches;
    }
}
