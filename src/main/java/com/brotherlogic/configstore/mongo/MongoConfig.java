package com.brotherlogic.configstore.mongo;

import java.io.IOException;

import com.brotherlogic.configstore.ConfigStore;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * Mongo version of the config store
 * 
 * @author simon
 * 
 */
public class MongoConfig implements ConfigStore
{
   /** The name of the collection where we'll store stuff */
   private static final String COL_NAME = "Config";

   /** The name of the database where we'll store stuff */
   private static final String DB_NAME = "MongoConfig";

   /** The name of the test DB to use */
   public static final String TEST_DB_NAME = "MongoConfigTest";

   /** The collection */
   private DBCollection collection;

   /** Flag indicating if we're running in test or not */
   private boolean testMode = false;

   /**
    * Connects to the database
    * 
    * @return The Collection for storage
    * @throws IOException
    *            If we can't read the db
    */
   private DBCollection connect() throws IOException
   {
      if (collection == null)
      {
         Mongo mongo = new Mongo();
         if (!testMode && System.getProperty("configstore.test") == null)
            collection = mongo.getDB(DB_NAME).getCollection(COL_NAME);
         else
            collection = mongo.getDB(TEST_DB_NAME).getCollection(COL_NAME);
      }

      return collection;
   }

   @Override
   public byte[] get(final String key) throws IOException
   {
      DBCollection col = connect();
      BasicDBObject query = new BasicDBObject();
      query.append("key", key);
      DBObject ret = col.findOne(query);

      // Return empty array if key not found
      if (ret == null)
         return new byte[0];

      return (byte[]) ret.get("value");
   }

   /**
    * Sets up for testing
    */
   public void setForTest()
   {
      testMode = true;
   }

   @Override
   public void store(final String key, final byte[] value) throws IOException
   {
      BasicDBObject store = new BasicDBObject();
      store.append("key", key);

      DBObject obj = connect().findOne(store);

      if (obj != null)
      {
         obj.put("value", value);
         connect().update(store, obj);
      }
      else
      {
         store.append("value", value);
         connect().insert(store);
      }
   }

}
