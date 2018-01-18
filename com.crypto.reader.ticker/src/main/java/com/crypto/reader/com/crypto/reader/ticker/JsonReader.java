package com.crypto.reader.com.crypto.reader.ticker;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static void main(String[] args) throws IOException, JSONException, AWTException {
		JSONObject json = readJsonFromUrl("https://koinex.in/api/ticker");
		double lastTradedPrice = json.getJSONObject("stats").getJSONObject("XRP").getDouble("last_traded_price");
		if (SystemTray.isSupported()) {
			displayTray(String.valueOf(lastTradedPrice));
		} else {
			System.err.println("System tray not supported!");
		}
		System.out.println(lastTradedPrice);
	}

	public static void displayTray(String lastTradedPrice) throws AWTException, java.net.MalformedURLException {
		// Obtain only one instance of the SystemTray object
		SystemTray tray = SystemTray.getSystemTray();

		// If the icon is a file
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		// Alternative (if the icon is on the classpath):
		// Image image =
		// Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));
		TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		// Let the system resizes the image if needed
		trayIcon.setImageAutoSize(true);
		// Set tooltip text for the tray icon
		trayIcon.setToolTip("System tray icon demo");
		tray.add(trayIcon);
		trayIcon.displayMessage("Current price of ripple in koinex", lastTradedPrice, MessageType.INFO);
	}
}