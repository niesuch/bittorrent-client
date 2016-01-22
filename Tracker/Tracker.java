package Tracker;

import java.util.ArrayList;
import java.util.Timer;

import TorrentMetadata.TorrentFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	//private ReentrantLock trackerLock = new ReentrantLock();
	private ArrayList<Peer> peers;
	private ArrayList<InfoHash> torrents = new ArrayList<InfoHash>();
	public int fileID, userID;
        public int port = 6961;
	public String announceUrl = "/announce";
	public InfoHash infoHash;
        
        private InetSocketAddress address;
	
	public Tracker(int fileID)
	{
		this.fileID = fileID;
		this.userID = 0;
		this.peers = new ArrayList<Peer>(fileID);
	}
	
	/**
	 * Locks tracker to prevent modification
	 */
	//public void lock()
	//{
	//	this.trackerLock.lock();
	//}
	
	/**
	 * Unlocks tracker to make modifications possible
	 */
	//public void unlock()
	//{
	//	this.trackerLock.unlock();
	//}
	/**
	 * Adds new torrent to track
	 * @param infoHash encoded in SHA1 hash of torrent metafile
	 */
	public void add(InfoHash infoHash)
	{
		//if(this.trackerLock.isHeldByCurrentThread() == true)
		//{
		//	System.out.println("Tracker is locked right now.");
		//}
		
		if(infoHash.toString().length() != 20)
		{
			System.out.println("InfoHash length is not correct!");
		}
		
		this.torrents.add(infoHash);
	}
        
        /**
         * Gets an ip address of host
         * @return IP address of host
         */
        public String getAnnounceIP() throws MalformedURLException 
        {
            Enumeration e;
            try 
            {
                e = NetworkInterface.getNetworkInterfaces();
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                InetAddress i = (InetAddress) ee.nextElement();
                return i.getHostAddress();
            } 
            catch (SocketException ex) 
            {
                Logger.getLogger(Tracker.class.getName()).log(Level.SEVERE, null, ex);
            }

            return null;
	}
	
	//TODO
	//add function for tracking peer and torrent
}
