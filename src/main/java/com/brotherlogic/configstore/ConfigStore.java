package com.brotherlogic.configstore;

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
    * @return a byte array
    */
   byte[] get(String key);

   /**
    * Store method for config
    * 
    * @param key
    *           The key to store information under
    * @param value
    *           The information to be stored
    */
   public void store(String key, byte[] value);
}
