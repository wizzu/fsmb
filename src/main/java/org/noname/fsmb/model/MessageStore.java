package org.noname.fsmb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageStore {
  // Very Simple Message Store Implementation.

  // Having this as one public variable for simplicity, for now.
  // This could be hidden and accessed through an access method,
  // or come from a factory or similar. In multi-process or
  // multi-server environment this wouldn't work, there would
  // need to be a central store with some access mechanism
  // (database, API, filesystem, etc.)
  public static final MessageStore INSTANCE = new MessageStore();

  private List<Message> messages = new ArrayList<Message>();

  public void addMessage(Message message) {
    messages.add(message);
  }
  public List<Message> listMessages() {
    return Collections.unmodifiableList(messages);
  }

  // For testing support.
  public void clearMessages() {
    messages.clear();
  }
}
