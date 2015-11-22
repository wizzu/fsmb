package org.noname.fsmb.api.plain;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.noname.fsmb.model.Message;

public class MessageFormatterJSON implements MessageFormatter {
  private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON;

  public String formatMessages(List<Message> messages) {
    JSONArray jsonMessageList = new JSONArray();

    for (Message message : messages) {
      jsonMessageList.put(formatMessage(message));
    }

    return jsonMessageList.toString();
  }

  protected JSONObject formatMessage(Message message) {
    JSONObject jsonMessage = new JSONObject();

    jsonMessage.put("title", message.getTitle());
    jsonMessage.put("content", message.getContent());
    jsonMessage.put("sender", message.getSender());

    return jsonMessage;
  }

  public String contentType() {
    return CONTENT_TYPE;
  }
}
