package org.noname.fsmb.api.marshal;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.noname.fsmb.api.CreateMessageServiceTestBase;
import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;

public class MarshalCreateMessageServiceTest extends CreateMessageServiceTestBase {

  private MarshalCreateMessageService service;

  @Before
  public void setUp() {
    service = new MarshalCreateMessageService();
    MessageStore.INSTANCE.clearMessages();
  }

  @Test
  public void testPostCreatedOk() {
    Message postedMessage = getMessageForTest();

    Response response = service.post(
        "{\"sender\":\""+postedMessage.getSender()+
        "\",\"content\":\""+postedMessage.getContent()+
        "\",\"title\":\""+postedMessage.getTitle()+
        "\",\"url\":\""+postedMessage.getUrl().toString()+"\"}");

    validatePostCreatedOk(response, postedMessage);
  }

  @Test
  public void testInvalidURL() {
    String content = "Test Content Text";
    String url = "invalid-URL";

    Response response = service.post("{\"content\":\""+content+"\",\"url\":\""+url+"\"}");

    validateInvalidURL(response);
  }

  @Test
  @Ignore("GSON does not support limiting field value length")
  public void testTooLongString() {
    String content = LONG_STRING;
    String url = "http://localhost/fsmb";

    Response response = service.post("{\"content\":\""+content+"\",\"url\":\""+url+"\"}");

    validateTooLongString(response);
  }

}
