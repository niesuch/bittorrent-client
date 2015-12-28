package BEncoding;

import BEncoding.BElement.*;
import BEncoding.BElement.BList;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for decoding Bencoding.
 */
public class BencodeReader 
{
    /**
     * Decodes the string.
     * @param bencodedString The Bencoded string.
     * @return Array of root elements.
     */
    public static BElement[] Decode(String bencodedString)
    {
            int index = 0;

            try
            {
                    if (bencodedString == null) return null;

                    List<BElement> rootElements = new ArrayList<BElement>();
                    while (bencodedString.length() > index)
                    {
                            rootElements.add(ReadElement(bencodedString, index));
                    }
                    //TODO not sure if this works correctly, check this later
                    BElement[] ret = new BElement[rootElements.size()];
                    return rootElements.toArray(ret);
            }
            catch (Exception ex) 
            { 
                System.err.println("Caught Exception in BencodeReader, Decode: " + ex.getMessage());
                return null;
            }
    }
    
    private static BElement ReadElement(String bencodedString, int index)
    {
            char ElementType = bencodedString.charAt(index);
            switch (ElementType)
            {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': 
                        return ReadString(bencodedString, index);
                    case 'i': 
                        return ReadInteger(bencodedString, index);
                    case 'l': 
                        return ReadList(bencodedString, index);
                    case 'd': 
                        return ReadDictionary(bencodedString, index);
                    default: 
                        throw new RuntimeException("Failed to identify type{" + ElementType + "}");
            }
    }
    
    private static BDictionary ReadDictionary(String bencodedString,int index)
    {
            index++;
            BDictionary dict = new BDictionary();

            try
            {
                    while (bencodedString.charAt(index) != 'e')
                    {
                            BString Key = ReadString(bencodedString, index);
                            BElement Value = ReadElement(bencodedString, index);
                            dict.Add(Key, Value);
                    }
            }
            catch (Exception ex)
            { 
                System.err.println("Caught Exception in BencodeReader, ReadList: " + ex.getMessage());
                return null;  
            }

            index++;
            return dict;
    }

    private static BList ReadList(String bencodedString, int index)
    {
            index++;
            BList lst = new BList();

            try
            {
                while (bencodedString.charAt(index) != 'e')
                {
                    lst.Add(ReadElement(bencodedString, index));
                }
            }
            catch (Exception ex) 
            {
                System.err.println("Caught Exception in BencodeReader, ReadList: " + ex.getMessage());
                return null; 
            }

            index++;
            return lst;
    }

    //TODO
    //cant pass by reference in java need to find way around this later
    private static BInteger ReadInteger(String bencodedString,/*this shold be reference*/ int index)
    {
        index++;

        int end = bencodedString.indexOf('e', index);
        if (end == -1) 
            throw new RuntimeException("ReadInteger error, wrong format. Bad ending of the Bencoded String ");

        Integer integer;

        try
        {
                integer = Integer.parseInt(bencodedString.substring(index, end - index));
                index = end + 1;
        }
        catch (Exception ex) 
        {      
            System.err.println("Caught Exception in BencodeReader, ReadInteger: " + ex.getMessage());
            return null; 
        }

        return new BInteger(integer);
    }

    private static BString ReadString(String bencodedString, int index)
    {

        Integer length, colon;

        try
        {
                colon = bencodedString.indexOf(':', index);
                if (colon == -1) 
                    throw new RuntimeException("ReadInteger error, wrong format. Bad ending of the Bencoded String ");
                
                length = Integer.parseInt(bencodedString.substring(index, colon - index));
        }
        catch (Exception ex) 
        {
            System.err.println("Caught Exception in BencodeReader, ReadString: " + ex.getMessage());
            return null; 
        }

        index = colon + 1;
        int tmpIndex = index;
        index += length;

        try
        {
            return new BString(bencodedString.substring(tmpIndex, length));
        }
        catch (Exception ex) 
        {
            System.err.println("Caught Exception in BencodeReader, ReadString: " + ex.getMessage());
            return null; 
        }
    }
}
