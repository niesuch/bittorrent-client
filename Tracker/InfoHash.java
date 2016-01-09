package Tracker;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Byte array identifying torrent file
 * @author Pawel
 *
 */

public class InfoHash 
{
	public ByteBuffer infoHash = ByteBuffer.allocate(20);
	
	/**
	 * Generates InfoHash for given torrent
	 * @param KEY_INFO info bytes from torrent info
	 * @throws NoSuchAlgorithmException
	 */
	
	public InfoHash (ByteBuffer KEY_INFO) throws NoSuchAlgorithmException 
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.update(KEY_INFO.array());
		byte[] hash = digest.digest();
		this.infoHash = ByteBuffer.wrap(hash); 
	}
}
