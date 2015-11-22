package org.noname.fsmb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FsmbPingServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final String PING_RESPONSE = "fsmb: pong\n";

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //System.out.println("ping");
    response.getOutputStream().write(PING_RESPONSE.getBytes("UTF-8"));
  }
}
