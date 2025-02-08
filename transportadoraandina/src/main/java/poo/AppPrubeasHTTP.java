package poo;

import java.util.Locale;

import org.json.JSONObject;

import io.javalin.Javalin;
import poo.helpers.Utils;
import poo.modelo.Client;
import poo.modelo.DeliveryStatus;
import poo.services.ClientService;
import poo.services.Service;

public class AppPrubeasHTTP {

    public static void main(String[] args) throws Exception {
        int port = 7070;
        // esencial para estandarizar el formato monetario con separador de punto decimal, no con coma
        Locale.setDefault(Locale.of("es_CO"));
        Service<Client> clientService = new ClientService();

        Javalin.create(/*config*/)
                .post(
                        "/cliente",
                        ctx -> {
                            JSONObject response = clientService.add(ctx.body());
                            ctx.json(response.toString());
                        })
                .get(
                        "/cliente",
                        ctx -> {
                            JSONObject response = clientService.getAll();
                            ctx.json(response.toString());
                        })
                .get(
                        "/cliente/{param}",
                        ctx -> {
                            String arg = ctx.pathParam("param");
                            JSONObject response;

                            if (arg.matches("-?\\d+")) {
                                // si es un número en base 10, buscar por posición en la lista
                                int i = Integer.parseInt(arg, 10);
                                response = clientService.get(i);
                            } else {
                                // en caso contrario, buscar por ID
                                response = clientService.get(arg);
                            }
                            ctx.json(response.toString());
                        })
                .patch(
                        "/cliente/{param}",
                        ctx -> {
                            String id = ctx.pathParam("param");
                            String newData = ctx.body();
                            ctx.json(clientService.update(id, newData).toString());
                        })
                        .get("/envio/estados", ctx -> ctx.json(DeliveryStatus.getAll().toString()))
                .delete(
                        "/cliente/{param}",
                        ctx -> ctx.json(clientService.remove(ctx.pathParam("param"))))
                .exception(
                        Exception.class,
                        (e, ctx) -> {
                            Utils.printStackTrace(e);
                            String error = Utils.keyValueToStrJson("message", e.getMessage(), "request", ctx.fullUrl());
                            ctx.json(error).status(400);
                        })
                .start(port);
    }
}
