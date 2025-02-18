package poo;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.json.JSONObject;

import poo.modelo.Client;
import poo.modelo.Delivery;
import poo.modelo.Status;
import poo.modelo.Pack;
import poo.modelo.DeliveryStatus;

public class Pruebas2 {
      public static void main(String[] args) throws Exception {

        ArrayList<Status> status = pruebaEstado();
        Client c = pruebaConstructoresClientes();
        Delivery paquete = new Pack(10, false, "Insumos", 30000, c, new Client(), status);
        System.out.println("-".repeat(20));
        System.out.println(paquete.toJSONObject().toString(2));
        String json = """
                        {
                            "content": "Insumos",
                            "numGuide": "4P5V4",
                            "weight": 10,
                            "isFragile": false,
                            "sender": {
                                "city": "Pereira",
                                "address": "El Lago",
                                "id": "C06",
                                "phoneNumber": "3125557777",
                                "name": "Andrea"
                            },
                            "value": 30000,
                            "addressee": {
                                "city": "",
                                "address": "",
                                "id": "MQGHX",
                                "phoneNumber": "",
                                "name": "NN"
                            },
                            "status": [
                                {
                                "deliveryStatus": "EN_PREPARACION",
                                "dateTime": "2024-10-06T17:35:23"
                                },
                                {
                                "deliveryStatus": "ENTREGADO",
                                "dateTime": "2024-10-06T17:35:23"
                                }
                            ]
                        }
                """;
        JSONObject jsonObject = new JSONObject(json);
        Pack p = new Pack(jsonObject);
        System.out.println("-".repeat(20));
        System.out.println(p.toJSONObject());
    }

    private static ArrayList<Status> pruebaEstado() {
        System.out.println();
        LocalDateTime ldt = LocalDateTime.now();
        Status e1 = new Status(ldt.plusDays(1) ,DeliveryStatus.IN_PREPARATION);
        Status e2 = new Status(ldt.plusDays(1) ,DeliveryStatus.DELIVERED);

        System.out.println(e1.equals(e2));
        System.out.println(ldt);
        System.out.println(ldt.withNano(0));
        System.out.println(ldt.truncatedTo(ChronoUnit.SECONDS));

        ArrayList<Status> status = new ArrayList<>();
        status.add(e1);
        status.add(e2);
        return status;
    }

    private static Client pruebaConstructoresClientes() {
        System.out.println();
        // constructor por defecto
        Client c1 = new Client();
        System.out.println(c1.toJSONObject());

        // constructor parametrizado
        Client c2 = new Client("C2", "Carlos", "Av. Fundadores", "3113334444", "Manizales");
        System.out.println(c2.toJSONObject());

        // Constructor copia
        Client c3 = new Client(c2);
        System.out.println(c3.toJSONObject());

        // Constructor que recibe sólo el ID y asigna los otros datos vacíos o arbitrarios
        Client c4 = new Client("C4");
        System.out.println(c4.toJSONObject());

        // Constructor que recibe todos los datos, menos el ID y genera éste último de forma aleatoria
        Client c5 = new Client("Jorge", "El Cable", "3128887777", "Manizales");
        System.out.println(c5.toJSONObject());


        JSONObject jsonObject = new JSONObject("{\"city\":\"Pereira\",\"address\":\"El Lago\",\"id\":\"C06\",\"phoneNumber\":\"3125557777\",\"name\":\"Andrea\"}");
        // Constructor que recibe un JSONObject
        Client c6 = new Client(jsonObject);
        System.out.println(c6.toJSONObject());

        return c6;
    }
}
