package BEncoding.BElement;

import java.util.List;


/**
 * An interface for bencoded elements.
 */
public interface BElement
{
    /**
     * Generates the bencoded equivalent of the element.
     * @return The bencoded equivalent of the element.
     */
    String ToBencodedString();

    /**
     * 
     * @param u The StringBuilder to append to.
     * @return The bencoded equivalent of the element.
     */
    StringBuilder ToBencodedString(StringBuilder u);

    public String getString();

    public List<BElement> getList();

    public int getInt();
    
    public long getLong();
}
