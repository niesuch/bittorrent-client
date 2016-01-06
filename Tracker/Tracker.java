package Tracker;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author Pawel
 *
 */

/**
 Tracker will check torrent files usage and make statistics from it,
 such as how many peers are downloading file, how much of it is being downloaded and so on.
 */

public class Tracker
{
	// timer is needed for calculating operations times
	Timer timer;
	Time time;
	// list of possible tracker states
	public static final String EVENT[] =
		{
		    "Started", "Stopped", "Completed", "Update"
		};
	// how long tracker should wait for response
	public int interval = 5 * 60;
	// this lock will prevent from unnecessary changes of tracker status
	private ReentrantLock trackerLock = new ReentrantLock();
	// list of peers
	ArrayList<Peer> peers;
	public int fileID, userID;
	public String url;
	public InfoHash infoHash;
	
	public Tracker(int fileID)
	{
		this.fileID = fileID;
		// infoHash value should be given here
		this.userID = time.getMinutes() * time.getSeconds();
		this.peers = new ArrayList<Peer>(fileID);
	}
	
	// managing tracker's lock
	
	public void lock()
	{
		this.trackerLock.lock();
	}
	
	public void unlock()
	{
		this.trackerLock.unlock();
	}
}
