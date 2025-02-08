package poo.model;

public class Keyboard extends ComputerDevice {

    // Atributos
    private boolean gamer;
    private boolean integrated;
    private Language language;

    // Constructores
    public Keyboard() {
        gamer = false;
        integrated = false;
        language = Language.UNDEFINED;
    }

    // Parametrizado
    public Keyboard(String model, boolean wireless, Language language, boolean gamer, boolean integrated) {
        super(model, wireless);
        setGamer(gamer);
        setIntegrated(integrated);
        setLanguage(language);
    }

    // Accesores y Mutadores
    public boolean getGamer() {
        return gamer;
    }

    public final void setGamer(boolean gamer) {
        this.gamer = gamer;
    }

    public boolean getIntegrated() {
        return integrated;
    }

    public final void setIntegrated(boolean integrated) {
        this.integrated = integrated;
    }

    public Language getLanguage() {
        return language;
    }

    public final void setLanguage(Language language) {
        this.language = language;
    }

}
