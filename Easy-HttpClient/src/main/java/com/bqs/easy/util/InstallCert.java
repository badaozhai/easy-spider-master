package com.bqs.easy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

/**
 * 根据网站下载证书<br>
 * 这个必须放到声明httpclient之前,否则可能报错
 * 
 * @author xym
 * @date 2015-08-03
 *
 */
public class InstallCert {

	private static Logger log = Logger.getLogger(InstallCert.class);

	private static char SEP = File.separatorChar;

	/**
	 * 根据网站下载证书<br>
	 * 这个必须放到声明httpclient之前,否则可能报错
	 * 
	 * @param wangzhi 网址
	 */
	public InstallCert(String wangzhi) {

		URL u = null;
		String host = null;
		try {
			if (wangzhi == null || !wangzhi.startsWith("https")) {
				return;
			}
			u = new URL(wangzhi);
			host = u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == host || "".equals(host) || "null".equals(host)) {
			return;
		}

		String path = System.getProperty("user.dir");

		String outpath = path + File.separator + "cer" + File.separator + host + ".cer";
		File cerfile = new File(outpath);
		System.setProperty("jsse.enableSNIExtension", "false");
		if (!cerfile.exists()) {// 如果证书不存在，那么下载证书
			log.info("cer doesn't exists .");
			if (!cerfile.getParentFile().exists()) {
				cerfile.getParentFile().mkdirs();
			}
			downCert(host, outpath);
		} else {// 如果证书最后修改时间在4天前，下载证书
			long lastmodify = cerfile.lastModified();
			long current = System.currentTimeMillis();
			if ((lastmodify - current) / 86400000 > 4) {
				log.info("cer lasmodify is 4 days ago .");
				downCert(host, outpath);
			}
		}
		if (cerfile.exists()) {
			System.setProperty("javax.net.ssl.trustStore", outpath);
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		}

	}

	/**
	 * 下载证书
	 * 
	 * @param host
	 * @param outpath
	 */
	public void downCert(String host, String outpath) {
		int port = 443;
		char[] passphrase = "changeit".toCharArray();

		try {

			File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
			File file = new File(dir, "cacerts");
			InputStream in = new FileInputStream(file);
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(in, passphrase);
			in.close();

			SSLContext context = SSLContext.getInstance("TLS");
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ks);
			X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
			SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
			context.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory factory = context.getSocketFactory();

			log.info("Opening connection to " + host + ":" + port + "...");
			SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
			socket.setSoTimeout(10000);

			try {
				log.info("Starting SSL handshake...");
				socket.startHandshake();
				socket.close();
				log.info("No errors, certificate is already trusted");
			} catch (SSLException e) {
				log.warn(e.getMessage());
			}

			X509Certificate[] chain = tm.chain;
			if (chain == null) {
				log.info("Could not obtain server certificate chain");
				return;
			}

			log.info("Server sent " + chain.length + " certificate(s):");
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			for (int i = 0; i < chain.length; i++) {
				X509Certificate cert = chain[i];
				log.info(" " + (i + 1) + " Subject " + cert.getSubjectDN());
				log.info("   Issuer  " + cert.getIssuerDN());
				sha1.update(cert.getEncoded());
				log.info("   sha1    " + toHexString(sha1.digest()));
				md5.update(cert.getEncoded());
				log.info("   md5     " + toHexString(md5.digest()));

			}

			log.info("add cer to trusted keystore");
			int k = 0;

			X509Certificate cert = chain[k];
			String alias = host + "-" + (k + 1);
			ks.setCertificateEntry(alias, cert);

			OutputStream out = new FileOutputStream(outpath);
			ks.store(out, passphrase);
			out.close();

			log.info("Added certificate to keystore '" + alias + "' using alias '" + alias + "'");
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (KeyStoreException e) {
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		} catch (CertificateException e) {
			log.error(e.getMessage());
		} catch (KeyManagementException e) {
			log.error(e.getMessage());
		}
	}

	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SavingTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}

	public static void main(String[] args) throws Exception {
		new InstallCert("https://m.my089.com");
	}

}