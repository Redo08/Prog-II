package poo.modelo;

import org.json.JSONArray;
import org.json.JSONObject;

public enum DeliveryStatus {
    RECEIVED("Received"),
    IN_PREPARATION("It's in preparation"),
    SENT("Sent"),
    ON_THE_WAY("On the way"),
    DELIVERED("Already delivered"),
    RETURNED("Returned"),
    RESHIPPED("Reshippend"),
    LOST("The delivery's lost"),
    AVAILABLE_AT_OFFICE("The delivery is available at office");
    // UNDEFINED("Indefinido");

    // Atributos
    private final String value;

    // Constructores
    private DeliveryStatus(String value) {
        this.value = value;
    }

    public String getDeliveryStatus() {
        return value;
    }

    public static DeliveryStatus getEnum(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        for (DeliveryStatus s : values()) {
            if (value.equalsIgnoreCase(s.getDeliveryStatus())) {
                return s;
            }
        }
        throw new IllegalArgumentException();

    }

    public static JSONObject getAll() {
        JSONArray jsonArray = new JSONArray();
        for (DeliveryStatus s : values()) {
            jsonArray.put(
                    new JSONObject()
                            .put("ordinal", s.ordinal())
                            .put("key", s)
                            .put("value", s.value));
        }
        return new JSONObject().put("message", "ok").put("data", jsonArray);
    }

}
