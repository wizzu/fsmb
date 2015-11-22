package org.noname.fsmb.api;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;
import org.noname.fsmb.model.StringTooLongException;

public class CreateMessageServiceTestBase {

  protected static String LONG_STRING = longString();
  protected static String longString() {
    int length = 1024 * 100;
    StringBuilder s = new StringBuilder(length);
    for (int i = 0; i < length; i++){
       s.append("A");
    }
    return s.toString();
  }

  protected Message getMessageForTest() {
    Message message = new Message();

    try {
      message.setSender("Test User");
      message.setContent("Test Content Text");
      message.setTitle("Test Title");
      message.setUrl("http://localhost/fsmb");
    } catch (StringTooLongException ex) {
      ex.printStackTrace();
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    }

    return message;
  }

  @Before
  public void setUp() {
    MessageStore.INSTANCE.clearMessages();
  }

  public void validatePostCreatedOk(Response response, Message postedMessage) {
    // Note: Using the simplistic actual MessageStore instance here.
    // For proper testing, this would have to be mocked or controlled
    // somehow, but how that is done depends on the actual storage
    // implementation. E.g. would need a unit test database from which
    // content could be verified, or mocking the store layer.
    MessageStore messageStore = MessageStore.INSTANCE;

    assertEquals(201, response.getStatus());

    List<Message> messages = messageStore.listMessages();
    assertEquals(1, messages.size());
    Message message = messages.get(0);
    assertEquals(postedMessage.getSender(), message.getSender());
    assertEquals(postedMessage.getContent(), message.getContent());
    assertEquals(postedMessage.getTitle(), message.getTitle());
    assertEquals(postedMessage.getUrl().toString(), message.getUrl().toString());
  }

  public void validateInvalidURL(Response response) {
    MessageStore messageStore = MessageStore.INSTANCE;

    assertEquals(400, response.getStatus());
    List<Message> messages = messageStore.listMessages();
    assertEquals(0, messages.size());
  }

  public void validateTooLongString(Response response) {
    MessageStore messageStore = MessageStore.INSTANCE;

    assertEquals(400, response.getStatus());
    List<Message> messages = messageStore.listMessages();
    assertEquals(0, messages.size());
  }

}
