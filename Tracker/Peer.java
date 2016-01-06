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
	public int port, fileID;
	public String id, ip;
	public Socket peerSocket = null;
	
	// Constructor
	
	public Peer(String id, String ip, int port, int fileID, Socket peerSocket)
	{
		this.ip = ip;
		this.id = id;
		this.port = port;
		this.fileID = fileID;
		this.peerSocket = peerSocket;
	}
}
