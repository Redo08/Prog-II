package poo.modelo;

import java.time.LocalDateTime;

import org.json.JSONObject;

public class Status implements Exportable {

    // Atributos
    private LocalDateTime dateTime;
    private DeliveryStatus deliveryStatus;

    // Constructor por defecto
    public Status() {
        this(LocalDateTime.now(), DeliveryStatus.RECEIVED);
    }

    // Constructor parametrizado
    public Status(LocalDateTime dateTime, DeliveryStatus deliveryStatus) {
        setDateTime(dateTime);
        setDeliveryStatus(deliveryStatus);
    }

    // Constructor copia
    public Status(Status s) {
        this(s.dateTime, s.deliveryStatus);
    }

    // Constructor JSON
    public Status(JSONObject json) {
        this(
                LocalDateTime.parse(json.getString("dateTime")),
                json.getEnum(DeliveryStatus.class, "deliveryStatus")
        );
    }

    // Accesores y mutadores
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public final void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public final void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    // Methods
    public String getId() {
        return String.valueOf(super.hashCode());
    }

    // Override methods
    @Override
    public JSONObject toJSONObject() {
        return new JSONObject(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        return this.deliveryStatus.equals(((Status) obj).deliveryStatus);
    }

}
