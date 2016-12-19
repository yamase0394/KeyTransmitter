package netpro.keyTransmitter;

public class EmptyKey extends Key {

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
