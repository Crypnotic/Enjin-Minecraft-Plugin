package com.enjin.common.utils;

import com.enjin.core.Enjin;
import com.enjin.rpc.EnjinRPC;

import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionUtil {
    public static boolean testHTTPconnection() {
        return testConnection(false);
    }

    public static boolean testHTTPSconnection() {
        return testConnection(true);
    }

    public static boolean testConnection(boolean https) {
        BufferedReader in = null;
        boolean ok = false;

        try {
            URL url = new URL((https ? "https" : "http") + EnjinRPC.getApiUrl());
            URLConnection con = url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = in.readLine();

            if (line != null && line.startsWith("OK")) {
                ok = true;
            }
        } catch (SSLHandshakeException e) {
            return false;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                return false;
            }
        }

        return ok;
    }

    public static boolean testWebConnection() {
        BufferedReader in = null;
        boolean ok = false;

        try {
            URL url = new URL("http://google.com");
            URLConnection con = url.openConnection();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = in.readLine();

            if (inputLine != null) {
                ok = true;
            }
        } catch (SocketTimeoutException e) {
            return false;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                return false;
            }
        }

        return ok;
    }

    public static boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}