package poo.model;

import org.json.JSONArray;
import org.json.JSONObject;

public enum Language {
    SPANISH("Idioma español"),
    ENGLISH("Idioma Inglés"),
    FRENCH("Idioma francés"),
    UNDEFINED("Idioma no definido");

    /*
     * Language tp = Language.SPANISH;
     * System.out.println(tp | tp.toString()); // SPANISH
     * Langauge.valueOf("SPANISH") // Language.SPANISH
     */
    private final String value;

    private Language(String value) {
        this.value = value;
    }

    // Accesor
    /**
     * Devuelve el valor de un constante enumerada en formato humano Ejemplo:
     * System.out.println(tp.getValue()); // Devuelve: Idioma español
     *
     * @return El valor del argumento value, recibido por el constructor
     */
    public String getValue() {
        return value;
    }

    // Metodo
    /**
     *Dado un string, devuelve la constante enumerada correspondiente. Ejemplo:
     * Language.getEnum("Idioma español") devuelve Language.SPANISH
     * no confundir con Language.valueOf("CONSTANTE_ENUMERADA")
     * 
     * @param value La expresión para humanos correspondiente a la constante
     * @return La constante enumerada
     */
    public static Language getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        for (Language l : values()) {
            if (value.equalsIgnoreCase(l.getValue())) {
                return l;
            }
        }
        throw new IllegalArgumentException();
    }

    public static JSONObject getAll() {
        JSONArray jsonArray = new JSONArray();
        for (Language v : values()) {
            jsonArray.put(
                    new JSONObject()
                            .put("ordinal", v.ordinal())
                            .put("key", v)
                            .put("value", v.value));
        }
        return new JSONObject().put("message", "ok").put("data", jsonArray);
    }
}
