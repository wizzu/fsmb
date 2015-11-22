package org.noname.fsmb.api.plain;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.noname.fsmb.api.CreateMessageServiceTestBase;
import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;

public class PlainCreateMessageServiceTest extends CreateMessageServiceTestBase {

  private PlainCreateMessageService service;

  @Before
  public void setUp() {
    service = new PlainCreateMessageService();
    MessageStore.INSTANCE.clearMessages();
  }

  @Test
  public void testPostCreatedOk() {
    Message postedMessage = getMessageForTest();

    Response response = service.post(
        postedMessage.getTitle(),
        postedMessage.getContent(),
        postedMessage.getSender(),
        postedMessage.getUrl().toString());

    validatePostCreatedOk(response, postedMessage);
  }

  @Test
  public void testInvalidURL() {
    String content = "Test Content Text";
    String url = "invalid-URL";

    Response response = service.post(null, content, null, url);

    validateInvalidURL(response);
  }

  @Test
  public void testTooLongString() {
    String content = LONG_STRING;
    String url = "http://localhost/fsmb";

    Response response = service.post(null, content, null, url);

    validateTooLongString(response);
  }
}
