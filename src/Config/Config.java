package Config;

import java.util.logging.Level;

/**
 * Config class - Abstrac class with application configs
 *
 * @author Niesuch
 */
public abstract class Config
{

    private static final int _WIDTH = 1024;
    private static final int _HEIGHT = 768;
    private static String _logFileName = "log_bittorrent.log";
    private static boolean _isFileLoggingEnabled = false;
    private static boolean _isConsoleLoggingEnabled = false;
    private static Level _logLevel = Level.OFF;

    /**
     * Return window width
     *
     * @return
     */
    public static int getWindowWidth()
    {
        return _WIDTH;
    }

    /**
     * Return window height
     *
     * @return
     */
    public static int getWindowHeight()
    {
        return _HEIGHT;
    }

    /**
     * Return logFileName
     *
     * @return
     */
    public static String getLogFileName()
    {
        return _logFileName;
    }

    /**
     * Return isFileLoggingEnabled
     *
     * @return
     */
    public static boolean getIsFileLoggingEnabled()
    {
        return _isFileLoggingEnabled;
    }

    /**
     * Return isConsoleLoggingEnabled
     *
     * @return
     */
    public static boolean getIsConsoleLoggingEnabled()
    {
        return _isConsoleLoggingEnabled;
    }

    /**
     * Return logLevel
     *
     * @return
     */
    public static Level getLogLevel()
    {
        return _logLevel;
    }
}
