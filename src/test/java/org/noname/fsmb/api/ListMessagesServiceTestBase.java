package org.noname.fsmb.api;

import java.net.MalformedURLException;
import java.util.List;

import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;
import org.noname.fsmb.model.StringTooLongException;

public class ListMessagesServiceTestBase {
  private static int messageCounter = 0;

  protected void resetMessageCounter() {
    messageCounter = 0;
  }

  protected Message getMessageForTest() {
    messageCounter++;
    Message message = new Message();

    try {
      message.setSender("Test User #" + messageCounter);
      message.setContent("Test Content Text for message " + messageCounter);
      message.setTitle("Test Title " + messageCounter);
      message.setUrl("http://localhost/msg/" + messageCounter);
    } catch (StringTooLongException ex) {
      ex.printStackTrace();
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    }

    return message;
  }

  protected void addMessages(int count) {
    for (int i = 0; i < count; i++) {
      MessageStore.INSTANCE.addMessage(getMessageForTest());
    }
  }

  // Note: This doesn't handle null titles.
  protected Message findMessageByTitle(String title) {

    List<Message> messages = MessageStore.INSTANCE.listMessages();
    for (Message message : messages) {
      if (message.getTitle().equals(title)) {
        return message; // found matching message
      }
    }

    return null;
  }

}
