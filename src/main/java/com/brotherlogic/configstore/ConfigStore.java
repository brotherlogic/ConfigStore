package com.brotherlogic.configstore;

import java.io.IOException;

/**
 * Interface for defininig a config store
 * 
 * @author simon
 * 
 */
public interface ConfigStore
{
   /**
    * Get method for config
    * 
    * @param key
    *           The key to get information for
    * @throws IOException
    *            if something goes wrong in retrieving
    * @return a byte array
    */
   byte[] get(String key) throws IOException;

   /**
    * Store method for config
    * 
    * @param key
    *           The key to store information under
    * @param value
    *           The information to be stored
    * @throws IOException
    *            If something goes wrong in storage
    */
   void store(String key, byte[] value) throws IOException;
}
