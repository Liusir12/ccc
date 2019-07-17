package test;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Test1 {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket soc = new Socket("192.168.4.3", 9527);
		System.out.println("已经连接服务器");
		new getinpser(new BufferedInputStream(soc.getInputStream())).start();
		new setoutser(new BufferedOutputStream(soc.getOutputStream())).start();

	}

	// 给服务器发消息
	public static class setoutser extends Thread {
		private BufferedOutputStream bufo;

		public setoutser(BufferedOutputStream bufo) {
			this.bufo = bufo;
		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("给服务器发信：");
					String msg = sc.next();
					if (msg != null) {
						bufo.write(msg.getBytes());
						bufo.flush();
					}
				} catch (IOException e) {
					break;
				}
			}
		}
	}

	// 接收服务器的消息
	public static class getinpser extends Thread {
		private BufferedInputStream dInput;

		public getinpser(BufferedInputStream _dInput) {
			dInput = _dInput;
		}

		@Override
		public void run() {
			while (true) {
				try {
					byte by[] = new byte[1024];
					dInput.read(by);
					String msg = new String(by);
					if (msg != null && msg != "") {
						System.out.println("服务器来信：" + msg);
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
}
