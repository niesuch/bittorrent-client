package Logger;

import Config.Config;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log extends SimpleFormatter
{

    static Logger log;

    public static void init()
    {
        log = Logger.getLogger("");
        log.setUseParentHandlers(false);

        if (Config.getIsFileLoggingEnabled())
        {
            try
            {
                log.addHandler(new FileHandler(Config.getLogFileName()));
            } catch (SecurityException e)
            {
                log.severe("Could not Log to File. Security Exception.");
            } catch (IOException e)
            {
                log.severe("Could not Log to File. IO Exception.");
            }
        }

        if (!Config.getIsConsoleLoggingEnabled())
        {
            ConsoleHandler formatedHandler = new ConsoleHandler();
            log.addHandler(formatedHandler);
        }

        replaceConsoleHandler(log, Level.OFF);
        log.setLevel(Config.getLogLevel());

    }

    /**
     * Configures and returns a logger
     *
     * @param cclass
     * @return
     */
    public static Logger getLogger(final Class<?> cclass)
    {
        Logger logger = Logger.getLogger(cclass.getName());
        return logger;
    }

    /**
     * Configures and returns a logger
     *
     * @param cclass
     * @param llevel
     * @return
     */
    public static Logger getLogger(final Class<?> cclass, final Level llevel)
    {
        Logger logger = Logger.getLogger(cclass.getName());
        logger.setLevel(llevel);
        return logger;
    }

    /**
     * Replaces the ConsoleHandler for a specific Logger with one that will log
     * all messages.
     *
     * @param logger
     * @param newLevel
     */
    public static void replaceConsoleHandler(Logger logger, Level newLevel)
    {
        Handler consoleHandler = null;

        for (Handler handler : logger.getHandlers())
        {
            if (handler instanceof ConsoleHandler)
            {
                consoleHandler = handler;
                break;
            }
        }

        if (consoleHandler == null)
        {
            consoleHandler = new ConsoleHandler();
            logger.addHandler(consoleHandler);
        }

        consoleHandler.setLevel(newLevel);
    }
}
