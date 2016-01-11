package Tracker;

import java.net.Socket;

/**
 * 
 * @author Pawel
 *
 */

/**
 Every user downloading a file should be identified as a peer,
 which will help tracking his use of torrent files.
 */

public class Peer 
{
	public static final String state[] =
		{
		    "Unknown", "Started", "Stopped", "Completed"
		};
	public int port, fileID;
	private String id, ip, peerState;
	public Socket peerSocket = null;
	public long downloaded, left;
	
	// Constructor
	
	public Peer(String id, String ip, int port, int fileID, Socket peerSocket)
	{
		this.ip = ip;
		this.id = id;
		this.port = port;
		this.fileID = fileID;
		this.peerSocket = peerSocket;
		this.peerState = "Unknown";
		this.downloaded = 0;
		this.left = 0;
	}
	
	/**
	 * Updates a state of tracked peer
	 * @param downloaded how much of a file this peer has downloaded
	 * @param left how much of a file is left to complete download
	 */
	public void update(long downloaded, long left)
	{
		if(left == 0)
			this.peerState = "Completed";
	}
	
	/**
	 * Return peer's id
	 * @return id
	 */
	public String getPeerId()
	{
		return this.id;
	}
	
	/**
	 * Return peer's ip
	 * @return ip address
	 */
	public String getPeerIp()
	{
		return this.ip;
	}
	
	//TODO
	//updating peer's status, updating peer's information about download
}
