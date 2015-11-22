package org.noname.fsmb.api.plain;

import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.noname.fsmb.model.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MessageFormatterV2XML implements MessageFormatter {
  private static final String CONTENT_TYPE = MediaType.APPLICATION_XML;

  public String formatMessages(List<Message> messages) {
    String result = null;

    // Hide any errors (unlikely). To properly handle these via raising, we
    // should have a generic FormattingException defined which could be raised
    // through the formatMessages() method in the MessageFormatter interface.
    try {
      result = formatMessagesToXML(messages);
    } catch (ParserConfigurationException e) {
      // TODO: Exception should be logged.
      // Getting an exception here is unlikely, as the parser configuration
      // is constant/default, not based on any parameters.
      e.printStackTrace();
    } catch (TransformerException e) {
      // TODO: Exception should be logged.
      // Getting an exception here is unlikely, as the transformer's input
      // data comes from internal data stores, which SHOULD validate the
      // data. The transformer configuration is constant, not based on any
      // parameters.
      e.printStackTrace();
    }
    return result;
  }

  protected String formatMessagesToXML(List<Message> messages) throws ParserConfigurationException, TransformerException {
    // Setup XML DOM builder.
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.newDocument();

    // Create XML root element.
    Element messagesElement = doc.createElement("messages");
    doc.appendChild(messagesElement);

    // Append each message.
    for (Message message : messages) {
      messagesElement.appendChild(formatMessage(doc, message));
    }

    // Convert to string.
    return convertDocumentToString(doc);
  }

  protected Node formatMessage(Document doc, Message message) {
    Element messageElement = doc.createElement("message");

    if (message.getTitle() != null) {
      messageElement.setAttribute("title", message.getTitle());
    }
    if (message.getSender() != null) {
      messageElement.setAttribute("sender", message.getSender());
    }
    if (message.getUrl() != null) {
      messageElement.setAttribute("url", message.getUrl().toString());
    }

    // For XML, content is included as text node contained within the
    // message element.
    if (message.getContent() != null) {
      messageElement.appendChild(doc.createTextNode(message.getContent()));
    }

    return messageElement;
  }

  private String convertDocumentToString(Document doc) throws TransformerException {
    DOMSource domSource = new DOMSource(doc);
    StringWriter writer = new StringWriter();
    StreamResult result = new StreamResult(writer);
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer();
    transformer.transform(domSource, result);
    return writer.toString();
  }

  public String contentType() {
    return CONTENT_TYPE;
  }
}
