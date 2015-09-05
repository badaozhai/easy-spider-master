package com.bqs.easy.httpclient.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Charsets;

/**
 * 封装的httpclient
 * 
 * @author xym
 * @date 2015年5月21日
 */
public class EasyHttpClient {

	private static Logger log = Logger.getLogger(EasyHttpClient.class);

	private CloseableHttpClient httpclient;

	public final static String Samsung_S4_User_Agent = "Mozilla/5.0 (Linux; Android 4.2.2; GT-I9505 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36";
	/**
	 * 谷歌浏览器请求头
	 */
	public final static String Chorme_User_Agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";

	/**
	 * 判断charset是否合理
	 */
	private static final Pattern patternForCharset = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)");

	/**
	 * 网页的content_type
	 */
	private String content_type;

	/**
	 * 网页的编码
	 */
	private String charset;

	/**
	 * 网页返回状态
	 */
	private int statusCode = 404;

	/**
	 * 网页最后更新时间
	 */
	private long lastmodify = 0;

	private BasicCookieStore cookieStore = new BasicCookieStore();

	private PoolingHttpClientConnectionManager connectionManager;

	/**
	 * 代理IP
	 */
	private String host;
	/**
	 * 代理端口
	 */
	private int port;

	public void setProxy(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public EasyHttpClient() {

		CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
			public CookieSpec create(HttpContext context) {

				return new BrowserCompatSpec() {
					public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
					}
				};
			}
		};

		SSLContext sslContext = SSLContexts.createDefault(); // 忽略证书主机名验证
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
		connectionManager = new PoolingHttpClientConnectionManager(reg);
		connectionManager.setDefaultMaxPerRoute(100);// 同一个路由允许最大连接数
		// connectionManager.setMaxTotal(poolSize);

		Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
				.register("easy", easySpecProvider).build();

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("easy").setSocketTimeout(10000)
				.setConnectTimeout(10000).build();
		ConnectionConfig connectioncfg = ConnectionConfig.custom().setCharset(Charsets.UTF_8).build();
		SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();

		HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3) {
					return false;
				}
				if (exception instanceof SSLHandshakeException) {
					return false;
				} else {
					return true;
				}
			}
		};

		HttpClientBuilder builder = HttpClients.custom()// 默认
				.setConnectionManager(connectionManager)// 链接管理器
				.setDefaultSocketConfig(socketConfig)// socket管理器
				.setRetryHandler(myRetryHandler)// 重试3次
				.setDefaultConnectionConfig(connectioncfg)// 链接配置，如默认字符编码
				.setDefaultCookieSpecRegistry(r)// cookie策略
				.setUserAgent(Chorme_User_Agent)// 浏览器请求头
				.setDefaultRequestConfig(requestConfig)// 链接配置，超时等
				.setDefaultCookieStore(cookieStore);// cookie

		builder.addInterceptorFirst(new HttpRequestInterceptor() {

			public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});

		httpclient = builder.build();

	}

	/**
	 * post提交的方法 如果想提交一段字符串<br>
	 * 那么需要将header中的content-type设置成非application/x-www-form-urlencoded;<br>
	 * 将字符串放到postdata中参数名postdata
	 * 
	 * @param posturl
	 *            提交的URL
	 * @param header
	 *            请求头
	 * @param postdata
	 *            请求参数
	 * @return
	 * @throws Exception
	 */
	public String Post(String posturl, Map<String, String> header, Map<String, String> postdata) {
		return Post(posturl, header, postdata, "");
	}

	/**
	 * post提交的方法 如果想提交一段字符串<br>
	 * 那么需要将header中的content-type设置成非application/x-www-form-urlencoded;<br>
	 * 将字符串放到postdata中参数名postdata
	 * 
	 * @param posturl
	 *            提交的URL
	 * @param header
	 *            请求头
	 * @param postdata
	 *            请求参数
	 * @param charset
	 *            字符集编码
	 * @return
	 * @throws Exception
	 */
	public String Post(String posturl, Map<String, String> header, Map<String, String> postdata, String charset) {
		String text = "";
		try {
			log.info("begin post url :" + posturl + " .");
			HttpUriRequest request = getHttpUriRequest(posturl, "post", postdata, header);
			text = execute_text(charset, header, request);
			log.info("end post url :" + posturl + " .");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return text;
	}

	/**
	 * 通过httpget请求网页内容
	 * 
	 * @param url
	 *            请求的URL
	 * @param header
	 *            请求头
	 * @param charset
	 *            字符集编码
	 * @return
	 * @throws IOException
	 */
	public String Get(String url, Map<String, String> header, String charset) {
		String text = "";
		try {
			log.info("begin get url :" + url + " .");
			HttpUriRequest request = getHttpUriRequest(url, "get", null, header);
			text = execute_text(charset, header, request);
			log.info("end get url :" + url + " .");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return text;
	}

	/**
	 * 构造请求的方法，如post，get，header等<br>
	 * 设置请求参数，如超时时间
	 * 
	 * @param url
	 *            请求的URL
	 * @param method
	 *            请求的方法
	 * @param postdata
	 *            post的数据
	 * @param headers
	 *            请求头
	 * @return
	 */
	protected HttpUriRequest getHttpUriRequest(String url, String method, Map<String, String> postdata,
			Map<String, String> headers) {
		RequestBuilder requestBuilder = selectRequestMethod(method, postdata, headers).setUri(url);

		requestBuilder.addHeader("Accept", "*/*");
		requestBuilder.addHeader("Connection", "keep-alive");
		if (headers != null) {
			for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
				requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
			}
		}

		int timeout = 45000;// 超时时间
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).setConnectTimeout(timeout).setCookieSpec(CookieSpecs.BEST_MATCH);

		if (null != host && !"".equals(host) && port > 10) {
			HttpHost proxy = new HttpHost(host, port);
			requestConfigBuilder.setProxy(proxy);
		}

		requestBuilder.setConfig(requestConfigBuilder.build());
		return requestBuilder.build();
	}

	/**
	 * 设置请求参数<br>
	 * 如果想提交一段字符串<br>
	 * 那么需要将header中的content-type设置成非application/x-www-form-urlencoded;<br>
	 * 将字符串放到postdata中参数名postdata
	 * 
	 * @param method
	 * @param postdata
	 * @param headers
	 * @return
	 */
	protected RequestBuilder selectRequestMethod(String method, Map<String, String> postdata,
			Map<String, String> headers) {
		if (method == null || method.equalsIgnoreCase("get")) {
			return RequestBuilder.get();
		} else if (method.equalsIgnoreCase("post")) {
			RequestBuilder requestBuilder = RequestBuilder.post();
			if (postdata != null && postdata.size() > 0) {

				String contenttype = "application/x-www-form-urlencoded; charset=UTF-8";
				if (headers != null && headers.size() > 0) {
					for (String ksy : headers.keySet()) {
						if ("Content-Type".equalsIgnoreCase(ksy)) {
							contenttype = headers.get(ksy).trim();
							break;
						}
					}
				}

				if ("".equals(contenttype) || contenttype.toLowerCase().contains("x-www-form-urlencoded")) {
					List<NameValuePair> formParams = new ArrayList<NameValuePair>();
					for (String str : postdata.keySet()) {
						NameValuePair n = new BasicNameValuePair(str, postdata.get(str));
						formParams.add(n);
					}

					HttpEntity entity = null;
					try {
						entity = new UrlEncodedFormEntity(formParams, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						log.error(e.getMessage(), e);
					}
					requestBuilder.setEntity(entity);
				} else {

					log.info("post Content-Type : [ " + contenttype + " ] , pay attention to it .");
					String charset = postdata.get("charset");// 提交数据的传输编码
					String pstdata = postdata.get("postdata");// 提交的数据
					if ("".equals(pstdata)) {
						pstdata = postdata.get("");// 提交的数据
					}
					if (charset == null) {
						charset = "utf-8";
					}
					StringEntity entity = new StringEntity(pstdata, charset);// 解决中文乱码问题
					entity.setContentEncoding(charset);
					entity.setContentType(contenttype);
					entity.setChunked(true);
					requestBuilder.setEntity(entity);
				}
			} else {
				log.warn("The Method Is Post,But No Post Data .");
			}
			return requestBuilder;
		} else if (method.equalsIgnoreCase("head")) {
			return RequestBuilder.head();
		} else if (method.equalsIgnoreCase("put")) {
			return RequestBuilder.put();
		} else if (method.equalsIgnoreCase("delete")) {
			return RequestBuilder.delete();
		} else if (method.equalsIgnoreCase("trace")) {
			return RequestBuilder.trace();
		}
		throw new IllegalArgumentException("Illegal HTTP Method " + method);
	}

	/**
	 * 执行请求，返回文字
	 * 
	 * @param charset
	 * @param httpUriRequest
	 * @return
	 */
	public String execute_text(String charset, Map<String, String> header, HttpUriRequest httpUriRequest) {
		String text = "";
		try {
			CloseableHttpResponse httpResponse = httpclient.execute(httpUriRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Header heade = httpResponse.getEntity().getContentType();
			if (heade != null) {
				setContent_type(heade.getValue());
				log.info("statusCode : " + statusCode + " ContentType : " + heade.getValue());
			} else {
				log.info("statusCode : " + statusCode + " ContentType : unknown .");
			}
			setStatusCode(statusCode);
			if (statusCode == 200) {
				text = getContent(charset, httpResponse);
			} else if (statusCode == 302 || statusCode == 300 || statusCode == 301) {
				URL referer = httpUriRequest.getURI().toURL();
				httpUriRequest.abort();
				Header location = httpResponse.getFirstHeader("Location");
				String locationurl = location.getValue();
				if (!locationurl.startsWith("http")) {
					URL u = new URL(referer, locationurl);
					locationurl = u.toExternalForm();
				}
				text = Get(locationurl, header, charset);
			} else {
				text = getContent(charset, httpResponse);
			}
		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (httpUriRequest != null) {
				httpUriRequest.abort();
			}
		}
		return text;
	}

	/**
	 * 执行请求，返回字节
	 * 
	 * @param charset
	 * @param httpUriRequest
	 * @return
	 */
	public byte[] execute_byte(HttpUriRequest httpUriRequest, Map<String, String> header) {
		byte[] data = null;
		HttpEntity entity = null;
		try {
			CloseableHttpResponse httpResponse = httpclient.execute(httpUriRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			entity = httpResponse.getEntity();
			Header heade = entity.getContentType();
			if (heade != null) {
				log.info("statusCode : " + statusCode + " ContentType : " + heade.getValue());
				setContent_type(heade.getValue());
			} else {
				log.info("statusCode : " + statusCode + " ContentType : unknown .");
			}
			setStatusCode(statusCode);
			if (statusCode == 200) {
				data = EntityUtils.toByteArray(entity);
			} else if (statusCode == 302 || statusCode == 300 || statusCode == 301) {
				URL referer = httpUriRequest.getURI().toURL();
				httpUriRequest.abort();
				Header location = httpResponse.getFirstHeader("Location");
				String locationurl = location.getValue();
				if (!locationurl.startsWith("http")) {
					URL u = new URL(referer, locationurl);
					locationurl = u.toExternalForm();
				}
				data = GetImg(locationurl, header);
			} else {
				data = EntityUtils.toByteArray(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpUriRequest != null) {
				httpUriRequest.abort();
			}
			if (entity != null) {
				EntityUtils.consumeQuietly(entity);
			}
		}
		return data;
	}

	/**
	 * 请求转换成汉字
	 * 
	 * @param charset
	 * @param httpResponse
	 * @return
	 * @throws IOException
	 */
	protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
		if ("".equals(charset) || charset == null) {// 没有传递编码
			HttpEntity entity = httpResponse.getEntity();
			byte[] byteContent = null;
			if (entity.isChunked()) {
				BufferedInputStream remoteBIS = new BufferedInputStream(entity.getContent());
				ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
				byte[] buf = new byte[1024];
				int bytesRead = 0;
				while (bytesRead >= 0) {
					baos.write(buf, 0, bytesRead);
					try {
						bytesRead = remoteBIS.read(buf);
					} catch (IOException ex) {
						remoteBIS.close();
						log.warn("chunked");
						break;
					}
				}
				byteContent = baos.toByteArray();
				baos.close();
			} else {
				byteContent = EntityUtils.toByteArray(entity);
			}
			Header header = httpResponse.getEntity().getContentType();
			// charset
			// 1、encoding in http header Content-Type
			String ContentType = "";
			if (header != null) {
				ContentType = header.getValue();
			}
			String htmlCharset = getHtmlCharset(ContentType, byteContent);
			log.info("charset is " + htmlCharset);
			if (htmlCharset != null) {
				setCharset(charset);
				return new String(byteContent, htmlCharset);
			} else {
				log.warn("Charset autodetect failed, use utf-8 as charset.");
				return new String(byteContent, "utf-8");
			}
		} else {// 如果已经传递编码，那么使用传递的
			setCharset(charset);
			return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
		}
	}

	/**
	 * 根据contenttype,判断网页字符集
	 * 
	 * @param ContentType
	 * @param contentBytes
	 * @return
	 * @throws IOException
	 */
	public static String getHtmlCharset(String ContentType, byte[] contentBytes) throws IOException {
		String charset;

		charset = getCharset(ContentType);
		if (StringUtils.isNotBlank(charset)) {
			log.debug("Auto get charset: {}" + charset);
			return charset;
		}
		// use default charset to decode first time
		Charset defaultCharset = Charset.defaultCharset();
		String content = new String(contentBytes, defaultCharset.name());
		// 2、charset in meta
		if (StringUtils.isNotEmpty(content)) {
			Document document = Jsoup.parse(content);
			Elements links = document.select("meta");
			for (Element link : links) {
				// 2.1、html4.01 <meta http-equiv="Content-Type"
				// content="text/html; charset=UTF-8" />
				String metaContent = link.attr("content");
				String metaCharset = link.attr("charset");
				if (metaContent.indexOf("charset") != -1) {
					metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
					charset = metaContent.split("=")[1];
					break;
				}
				// 2.2、html5 <meta charset="UTF-8" />
				else if (StringUtils.isNotEmpty(metaCharset)) {
					charset = metaCharset;
					break;
				}
			}
		}
		log.debug("Auto get charset: {}" + charset);
		// 3、todo use tools as cpdetector for content decode
		return charset;
	}

	public static String getCharset(String contentType) {
		Matcher matcher = patternForCharset.matcher(contentType);
		if (matcher.find()) {
			String charset = matcher.group(1);
			if (Charset.isSupported(charset)) {
				return charset;
			}
		}
		return null;
	}

	/**
	 * 通过httpget请求验证码
	 * 
	 * @param url
	 *            请求的URL
	 * @param header
	 *            请求头
	 * @return
	 * @throws IOException
	 */
	public byte[] GetImg(String url, Map<String, String> header) {
		try {
			log.info("begin get url :" + url + " .");
			HttpUriRequest request = getHttpUriRequest(url, "get", null, header);
			byte[] data = execute_byte(request, header);
			log.info("end get url :" + url + " .");
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			return new byte[0];
		}
	}

	/**
	 * 通过httpget请求验证码
	 * 
	 * @param url
	 *            请求的URL
	 * @return
	 * @throws IOException
	 */
	public byte[] GetImg(String url) {
		return GetImg(url, null);
	}



	/**
	 * 通过get请求页面内容
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String Get(String url) {
		return Get(url, null, "");
	}

	/**
	 * 获得cookiestore
	 * 
	 * @return
	 */
	public CookieStore getCookieStore() {
		return cookieStore;
	}

	/**
	 * 通过正则获得页面指定内容
	 * 
	 * @param str
	 * @param reg
	 * @return
	 */
	public static String GetText(String str, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		String text = "";
		if (m.find()) {
			if (m.groupCount() > 0) {
				text = m.group(1);
			} else {
				text = m.group();
			}
		}
		return text.trim();
	}

	/**
	 * 使用结束要关闭httpclient
	 */
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * @return 网页的content_type
	 */
	public String getContent_type() {
		return content_type;
	}

	/**
	 * @param 网页的content_type
	 */
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	/**
	 * @return 网页的字符集
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param 网页的字符集
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return 网页最后更新时间
	 */
	public long getLastmodify() {
		return lastmodify;
	}

	/**
	 * @param 网页最后更新时间
	 */
	public void setLastmodify(long lastmodify) {
		this.lastmodify = lastmodify;
	}

	/**
	 * @return 网页返回状态
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param 网页返回状态
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public CloseableHttpClient getHttpClient() {
		return httpclient;
	}

}
