package Tracker;

import java.nio.ByteBuffer;

/**
 * 
 * @author Pawel
 *
 */

/**
 InfoHash is needed for identifying a torrent, its length is 20 bytes.
 */

public class InfoHash 
{
	private ByteBuffer infoHashBuffer = ByteBuffer.allocate(20);
	
	// Constructor
	
	public InfoHash (byte[] infoHashBytes) {

		if ((infoHashBytes == null) || (infoHashBytes.length != 20)) 
		{
			System.out.println("Provided bytes are not correct!");
		}

		this.infoHashBuffer.put (infoHashBytes);
		this.infoHashBuffer.rewind();

	}
}
