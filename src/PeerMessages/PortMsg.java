/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerMessages;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author pawel.banasiuk
 */
public class PortMsg extends Messages {

    public int port;

    public PortMsg(int port) {
        super(Messages.ID_PORT);
        this.port = port;
    }

    static Messages readPort(DataInputStream in) throws IOException {

        short port = in.readShort();
        Messages msg = new PortMsg(port);
        return msg;

    }

}
