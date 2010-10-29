package org.phxandroid.examples.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class PermissiveSSLSocketFactory implements LayeredSocketFactory {
	private static final String TAG = "PermissiveSSLSocketFactory";
	private static final PermissiveSSLSocketFactory DEFAULT_FACTORY = new PermissiveSSLSocketFactory();

	private static SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] { new FullTrustManager() }, null);
			return context;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			throw new RuntimeException(e.toString());
		}
	}

	public static PermissiveSSLSocketFactory getSocketFactory() {
		return DEFAULT_FACTORY;
	}

	private SSLContext sslcontext = null;
	private final javax.net.ssl.SSLSocketFactory socketfactory;
	private X509HostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier();

	public PermissiveSSLSocketFactory() {
		super();
		this.sslcontext = getSSLContext();
		this.socketfactory = this.sslcontext.getSocketFactory();
	}

	// non-javadoc, see interface org.apache.http.conn.SocketFactory
	public Socket connectSocket(final Socket sock, final String host, final int port, final InetAddress localAddress,
			int localPort, final HttpParams params) throws IOException {

		if (host == null) {
			throw new IllegalArgumentException("Target host may not be null.");
		}
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null.");
		}

		SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

		if ((localAddress != null) || (localPort > 0)) {

			// we need to bind explicitly
			if (localPort < 0)
				localPort = 0; // indicates "any"

			InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
			sslsock.bind(isa);
		}

		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);

		sslsock.connect(remoteAddress, connTimeout);

		sslsock.setSoTimeout(soTimeout);
		try {
			hostnameVerifier.verify(host, sslsock);
			// verifyHostName() didn't blowup - good!
		} catch (IOException iox) {
			// close the socket before re-throwing the exception
			try {
				sslsock.close();
			} catch (Exception x) { /* ignore */
			}
			throw iox;
		}

		return sslsock;
	}
	// non-javadoc, see interface org.apache.http.conn.SocketFactory
	public Socket createSocket() throws IOException {

		// the cast makes sure that the factory is working as expected
		return (SSLSocket) this.socketfactory.createSocket();
	}

	@Override
	public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
			throws IOException, UnknownHostException {
		SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(socket, host, port, autoClose);
		hostnameVerifier.verify(host, sslSocket);
		return sslSocket;
	}

	public boolean equals(Object obj) {
		return ((obj != null) && obj.getClass().equals(PermissiveSSLSocketFactory.class));
	}

	public X509HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createEasySSLContext();
		}
		return this.sslcontext;
	}

	public int hashCode() {
		return PermissiveSSLSocketFactory.class.hashCode();
	}

	public boolean isSecure(Socket sock) throws IllegalArgumentException {

		if (sock == null) {
			throw new IllegalArgumentException("Socket may not be null.");
		}
		// This instanceof check is in line with createSocket() above.
		if (!(sock instanceof SSLSocket)) {
			throw new IllegalArgumentException("Socket not created by this factory.");
		}
		// This check is performed last since it calls the argument object.
		if (sock.isClosed()) {
			throw new IllegalArgumentException("Socket is closed.");
		}

		return true;
	}

	public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
		if (hostnameVerifier == null) {
			throw new IllegalArgumentException("Hostname verifier may not be null");
		}
		this.hostnameVerifier = hostnameVerifier;
	}
}
