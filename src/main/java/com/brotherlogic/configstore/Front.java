package com.brotherlogic.configstore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brotherlogic.configstore.mongo.MongoConfig;

public class Front extends HttpServlet
{
   Logger logger = Logger.getLogger(this.getClass().getName());

   ConfigStore store = null;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
         IOException
   {
      String key = null;

      if (req != null)
         key = req.getParameter("key");

      if (key == null)
      {
         resp.setContentType("text/html");

         // Display all the keys
         PrintStream ps = new PrintStream(resp.getOutputStream());
         ps.println("<HTML><BODY>");

         ps.println("<FORM action=\"/\" method=\"post\"><LABEL>Key:</LABEL><INPUT NAME=\"key\" TYPE=\"text\" /><LABEL>Value:</LABEL><INPUT NAME=\"value\" TYPE=\"text\" /><INPUT TYPE=\"submit\"/></FORM>");

         for (String storeKey : getConfigStore().getKeys())
            ps.println("<P>" + storeKey + "</P>");

         ps.println("</BODY></HTML>");
      }
      else
      {
         resp.setContentType("application/octet-stream");

         byte[] content = getConfigStore().get(key);

         logger.log(Level.INFO, "Getting " + key + "->" + new String(content));

         PrintStream bos = new PrintStream(resp.getOutputStream());
         bos.write(content);
         bos.close();
      }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
         IOException
   {
      String key = req.getParameter("key");
      String value = req.getParameter("value");

      getConfigStore().store(key, value.getBytes());

      doGet(null, resp);
   }

   @Override
   protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
         IOException
   {
      // Get the key
      String key = req.getParameter("key");
      InputStream is = req.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read = is.read(buffer);
      while (read > 0)
      {
         baos.write(buffer, 0, read);
         read = is.read(buffer);
      }
      is.close();

      byte[] inputData = baos.toByteArray();
      baos.close();
      logger.log(Level.INFO, "Putting " + key + "->" + new String(inputData));
      getConfigStore().store(key, inputData);
   }

   private ConfigStore getConfigStore()
   {
      if (store == null)
         store = new MongoConfig();
      return store;
   }
}
