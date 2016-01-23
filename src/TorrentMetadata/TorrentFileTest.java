/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TorrentMetadata;

import java.io.IOException;

/**
 *
 * @author Robert
 */
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class TorrentFileTest
{   
    public static void main(String [] args) throws IOException         
    { 
        TorrentFile test = null;
        
        //TODO make it universal, get the path of the project or smth
        //Test loading .torrent
        String torrent = "C:\\Users\\Robert\\Downloads\\sample.torrent";
        
        File torrentFile = new File(torrent);
        try
        {
            test = TorrentFile.load(torrentFile);
        } 
        catch (IOException | NoSuchAlgorithmException ex)
        {
            Logger.getLogger("TorrentFile Test").log(Level.SEVERE, null, ex);
        }
        
        //TODO make it universal, get the path of the project or smth
        //Test saving .torrent
        FileOutputStream fop = null;
        File file;

        try
        {
            file = new File("C:\\Users\\Robert\\Downloads\\sampleSaved.torrent");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) 
            {
                file.createNewFile();
            }

            test.save(fop);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                if (fop != null) 
                {
                    fop.close();
                }
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        
        //Test creating .torrent
        File createTorrentFile;
        File fileTMP;
        TorrentFile testCreate = null;
        FileOutputStream fopTestCreate = null;
        try
        {
            createTorrentFile = new File("C:\\Users\\Robert\\Downloads\\test.txt");
            URI testAnnounce = new URI("udp://tracker.openbittorrent.com");
            testCreate = TorrentFile.create(createTorrentFile,testAnnounce , "robert");
            fileTMP = new File("C:\\Users\\Robert\\Downloads\\sampleCreate.torrent");
            fopTestCreate = new FileOutputStream(fileTMP);
            
             // if file doesnt exists, then create it
            if (!fileTMP.exists()) 
            {
                fileTMP.createNewFile();
            }
            
            testCreate.save(fopTestCreate);
            fopTestCreate.flush();
            fopTestCreate.close();

            System.out.println("Creating .torrent from test.txt succesfull");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
