
import java.util.Observable;

/**
 * Download class - class which support download events
 *
 * @author Niesuch
 */
class DownloadManager extends Observable implements Runnable
{

    public static final String STATUSES[] =
    {
        "Downloading", "Paused", "Complete", "Cancelled", "Error"
    };
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    private final String _name;
    private final int _size;
    private final int _downloaded;
    private int _status;
    private final int _downloadSpeed, _uploadSpeed;
    private final int _timeRemaining;

    public DownloadManager(String name)
    {
        this._name = name;
        _size = -1;
        _downloaded = 0;
        _status = DOWNLOADING;
        _downloadSpeed = 0;
        _uploadSpeed = 0;
        _timeRemaining = -1;

        _download();
    }

    /**
     * Return download size
     *
     * @return
     */
    public int getSize()
    {
        return _size;
    }

    /**
     * Return download progress
     *
     * @return
     */
    public float getProgress()
    {
        return ((float) _downloaded / _size) * 100;
    }

    /**
     * Return download status
     *
     * @return
     */
    public int getStatus()
    {
        return _status;
    }

    /**
     * Return download file name
     *
     * @return
     */
    public String getFileName()
    {
        return _name;
    }

    /**
     * Return download speed
     *
     * @return
     */
    public int getDownloadSpeed()
    {
        return _downloadSpeed;
    }

    /**
     * Return upload speed
     *
     * @return
     */
    public int getUploadSpeed()
    {
        return _uploadSpeed;
    }

    /**
     * Return download time remaining
     *
     * @return
     */
    public int getTimeRemaining()
    {
        return _timeRemaining;
    }

    /**
     * Pause download
     */
    public void pause()
    {
        _status = PAUSED;
        _stateChanged();
    }

    /**
     * Resume download
     */
    public void resume()
    {
        _status = DOWNLOADING;
        _stateChanged();
        _download();
    }

    /**
     * Cancel download
     */
    public void cancel()
    {
        _status = CANCELLED;
        _stateChanged();
    }

    /**
     * Error download
     */
    private void error()
    {
        _status = ERROR;
        _stateChanged();
    }

    /**
     * Start or resume downloading
     */
    private void _download()
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Function to change states
     */
    private void _stateChanged()
    {
        setChanged();
        notifyObservers();
    }

    @Override
    public void run()
    {

    }
}
