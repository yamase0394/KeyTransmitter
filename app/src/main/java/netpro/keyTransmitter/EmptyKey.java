package netpro.keyTransmitter;

import java.io.Serializable;

public class EmptyKey extends Key implements Serializable{

    public EmptyKey() {
        super(1, 1, "", "", Type.EMPTY);
    }

    @Override
    public void onActionDown() {

    }

    @Override
    public void onActionUp() {

    }

    @Override
    public void onLongClick() {

    }
}
