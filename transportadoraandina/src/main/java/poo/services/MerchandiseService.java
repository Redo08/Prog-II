package poo.services;

import java.io.IOException;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.parse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.modelo.Client;
import poo.modelo.Merchandise;

public class MerchandiseService implements Service<Merchandise> {

    // Atributos
    private List<Merchandise> list;
    private final String fileName;
    private final Service<Client> clients;

    // Constructor por defecto
    public MerchandiseService(Service<Client> clients) throws Exception {
        this.clients = clients;
        fileName = Utils.PATH + "Merchandise.json";

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    // Override Methods from the Interface
    @Override
    /**
     * Practicamente lo mismo que con ClientService, solo que aqui miramos los
     * parametros de la clase Merchandise.
     *
     * @param strJson La información de json en string
     * @return Una instancia tipo mercancia que cumple con los parametros
     * @throws Exception Si algo del strJson no cumple, saca la excepción
     */
    public Merchandise dataToAddOk(String strJson) throws Exception {
        JSONObject json = new JSONObject(strJson);
        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(8));
        }
        // Función para añadir los clientes al json
        updateClient(json);
        Utils.stringOk("id", 8, json);
        Utils.stringOk("content", 4, json);
        Utils.doubleOk("width", 0.1, 2.44, json);
        Utils.doubleOk("height", 0.1, 2.59, json);
        Utils.doubleOk("lenght", 0.1, 12.19, json);

        // dateOfArrival
        Utils.stringOk("dateOfArrival", 16, json);
        String dateOfArrival = json.getString("dateOfArrival");
        LocalDateTime dateArrival = parse(dateOfArrival);

        // dateOfDeparture
        Utils.stringOk("dateOfDeparture", 16, json);
        String dateOfDeparture = json.getString("dateOfDeparture");
        LocalDateTime dateDeparture = parse(dateOfDeparture);

        // Chequear si la fecha de entrada es antes de la fecha de salida
        if (dateDeparture.isBefore(dateArrival)) {
            throw new IllegalArgumentException("The date of departure cannot be before the date of entry");
        }

