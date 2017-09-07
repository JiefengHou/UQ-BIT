/**
 * 
 * @author Mingyang Zhong
 * Feb. 2014
 * the University of Queensland
 * Code example for course: COMS3200
 * 
 This is simple network program based on Java-NIO, TCP non-blocking mode and single thread. 
 The TCPClient reads inputs from the keyboard then sends it to TCPServer.
 The TCPServer reads packets from the socket channel and convert it to upper case, and then sends back to TCPClient. 
 The program assumes that the data in any received packets will be in string form.
 Typing 'exit' will close the program.
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class TCPClientNIO {

	public TCPClientNIO() {
		SocketChannel channel = null;
		try {
			// set port from connection
			int port = 9000;
			// open socket channel
			channel = SocketChannel.open();
			// set Blocking mode to non-blocking
			channel.configureBlocking(false);
			// set Server info
			InetSocketAddress target = new InetSocketAddress("127.0.0.1", port);
			// open selector
			Selector selector = Selector.open();
			// connect to Server
			channel.connect(target);
			// registers this channel with the given selector, returning a selection key
			channel.register(selector, SelectionKey.OP_CONNECT);

			while (selector.select() > 0) {
				for (SelectionKey key : selector.selectedKeys()) {
					// test connectivity
					if (key.isConnectable()) {
						SocketChannel sc = (SocketChannel) key.channel();
//						sc.configureBlocking(true);
						// set register status to WRITE
						sc.register(selector, SelectionKey.OP_WRITE);
						sc.finishConnect();
					}
					// test whether this key's channel is ready for reading from Server
					else if (key.isReadable()) {
						// allocate a byte buffer with size 1024
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						SocketChannel sc = (SocketChannel) key.channel();
						int readBytes = 0;
						// try to read bytes from the channel into the buffer
						try {
							int ret = 0;
							try {
								while ((ret = sc.read(buffer)) > 0)
									readBytes += ret;
							} finally {
								buffer.flip();
							}
							// finished reading, print to Client
							if (readBytes > 0) {
								System.out.println(Charset.forName("UTF-8").decode(buffer).toString());
								buffer = null;
							}
						} finally {
							if (buffer != null)
								buffer.clear();
						}
						// set register status to WRITE
						sc.register(selector, SelectionKey.OP_WRITE);
					}
					// test whether this key's channel is ready for writing to Server
					else if (key.isWritable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						// Client input
						BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("Please input your message:");
						// read Client input
						String command = "";
						// input is not empty
						while ((command = systemIn.readLine()).trim().isEmpty()) {
						}
						// send to Server
						channel.write(Charset.forName("UTF-8").encode(command));
						// set register status to READ
						sc.register(selector, SelectionKey.OP_READ);
						// when Client exit
						if ("exit".equalsIgnoreCase(command.trim())) {
							// close everything
							systemIn.close();
							selector.close();
							System.out.println("Client exit !");
							break;
						}
					}
				}
				if (selector.isOpen()) {
					selector.selectedKeys().clear();
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new TCPClientNIO();
	}

}