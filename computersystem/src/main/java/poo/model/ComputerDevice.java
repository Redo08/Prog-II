package poo.model;

import org.json.JSONObject;


public abstract class ComputerDevice {

    // Atributos
    protected String model;
    protected String type;
    protected boolean wireless;

    // Constructores
    public ComputerDevice(){
        model = "Undefined";
        type = "Undefined";
        wireless = false;
    }
    // Parametrizado
    public ComputerDevice(String model, boolean wireless){
        setModel(model);
        setWireless(wireless);
    }

    // Accesores y mutadores
    public String getType() {
        return this.getClass().getSimpleName();
    }
  
    public String getModel() {
        return model;
    }
    public final void setModel(String model){
        this.model = model;
    }
    public boolean getWireless(){
        return wireless;
    }
    public final void setWireless(boolean wireless){
        this.wireless = wireless;
    }
    // toString
    @Override
    public String toString(){
      //  String isWireless = wireless?"Yes":"No";
      //  return String.format("%-35s  %s ", model,isWireless);
      return (new JSONObject(this)).toString(2);
      // Esta impresión permite ahorrarse el toString a cada subclase
    }
}
