
package BEncoding.BElement;

/**
 * Bencode integer.
 */
public class BInteger implements BElement
{
    /**
     * Value of the bencoded Integer.
     */
    public Integer Value = null;
    
    public BInteger(Integer val)
    {
        this.Value = val;
    }
    
    /**
     * Generates the bencoded equivalent of the integer.
     * @return The bencoded equivalent of the integer.
     */  
    @Override
    public String ToBencodedString()
    {
            return this.ToBencodedString(new StringBuilder()).toString();
    }

    /**
     * 
     * @param u Generates the bencoded equivalent of the integer.
     * @return The bencoded equivalent of the integer.
     */
    @Override
    public StringBuilder ToBencodedString(StringBuilder u)
    {
            if (u == null) u = new StringBuilder('i');
            else u.append('i');
            return u.append(Value.toString()).append('e');
    }
}
