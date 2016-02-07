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
public class HaveMsg extends Messages {

    public int index;

    public HaveMsg(int index) {
        super(Messages.ID_HAVE);
        this.index = index;
    }

    static Messages readHave(DataInputStream in) throws IOException {

        int index = in.readInt();
        Messages msg = new HaveMsg(index);
        return msg;

    }
}
