package com.brotherlogic.configstore;

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

public class WebInterface implements ConfigStore
{
   String baseAddress;
   DefaultHttpClient httpClient = new DefaultHttpClient();

   public WebInterface(String base)
   {
      baseAddress = base;
   }

   @Override
   public byte[] get(String key) throws IOException
   {
      try
      {
         HttpGet getRequest = new HttpGet(baseAddress + "store?key=" + key);
         HttpResponse resp = httpClient.execute(getRequest);
         HttpEntity ent = resp.getEntity();
         InputStream is = ent.getContent();

         // Maximum 1Mb file
         byte[] buffer = new byte[1024 * 1024];
         int read = is.read(buffer);
         is.close();

         if (read > 0)
         {
            byte[] ret = new byte[read];
            for (int i = 0; i < ret.length; i++)
               ret[i] = buffer[i];
            return ret;
         }

         return new byte[0];
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
      catch (HttpException e)
      {
         throw new IOException(e);
      }
   }

   @Override
   public void store(String key, byte[] value) throws IOException
   {
      try
      {

         HttpPut putRequest = new HttpPut(baseAddress + "store?key=" + key);
         ByteArrayEntity input = new ByteArrayEntity(value);
         putRequest.setEntity(input);
         httpClient.execute(putRequest);
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
      catch (HttpException e)
      {
         throw new IOException(e);
      }
   }

}
