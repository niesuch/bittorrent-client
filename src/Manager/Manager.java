package Manager;

import Manager.DownloadManager;
import Logger.Log;
import TorrentMetadata.TorrentFile;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Niesuch
 */
public class Manager extends Thread
{

    private TorrentFile _torrentInfo;
    private File _outputFile;
    private static final Logger log = Log.getLogger(DownloadManager.class);

    public Manager()
    {

    }

    /**
     * Reads a piece from file on the disk in instances where we are uploading a
     * piece or if we are checking the pieces of the file we already have
     *
     * @param index
     * @param offset
     * @param length
     * @return
     * @throws IOException
     */
    public byte[] readFile(int index, int offset, int length) throws IOException
    {
        byte[] data;
        try (RandomAccessFile raf = new RandomAccessFile(_outputFile, "r"))
        {
            data = new byte[length];
            raf.seek(_torrentInfo.pieceLength * index + offset);
            raf.readFully(data);
        }

        return data;
    }

    /**
     * Verifies the SHA1 of a piece of a file against the info hash values
     *
     * @param piece
     * @param SHA1Hash
     * @param index
     * @return
     */
    public static boolean verifySHA1(byte[] piece, ByteBuffer SHA1Hash, int index)
    {
        MessageDigest SHA1;

        try
        {
            SHA1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e)
        {
            log.severe("Unable to find SHA1 Algorithm");
            return false;
        }

        SHA1.update(piece);
        byte[] pieceHash = SHA1.digest();

        if (Arrays.equals(pieceHash, SHA1Hash.array()))
        {
            log.info("Verified - " + SHA1.digest() + " - " + SHA1Hash.array() + " for index " + index);
            return true;
        } else
        {
            return false;
        }
    }
}
