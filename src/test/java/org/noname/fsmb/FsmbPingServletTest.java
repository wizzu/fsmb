package org.noname.fsmb;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class FsmbPingServletTest {

  @Test
  public void testDoGetHttpServletRequestHttpServletResponse() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    FsmbPingServlet pingServlet = new FsmbPingServlet();
    pingServlet.doGet(request, response);

    assertEquals(200, response.getStatus());
    String responseContent = response.getOutputStreamContent().trim(); // strip newline from end
    assertEquals("fsmb: pong", responseContent);
  }

}
