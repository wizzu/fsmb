package org.noname.fsmb.model;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.annotations.Since;

public class Message {
  private static int MAX_STRING_LENGTH = 1024;

  // GSON defines the @Since annotation for object field versioning.

  @Since(1)
  private String title;
  @Since(1)
  private String content;
  @Since(1)
  private String sender;
  @Since(2)
  private URL url;

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) throws StringTooLongException {
    validateStringLength(title);
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) throws StringTooLongException {
    validateStringLength(content);
    this.content = content;
  }
  public String getSender() {
    return sender;
  }
  public void setSender(String sender) throws StringTooLongException {
    validateStringLength(sender);
    this.sender = sender;
  }
  public URL getUrl() {
    return url;
  }
  public void setUrl(String urlString) throws MalformedURLException {
    URL newUrl = null;
    if (urlString != null && urlString.length() > 0) {
      newUrl = new URL(urlString);
    }
    this.setUrl(newUrl);
  }
  public void setUrl(URL url) {
    this.url = url;
  }

  private void validateStringLength(String string) throws StringTooLongException {
    if (string != null && string.length() > MAX_STRING_LENGTH) {
      throw new StringTooLongException();
    }
  }
}
