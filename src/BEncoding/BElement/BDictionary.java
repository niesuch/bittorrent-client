/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BEncoding.BElement;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Robert
 */
public class BDictionary implements BElement
{
    public TreeMap<BString, BElement> Values = new TreeMap<BString, BElement>();
    
    /**
     * Generates the bencoded equivalent of the dictionary.
     * @return Bencoded equivalent of the dictionary.
     */
    @Override
    public String ToBencodedString()
    {
            return this.ToBencodedString(new StringBuilder()).toString();
    }

    /**
     * Generates the bencoded equivalent of the dictionary.
     * @param u StringBuilder to append to.
     * @return Bencoded equivalent of the dictionary.
     */
    @Override
    public StringBuilder ToBencodedString(StringBuilder u)
    {
            if (u == null) u = new StringBuilder('d');
            else u.append('d');

          for (Map.Entry<BString, BElement> entry : Values.entrySet())
        {
            entry.getKey().ToBencodedString(u);
            entry.getValue().ToBencodedString(u);
        }

           return u.append('e');
    }

        /**
     * Adds the specified key-value pair to the dictionary.
     * @param key Specified BString key.
     * @param value specified BElement value.
     */
    public void Add(BString key, BElement value)
    {
            Values.put(key, value);
    }
    
    /**
     * Adds the specified key-value pair to the dictionary.
     * @param key Specified key.
     * @param value specified BElement value.
     */
    public void Add(String key, BElement value)
    {
            Values.put(new BString(key), value);
    }

     /**
     * Adds the specified key-value pair to the dictionary.
     * @param key Specified key.
     * @param value Specified String value
     */
    public void Add(String key, String value)
    {
            Values.put(new BString(key), new BString(value));
    }

     /**
     * Adds the specified key-value pair to the dictionary.
     * @param key Specified key.
     * @param value Specified Integer value
     */
    public void Add(String key, Integer value)
    {
            Values.put(new BString(key), new BInteger(value));
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for (Map.Entry<BString, BElement> entry : Values.entrySet())
        {
            s+= "key:" + entry.getKey().toString() + " ";
            s+= "value:" + entry.getValue().toString() + " ";
        }
        return s;
    }
}
