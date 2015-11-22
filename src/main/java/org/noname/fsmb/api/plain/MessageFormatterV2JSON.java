package org.noname.fsmb.api.plain;

import org.json.JSONObject;
import org.noname.fsmb.model.Message;

public class MessageFormatterV2JSON extends MessageFormatterJSON {
  
  protected JSONObject formatMessage(Message message) {
    JSONObject jsonMessage = super.formatMessage(message);
    if (message.getUrl() != null) {
      jsonMessage.append("url", message.getUrl().toString());
    }
    return jsonMessage;
  }

}
