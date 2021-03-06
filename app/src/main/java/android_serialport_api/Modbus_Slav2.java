package android_serialport_api;

import it.ma.crc.CRC_16;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class Modbus_Slav2 extends Thread {
    private short phone_dial_1;
    private short phone_dial_2;
    private short phone_dial_3;
    private short phone_dial_4;
    private short phone_dial_5;
    private short phone_dial_6;
    private short phone_dial_7;
    private short phone_dial_8;
    private short phone_dial_9;
    private short phone_dial_0;
    private short phone_dial_miHao;
    private short phone_dial_jingHao;
    private short phone_dial_miantiJian;
    private short duiJiangJian;
    private short backMusic;
    private short BackMusic_upDown;

    int[] regHodingBuf = new int[1024];

    public int SLAV_addr = 1;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    private SerialPort mserialPort = null;


    public Modbus_Slav2() {

        try {
            mserialPort = getSerialPort();
        } catch (InvalidParameterException e) {

            e.printStackTrace();
        } catch (SecurityException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        mInputStream = mserialPort.getInputStream();
        mOutputStream = mserialPort.getOutputStream();

    }


    /**
     * 数据等待接收
     */


    public void run() {
        super.run();
        while (!isInterrupted()) {

            int size;
            try {
                byte[] reBuf = new byte[1024];
                if (mInputStream == null) return;
                size = mInputStream.read(reBuf);
                if (size > 3)
                    onDataReceived(reBuf, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


/***********************************************  *********************************************/

    /***
     *
     * @return mserialPort_1
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */


    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mserialPort == null) {


            String path = "/dev/ttyS1";
            int baudrate = 19200;
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            mserialPort = new SerialPort(new File(path), baudrate, 0);

        }
        return mserialPort;
    }


    private void onDataReceived(byte[] reBuf, int size) {

        if (!(SLAV_addr == reBuf[0])) {
            return;
        }
        if (size <= 3)
            return;
        if (CRC_16.checkBuf(reBuf)) {
            switch (reBuf[1]) {
                case 0x03:
                    mod_Fun_03_Slav(reBuf);
                    break;
                case 0x06:
                    //	mod_Fun_06_Slav_1(reBuf_1,size);	break;
                case 0x10:
                    mod_Fun_16_Slav(reBuf, size);
                    break;
            }
        }

    }

    public void onDataSend(byte[] seBuf, int size) {
        try {
            mOutputStream.write(seBuf, 0, size);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private void mod_Fun_16_Slav(byte[] reBuf, int size) {

        int addr, len;
        short val;
        byte[] seBuf = new byte[1024];
        CRC_16 crc = new CRC_16();

        addr = (crc.getUnsignedByte(reBuf[2])) << 8;
        addr |= crc.getUnsignedByte(reBuf[3]);
        len = (crc.getUnsignedByte(reBuf[4])) << 8;
        len |= crc.getUnsignedByte(reBuf[5]);
        for (int i = 0; i < len; i++) {
            val = (short) ((crc.getUnsignedByte(reBuf[7 + 2 * i])) << 8);
            val |= crc.getUnsignedByte(reBuf[8 + 2 * i]);
            regHodingBuf[addr + i] = val;
        }
        for (int i = 0; i < 6; i++) {
            seBuf[i] = reBuf[i];
        }
        crc.update(seBuf, 6);
        int value = crc.getValue();
        seBuf[6] = (byte) crc.getUnsignedByte((byte) ((value >> 8) & 0xff));
        seBuf[7] = (byte) crc.getUnsignedByte((byte) (value & 0xff));
        slav_hand_10();
        onDataSend(seBuf, 8);

    }

    private void slav_hand_10() {

    }

    private void mod_Fun_03_Slav(byte[] reBuf) {
        slav_int_03();
        int addr;
        int len;
        CRC_16 crc = new CRC_16();
        byte[] seBuf = new byte[1024];
        addr = (crc.getUnsignedByte(reBuf[2])) << 8;
        addr |= crc.getUnsignedByte(reBuf[3]);
        len = (crc.getUnsignedByte(reBuf[4])) << 8;
        len |= crc.getUnsignedByte(reBuf[5]);
        if (len + addr > 64)
            return;
        else {
            seBuf[0] = (byte) reBuf[0];
            seBuf[1] = (byte) reBuf[1];
            seBuf[2] = (byte) (2 * len);
            for (int i = 0; i < len; i++) {
                seBuf[3 + 2 * i] = (byte) (crc.getUnsignedIntt(regHodingBuf[i + addr]) >> 8);
                seBuf[4 + 2 * i] = (byte) (crc.getUnsignedIntt(regHodingBuf[i + addr]));

            }
            crc.update(seBuf, 2 * len + 3);
            int value = crc.getValue();
            seBuf[3 + 2 * len] = (byte) crc.getUnsignedByte((byte) ((value >> 8) & 0xff));
            seBuf[4 + 2 * len] = (byte) crc.getUnsignedByte((byte) (value & 0xff));
        }

        onDataSend(seBuf, 4 + 2 * len + 1);


    }


    private void slav_int_03() {

        regHodingBuf[0] = (phone_dial_0 << 0) | (phone_dial_1 << 1) | (phone_dial_2 << 2) | (phone_dial_3 << 3) | (phone_dial_4 << 4) | (phone_dial_5 << 5) | (phone_dial_6 << 6) | (phone_dial_7 << 7) | (phone_dial_8 << 8) | (phone_dial_9 << 9) | (phone_dial_miHao << 10) | (phone_dial_jingHao << 11) | (phone_dial_miantiJian << 12);
        regHodingBuf[1] = duiJiangJian;
        regHodingBuf[2] = backMusic;
        regHodingBuf[3] = BackMusic_upDown;

    }


    public short getPhone_dial_1() {
        return phone_dial_1;
    }


    public void setPhone_dial_1(short phone_dial_1) {
        this.phone_dial_1 = phone_dial_1;
    }


    public short getPhone_dial_2() {
        return phone_dial_2;
    }


    public void setPhone_dial_2(short phone_dial_2) {
        this.phone_dial_2 = phone_dial_2;
    }


    public short getPhone_dial_3() {
        return phone_dial_3;
    }


    public void setPhone_dial_3(short phone_dial_3) {
        this.phone_dial_3 = phone_dial_3;
    }


    public short getPhone_dial_4() {
        return phone_dial_4;
    }


    public void setPhone_dial_4(short phone_dial_4) {
        this.phone_dial_4 = phone_dial_4;
    }


    public short getPhone_dial_5() {
        return phone_dial_5;
    }


    public void setPhone_dial_5(short phone_dial_5) {
        this.phone_dial_5 = phone_dial_5;
    }


    public short getPhone_dial_6() {
        return phone_dial_6;
    }


    public void setPhone_dial_6(short phone_dial_6) {
        this.phone_dial_6 = phone_dial_6;
    }


    public short getPhone_dial_7() {
        return phone_dial_7;
    }


    public void setPhone_dial_7(short phone_dial_7) {
        this.phone_dial_7 = phone_dial_7;
    }


    public short getPhone_dial_8() {
        return phone_dial_8;
    }


    public void setPhone_dial_8(short phone_dial_8) {
        this.phone_dial_8 = phone_dial_8;
    }


    public short getPhone_dial_9() {
        return phone_dial_9;
    }


    public void setPhone_dial_9(short phone_dial_9) {
        this.phone_dial_9 = phone_dial_9;
    }


    public short getPhone_dial_0() {
        return phone_dial_0;
    }


    public void setPhone_dial_0(short phone_dial_0) {
        this.phone_dial_0 = phone_dial_0;
    }


    public short getPhone_dial_miHao() {
        return phone_dial_miHao;
    }


    public void setPhone_dial_miHao(short phone_dial_miHao) {
        this.phone_dial_miHao = phone_dial_miHao;
    }


    public short getPhone_dial_jingHao() {
        return phone_dial_jingHao;
    }


    public void setPhone_dial_jingHao(short phone_dial_jingHao) {
        this.phone_dial_jingHao = phone_dial_jingHao;
    }


    public short getPhone_dial_miantiJian() {
        return phone_dial_miantiJian;
    }


    public void setPhone_dial_miantiJian(short phone_dial_miantiJian) {
        this.phone_dial_miantiJian = phone_dial_miantiJian;
    }


    public short getDuiJiangJian() {
        return duiJiangJian;
    }


    public void setDuiJiangJian(short duiJiangJian) {
        this.duiJiangJian = duiJiangJian;
    }


    public short getBackMusic() {
        return backMusic;
    }


    public void setBackMusic(short backMusic) {
        this.backMusic = backMusic;
    }

    public short getBackMusic_upDown() {
        return BackMusic_upDown;
    }


    public void setBackMusic_upDown(short backMusic_upDown) {
        BackMusic_upDown = backMusic_upDown;
    }


}