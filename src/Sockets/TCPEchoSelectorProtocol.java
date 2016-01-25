/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Lukasz
 */
public class TCPEchoSelectorProtocol implements TCPProtocol {

    private final int iBufforSize;

    public TCPEchoSelectorProtocol(int iBufforSize) {
        this.iBufforSize = iBufforSize;
    }
    
    @Override
    public void handleAccept(SelectionKey oKey) throws IOException {
        SocketChannel oClientChannel = ((ServerSocketChannel) oKey.channel()).accept();
        oClientChannel.configureBlocking(false);
        oClientChannel.register(oKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(iBufforSize));

    }
    @Override
    public void handleRead(SelectionKey oKey) throws IOException {

        SocketChannel oClientChannel = (SocketChannel) oKey.channel();
        ByteBuffer oByteBuffer = (ByteBuffer) oKey.attachment();
        long lBytesRead = oClientChannel.read(oByteBuffer);
        if (lBytesRead == -1) {
            oClientChannel.close();
        } else if (lBytesRead > 0) {
            oKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
    }

    @Override
    public void handleWrite(SelectionKey oKey) throws IOException {

        ByteBuffer oByteBuffer = (ByteBuffer) oKey.attachment();
        oByteBuffer.flip();
        SocketChannel oClientChannel = (SocketChannel) oKey.channel();
        oClientChannel.write(oByteBuffer);
        if (!oByteBuffer.hasRemaining()) {
            oKey.interestOps(SelectionKey.OP_READ);
        }
        oByteBuffer.compact();
    }
}
