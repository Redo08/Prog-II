package poo.model;

public class Monitor extends ComputerDevice {

    // Atributos
    private double inches;

    // Constructores
    public Monitor() {
        inches = 0;
    }

    // Parametrizado
    public Monitor(String model, boolean wireless, double inches) {
        super(model, wireless);
        setInches(inches);
        
    }

    // Accesores y mutadores
    public double getInches() {
        return inches;
    }
    public final void setInches(double inches){
        this.inches = inches;
    }

    // toString
    //@Override
    //public String toString() {
    //   return String.format("%s %-5f", super.toString(), getInches());
    //}

}
