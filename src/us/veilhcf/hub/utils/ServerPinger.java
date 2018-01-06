package us.veilhcf.hub.utils;

import java.net.*;
import java.io.*;

public class ServerPinger
{
    public static String[] pingServer(final String ip, final int port, final int timeout) throws PingException {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(timeout);
            final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            final DataInputStream in = new DataInputStream(socket.getInputStream());
            out.write(254);
            final StringBuilder str = new StringBuilder();
            int b;
            while ((b = in.read()) != -1) {
                if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                    str.append((char)b);
                }
            }
            final String[] data = str.toString().split("§");
            return data;
        }
        catch (IOException e) {
            throw new PingException(e.getMessage());
        }
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            }
            catch (IOException e2) {
                throw new PingException(e2.getMessage());
            }
        }
    }

    public static class PingException extends Exception
    {
        private static final long serialVersionUID = 5694501675795361821L;

        private PingException(final String message) {
            super(message);
        }
    }
}
