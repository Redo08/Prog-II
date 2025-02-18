package poo.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.modelo.Client;
import poo.modelo.Delivery;
import poo.modelo.DeliveryStatus;
import poo.modelo.Status;

public class DeliveryService implements Service<Delivery> {

    // Atributos
    private final Class<? extends Delivery> subclass;
    private final Service<Client> clients;
    private final String fileName;
    protected List<Delivery> list;

    // Constructor 
    public DeliveryService(Class<? extends Delivery> subclass, Service<Client> clients) throws Exception {
        this.subclass = subclass;
        this.clients = clients;
        fileName = Utils.PATH + subclass.getSimpleName() + ".json";

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }

    }

    private void updateClient(String idClient, JSONObject json) throws Exception {

        JSONObject jsonClient = clients.get(json.getString(idClient));
        if (jsonClient == null) {
            throw new IllegalArgumentException(String.format("Error al determinar el cliente %s del envio.", idClient));
        }
        json.put(idClient, jsonClient);
    }

    private void updateClient(String idClient, JSONObject json, JSONObject newData) throws Exception {

        JSONObject jsonClient = clients.get(newData.getString(idClient));
        if (jsonClient == null) {
            throw new IllegalArgumentException(String.format("Error al determinar el cliente %s del envio.", idClient));
        }
        json.put(idClient, jsonClient);
    }

    // Override methods
    @Override
    public JSONObject add(String strJson) throws Exception {
        Delivery delivery = dataToAddOk(strJson);
        // Lo añade si lo puede añadir
        if (list.add(delivery)) {
            Utils.writeJSON(list, fileName);
        }
        return new JSONObject().put("message", "ok").put("data", delivery.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String numGuide) throws Exception {
        Delivery d = getItem(numGuide);
        if (d == null) {
            throw new NoSuchElementException(String.format("No se encontro el envio con numGuide %s", numGuide));
        }
        return d.toJSONObject();
    }

    @Override
    public Delivery getItem(String numGuide) throws Exception {
        Delivery e = subclass.getConstructor(String.class).newInstance(numGuide);
        int i = list.indexOf(e);
        if (i > -1) {
            return list.get(i);
        } else {
            return null;
        }
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray();
            if (Utils.fileExists(fileName)) {
                data = new JSONArray(Utils.readText(fileName));
            }
            return new JSONObject().put("message", "ok").put("data", data).put("size", data.length());
        } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message", "Sin acceso a datos de envios", "error", e.getMessage());
        }
    }

    @Override
    public final List<Delivery> load() throws Exception {
        list = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(subclass.getConstructor(JSONObject.class).newInstance(jsonObj));
        }
        return list;
    }

    @Override
    public JSONObject update(String numGuide, String strJson) throws Exception {
        JSONObject newData = new JSONObject(strJson);

        // Buscar y referenciar el envío que se debe actualizar
        Delivery delivery = subclass.cast(getItem(numGuide));

        if (delivery == null) {
            throw new NullPointerException("No se encontro el envío " + numGuide);
        }

        // Buscar la posición del envio en la lista y actualizarlo
        int i = list.indexOf(delivery);
        delivery = getUpdated(newData, delivery);
        list.set(i, delivery);

        // Actualizar el archivo y retornar el reporte de la acción
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", delivery.toJSONObject());
    }

    @Override
    public JSONObject remove(String numGuide) throws Exception {
        JSONObject delivery = get(numGuide);
        if (delivery == null) {
            throw new NoSuchElementException("No existe Envio con numero de guia " + numGuide);
        }
        Delivery d = subclass.getConstructor(JSONObject.class).newInstance(delivery);
        list.remove(d);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", d.toJSONObject());
    }

    @Override
    public Delivery dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        JSONArray statuses = new JSONArray();

        if (!json.has("numGuide") || json.getString("numGuide").isBlank()) {
            json.put("numGuide", Utils.getRandomKey(8));
        }
        if (!json.has("status")) {
            // Se crea un nuevo status
            Status status = new Status(LocalDateTime.now().withNano(0), DeliveryStatus.RECEIVED);
            // Se añade a un array 
            statuses.put(status.toJSONObject());
            // Se agrega al json
            json.put("status", statuses);
        }
        if (!json.has("isFragile")) {
            json.put("isFragile", false);
        }
        // Información a revisar
        updateClient("sender", json);
        updateClient("addressee", json);
        if (!json.has("content")) {
            json.put("content", "Documentos");
        } else {
            Utils.stringOk("content", 3, json);

        }
        if (!json.has("weight")) {
            json.put("weight", 0);
        } else {
            Utils.doubleOk("weight", 0, 100000, json);
        }
        if (!json.has("value")) {
            json.put("value", 0);
        } else {
            Utils.doubleOk("value", 0, 10000000, json);
        }

        // Creación de la subclase a partir del json
        Delivery delivery = subclass.getConstructor(JSONObject.class).newInstance(json);
        // Condiciones
        if (json.get("sender").equals(json.get("addressee"))) {
            throw new IllegalArgumentException("Se espera un destinatario diferente al remitente: id = " + json.getString("numGuide"));
        }
        if (delivery.getNumGuide().equals(json.getString("numGuide")) && list.contains(delivery)) {
            throw new ArrayStoreException(String.format("La instancia de %s con guia %s, ya existe", subclass.getSimpleName(), json.getString("numGuide")));
        }
        return delivery;
    }

    @Override
    public Delivery getUpdated(JSONObject newData, Delivery current) throws Exception {
        DeliveryStatus lastStatus = current.getStatus().getLast().getDeliveryStatus();
        JSONObject updated = new JSONObject(current);
        if (!"RETURNED|IN_PREPARATION|UNDEFINED|RECEIVED".contains(lastStatus.toString()) && !newData.has("status")) {
            throw new IllegalStateException(String.format("Un envío en estado %s no puede ser cambiado", lastStatus.toString()));
        }

        if (newData.has("status") && statusLogic(newData, current)) {
            // Crear función de verificación logica de posición estados
            /*   if (statusLogic(newData, current)) {
                // Saca los estados actuales
                JSONArray listStatus = updated.getJSONArray("status");

                // Saca el JSONArray de la petición
                JSONArray jsonNew = newData.getJSONArray("status");
                JSONObject newObject = jsonNew.getJSONObject(0);
                System.out.println(newObject);

                // Añadir al array principal
                listStatus.put(newObject);

                // Añadir al JSONObject de verdad
                updated.put("status", listStatus);
                System.out.println("Ya termine de añadir");
            } */

            updated.put("status", newData.getJSONArray("status"));
            // System.out.println("Importante: No se permite modificar los estados de un envio de tipo " + getDataType().getSimpleName());
            // newData.remove("status");
        }
        if (newData.has("sender")) {
            updateClient("sender", updated, newData);
        }
        if (newData.has("addressee")) {
            updateClient("addressee", updated, newData);
        }
        if (newData.has("weight")) {
            updated.put("weight", Utils.doubleOk("weight", 0, 100000000, newData));
        }
        if (newData.has("isFragile")) {
            updated.put("isFragile", newData.getBoolean("isFragile"));
        }
        if (newData.has("content")) {
            updated.put("content", Utils.stringOk("content", 4, newData));
        }
        if (newData.has("value")) {
            updated.put("value", Utils.doubleOk("value", 0, 10000000, newData));
        }
        if (newData.has("isCertified")) {
            updated.put("isCertified", newData.getBoolean("isCertified"));
        }
        if (newData.has("width")) {
            updated.put("width", Utils.doubleOk("width", 0.1, 2.44, newData));
        }
        if (newData.has("height")) {
            updated.put("height", Utils.doubleOk("height", 0.1, 2.59, newData));
        }
        if (newData.has("lenght")) {
            updated.put("lenght", Utils.doubleOk("lenght", 0.1, 12.19, newData));
        }

        Delivery subclase = subclass.getConstructor(JSONObject.class).newInstance(updated);

        return subclase;
    }

    /**
     * Primero que todo, esto esta muy feo y pido perdón por eso. Ahora bien,
     * esto aqui tenemos la verificación del orden de los estados
     *
     * @param newData La información que recibimos de la petición
     * @param current La información que ya teniamos
     * @return returnamos un verdadero o falso si es que si cumple con los
     * parametros
     * @throws Exception Si no cumple con los parametros sacamos excepción
     */
    public Boolean statusLogic(JSONObject newData, Delivery current) throws Exception {
        // Sacamos el JSONArray que se tiene deliveryStatus
        JSONArray actualTotal = new JSONArray(current.getStatus());
        int last = actualTotal.length() - 1;
        //System.out.println(actualTotal);
        //System.out.println("_---------------------");
        //System.out.println(actualTotal.getJSONObject(last).get("deliveryStatus"));
        // Sacamos el string del ultimo
        String actual = actualTotal.getJSONObject(last).get("deliveryStatus").toString();
        //System.out.println(actual);
        // Actual anterior 
        String actualAnterior = "";
        if (last >= 1) {
            actualAnterior = actualTotal.getJSONObject(last - 1).get("deliveryStatus").toString();
        }
        //System.out.println(actual);
        //System.out.println(actualAnterior);

        JSONArray status = newData.getJSONArray("status");
        //System.out.println(status);
        JSONObject statusTo = status.getJSONObject((status.length() - 1));
        String statusToAdd = statusTo.getString("deliveryStatus");
        //System.out.println(statusToAdd);
        // String statusToAdd = status.get
        Boolean verification = false;

        if (actualTotal.length() < status.length()) {
            if(actual.equals("RETURNED")) {
                throw new IllegalArgumentException("After being returned you cannot add any more statuses.");
            }
            if (actual.equals("DELIVERED")) {
                if (statusToAdd.equals("RETURNED")) {
                    verification = true;
                } else {
                    throw new IllegalArgumentException("After delivered you can onlye have returned as a status.");
                }
            }
            if (actual.equals("AVAILABLE_AT_OFFICE")) {
                if (statusToAdd.equals("DELIVERED")) {
                    verification = true;
                } else {
                    throw new IllegalArgumentException("After available_at_office you can only have Delivered as a status");
                }
            }
            if (actual.equals("RESHIPPED")) {
                if (statusToAdd.equals("DELIVERED")) {
                    verification = true;
                } else if (statusToAdd.equals("AVAILABLE_AT_OFFICE")) {
                    verification = true;
                } else if (statusToAdd.equals("RESHIPPED") && !actualAnterior.equals("RESHIPPED")) {
                    verification = true;
                } else {
                    throw new IllegalArgumentException(String.format("After reshipped, you can only have delivered, available at office or reshipped only if it's it second time"));
                }
            }
            if (actual.equals("ON_THE_WAY")) {
                switch (statusToAdd) {
                    case "RESHIPPED" ->
                        verification = true;
                    case "DELIVERED" ->
                        verification = true;
                    case "LOST" ->
                        verification = true;
                    default ->
                        throw new IllegalArgumentException("After the status On the way, you can only have, reshipped, delivered and lost");
                }
            }
            if (actual.equals("SENT")) {
                switch (statusToAdd) {
                    case "ON_THE_WAY" ->
                        verification = true;
                    case "LOST" ->
                        verification = true;
                    default ->
                        throw new IllegalArgumentException("After the status Sent, you can only have On the way and Lost.");
                }
            }
            if (actual.equals("IN_PREPARATION")) {
                switch (statusToAdd) {
                    case "SENT" ->
                        verification = true;
                    case "LOST" ->
                        verification = true;
                    default ->
                        throw new IllegalArgumentException("After the status In preparation, you can only have sent or lost.");
                }
            }
            if (actual.equals("RECEIVED")) {
                switch (statusToAdd) {
                    case "IN_PREPARATION" ->
                        verification = true;
                    case "LOST" ->
                        verification = true;
                    default ->
                        throw new IllegalArgumentException("After the status Received, you can only have in preparation or lost.");
                }
            }
            if (actual.equals("LOST")) {
                if (actualAnterior.equals("ON_THE_WAY") && (statusToAdd.equals("RESHIPPED") || statusToAdd.equals("DELIVERED"))) {
                    verification = true;
                } else if (actualAnterior.equals("RESHIPPED") && (statusToAdd.equals("DELIVERED") || statusToAdd.equals("AVAILABLE_AT_OFFICE") || (statusToAdd.equals("RESHIPPED") && !actualAnterior.equals("RESHIPPED")))) {
                    verification = true;
                } else if (actualAnterior.equals("SENT") && (statusToAdd.equals("ON_THE_WAY"))) {
                    verification = true;
                } else if (actualAnterior.equals("IN_PREPARATION") && statusToAdd.equals("SENT")) {
                    verification = true;
                } else if (actualAnterior.equals("RECEIVED") && statusToAdd.equals("IN_PREPARATION")) {
                    verification = true;
                } else {
                    throw new IllegalArgumentException("After Lost, you can only have the next logical status before you got lost");
                }
            }
            //System.out.println(verification);
            return verification;
        } else {
            return true;
        }
    
    }

    @Override
    public JSONObject size() {
        return new JSONObject().put("size", list.size()).put("message", "ok");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Delivery> getDataType() {
        return (Class<Delivery>) subclass;
    }

}
