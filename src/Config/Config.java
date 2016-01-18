package Config;

/**
 * Config class - Abstrac class with application configs
 *
 * @author Niesuch
 */
public abstract class Config
{

    private static final int _WIDTH = 1024;
    private static final int _HEIGHT = 768;

    /**
     * Return window width
     * @return 
     */
    public static int getWindowWidth()
    {
        return _WIDTH;
    }
    
    /**
     * Return window height
     * @return 
     */
    public static int getWindowHeight()
    {
        return _HEIGHT;
    }
}
