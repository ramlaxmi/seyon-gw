package io.seyon.apigateway.service;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import io.seyon.apigateway.common.SeyonGwProperties;

@Service
public class RequestService {

	@Autowired
	SeyonGwProperties props;

	public HttpComponentsClientHttpRequestFactory getRequestFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
			{

		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		// Skip SSL validation based on condition
		if (props.isSslValidationFlag()) {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

			httpClientBuilder = httpClientBuilder.setSSLSocketFactory(csf);
		}

		// Set proxy based on condition
		if (props.isUseProxyFlag()) {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(props.getProxyUser(), props.getProxyPassword()));
			httpClientBuilder = httpClientBuilder.setProxy(new HttpHost(props.getProxyHost(), props.getProxyPort()));
			httpClientBuilder = httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		}

		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return requestFactory;
	}
}
