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
public class PieceMsg extends Messages {

    public int index;
    public int begin;
    public byte[] piece;

    public PieceMsg(int index, int begin, byte[] piece) {
        super(Messages.ID_PIECE);
        this.index = index;
        this.begin = begin;
        this.piece = piece;
    }

    static Messages readPiece(DataInputStream in, int length)
            throws IOException {

        int index = in.readInt();
        int begin = in.readInt();
        byte[] block = new byte[length - 9];
        in.readFully(block);
        Messages msg = new PieceMsg(index, begin, block);
        return msg;

    }

}
