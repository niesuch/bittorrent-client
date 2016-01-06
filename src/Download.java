
import java.util.Observable;

class Download extends Observable implements Runnable
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

    public Download(String name)
    {
        this._name = name;
        _size = -1;
        _downloaded = 0;
        _status = DOWNLOADING;

        _download();
    }

    public int getSize()
    {
        return _size;
    }

    public float getProgress()
    {
        return ((float) _downloaded / _size) * 100;
    }

    public int getStatus()
    {
        return _status;
    }
    
    public String getFileName()
    {
        return _name;
    }

    public void pause()
    {
        _status = PAUSED;
        _stateChanged();
    }

    public void resume()
    {
        _status = DOWNLOADING;
        _stateChanged();
        _download();
    }

    public void cancel()
    {
        _status = CANCELLED;
        _stateChanged();
    }

    private void error()
    {
        _status = ERROR;
        _stateChanged();
    }

    private void _download()
    {

    }

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
