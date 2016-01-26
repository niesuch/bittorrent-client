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
public class RequestMsg extends Messages {

    public int index;
    public int begin;
    public int length;

    public RequestMsg(int index, int begin, int length) {
        super(Messages.ID_REQUEST);
        this.index = index;
        this.begin = begin;
        this.length = length;
    }

    private static Messages readRequest(DataInputStream in) throws IOException {

        int index = in.readInt();
        int begin = in.readInt();
        int length = in.readInt();
        Messages msg = new RequestMsg(index, begin, length);
        return msg;

    }
}
