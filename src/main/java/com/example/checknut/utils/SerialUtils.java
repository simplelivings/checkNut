package com.example.checknut.utils;

import gnu.io.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-06-16 15:06
 */

public class SerialUtils {

    public static int sendDataToPort() throws PortInUseException {

        //1 获得串口列表，并取得第一个串口值
        List<String> portNameList = findPorts();
        String port1 = portNameList.get(0);

        //2 打开第一个串口
        final SerialPort p = openPort(port1, 9600);
        String data = "112233";
        byte[] b = data.getBytes();

        //3 向串口发送数据
        SerialUtils.sendToPort(p, b);
        return 1;
    }



    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public static final ArrayList<String> findPorts() {
        // 获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<String>();
        // 将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     * @throws PortInUseException 串口已被占用
     */
    public static final SerialPort openPort(String portName, int baudrate) throws PortInUseException {
        try {
            // 通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            // 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            // 判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    // 设置一下串口的波特率等参数
                    // 数据位：8
                    // 停止位：1
                    // 校验位：None
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
                return serialPort;
            }
        } catch (NoSuchPortException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭串口
     *
     * @param serialPort 待关闭的串口对象
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            System.out.println("closed");
        }
    }

    /**
     * 向串口发送数据
     *
     * @param serialPort 串口对象
     * @param order      待发送数据
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) {
        OutputStream out = null;
        try {
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeIO(out);
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    public static byte[] readFromPort(SerialPort serialPort) {
        InputStream in = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = {};
        try {
            in = serialPort.getInputStream();
            int n = 0;
            System.out.println("available = " + in.available());

            while (in.available() > 0) {
                System.out.println("available 2= " + in.available());
                byte[] buffer = new byte[1024];
                n = in.read(buffer);
                System.out.println("==n===" + n);
                bos.write(buffer, 0, n);
                in.read(bytes);
//
            }
//            while ((n = in.read(buffer)) != -1) {
//                System.out.println("available 2= "+ in.available());
//
//                System.out.println("=====" + in.read(buffer));
//                bos.write(buffer, 0, n);
//
//            }
            bos.flush();
            bytes = bos.toByteArray();
            System.out.println("length=" + bytes.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeIO(in, bos);
        }
        return bytes;
    }

    /**
     * 添加监听器
     *
     * @param serialPort 串口对象
     * @param listener   串口存在有效数据监听
     */
    public static void addListener(SerialPort serialPort, DataAvailableListener listener) {
        try {
            // 给串口添加监听器
            serialPort.addEventListener(new SerialPortListener(listener));
            // 设置当有数据到达时唤醒监听接收线程
            serialPort.notifyOnDataAvailable(true);
            // 设置当通信中
            // 断时唤醒中断线程
            serialPort.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    /**
     * 串口监听
     */
    public static class SerialPortListener implements SerialPortEventListener {

        private DataAvailableListener mDataAvailableListener;

        public SerialPortListener(DataAvailableListener mDataAvailableListener) {
            this.mDataAvailableListener = mDataAvailableListener;
        }

        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
                    if (mDataAvailableListener != null) {
                        try {
                            System.out.println("====case1====");
                            mDataAvailableListener.dataAvailable();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
                    System.out.println("====case2====");

                    break;

                case SerialPortEvent.CTS: // 3.清除待发送数据
                    System.out.println("====case3====");

                    break;

                case SerialPortEvent.DSR: // 4.待发送数据准备好了
                    System.out.println("====case4====");

                    break;

                case SerialPortEvent.RI: // 5.振铃指示
                    System.out.println("====case5====");

                    break;

                case SerialPortEvent.CD: // 6.载波检测
                    System.out.println("====case6====");

                    break;

                case SerialPortEvent.OE: // 7.溢位（溢出）错误
                    System.out.println("====case7====");

                    break;

                case SerialPortEvent.PE: // 8.奇偶校验错误
                    System.out.println("====case8====");

                    break;

                case SerialPortEvent.FE: // 9.帧错误
                    System.out.println("====case9====");

                    break;

                case SerialPortEvent.BI: // 10.通讯中断.
                    System.out.println("====case10====");

                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 串口存在有效数据监听
     */
    public interface DataAvailableListener {
        /**
         * 串口存在有效数据
         */
        void dataAvailable() throws UnsupportedEncodingException;
    }
}
