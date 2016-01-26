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
public class BitfieldMsg extends Messages {

    public byte[] bytefield;

    public BitfieldMsg(byte[] bytefield) {
        super(Messages.ID_BITFIELD);
        this.bytefield = bytefield;
    }

    private static Messages readBitfield(DataInputStream in, int length)
            throws IOException {

        byte[] bytefield = new byte[length - 1];
        in.read(bytefield);
        Messages msg = new BitfieldMsg(bytefield);
        return msg;

    }
}
