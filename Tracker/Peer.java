package Tracker;

import java.net.Socket;
import java.util.Date;

import BEncoding.BElement.BDictionary;

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
        private Date lastAnnounce;
        private InfoHash torrent;
	
	// Constructor
	
	public Peer(String id, String ip, int port, int fileID, Socket peerSocket, InfoHash infoHash)
	{
		this.ip = ip;
		this.id = id;
		this.port = port;
		this.fileID = fileID;
		this.peerSocket = peerSocket;
		this.peerState = "Unknown";
		this.downloaded = 0;
		this.left = 0;
                this.lastAnnounce = null;
                this.torrent = infoHash;
	}
	
	/**
	 * Updates a state of tracked peer
	 * @param downloaded how much of a file this peer has downloaded
	 * @param left how much of a file is left to complete download
         * @param state represents the state of tracked peer
	 */
	public void update(long downloaded, long left, String state)
	{
		if(left == 0)
                {
                    this.peerState = "Completed";
                }
                else
                {
                    this.lastAnnounce = new Date();
                    this.peerState = state;
                }
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
        
        public Integer getPeerPort()
	{
		return this.port;
	}
        
        public InfoHash getInfoHash()
	{
		return this.torrent;
	}
        
        public BDictionary toDictionary()
        {
            BDictionary peer = new BDictionary();
            peer.Add("ip", this.getPeerIp());
            peer.Add("port", this.getPeerPort());
            peer.Add("id", this.getPeerId());
            
            return peer;
        }
}
