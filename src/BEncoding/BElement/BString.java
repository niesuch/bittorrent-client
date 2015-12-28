package BEncoding.BElement;

/**
 * Bencode string.
 */
public class BString implements BElement
{  
    /**
     *  Value of the bencoded string.
     */
    public String Value = null;

    public BString(String value)
    {
            this.Value = value;
    }
    
    /**
     * Generates the bencoded equivalent of the string.
     * @return The bencoded equivalent of the string.
     */
    @Override
    public String ToBencodedString()
    {
            return this.ToBencodedString(new StringBuilder()).toString();
    }
    /**
     * Generates the bencoded equivalent of the string.
     * @param u The StringBuilder to append to.
     * @return The bencoded equivalent of the string.
     */
    @Override
    public StringBuilder ToBencodedString(StringBuilder u)
    {
            if (u == null) 
                u = new StringBuilder(this.Value.length());
            else 
                u.append(this.Value.length());
            
            return u.append(':').append(this.Value);
    }
}
