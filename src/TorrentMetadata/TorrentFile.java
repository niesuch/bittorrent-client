package TorrentMetadata;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Only single file mode for now
 * @author Robert
 */
public class TorrentFile
{
    //The name key maps to a UTF-8 encoded string which is the suggested name to save the file (or directory) as.
    
    /**
     * Key used to retrieve the info dictionary from the torrent metainfo file.
     */
    public final static ByteBuffer KEY_INFO = ByteBuffer.wrap(new byte[]
    { 'i', 'n', 'f', 'o' });
	
    /**
     * Key used to retrieve the length of the torrent.
     */
    public final static ByteBuffer KEY_LENGTH = ByteBuffer.wrap(new byte[]
    { 'l', 'e', 'n', 'g', 't', 'h' });

    /**
     * Key used to retrieve the piece hashes.
     */
    public final static ByteBuffer KEY_PIECES = ByteBuffer.wrap(new byte[]
    { 'p', 'i', 'e', 'c', 'e', 's' });

    /**
     * Key used to retrieve the file name.
     */
    public final static ByteBuffer KEY_NAME = ByteBuffer.wrap(new byte[]
    { 'n', 'a', 'm', 'e' });

    /**
     * Key used to retrieve the default piece length.
     */
    public final static ByteBuffer KEY_PIECE_LENGTH = ByteBuffer.wrap(new byte[]
    { 'p', 'i', 'e', 'c', 'e', ' ', 'l', 'e', 'n', 'g', 't', 'h' });
	
    /**
    * ByteBuffer to retrieve the announce URL from the metainfo dictionary.
     */
    public static final ByteBuffer KEY_ANNOUNCE = ByteBuffer.wrap(new byte[] {'a','n','n','o','u','n','c','e'});
        
    /** TODO
     * The base dictionary of the torrent metainfo file.&nbsp; See {@link http://www.bittorrent.org/beps/bep_0003.html} for an explanation
     * of what keys are available and how they map.
     */
    public /*final*/ Map<ByteBuffer,Object> torrent_file_map;

    /** TODO
     * The unbencoded info dictionary of the torrent metainfo file.&nbsp; See {@link http://www.bittorrent.org/beps/bep_0003.html} for 
     * an explanation of what keys are available and how they map.
     */
    public /*final*/ Map<ByteBuffer,Object> info_map;
    
    /**
     *  The URL of the tracker.
     */
    public final URL announce;
    
    /** 
     * Number of bytes in each piece (integer).
     * pieceLength maps to the number of bytes in each piece the file is split into.
     * For the purposes of transfer, files are split into fixed-size pieces
     * which are all the same length except for possibly the last one which may be truncated.
     * piece length is almost always a power of two, most commonly 2 18 = 256 K (BitTorrent prior to version 3.2 uses 2 20 = 1 M as default).
     */
    public final int pieceLength;
    
    /**
     * fileName - This is purely advisory. (string)
     * The name of the file referenced in the torrent metainfo file.
     */
    public final String fileName;

    /**
     * The length of the file in bytes.
     */
    public final int fileLength;
    
    public TorrentFile(byte[] torrentFileBytes) throws MalformedURLException, UnsupportedEncodingException
    {
        //TODO
        
        // Make sure the input is valid
        if(torrentFileBytes == null || torrentFileBytes.length == 0)
            throw new IllegalArgumentException("Torrent file bytes is null or its lentgth is 0");
                
        String torrentString  = new String(torrentFileBytes, "UTF-8");
        
        this.fileName = ""; //TODO temporary soultion for compilation purposes
        this.fileLength = 0; //TODO temporary soultion for compilation purposes
        this.pieceLength = 256; //TODO temporary soultion for compilation purposes
        try
        {
            announce = new URL("");
        }
        catch(MalformedURLException ex)
        {
            throw new MalformedURLException("File "+ fileName + "malformed: " + ex.getMessage() + "in TorrentFile, TorrentFile(String fileName)");
            //System.err.println("File "+ fileName + "malformed: " + ex.getMessage() + "in TorrentFile, TorrentFile(String fileName)");
        }
    }
}
