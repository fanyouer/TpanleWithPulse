package android_serialport_api;

import it.ma.crc.CRC_16;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class Modbus_Slav1 extends Thread {

    private short backMusic;
    private short BackMusic_upDown;

    /***
     * 氧气
     */
    private short Oxygen_IS_Normal;

    /***
     * 笑气
     */
    private short LaughingGas_IS_Normal;


    /***
     * 氩气
     */
    private short ArgonGas_IS_Normal;


    /***
     * 氮气
     */
    private short NitrogenGas_IS_Normal;

    /***
     * 负压
     */
    private short NegativePressure_IS_Normal;


    /***
     * 压缩空气
     */
    private short PressAirGas_IS_Normal;


    /***
     * 二氧化碳
     */
    private short Carbon_IS_Normal;


    private short Lightling_1 = 1;

    private short Lightling_2 = 1;

    /***
     * 无影灯
     */
    private short Shadowless_Lamp = 1;
    /***
     * 术中灯
     */
    private short Intraoperative_Lamp = 1;
    /***
     * 观片灯
     */
    private short OfLightThe_Lamp = 1;
    /***
     * 备用
     */
    private short Prepare = 1;
    /***
     * 消音
     */
    private short Erasure = 1;

    int[] regHodingBuf = new int[1024];


    public int SLAV_addr = 1;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    private SerialPort mserialPort = null;


    public Modbus_Slav1() {


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
                try {
                    mInputStream.close();
                    mInputStream.close();
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
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

            String path = "/dev/ttyS3";
            int baudrate = 9600;
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

                case 0x10:
                    mod_Fun_16_Slav(reBuf, size);
                    break;
            }
        }

    }

    public void onDataSend(byte[] seBuf, int size) {
        try {
            mOutputStream = mserialPort.getOutputStream();
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
        int value_1 = crc.getValue();
        seBuf[6] = (byte) crc.getUnsignedByte((byte) ((value_1 >> 8) & 0xff));
        seBuf[7] = (byte) crc.getUnsignedByte((byte) (value_1 & 0xff));

        slav_hand_10();
        onDataSend(seBuf, 8);

    }

    private void slav_hand_10() {
        /***
         * 氧气
         */
        Oxygen_IS_Normal = (short) (regHodingBuf[6]);

        /***
         * 笑气
         */
        LaughingGas_IS_Normal = (short) (regHodingBuf[7]);


        /***
         * 氩气
         */
        ArgonGas_IS_Normal = (short) (regHodingBuf[8]);


        /***
         * 氮气
         */
        NitrogenGas_IS_Normal = (short) (regHodingBuf[9]);


        /***
         * 负压
         */

        NegativePressure_IS_Normal = (short) (regHodingBuf[10]);

        /***
         * 压缩空气
         */
        PressAirGas_IS_Normal = (short) (regHodingBuf[11]);


        /***
         * 二氧化碳
         */
        Carbon_IS_Normal = (short) (regHodingBuf[12]);


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

        regHodingBuf[0] = backMusic;
        regHodingBuf[1] = BackMusic_upDown;
        regHodingBuf[2] = (Prepare << 0) | (Intraoperative_Lamp << 1) | (Lightling_2 << 2) | (OfLightThe_Lamp << 3) | (Shadowless_Lamp << 4) | (Lightling_1 << 5) | (Erasure << 6);


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


    public short getOxygen_IS_Normal() {
        return Oxygen_IS_Normal;
    }


    public void setOxygen_IS_Normal(short oxygen_IS_Normal) {
        Oxygen_IS_Normal = oxygen_IS_Normal;
    }


    public short getLaughingGas_IS_Normal() {
        return LaughingGas_IS_Normal;
    }


    public void setLaughingGas_IS_Normal(short laughingGas_IS_Normal) {
        LaughingGas_IS_Normal = laughingGas_IS_Normal;
    }


    public short getArgonGas_IS_Normal() {
        return ArgonGas_IS_Normal;
    }


    public void setArgonGas_IS_Normal(short argonGas_IS_Normal) {
        ArgonGas_IS_Normal = argonGas_IS_Normal;
    }


    public short getNitrogenGas_IS_Normal() {
        return NitrogenGas_IS_Normal;
    }


    public void setNitrogenGas_IS_Normal(short nitrogenGas_IS_Normal) {
        NitrogenGas_IS_Normal = nitrogenGas_IS_Normal;
    }


    public short getNegativePressure_IS_Normal() {
        return NegativePressure_IS_Normal;
    }


    public void setNegativePressure_IS_Normal(short negativePressure_IS_Normal) {
        NegativePressure_IS_Normal = negativePressure_IS_Normal;
    }


    public short getPressAirGas_IS_Normal() {
        return PressAirGas_IS_Normal;
    }


    public void setPressAirGas_IS_Normal(short pressAirGas_IS_Normal) {
        PressAirGas_IS_Normal = pressAirGas_IS_Normal;
    }


    public short getCarbon_IS_Normal() {
        return Carbon_IS_Normal;
    }


    public void setCarbon_IS_Normal(short carbon_IS_Normal) {
        Carbon_IS_Normal = carbon_IS_Normal;
    }


    public short getLightling_1() {
        return Lightling_1;
    }


    public void setLightling_1(short lightling_1) {
        Lightling_1 = lightling_1;
    }


    public short getLightling_2() {
        return Lightling_2;
    }


    public void setLightling_2(short lightling_2) {
        Lightling_2 = lightling_2;
    }


    public short getShadowless_Lamp() {
        return Shadowless_Lamp;
    }


    public void setShadowless_Lamp(short shadowless_Lamp) {
        Shadowless_Lamp = shadowless_Lamp;
    }


    public short getIntraoperative_Lamp() {
        return Intraoperative_Lamp;
    }


    public void setIntraoperative_Lamp(short intraoperative_Lamp) {
        Intraoperative_Lamp = intraoperative_Lamp;
    }


    public short getOfLightThe_Lamp() {
        return OfLightThe_Lamp;
    }


    public void setOfLightThe_Lamp(short ofLightThe_Lamp) {
        OfLightThe_Lamp = ofLightThe_Lamp;
    }


    public short getPrepare() {
        return Prepare;
    }


    public void setPrepare(short prepare) {
        Prepare = prepare;
    }


    public short getErasure() {
        return Erasure;
    }


    public void setErasure(short erasure) {
        Erasure = erasure;
    }


    public int[] getRegHodingBuf() {
        return regHodingBuf;
    }

    public void setRegHodingBuf_1(int[] regHodingBuf) {
        this.regHodingBuf = regHodingBuf;
    }

}