package poo.model;

public class Mouse extends ComputerDevice {

    // Atributos
    private int dpi;
    private int buttons;

    // Constructores
    public Mouse() {
        dpi = 0;
        buttons = 0;
    }

    //Parametrizado
    public Mouse( String model, boolean wireless, int dpi, int buttons) {
        super(model, wireless);
        setDpi(dpi);
        setButtons(buttons);
    }

    // Accesores y mutadores
    public int getDpi() {
        return dpi;
    }

    public final void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getButtons() {
        return this.buttons;
    }

    public final void setButtons(int buttons) {
        this.buttons = buttons;
    }

    // toString
    //@Override
    //public String toString() {
    //    return String.format("%s %-6d %-2d%n ", super.toString(),getDpi(), getButtons());
    //}

}
