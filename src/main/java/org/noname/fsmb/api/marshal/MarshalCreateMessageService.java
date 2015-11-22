package org.noname.fsmb.api.marshal;

import java.net.MalformedURLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;
import org.noname.fsmb.model.StringTooLongException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Path("/marshal/createMessage")
public class MarshalCreateMessageService {
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response post(String json) {
    //System.out.println("marshal.createMessage called");
    //System.out.println("json:\n"+json);

    int resultCode = 201;
    String result;

    Gson gson = new Gson();

    try {
      Message message = gson.fromJson(json, Message.class);
      MessageStore.INSTANCE.addMessage(message);
      result = "{message:\"ok\"}";
    } catch (JsonSyntaxException ex) {
      Throwable cause = ex.getCause();
      resultCode = 400;
      if (cause != null) {
        if (cause instanceof MalformedURLException) {
          result = "{message:\"invalid URL\"}";
        } else if (cause instanceof StringTooLongException) {
          result = "{message:\"string too long\"}";
        } else {
          // Some other GSON exception with nested cause.
          result = "{message:\"invalid json\"}";
        }
      } else {
        // Some GSON exception without cause.
        result = "{message:\"invalid json\"}";
      }
    }

    return Response.status(resultCode).entity(result).build();
  }
}
