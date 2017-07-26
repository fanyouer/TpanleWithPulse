package android_serialport_api;

public class SendDataThread extends Thread {
    boolean mByteReceivedBack;
    Object mByteReceivedBackSemaphore = new Object();

    @Override
    public void run() {

        super.run();
        while (!isInterrupted()) {
            synchronized (mByteReceivedBackSemaphore) {
                mByteReceivedBack = false;


            }


        }

    }
}
