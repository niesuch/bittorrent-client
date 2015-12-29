package BEncoding;

import BEncoding.BElement.*;
import java.util.Arrays;

public class BencodingTest
{
    public static void main(String [] args)
    {
        BElement[] elements3 = BencodeReader.Decode("i523e5:abcdel4:spam4:eggsed3:cow3:moo4:spam4:eggse");
        System.out.println(Arrays.toString(elements3));
    }
}
