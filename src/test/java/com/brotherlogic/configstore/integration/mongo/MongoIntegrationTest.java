package com.brotherlogic.configstore.integration.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.configstore.WebInterface;

public class MongoIntegrationTest
{
   @Test
   public void testWebService() throws IOException, HttpException, URISyntaxException
   {
      String toStore = "Testing Storage";
      byte[] arr = toStore.getBytes();

      // Store the string
      DefaultHttpClient httpClient = new DefaultHttpClient();
      HttpPut putRequest = new HttpPut("http://localhost:8080/configstore/store?key=mirror");
      ByteArrayEntity input = new ByteArrayEntity(arr);
      putRequest.setEntity(input);
      httpClient.execute(putRequest);

      // Retrieve the string
      HttpGet getRequest = new HttpGet("http://localhost:8080/configstore/store?key=mirror");
      HttpResponse resp = httpClient.execute(getRequest);
      HttpEntity ent = resp.getEntity();
      InputStream is = ent.getContent();

      // Maximum 1Mb file
      byte[] buffer = new byte[1024 * 1024];
      int read = is.read(buffer);
      is.close();

      String retString = new String(buffer, 0, read);
      Assert.assertEquals("String retrieved incorrectly", toStore, retString);
   }

   @Test
   public void testWebInterface() throws IOException
   {
      WebInterface inter = new WebInterface("http://localhost:8080/configstore/");
      String testString = "donkey";
      inter.store("mirror2", testString.getBytes());
      byte[] retr = inter.get("mirror2");
      String retString = new String(retr);
      Assert.assertEquals("String retrieved incorrectly", testString, retString);
   }

   @Test
   public void testWebInterfaceFail() throws IOException
   {
      WebInterface inter = new WebInterface("http://localhost:8080/configstore/");
      byte[] retr = inter.get("mirror4");
      Assert.assertTrue("Error in retrieve", retr.length == 0);
   }
}
