package com.brotherlogic.configstore.mongo;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.brotherlogic.configstore.ConfigStore;
import com.mongodb.Mongo;

/**
 * Tests the mongo storage thingy
 * 
 * @author simon
 * 
 */
public class MongoStoreTest
{
   /** The store to use for tests */
   private ConfigStore conf;

   /**
    * Prepares for testing
    * 
    * @throws IOException
    *            if something goes wrong
    */
   @Before
   public void deleteDB() throws IOException
   {
      Mongo m = new Mongo();
      m.dropDatabase(MongoConfig.TEST_DB_NAME);

      MongoConfig mc = new MongoConfig();
      mc.setForTest();
      conf = mc;
   }

   /**
    * Tests that we can store and retrieve from the mongo
    * 
    * @throws IOException
    *            if something goes wrong
    */
   @Test
   public void testStoreAndRetreive() throws IOException
   {
      String key = "test.key";
      String data = "This is some test data";

      conf.store(key, data.getBytes());

      byte[] dateRet = conf.get(key);
      String strRet = new String(dateRet);

      Assert.assertEquals("Mismatch in retrieved data: " + strRet + " vs " + data, strRet, data);
   }
}
