package com.snowballer.api.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class KakaoService {

	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String tokenUri;
	@Value("${spring.security.oauth2.client.registration.kakao.clientId}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;
	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String userInfoUri;

	public Map<String, Object> login(String code) {
		return getUserInfo(getAccessToken(code));
	}

	public String getAccessToken(String code) {

		String accessToken = "";
		String refreshToken = "";

		try {
			URL url = new URL(tokenUri);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=" + clientId);
			sb.append("&redirect_uri=" + redirectUri);
			sb.append("&code=" + code);

			bw.write(sb.toString());
			bw.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}

			JsonElement element = JsonParser.parseString(result);
			accessToken = element.getAsJsonObject().get("access_token").getAsString();
			refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	public Map<String, Object> getUserInfo(String accessToken) {
		Map<String, Object> userInfo = new HashMap<String, Object>();
		try {
			URL url = new URL(userInfoUri);

			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode =" + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body =" + result);

			JsonElement element = JsonParser.parseString(result);

			String id = element.getAsJsonObject().get("id").getAsString();
			JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();

			String nickname = properties.getAsJsonObject().get("nickname").getAsString();

			userInfo.put("id", id);
			userInfo.put("name", nickname);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userInfo;
	}

	public void logout(String accessToken) {
		String reqURL = "http://kapi.kakao.com/v1/user/logout";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			int responseCode = conn.getResponseCode();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String result = "";
			String line = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
