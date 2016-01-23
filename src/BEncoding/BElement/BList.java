
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
    public List<BElement> Values = new ArrayList<BElement>();
    
    public BList() {}
        
    public BList(List<BElement> vals)
    {
        Values = vals;
    }

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

            for(BElement element : Values)
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
            Values.add(value);
    }
    
    /**
     * Adds the specified value to the list.
     * @param value The specified String value.
     */
    public void Add(String value)
    {
            Values.add(new BString(value));
    }

    /**
     * Adds the specified value to the list.
     * @param value The specified Integer value.
     */
    public void Add(Integer value)
    {
            Values.add(new BInteger(value));
    }
    
    @Override
    public String getString()
    {
        throw new RuntimeException("Type is BList, shouldnt it be BString??");
    }
    
    @Override
    public List<BElement> getList()
    {
        return Values;
    }
    
    @Override
    public int getInt()
    {
         throw new RuntimeException("Type is BInteger, shouldnt it be BInteger??");
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for(BElement el: Values)
        {
            s += el.toString() + " ";
        }
        return s;
    }
}
