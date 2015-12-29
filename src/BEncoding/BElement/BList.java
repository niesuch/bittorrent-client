
package BEncoding.BElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Bencode List.
 */
public class BList implements BElement 
{
    /**
     * List of Bencode Elements.
     */
    public List<BElement> Value = new ArrayList<BElement>();
    
    /**
     * Generates the bencoded equivalent of the list.
     * @return Bencoded equivalent of the list.
     */
    @Override
    public String ToBencodedString()
    {
            return this.ToBencodedString(new StringBuilder()).toString();
    }
    /**
     * Generates the bencoded equivalent of the list.
     * @param u StringBuilder to append to.
     * @return Bencoded equivalent of the list.
     */
    @Override
    public StringBuilder ToBencodedString(StringBuilder u)
    {
            if (u == null) u = new StringBuilder('l');
            else u.append('l');

            for(BElement element : Value)
            {
                    element.ToBencodedString(u);
            }

            return u.append('e');
    }
    /**
     * Adds the specified value to the list.
     * @param value The specified Integer value.
     */
    public void Add(BElement value)
    {
            Value.add(value);
    }
    
    /**
     * Adds the specified value to the list.
     * @param value The specified String value.
     */
    public void Add(String value)
    {
            Value.add(new BString(value));
    }

    /**
     * Adds the specified value to the list.
     * @param value The specified Integer value.
     */
    public void Add(Integer value)
    {
            Value.add(new BInteger(value));
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for(BElement el: Value)
        {
            s += el.toString() + " ";
        }
        return s;
    }
}
