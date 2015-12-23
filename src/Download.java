
class Download
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
    private int _size;
    private int _downloaded;
    private int _status;

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

    public void pause()
    {
        _status = PAUSED;
    }

    public void resume()
    {
        _status = DOWNLOADING;
    }

    public void cancel()
    {
        _status = CANCELLED;
    }

    private void error()
    {
        _status = ERROR;
    }
}
