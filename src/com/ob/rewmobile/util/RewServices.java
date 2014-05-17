package com.ob.rewmobile.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RewServices {

	private HttpClient httpClient = null;
	HttpResponse resp = null;
	
	public RewServices() {
		httpClient = new DefaultHttpClient();
	}
	
	public JSONArray get(String url) throws ClientProtocolException, IOException, JSONException {
		JSONObject jsonReturn = null;
		HttpGet get = new HttpGet(url);
		get.setHeader("content-type", "application/json");
		resp = httpClient.execute(get);
		jsonReturn = new JSONObject(EntityUtils.toString(resp.getEntity()));
		return jsonReturn.getJSONArray("data");
	}
	
	public int post(String url, JSONObject datos) throws ClientProtocolException, IOException, JSONException, URISyntaxException {
		JSONObject jsonReturn = null;
		HttpPost post = new HttpPost(url);
		
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", datos.toString()));
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		
		resp = httpClient.execute(post);
		jsonReturn = new JSONObject(EntityUtils.toString(resp.getEntity()));
		if (jsonReturn.get("success").equals(true)) {
			return jsonReturn.getInt("id");
		} else {
			return 0;
		}
	}
	
	public boolean put(String url, JSONObject datos) throws ClientProtocolException, IOException, JSONException {
		JSONObject jsonReturn = null;
		HttpPut put = new HttpPut(url);
		//put.setHeader("content-type", "application/json");
		
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", datos.toString()));
		put.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		
		resp = httpClient.execute(put);
		jsonReturn = new JSONObject(EntityUtils.toString(resp.getEntity()));
		Log.e("jsonReturn", jsonReturn.toString());
		return (Boolean) jsonReturn.get("success");
	}
	
	public boolean delete(String url) throws ClientProtocolException, IOException, JSONException {
		JSONObject jsonReturn = null;
		HttpDelete delete = new HttpDelete(url);
		delete.setHeader("content-type", "application/json");
		resp = httpClient.execute(delete);
		jsonReturn = new JSONObject(EntityUtils.toString(resp.getEntity()));
		return (Boolean) jsonReturn.get("success");
	}

}
