package com.gob.rewmobile.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class JSONParser {

	public JSONObject getJSONFromUrl(String url) throws IOException, Exception {
		HttpClient httpClient = new DefaultHttpClient();
		// httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
		// "android");
		HttpGet httpGet = new HttpGet();
		// HttpPost
		httpGet.setHeader("Content-Type", "text/plain; charset=utf-8");
		httpGet.setURI(new URI(url));

		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream is = httpEntity.getContent();

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer("");
		String line = null;
		String NL = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			sb.append(line + NL);
		}
		is.close();
		return new JSONObject(sb.toString());
	}
}
