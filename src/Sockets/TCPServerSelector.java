/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lukasz
 */
public class TCPServerSelector {

    private static final int BUFSIZE = 256;
    private static final int TIMEOUT = 3000;
    private Selector oSelector;
    private String[] sPorts;
    private TCPProtocol protocol;
    Iterator<SelectionKey> keyIterator;

    public TCPServerSelector() {
        this.protocol = new TCPEchoSelectorProtocol(BUFSIZE);
        this.oSelector = oSelector;
        init();
    }

    private void init() {
        try {
            this.oSelector = Selector.open();
            createServerSocketChannel();
            waitForOutputOrInputSignal();
        } catch (IOException ex) {
            Logger.getLogger(TCPServerSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Selector getoSelector() {
        return oSelector;
    }

    public String[] getiPorts() {
        return sPorts;
    }

    public void setiPorts(String[] sPorts) {
        this.sPorts = sPorts;
    }

    public void setoSelector(Selector oSelector) {
        this.oSelector = oSelector;
    }

    public TCPProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(TCPProtocol protocol) {
        this.protocol = protocol;
    }

    public Iterator<SelectionKey> getKeyIterator() {
        return keyIterator;
    }

    public void setKeyIterator(Iterator<SelectionKey> keyIterator) {
        this.keyIterator = keyIterator;
    }

    private void createServerSocketChannel() throws IOException {
        for (String sPort : getiPorts()) {
            ServerSocketChannel listnChannel = ServerSocketChannel.open();
            listnChannel.socket().bind(new InetSocketAddress(Integer.parseInt(sPort)));
            listnChannel.configureBlocking(false);
            listnChannel.register(getoSelector(), SelectionKey.OP_ACCEPT);
        }
    }

    private void waitForOutputOrInputSignal() throws IOException {
        while (true) {
            if (getoSelector().select(TIMEOUT) == 0) {
                System.out.print(".");
                continue;
            }
            Iterator<SelectionKey> keyIter = getoSelector().selectedKeys().iterator();
            while (keyIter.hasNext()) {
                SelectionKey key = keyIter.next();
                if (key.isAcceptable()) {
                    protocol.handleAccept(key);
                }

                if (key.isReadable()) {
                    protocol.handleRead(key);

                    if (key.isValid() && key.isWritable()) {
                        protocol.handleWrite(key);
                    }
                    keyIter.remove();
                }
            }
        }
    }
}
