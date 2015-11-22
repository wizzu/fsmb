package org.noname.fsmb.api.plain;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;

@Path("/plain/listMessages")
public class PlainListMessagesService {

  private static final MessageFormatter FORMATTER_V1_JSON = new MessageFormatterJSON();
  private static final MessageFormatter FORMATTER_V2_JSON = new MessageFormatterV2JSON();
  private static final MessageFormatter FORMATTER_V2_XML  = new MessageFormatterV2XML();

  @GET
  @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
  public Response get(@QueryParam("version") int version, @QueryParam("format") String format) {
    // Response format could also be decided based on sender's Accept header,
    // instead of a query parameter.

    //System.out.println("plain.listMessages called, version="+version+", format="+format);

    Response response;
    List<Message> messages = MessageStore.INSTANCE.listMessages();

    // Discussion on versioning:

    // Implemented versioning here with version-specific methods.
    // Later refactoring could switch implementation to dedicated
    // classes for each version, if the implementation complexity
    // grows.

    // Version detection here is depending  on version parameter. This
    // is not ideal; better would be to version the path (but specification
    // apparently forbade this).
    // Another way to version would be using custom headers to provide
    // the version information, whether that would be feasible would
    // depend on the caller (UI) implementation.

    // If path versioning is done, ideally the versioning is done via
    // the  full API version in the path, eg. "/v1/listMessages",
    // "/v2/listMessages", etc. so that the full API can be versioned.
    // Alternatively the endpoints could be versioned independently,
    // e.g. "/listMessages/v1", "/listMessages/v2".

    switch (version) {
    // version == 0 if version parameter is not provided, default to v1
    case 0:
    case 1:
      response = getV1(messages);
      break;

    case 2:
      response = getV2(messages, format);
      break;

    default:
      response = Response.status(400).entity("{message:\"unknown version\"}").build();
      break;
    }

    return response;
  }

  private Response getV1(List<Message> messages) {
    return Response
        .status(200)
        .type(FORMATTER_V1_JSON.contentType())
        .entity(FORMATTER_V1_JSON.formatMessages(messages))
        .build();
  }

  private Response getV2(List<Message> messages, String format) {
    Response response;

    if (format == null || "json".equals(format)) {
      response = Response
          .status(200)
          .type(FORMATTER_V2_JSON.contentType())
          .entity(FORMATTER_V2_JSON.formatMessages(messages))
          .build();
    } else if ("xml".equals(format)) {
      response = Response
          .status(200)
          .type(FORMATTER_V2_JSON.contentType())
          .entity(FORMATTER_V2_XML.formatMessages(messages))
          .build();
    } else {
      response = Response.status(400).type("application/json").entity("{message:\"unknown format\"}").build();
    }

    return response;
  }

}
