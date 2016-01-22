package TorrentMetadata;

import BEncoding.BencodeReader;
import BEncoding.BElement.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author Robert
 */
public class TorrentFile
{    
    /** 
     * Number of bytes in each piece (integer).
     * pieceLength maps to the number of bytes in each piece the file is split into.
     * For the purposes of transfer, files are split into fixed-size pieces
     * which are all the same length except for possibly the last one which may be truncated.
     * piece length is almost always a power of two, most commonly 2 18 = 256 K (BitTorrent prior to version 3.2 uses 2 20 = 1 M as default).
     */
    public static final int pieceLength = 512 * 1024;
    
    public static final int pieceHashSize = 20;
    
    /**
     * fileName - This is purely advisory. (string)
     * The name of the file referenced in the torrent metainfo file.
     */
    public final String fileName;
    
    /**
     * The Bencoded byte array with .torrent metadata
     */
    protected final byte[] encodedFile;
    
    protected final Map<String, BElement> decodedFile;
    
    protected final Map<String, BElement> decodedInfo;
    
    /**
     * Contains information if we are seeder or not.
     */
    protected /*final*/ boolean isSeeder;
    
    private final List<List<URI>> trackers;
    private final Set<URI> allTrackers;
    private final Date creationDate;
    private final String comment;
    private final String createdBy;
    private final long size;
    private final byte[] infoHash;
    private final String hexInfoHash;
    
    /**
     * Bencoded metadata info
     */
    protected final String encodedInfo;
    
    /**
     * Physical files to share within one .torrent metadata file
     */
    public static class TFile 
    {
        public final File file;
        public final long size;

        public TFile(File file, long size) 
        {
                this.file = file;
                this.size = size;
        }
    };
    
    protected final List<TFile> files;
    
