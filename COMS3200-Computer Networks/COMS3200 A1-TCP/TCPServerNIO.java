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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class TCPServerNIO {
	
	// set Server parameters
	private int port = 9000;
	private Selector selector = null;
	private ServerSocketChannel serverSocketChannel = null;
	private ServerSocket serverSocket = null;

	public TCPServerNIO() {
		try {
			// open selector
			selector = Selector.open();
			// open socket channel
			serverSocketChannel = ServerSocketChannel.open();
			// set the socket associated with this channel
			serverSocket = serverSocketChannel.socket();
			// set Blocking mode to non-blocking
			serverSocketChannel.configureBlocking(false);
			// bind port
			serverSocket.bind(new InetSocketAddress(port));
			// registers this channel with the given selector, returning a selection key
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("<TCPServer> Server is activated, listening on port: "+ port);

			while (selector.select() > 0) {
				for (SelectionKey key : selector.selectedKeys()) {
					// test whether this key's channel is ready to accept a new socket connection
					if (key.isAcceptable()) {
						// accept the connection
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel sc = server.accept();
						if (sc == null)
							continue;
						System.out.println("Connection accepted from: " + sc.getRemoteAddress());
						// set blocking mode of the channel
						sc.configureBlocking(false);
						// allocate buffer
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						// set register status to READ
						sc.register(selector, SelectionKey.OP_READ, buffer);
					}
					// test whether this key's channel is ready for reading from Client
					else if (key.isReadable()) {
						// get allocated buffer with size 1024
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						SocketChannel sc = (SocketChannel) key.channel();
						int readBytes = 0;
						String message = null;
						// try to read bytes from the channel into the buffer
						try {
							int ret;
							try {
								while ((ret = sc.read(buffer)) > 0)
									readBytes += ret;
							} catch (Exception e) {
								readBytes = 0;
							} finally {
								buffer.flip();
							}
							// finished reading, form message
							if (readBytes > 0) {
								message = Charset.forName("UTF-8").decode(buffer).toString();
								buffer = null;
							}
						} finally {
							if (buffer != null)
								buffer.clear();
						}
						// react by Client's message
						if (readBytes > 0) {
							System.out.println("Message from Client" + sc.getRemoteAddress() + ": " + message);
							// if exit, close socket channel
							if ("exit".equalsIgnoreCase(message.trim())) {
								System.out.println("Client " + sc.getRemoteAddress() +" finish up");
								sc.close();
							} else {
								// set register status to WRITE
								sc.register(key.selector(), SelectionKey.OP_WRITE, message.toUpperCase());
							}
						}
					}
					// test whether this key's channel is ready for sending to Client
					else if (key.isWritable()) {
						SocketChannel sc = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						buffer.put(((String) key.attachment()).getBytes());
						buffer.flip();
						sc.write(buffer);
						// set register status to READ
						sc.register(key.selector(), SelectionKey.OP_READ, buffer);
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
			if (serverSocketChannel != null) {
				try {
					serverSocketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new TCPServerNIO();
	}
}