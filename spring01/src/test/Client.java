package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 客户端
 * 
 * @author hdm
 *
 */
public class Client {
	static Scanner sc = new Scanner(System.in);
	private static final String SERVER_IP="192.168.4.4";
	private static final int port=9527;
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket soc = new Socket("192.168.4.4", port);
		System.out.println("已连接ooooo");
		new GetData(new DataInputStream(soc.getInputStream())).start();
		new SendData(new DataOutputStream(soc.getOutputStream())).start();
	}
	/**
	 * 接收消息
	 * @author hdm
	 *
	 */
	public static class GetData extends Thread {
        private DataInputStream dInput;
        public GetData(DataInputStream _dInput) {
            dInput = _dInput;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    String msg = dInput.readUTF();
                    if (msg != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    try {
                        dInput.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
	/**
	 * 发送消息
	 * @author hdm
	 *
	 */
	public static class SendData extends Thread {
        private DataOutputStream dOutput;
        public SendData(DataOutputStream _dOutput) {
            dOutput = _dOutput;
        }
        @Override
        public void run() {
            while (true) {
                try {
                	String ip="";
                	while(true) {
                		System.out.println("输入另一个客户端的ip(列如：192.168.4.1)");
                        ip=sc.next();
                        String st[]=ip.split("\\.");
                        if((st[0].equals("192")&&st[1].equals("168"))&&(st[2].equals("4"))) {
                        	int ipin = Integer.parseInt(st[3]);
                        	if(ipin>0&&ipin<255) {
                        		break;
                        	}else {
                        		System.out.println("请在（0~255之间）");
                        	}
                        }else {
                        	System.out.println("请输入同一个网段");
                        }
                	}
                    System.out.println("请输入内容：");
                    String msg =sc.next();
                    if (msg != null) {
                        dOutput.writeUTF(msg+">"+ip);
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    }	
}
