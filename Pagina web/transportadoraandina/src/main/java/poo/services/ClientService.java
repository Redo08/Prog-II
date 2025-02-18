package poo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poo.helpers.Utils;
import poo.modelo.Client;

public class ClientService implements Service<Client> {

    // Atributos
    private List<Client> list;
    private final String fileName;

    // Constructor por defecto
    public ClientService() throws Exception {
        fileName = Utils.PATH + "Client.json";

        if (Utils.fileExists(fileName)) {
            load();
        } else {
            list = new ArrayList<>();
        }
    }

    private void exists(JSONObject client) throws Exception {
        String id = client.getString("id");

        // Buscar el cliente en mercancias y si existe no permitir elminarlo
        if (Utils.exists(Utils.PATH + "Merchandise", "storer", client)) {
            throw new Exception(String.format("No eliminado. El cliente %s tiene mercancias registradas", id));
        }

        // Buscar el cliente en envios y si existe no permitir eliminarlo
        exists("Pack", client);
        exists("Envelope", client);
        exists("Box", client);
        exists("Sack", client);
    }

    /**
     *
     * @param filename Nombre del archivo que se busca
     * @param client El JSONObject del que se saca la información
     * @throws Exception Cuando no exista, saca la excepción
     */
    private void exists(String filename, JSONObject client) throws Exception {
        if (Utils.exists(Utils.PATH + filename, "sender", client) || Utils.exists(Utils.PATH + filename, "addressee", client)) {
            throw new Exception(String.format("No eliminado. El cliente %s esta registrado en envíos de tipo %s", client.getString("id"), filename));
        }

    }

    // Override Methods from the interface
    @Override
    /**
     * Este metodo lo que hace es hacer uso de stringOk para poder verificar si
     * los valores asociados a las llaves si cumplen con los requisitos y si si,
     * añadirlos.
     *
     * @param newData La nueva información del cliente
     * @param current El cliente actual
     * @return Un nuevo cliente con la información nueva actualizada en el
     * anterior cliente
     */
    public Client getUpdated(JSONObject newData, Client current) throws Exception {
        JSONObject updated = current.toJSONObject();
        if (newData.has("name")) {
            updated.put("name", Utils.stringOk("name", 1, newData));
        }
        if (newData.has("address")) {
            updated.put("address", Utils.stringOk("address", 10, newData));
        }
        if (newData.has("phoneNumber")) {
            updated.put("phoneNumber", Utils.stringOk("phoneNumber", 10, newData));
        }
        if (newData.has("city")) {
            updated.put("city", Utils.stringOk("city", 4, newData));
        }
        Client updatedClient = new Client(updated);
        return updatedClient;
    }

    @Override
    /**
     * Chequea si los parametros de las key de json si cumplen. También mira si
     * tiene id, o si no crea uno.
     *
     * @param strJson El string de Json
     * @return Instancia tipo Client
     */
    public Client dataToAddOk(String strJson) {
        JSONObject json = new JSONObject(strJson);
        int[] arrayValues = {5, 1, 10, 10, 4};
        if (!json.has("id") || json.getString("id").isBlank()) {
            json.put("id", Utils.getRandomKey(5));
        }
        for (Object i : json.names()) {
            int u = 0;
            Utils.stringOk(i.toString(), arrayValues[u], json);
            u++;
        }

        Client client = new Client(json);
        if (list.contains(client)) {
            throw new ArrayStoreException(String.format("El cliente %s - %s ya existe", client.getId(), client.getName()));
        }
        return client;
    }

    @Override
    public JSONObject add(String strJson) throws Exception {
        Client c = dataToAddOk(strJson);

        if (list.add(c)) {
            Utils.writeJSON(list, fileName);
        }

        return new JSONObject().put("message", "ok").put("data", c.toJSONObject());
    }

    @Override
    public JSONObject get(int index) {
        return list.get(index).toJSONObject();
    }

    @Override
    public JSONObject get(String id) throws Exception {
        Client c = getItem(id);
        if (c == null) {
            throw new NoSuchElementException(String.format("No se encontro el cliente con ID %s", id));
        }
        return c.toJSONObject();
    }

    @Override
    public Client getItem(String id) throws Exception {
        int i = list.indexOf(new Client(id));
        if (i > -1) {
            return list.get(i);
        }
        return null;
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
            return Utils.keyValueToJson("message", "Sin acceso a datos de clientes", "error", e.getMessage());
        }
    }

    @Override
    public final List<Client> load() throws Exception {
        list = new ArrayList<>();
        JSONArray jsonArr = new JSONArray(Utils.readText(fileName));
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            list.add(new Client(jsonObj));
        }
        return list;
    }

    @Override
    public JSONObject update(String id, String strJson) throws Exception {
        // crear un JSONObject con las claves y valores que hay que actualizar
        JSONObject newData = new JSONObject(strJson);
        // buscar el cliente que se debe actualizar y recordar a posición
        Client client = getItem(id);
        int i = list.indexOf(client);
        if (client == null) {
            throw new NullPointerException("No se encontró el cliente " + id);
        }
        client = getUpdated(newData, client);

        // Buscar la posición del cliente en la lista y actualizarlo
        list.set(i, client);
        // actualizar el archivo de clientes
        Utils.writeJSON(list, fileName);
        // devolver el cliente con los cambios realizados
        return new JSONObject().put("message", "ok").put("data", client.toJSONObject());
    }

    @Override
    public JSONObject remove(String id) throws Exception {
        JSONObject client = get(id);
        if (client == null) {
            throw new NoSuchElementException("No existe el cliente con ID " + id);
        }
        Client c = new Client(client);
        exists(client);
        list.remove(c);
        Utils.writeJSON(list, fileName);
        return new JSONObject().put("message", "ok").put("data", c.toJSONObject());
    }

    @Override
    public Class<Client> getDataType() {
        return Client.class;
    }

    @Override
    public JSONObject size() {
       return new JSONObject().put("size", list.size()).put("message", "ok");
    }
    public void info() {
        System.out.println(String.format("Creada la instancia %s", getDataType().getSimpleName()));
    }

    
}
