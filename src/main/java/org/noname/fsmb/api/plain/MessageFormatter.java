package org.noname.fsmb.api.plain;

import java.util.List;

import org.noname.fsmb.model.Message;

// Generic MessageFormatter interface.

public interface MessageFormatter {
  String formatMessages(List<Message> messages);
  String contentType();
}