    public TorrentFile(byte[] torrentFileBytes, boolean seeder) throws IOException, NoSuchAlgorithmException 
    {
        this.encodedFile = torrentFileBytes;
        this.isSeeder = seeder;

        String torrentString  = new String(torrentFileBytes, "UTF-8");
        BElement[] elements = BencodeReader.Decode(torrentString);
        BDictionary decoded = null;
        if(elements != null)
            decoded = elements[0] instanceof BDictionary ? (BDictionary) elements[0] : null;//elements[0] as BDictionary;
        if(decoded == null)
            throw new RuntimeException("Exception in TorrentFile constructor, cast to BDictionary failed ");
        
          decodedFile = decoded.getMap();
        
//        this.decoded = BDecoder.bdecode(new ByteArrayInputStream(this.encoded)).getMap();
//
        this.decodedInfo = ((BDictionary)this.decodedFile.get("info")).getMap();
        
        encodedInfo =  ((BDictionary)this.decodedFile.get("info")).ToBencodedString();
        this.infoHash = TorrentFile.hash(encodedInfo.getBytes(Charset.forName("UTF-8")));
        this.hexInfoHash = TorrentFile.byteArrayToHex(this.infoHash);
        try 
        {
            this.trackers = new ArrayList<List<URI>>();
            this.allTrackers = new HashSet<URI>();

            if (!this.decodedFile.containsKey("announce-list")) 
            {
                URI tracker = new URI(((BString)this.decodedFile.get("announce")).Value);
                this.allTrackers.add(tracker);

                // Build a single-tier announce list.
                List<URI> tier = new ArrayList<URI>();
                tier.add(tracker);
                this.trackers.add(tier);
            } 
            else 
            {
                List<BElement> tiers = ((BList)this.decodedFile.get("announce-list")).Values;
                for (BElement tv : tiers) 
                {
                    List<BElement> trackers = ((BList)tv).Values;
                    if (trackers.isEmpty())
                    {
                        continue;
                    }

                    List<URI> tier = new ArrayList<URI>();
                    for (BElement tracker : trackers) 
                    {
                            URI uri = new URI(((BString)tracker).Value);

                            // Make sure we're not adding duplicate trackers.
                            if (!this.allTrackers.contains(uri)) 
                            {
                                tier.add(uri);
                                this.allTrackers.add(uri);
                            }
                    }

                    // Only add the tier if it's not empty.
                    if (!tier.isEmpty()) 
                    {
                        this.trackers.add(tier);
                    }
                }
            }
        } 
        catch (URISyntaxException ex) 
        {
            throw new IOException(ex);
        }

        this.creationDate = this.decodedFile.containsKey("creation date")
                ? new Date(((BInteger)this.decodedFile.get("creation date")).Value * 1000)
                : null;
        this.comment = this.decodedFile.containsKey("comment")
                ? this.decodedFile.get("comment").getString()
                : null;
        this.createdBy = this.decodedFile.containsKey("created by")
                ? this.decodedFile.get("created by").getString()
                : null;
        this.fileName = this.decodedInfo.get("name").getString();

        this.files = new LinkedList<>();

        // Parse multi-file torrent file information structure.
        if (this.decodedInfo.containsKey("files")) 
        {
            for (BElement file : this.decodedInfo.get("files").getList()) 
            {
                Map<String, BElement> fileInfo = ((BDictionary)file).getMap();
                StringBuilder path = new StringBuilder();
                for (BElement pathElement : fileInfo.get("path").getList()) 
                {
                        path.append(File.separator).append(pathElement.getString());
                }
                this.files.add(new TFile(new File(this.fileName, path.toString()), ((BInteger)fileInfo.get("length")).Value));
            }
        }
        else 
        {
            // For single-file torrents, the fileName of the torrent is
            // directly the fileName of the file.
            this.files.add(new TFile(new File(this.fileName), ((BInteger)this.decodedInfo.get("length")).Value));
        }

        // Calculate the total size of this torrent from its files' sizes.
        long size = 0;
        for (TFile file : this.files) 
        {
            size += file.size;
        }
        this.size = size;
        
        //TODO make some kind of logging to file or smth
        String msg = (files.size() > 1 ? "Multi" : "Single") + "-file torrent information: " ;
        System.out.println(msg);
        System.out.println("  Torrent name: " + this.fileName);
        System.out.print("  Announced at: ");
        for (int i=0; i < this.trackers.size(); i++) 
        {
            List<URI> tier = this.trackers.get(i);
            for (int j=0; j < tier.size(); j++) 
            {
                //TODO make some kind of logging to file or smth
                System.out.println(tier.get(j).toString());
            }
        }

        if (this.creationDate != null) 
        {
            //TODO make some kind of logging to file or smth
            System.out.println("  Created on: " + this.creationDate.toString());
        }
        if (this.comment != null) 
        {
            //TODO make some kind of logging to file or smth
            System.out.println("  Comment: " +  this.comment);
        }
        if (this.createdBy != null) 
        {
            //TODO make some kind of logging to file or smth
            System.out.println("  Created by: " +  this.createdBy);
        }

        if (files.size() > 1) 
        {
            //TODO make some kind of logging to file or smth
            System.out.println("  Found " + String.valueOf(this.files.size()) +  "file(s) in multi-file torrent structure.");
            int i = 0;
            for (TFile file : this.files) 
            {
                //TODO make some kind of logging to file or smth
                System.out.println(file.file.getPath());
            }
        }
        
        //TODO make some kind of logging to file or smth
        String s = String.valueOf((this.size / ((BInteger)this.decodedInfo.get("piece length")).Value) + 1);
        System.out.println("  Pieces: "+ s + " piece(s) (" +((this.decodedInfo.get("piece length")).toString()) + "byte(s)/piece)");
        System.out.println("  Total size..: " + this.size + " byte(s)");
    }
    
    
    /**
    * Save this {@link TorrentFile} object into a .torrent file.
    *
    * @param output The stream to write to.
    * @throws IOException If an I/O error occurs while writing the file.
    */
   public void save(FileOutputStream output) throws IOException 
   {
       output.write(this.encodedFile);
   }
        
    /**
     * Load torrent from file
     * 
     * @param torrent .torrent file
     * @return 
     * @throws IOException When the torrent file cannot be read.
     * @throws NoSuchAlgorithmException
     */
    public static TorrentFile load(File torrent) throws IOException, NoSuchAlgorithmException 
    {
        return TorrentFile.load(torrent, false);
    }

