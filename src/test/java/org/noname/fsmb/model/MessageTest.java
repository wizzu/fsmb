package org.noname.fsmb.model;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

public class MessageTest {
  private Message message;

  private static String LONG_STRING = longString();
  private static String longString() {
    int length = 1024 * 100;
    StringBuilder s = new StringBuilder(length);
    for (int i = 0; i < length; i++){
       s.append("A");
    }
    return s.toString();
  }

  @Before
  public void setUp() throws Exception {
    message = new Message();
  }

  @Test
  public void testSetTitle() throws StringTooLongException {
    String title = "test title";
    message.setTitle(title);
    assertEquals(title, message.getTitle());
  }

  @Test(expected=StringTooLongException.class)
  public void testSetTooLongTitle() throws StringTooLongException {
    message.setTitle(LONG_STRING);
  }

  @Test
  public void testSetContent() throws StringTooLongException {
    String content = "test content";
    message.setContent(content);
    assertEquals(content, message.getContent());
  }

  @Test(expected=StringTooLongException.class)
  public void testSetTooLongContent() throws StringTooLongException {
    message.setContent(LONG_STRING);
  }

  @Test
  public void testSetSender() throws StringTooLongException {
    String sender = "test sender";
    message.setSender(sender);
    assertEquals(sender, message.getSender());
  }

  @Test(expected=StringTooLongException.class)
  public void testSetTooLongSender() throws StringTooLongException {
    message.setSender(LONG_STRING);
  }

  @Test
  public void testSetUrlString() throws MalformedURLException {
    String url = "http://localhost/fsmb";
    message.setUrl(url);
    assertEquals(url, message.getUrl().toString());
  }

  @Test(expected=MalformedURLException.class)
  public void testSetInvalidUrl() throws MalformedURLException {
    String url = "blahblah";
    message.setUrl(url);
  }

}
