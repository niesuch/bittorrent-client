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
public class Messages {

    public static final int ID_ERROR = -1;
    public static final int ID_KEEP_ALIVE = 200;
    public static final int ID_CHOKE = 0;
    public static final int ID_UNCHOKE = 1;
    public static final int ID_INTERESTED = 2;
    public static final int ID_NOT_INTERESTED = 3;
    public static final int ID_HAVE = 4;
    public static final int ID_BITFIELD = 5;
    public static final int ID_REQUEST = 6;
    public static final int ID_PIECE = 7;
    public static final int ID_CANCEL = 8;
    public static final int ID_PORT = 9;

    public int messageType;

    public static final Messages KEEP_ALIVE = new Messages(ID_KEEP_ALIVE);

    public static final Messages CHOKE = new Messages(ID_CHOKE);

    public static final Messages UNCHOKE = new Messages(ID_UNCHOKE);

    public static final Messages INTERESTED = new Messages(ID_INTERESTED);

    public static final Messages UNINTERESTED = new Messages(ID_NOT_INTERESTED);

    public Messages(int messageType) {
        this.messageType = messageType;
    }

    public static Messages decode(DataInputStream in) throws IOException {

        int length = in.readInt();

        if (length == 0) {
            return KEEP_ALIVE;
        }

        int msgID = (int) in.readByte() & 0xFF;

        switch (msgID) {

            case Messages.ID_CHOKE:
                return CHOKE;
            case Messages.ID_UNCHOKE:
                return UNCHOKE;
            case Messages.ID_INTERESTED:
                return INTERESTED;
            case Messages.ID_NOT_INTERESTED:
                return UNINTERESTED;
            case Messages.ID_HAVE:
                return HaveMsg.readHave(in);
            case Messages.ID_BITFIELD:
                return BitfieldMsg.readBitfield(in, length);
            case Messages.ID_REQUEST:
                return RequestMsg.readRequest(in);
            case Messages.ID_PIECE:
                return PieceMsg.readPiece(in, length);
            case Messages.ID_CANCEL:
                return CancelMsg.readCancel(in);
            case Messages.ID_PORT:
                return PortMsg.readPort(in);
            default:
                throw new IOException("invalid msg:" + msgID);
        }
    }
}
