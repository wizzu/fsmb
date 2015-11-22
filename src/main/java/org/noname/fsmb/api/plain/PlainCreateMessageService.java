package org.noname.fsmb.api.plain;

import java.net.MalformedURLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;
import org.noname.fsmb.model.StringTooLongException;

@Path("/plain/createMessage")
public class PlainCreateMessageService {

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response post(
      @FormParam("title") String title,
		  @FormParam("content") String content,
		  @FormParam("sender") String sender,
		  @FormParam("url") String url) {

    //System.out.println("plain.createMessage called");

    int resultCode = 201;
    String result;

    try {
      Message message = new Message();
      message.setTitle(title);
      message.setContent(content);
      message.setSender(sender);
      message.setUrl(url);

      MessageStore.INSTANCE.addMessage(message);
      result = "{message:\"ok\"}";

    // NOTE:
    // Exception handling could be done with ExceptionMapper which allows
    // mapping exceptions into responses in a generic way. Since we only
    // have two exception types, for now, it's straightforward to keep
    // the error handling in-line.
    } catch (MalformedURLException ex) {
      resultCode = 400;
      result = "{message:\"invalid URL\"}";
    } catch (StringTooLongException ex) {
      resultCode = 400;
      result = "{message:\"string too long\"}";
    }

    return Response.status(resultCode).entity(result).build();
  };
}
