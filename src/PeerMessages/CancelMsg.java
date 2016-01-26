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
public class CancelMsg extends Messages {

    public int index;
    public int begin;
    public int length;

    public CancelMsg(int index, int begin, int length) {
        super(Messages.ID_CANCEL);
        this.index = index;
        this.begin = begin;
        this.length = length;
    }

    static Messages readCancel(DataInputStream in) throws IOException {

        int index = in.readInt();
        int begin = in.readInt();
        int length = in.readInt();
        Messages msg = new CancelMsg(index, begin, length);
        return msg;

    }
}
