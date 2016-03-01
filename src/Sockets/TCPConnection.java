package Sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukasz
 */
public class TCPConnection {

    private String sServerName;
    private int iServerPort;
    private byte[] byArguments;
    private SocketChannel oClientChannel;
    private ByteBuffer oWriteBuffer;
    private ByteBuffer oReadBuffer;
    private int iTotalBytesReceived;
    private int iBytesReceived;

    public TCPConnection(String sServerName, int iServerPort) {
        this.sServerName = sServerName;
        this.iServerPort = iServerPort;
        this.byArguments = sServerName.getBytes();
        init();
    }

    public void init() {
        openChannel();
        //readOrWriteArguments();
        //sentAndReceiveAllBytes();
        //closeConnection();
    }

    public String getsServerName() {
        return sServerName;
    }

    public void setsServerName(String sServerName) {
        this.sServerName = sServerName;
    }

    public int getiServerPort() {
        return iServerPort;
    }

    public void setiServerPort(int iServerPort) {
        this.iServerPort = iServerPort;
    }

    public byte[] getByArguments() {
        return byArguments;
    }

    public void setByArguments(byte[] byArguments) {
        this.byArguments = byArguments;
    }

    public SocketChannel getoClientChannel() {
        return oClientChannel;
    }

    public void setoClientChannel(SocketChannel oClientChannel) {
        this.oClientChannel = oClientChannel;
    }

    public ByteBuffer getoWriteBuffer() {
        return oWriteBuffer;
    }

    public void setoWriteBuffer(ByteBuffer oWriteBuffer) {
        this.oWriteBuffer = oWriteBuffer;
    }

    public ByteBuffer getoReadBuffer() {
        return oReadBuffer;
    }

    public void setoReadBuffer(ByteBuffer oReadBuffer) {
        this.oReadBuffer = oReadBuffer;
    }

    public int getiTotalBytesReceived() {
        return iTotalBytesReceived;
    }

    public void setiTotalBytesReceived(int iTotalBytesReceived) {
        this.iTotalBytesReceived = iTotalBytesReceived;
    }

    public int getiBytesReceived() {
        return iBytesReceived;
    }

    public void setiBytesReceived(int iBytesReceived) {
        this.iBytesReceived = iBytesReceived;
    }

    private void openChannel() {
        try {
            this.oClientChannel = SocketChannel.open();
            this.oClientChannel.configureBlocking(false);
            initializeServerConnection();
        } catch (IOException ex) {
            Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeServerConnection() throws IOException {
        try{
         InetAddress oAddress;
         Socket oSocket = new Socket(getsServerName(), getiServerPort());
         oAddress = oSocket.getInetAddress();
         System.out.println("Connected to " + oAddress);
         oSocket.close();
      } catch (java.io.IOException e) {
         System.out.println("Can't connect to " );
         System.out.println(e);
      }
    }

    private void readOrWriteArguments() {
        this.oWriteBuffer = ByteBuffer.wrap(getByArguments());
        this.oReadBuffer = ByteBuffer.allocate(getByArguments().length);
    }

    private void sentAndReceiveAllBytes() {
        while (this.iTotalBytesReceived < getByArguments().length) {
            System.out.println(this.oWriteBuffer);
            if (this.oWriteBuffer.hasRemaining()) {
                try {
                    this.oClientChannel.write(getoWriteBuffer());
                } catch (IOException ex) {
                    Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                if ((this.iTotalBytesReceived = this.oClientChannel.read(getoReadBuffer())) == -1) {
                    throw new SocketException("Connection has been closed prematurely");
                }
            } catch (IOException ex) {
                Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.iTotalBytesReceived += this.iBytesReceived;
            System.out.print("Test");
        }
    }
   
    public void printData() {
        System.out.println("Received: " + new String(getoReadBuffer().array(), 0, getiTotalBytesReceived()));
    }

    private void closeConnection() {
        try {
            getoClientChannel().close();
        } catch (IOException ex) {
            Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
