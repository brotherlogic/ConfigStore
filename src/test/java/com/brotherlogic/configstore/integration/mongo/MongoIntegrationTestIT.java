package com.brotherlogic.configstore.integration.mongo;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class MongoIntegrationTestIT
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
      HttpResponse response = httpClient.execute(putRequest);
   }
}
