package ptools;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import gnu.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Terminal implements SerialPortEventListener {
    private static final int BUFF_SIZE = 20000;
    private static final int READ = 1;
    private static final int WRITE = 2;
    private static final int READ_LIST = 3;
    private byte[] buffer = new byte[BUFF_SIZE];
    private SerialPort serial = null;
    private InputStream instream = null;
    private OutputStream outstream = null;
    private boolean istimeout = false;
    private int offset = 0;
    private int portstatus = -1;
    private String command = "";
    private int timeout = 5000;
    private boolean success = false;
    private int pdulength = 0;
    private boolean pdutypereadable = true;
    private String result = null;

    private ArrayList  numbers = null;
    private int signal = 0;
    private String clock = "";
    private String phonenumber = "";
    private String phonename = "";
    private Map phonebooks = new HashMap();
    private int startnumber = 0;
    private int stopnumber = 0;

    private String resultcmd = null;
    private int replystatus = 0;
	
	private TerminalHandler handler = null;
	private TerminalHandler tmpHandler = null;
    public static int REPLY_OK = 0;
    public static int REPLY_ERROR = -1;
	
	public void setTerminalHandler(TerminalHandler handler) {
		this.handler = handler;
		this.tmpHandler = handler;
	}
	
	public TerminalHandler getTerminalHandler(){
		return handler;
	}
	public void connect(String portName, int baudRate, int data, int stop, int parity){
        CommPortIdentifier port = null;
        Enumeration portlist = CommPortIdentifier.getPortIdentifiers();
        while (portlist.hasMoreElements()) {
            port = (CommPortIdentifier) portlist.nextElement();
            System.out.println(port.getName());
            if (portName.equalsIgnoreCase(port.getName())) {
                System.out.println(port.getName() + " found, break");
                break;
            }
        }
        if (port == null) {
            System.out.println(portName +
                               " not found, please select other port");

        }
        else {
            System.out.println("try using port : " + port.getName());
            try {
                serial = (SerialPort) port.open("sms server", 3000);
                System.out.println("connected");
            }
            catch (PortInUseException piue) {
                System.out.println(piue);
            }

            try {
            	System.out.println("try to configure");
                serial.setSerialPortParams(baudRate,
                                           data,
                                           stop,
                                           parity);
                serial.notifyOnDataAvailable(true);
                serial.notifyOnOutputEmpty(true);
                serial.notifyOnBreakInterrupt(true);
                serial.notifyOnFramingError(true);
                serial.notifyOnOverrunError(true);
                serial.notifyOnParityError(true);
                serial.setOutputBufferSize(2048);
                System.out.println("configured");
            }
            catch (UnsupportedCommOperationException ucoe) {
                System.out.println(ucoe);
            }

            try {
                serial.addEventListener(this);
            }
            catch (TooManyListenersException e) {
                serial.close();
                System.out.println(e);
            }
            try {
                instream = serial.getInputStream();
                outstream = serial.getOutputStream();
                Thread.currentThread().sleep(3000);
                clearBuffer();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void clearBuffer(){
        try{
            while (instream.available() > 0) {
                instream.read();
            }
        }catch(IOException ioe){
            System.out.println(ioe);
        }
    }

    public void serialEvent(SerialPortEvent event) {

        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                 break;
            case SerialPortEvent.DATA_AVAILABLE:
                 int numBytes;
                 synchronized(serial){
                    try{
                      while ((numBytes = instream.available()) > 0) {
                          numBytes = instream.read(buffer, offset, numBytes);
                          offset += numBytes;

                          if ((buffer[offset - 1] == 10) &
                              ((buffer[offset - 2]) == 13)) {
                              String sbuf = new String(buffer, 0, offset );
                              offset  = 0;

                              //processData(sbuf);
							  if (handler != null) {
								handler.loaded(sbuf);
								this.handler = tmpHandler;
							  }
                              //clearBuffer();
                              serial.notify();
                          }
                       }
                      }catch (Exception e) {
                         System.out.println(e);                        
                      }
                  }
                 break;
        }

    }

    public void executeAT(String cmd,int timeout){
        command = cmd;

            try {
                synchronized (serial) {
                    //log.debug("exec");
                    outstream.write(cmd.getBytes());
                    outstream.flush();
                    try {
                        serial.wait(10000);

                    }catch (Exception ie) {
                        System.out.println(ie);
                        
                        istimeout = true;
                    }
                }
            }
            catch (Exception ioe) {
                System.out.println(ioe);
            }

    }

	public void executeAT(String cmd, int timeout, TerminalHandler h) {
		tmpHandler = getTerminalHandler();
		this.handler = h;
		executeAT(cmd, timeout);
	}


	public void processData(String data){
		System.out.println(data);
		
	}
    
}