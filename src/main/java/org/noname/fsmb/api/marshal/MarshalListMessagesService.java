package org.noname.fsmb.api.marshal;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

@Path("/marshal/listMessages")
public class MarshalListMessagesService {

  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  public Response get(@QueryParam("version") int version, @QueryParam("format") String format) {
    //System.out.println("marshal.listMessages called, version="+version+", format="+format);

    // Response format could also be decided based on sender's Accept header,
    // instead of a query parameter.

    Response response;
    List<Message> messages = MessageStore.INSTANCE.listMessages();

    // Handle defaults: version 1, format json.
    if (version == 0) {
      // version == 0 if version parameter is not provided, default to v1
      version = 1;
    }
    if (format == null || format.length() == 0) {
      format = "json";
    }

    // Validate that version is valid (i.e. either 1 or 2).
    if (version >= 1 && version <= 2) {
      // Generate response with specified format.

      if ("json".equals(format)) {
        Gson gson = new GsonBuilder().setVersion(version).create();
        response = Response
            .status(200)
            .type("application/json")
            .entity(gson.toJson(messages))
            .build();
      } else {
        XStream xStream = new XStream();
        xStream.alias("messages", List.class);
        xStream.alias("message", Message.class);
        xStream.useAttributeFor(Message.class, "sender");
        xStream.useAttributeFor(Message.class, "title");
        xStream.useAttributeFor(Message.class, "url");
        // Unfortunately, XStream doesn't like Immutable wrappers, so
        // need to re-build the existing list as new List instance.
        List<Message> xstreamMessages = new ArrayList<Message>(messages);

        response = Response
            .status(200)
            .type("application/json")
            .entity(xStream.toXML(xstreamMessages))
            .build();
      }
    } else {
      response = Response.status(400).entity("{message:\"unknown version\"}").build();
    }

    return response;
  }

}