        Utils.stringOk("warehouse", 10, json);
        Merchandise m = new Merchandise(json);
        // Si ya esta en la lista, muestra que ya existe
        if (list.contains(m)) {
            throw new ArrayStoreException(String.format("The merchandise %s - %s already exists", m.getId(), m.getWarehouse()));
        }
        return m;
    }

    // Por referencia solo sirven los objetos y los arrays
    /**
     * Busca el cliente con el id dado en el json, y cuando lo encuentra se lo
     * asgina
     *
     * @param json El Objeto con la información
     * @throws Exception Si no existe el cliente
     */
    private void updateClient(JSONObject json) throws Exception {
        String idClient = json.getString("storer");
        JSONObject jsonClient = clients.get(idClient);
        if (jsonClient == null) {
            throw new IllegalArgumentException("Problems with getting the owner of the merchandise");
        }
        json.put("storer", jsonClient);

    }

    /**
     * Entonces busca el cliente con la información nueva, y con el cliente
     * encontrado, lo añade al json que se va a devolver.
     *
     * @param json Json a devolver
     * @param newData Nueva información para añadir al json
     * @throws Exception Si no se puede encontrar el cliente, saca excepción
     */
    private void updateClient(JSONObject json, JSONObject newData) throws Exception {
        String idClient = newData.getString("storer");
        JSONObject jsonClient = clients.get(idClient);
        if (jsonClient == null) {
            throw new IllegalArgumentException("Problems with getting the owner of the merchandise");
        }
        json.put("storer", jsonClient);
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        // busca si la información a añadir cumple los estandares
        Merchandise m = dataToAddOk(strJson);

        // Lo añade si se puede añadir
        if (list.add(m)) {
            Utils.writeJSON(list, fileName);
        }

        return new JSONObject().put("message", "ok").put("data", m.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Merchandise m = getItem(id);
        if (m == null) {
            throw new NoSuchElementException(String.format("Unable to find the warehouse with ID %s", id));
        }
        return m.toJSONObject();
    }

    @Override
    public Merchandise getItem(String id) throws Exception {
        int i = list.indexOf(new Merchandise(id));
        if (i > -1) {
            return list.get(i);
        }
        return null;
        // throw new Exception("La mercancia con id " + id + " no existe");
    }

    @Override
    public JSONObject getAll() {
        try {
            JSONArray data = new JSONArray();
            if(Utils.fileExists(fileName)){
                data = new JSONArray(Utils.readText(fileName));
            }
            return new JSONObject().put("message", "ok").put("data", data).put("size", data.length());
        } catch (IOException | JSONException e) {
            Utils.printStackTrace(e);
            return Utils.keyValueToJson("message", "Without access to client Data", "error", e.getMessage());
        }
    }

    @Override
    public final List<Merchandise> load() throws Exception {
        list = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(new Merchandise(jsonObj));
        }
        return list;
    }

    @Override
    /**
     * Este metodo lo que hace es hacer uso de stringOk para poder verificar si
     * los valores asociados a las llaves si cumplen con los requisitos y si si,
     * añadirlos.
     *
     * @param newData Nueva información para actualizar
     * @param current Información actual en el json
     * @return El json actualizado
     * @throws Exception Cuando alguna de las cosas a actualizar no cumple
     * parametros
     */
    public Merchandise getUpdated(JSONObject newData, Merchandise current) throws Exception {
        JSONObject updated = current.toJSONObject();
        LocalDateTime dateArrival = parse(updated.getString("dateOfArrival"));
        LocalDateTime dateDeparture = parse(updated.getString("dateOfDeparture"));

        if (newData.has("storer")) {
            updateClient(updated, newData);
            //JSONObject storer = clients.get(newData.getString("storer"));
            //updated.put("storer", storer);
        }
        if (newData.has("content")) {
            updated.put("content", Utils.stringOk("content", 4, newData));
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
        if (newData.has("dateOfArrival")) {
            dateArrival = parse(newData.getString("dateOfArrival"));
        }
        if (newData.has("dateOfDeparture")) {
            dateDeparture = parse(newData.getString("dateOfDeparture"));
        }
        // COmprobación de la fecha
        if (dateDeparture.isBefore(dateArrival)) {
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la de entrada");
        } else {
            updated.put("dateOfArrival", newData.getString("dateOfArrival"));
            updated.put("dateOfDeparture", newData.getString("dateOfDeparture"));
        }
        if (newData.has("warehouse")) {
            updated.put("warehouse", Utils.stringOk("warehouse", 10, newData));
        }
        Merchandise updatedMerchandise = new Merchandise(updated);
        return updatedMerchandise;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        JSONObject newData = new JSONObject(strJson);
        // buscar la mercancia que se debe actualizar
        Merchandise merchandise = getItem(id);
        int i = list.indexOf(merchandise);
        if (merchandise == null) {
            throw new NullPointerException("No se encontró la bodega " + id);
        }

        merchandise = getUpdated(newData, merchandise);

        list.set(i, merchandise);
        // actualizar el archivo de mercancia
        Utils.writeJSON(list, fileName);
        // devolver la Mercancia con los cambios realizados
        return new JSONObject().put("message", "ok").put("data", merchandise.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        JSONObject merchandise = get(id);
        if (merchandise == null) {
            throw new NoSuchElementException("No existe bodega con ID " + id);
        }
        Merchandise m = new Merchandise(merchandise);
        list.remove(m);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", m.toJSONObject());
    }

    @Override
    public Class<Merchandise> getDataType() {
        return Merchandise.class;
    }

    @Override
    public JSONObject size() {
        return new JSONObject().put("size", list.size()).put("message", "ok");
    }

    public void info() {
        System.out.println(String.format("Creada la instancia %s", getDataType().getSimpleName()));
    }

}
