package Tracker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantLock;

import TorrentMetadata.TorrentFile;

/**
 * Class used for tracking torrent files and peers
 * @author Pawel
 *
 */

public class Tracker
{
	Timer timer;
	public static final String state[] =
		{
		    "Started", "Stopped", "Completed", "Update"
		};
	public int interval = 5 * 60;
	private ReentrantLock trackerLock = new ReentrantLock();
	private ArrayList<Peer> peers;
	private ArrayList<InfoHash> torrents = new ArrayList<InfoHash>();
	public int fileID, userID;
	public String url;
	public InfoHash infoHash;
	
	public Tracker(int fileID)
	{
		this.fileID = fileID;
		// infoHash value should be given here
		this.userID = 0;
		this.peers = new ArrayList<Peer>(fileID);
	}
	
	/**
	 * Locks tracker to prevent modification
	 */
	public void lock()
	{
		this.trackerLock.lock();
	}
	
	/**
	 * Unlocks tracker to make modifications possible
	 */
	public void unlock()
	{
		this.trackerLock.unlock();
	}
	/**
	 * Adds new torrent to track
	 * @param infoHash encoded in SHA1 hash of torrent metafile
	 */
	public void add(InfoHash infoHash)
	{
		if(this.trackerLock.isHeldByCurrentThread() == true)
		{
			System.out.println("Tracker is locked right now.");
		}
		
		if(infoHash.toString().length() != 20)
		{
			System.out.println("InfoHash length is not correct!");
		}
		
		this.torrents.add(infoHash);
	}
	
	//TODO
	//add function for tracking peer and torrent
}
