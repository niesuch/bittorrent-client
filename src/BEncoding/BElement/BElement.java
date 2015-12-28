package BEncoding.BElement;

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
}