    /**
     * Load torrent from file.
     *
     * @param torrent
     * @param seeder Whether we are a seeder for this torrent or not (disables
     * local data validation).
     * @return 
     * @throws IOException When the torrent file cannot be read.
     * @throws NoSuchAlgorithmException
     */
    public static TorrentFile load(File torrent, boolean seeder) throws IOException, NoSuchAlgorithmException
    {
        FileInputStream fis = null;
        try 
        {
            fis = new FileInputStream(torrent);
            byte[] data = new byte[(int)torrent.length()];
            fis.read(data);
            return new TorrentFile(data, seeder);
        }
        finally
        {
            if (fis != null)
                fis.close();
        }
    }
    
    
	/**
	 * Create a {@link TorrentFile} object for a single file.
	 *
	 * Hash the given file to create the {@link TorrentFile} object representing
	 * the Torrent metainfo about this file, needed for announcing and/or
	 * sharing said file.
	 *
	 * @param source The file to use in the torrent.
	 * @param announce The announce URI that will be used for this torrent.
	 * @param createdBy The creator's name, or any string identifying the
	 * torrent's creator.
	 */
	public static TorrentFile create(File source, URI announce, String createdBy)
                throws NoSuchAlgorithmException, InterruptedException, IOException 
        {
            return TorrentFile.create(source, null, announce, createdBy);
	}
        
        /**
	 * Create a {@link TorrentFile} object for a multiple files.
	 *
	 * Hash the given files to create the multi-file {@link Torrent} object
	 * representing the Torrent meta-info about them, needed for announcing
	 * and/or sharing these files. Since we created the torrent, we're
	 * considering we'll be a full initial seeder for it.
	 *
	 * @param parent The parent directory or location of the torrent files,
	 * also used as the torrent's name.
	 * @param files The files to add into this torrent.
	 * @param announce The announce URI that will be used for this torrent.
	 * @param createdBy The creator's name, or any string identifying the
	 * torrent's creator.
	 */
	public static TorrentFile create(File parent, List<File> files, URI announce, String createdBy) 
            throws NoSuchAlgorithmException, InterruptedException, IOException 
        {
            if (files == null || files.isEmpty()) 
            {
                 //TODO make some kind of logging to file or smth
                System.out.println("Creating single-file torrent for: " + parent.getName());
            }
            else 
            {   
                //TODO make some kind of logging to file or smth
                System.out.println("Creating " + String.valueOf(files.size())+ " - file torrent " + parent.getName());
            }

            Map<String, BElement> torrent = new HashMap<String, BElement>();
            torrent.put("announce", new BString(announce.toString()));
            torrent.put("creation date", new BInteger(new Date().getTime() / 1000));
            torrent.put("created by", new BString(createdBy));

            Map<String, BElement> info = new TreeMap<String, BElement>();
            info.put("name", new BString(parent.getName()));
            info.put("piece length", new BInteger(TorrentFile.pieceLength));

            if (files == null || files.isEmpty()) 
            {
                info.put("length", new BInteger(parent.length()));
                //TODO
                info.put("pieces", new BString(TorrentFile.fileToHash(parent)/*, TorrentFile.BYTE_ENCODING*/));
            } 
            else 
            {
                List<BElement> fileInfo = new LinkedList<BElement>();
                for (File file : files) 
                {
                    Map<String, BElement> fileMap = new HashMap<String, BElement>();
                    fileMap.put("length", new BInteger(file.length()));

                    List<BElement> filePath = new LinkedList<BElement>();
                    while (file != null) 
                    {
                            if (file.equals(parent))
                            {
                                    break;
                            }

                            filePath.add(new BString(file.getName()));
                            file = file.getParentFile();
                    }

                    fileMap.put("path", new BList(filePath));
                    fileInfo.add(new BDictionary(fileMap));
                }
                info.put("files", new BList(fileInfo));
                info.put("pieces", new BString(TorrentFile.filesToHash(files)/*,TorrentFile.BYTE_ENCODING*/));
            }
            torrent.put("info", new BDictionary(info));

            BDictionary b = new BDictionary(torrent);
            return new TorrentFile(b.ToBencodedString().getBytes(Charset.forName("UTF-8")), true);
	}
    
    public static byte[] hash(byte[] data) throws NoSuchAlgorithmException 
    {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data);
            return md.digest();
    }

    /**
     * Convert a byte string to a string containing an hexadecimal
     * representation of the original data.
     *
     * @param bytes The byte array to convert.
     * @return 
     */
    public static String byteArrayToHex(byte[] bytes)
    {
            BigInteger bi = new BigInteger(1, bytes);
            return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
    
    private static String fileToHash(File file)
	throws NoSuchAlgorithmException, InterruptedException, IOException 
    {
	return TorrentFile.filesToHash(Arrays.asList(new File[] { file }));
    }
    
    private static String filesToHash(List<File> files)
	throws NoSuchAlgorithmException, InterruptedException, IOException 
    {
        //TODO
        return null;
    }
}
