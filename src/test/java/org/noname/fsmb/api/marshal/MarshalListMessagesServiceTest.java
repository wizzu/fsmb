package org.noname.fsmb.api.marshal;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.noname.fsmb.api.ListMessagesServiceTestBase;
import org.noname.fsmb.model.Message;
import org.noname.fsmb.model.MessageStore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MarshalListMessagesServiceTest extends ListMessagesServiceTestBase {

  private MarshalListMessagesService service;

  protected JSONArray validateValidJsonArray(String string) {
    return new JSONArray(string); // throws JSONException if invalid
  }

  protected Document validateValidXml(String string) throws SAXException, IOException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    ByteArrayInputStream input =  new ByteArrayInputStream(string.getBytes("UTF-8"));
    return builder.parse(input);
  }

  @Before
  public void setUp() {
    service = new MarshalListMessagesService();
    MessageStore.INSTANCE.clearMessages();
    resetMessageCounter();
  }

  @Test
  public void testEmptyListNoVersion() {
    // version unspecified
    // format unspecified
    Response response = service.get(0, null);

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    validateValidJsonArray(responseString);
    assertEquals("[]", responseString);
  }

  @Test
  public void testEmptyListV1() {
    Response response = service.get(1, null);

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    validateValidJsonArray(responseString);
    assertEquals("[]", responseString);
  }

  @Test
  public void testEmptyListV2Json() {
    Response response = service.get(2, "json");

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    validateValidJsonArray(responseString);
    assertEquals("[]", responseString);
  }

  @Test
  public void testEmptyListV2Xml() throws SAXException, IOException, ParserConfigurationException {
    Response response = service.get(2, "xml");

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    validateValidXml(responseString);
    assertEquals("<messages/>", responseString);
  }

  @Test
  public void testListV1() {
    final int COUNT = 2;
    addMessages(COUNT);

    Response response = service.get(1, null);

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    JSONArray json = validateValidJsonArray(responseString);

    // Validate that message array looks correct (size, fields of each object).
    assertEquals(COUNT, json.length());
    for (int i = 0 ; i < json.length(); i++) {
      JSONObject obj = json.getJSONObject(i);
      Set<String> fieldNames = new HashSet<String>(Arrays.asList(JSONObject.getNames(obj)));
      assertThat(fieldNames, hasItems("title", "content", "sender"));
      assertThat(fieldNames, not(hasItems("url")));
    }

    // Validate content of first message object
    JSONObject obj1 = json.getJSONObject(0);
    String obj1Title = obj1.getString("title");

    Message message1 = findMessageByTitle(obj1Title);
    assertNotNull(message1); // expect to find messages by title in all messages
    assertEquals(message1.getContent(), obj1.getString("content"));
    assertEquals(message1.getSender(), obj1.getString("sender"));
  }

  @Test
  public void testListV2Json() {
    final int COUNT = 3;
    addMessages(COUNT);

    Response response = service.get(2, "json");

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    JSONArray json = validateValidJsonArray(responseString);

    // Validate that message array looks correct (size, fields of each object).
    assertEquals(COUNT, json.length());
    for (int i = 0 ; i < json.length(); i++) {
      JSONObject obj = json.getJSONObject(i);
      Set<String> fieldNames = new HashSet<String>(Arrays.asList(JSONObject.getNames(obj)));
      assertThat(fieldNames, hasItems("title", "content", "sender", "url"));
    }

    // Validate content of first message object
    JSONObject obj1 = json.getJSONObject(0);
    String obj1Title = obj1.getString("title");

    Message message1 = findMessageByTitle(obj1Title);
    assertNotNull(message1);
    assertEquals(message1.getContent(), obj1.getString("content"));
    assertEquals(message1.getSender(), obj1.getString("sender"));
    assertEquals(message1.getUrl().toString(), obj1.getString("url"));
  }

  @Test
  public void testListV2Xml() throws SAXException, IOException, ParserConfigurationException {
    final int COUNT = 3;
    addMessages(COUNT);

    Response response = service.get(2, "xml");

    assertEquals(200, response.getStatus());
    String responseString = response.getEntity().toString();
    Document doc = validateValidXml(responseString);

    Element root = doc.getDocumentElement();
    assertEquals("messages", root.getTagName());

    // First, extract only the Element nodes.
    NodeList childNodes = root.getChildNodes();
    List<Node> messageElements = new ArrayList<Node>();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node node = childNodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        messageElements.add(node);
      }
    }

    // Validate that message array looks correct (size, fields of each object).
    assertEquals(COUNT, messageElements.size());

    for (int i = 0; i < messageElements.size(); i++) {
      // First find the field names of each element.
      Node msgNode = messageElements.get(i);
      NamedNodeMap msgAttributes = msgNode.getAttributes();
      Set<String> fieldNames = new HashSet<String>();
      for (int j = 0; j < msgAttributes.getLength(); j++) {
        Node fieldNode = msgAttributes.item(j);
        if (fieldNode.getNodeType() == Node.ATTRIBUTE_NODE) {
          fieldNames.add(fieldNode.getNodeName());
        }
      }
      // Validate that the set of field names is correct.
      assertThat(fieldNames, hasItems("title", "sender", "url")); // content is stored as contained node
    }
  }

}
