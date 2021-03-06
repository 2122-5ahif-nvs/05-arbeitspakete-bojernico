package org.acme.vertx;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/async")
public class EventResource {

    @Inject
    EventBus bus;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public Uni<String> greeting(@PathParam("name") String name) {
        return bus.<String>request("greeting", new MyName(name))
                .onItem().transform(Message::body);
    }
    /*
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public Uni<String> greeting(@PathParam("name") String name) {
        return bus.<String>request("greeting", name,
                new DeliveryOptions().setCodecName(MyNameCodec.class.getName()))
                .onItem().transform(Message::body);
    }*/

    @ConsumeEvent(value = "greeting"/*, codec = MyNameCodec.class*/)
    Uni<String> greeting(MyName name) {
        return Uni.createFrom().item(() -> "Hello " + name.getName());
    }
}