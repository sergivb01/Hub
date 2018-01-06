package us.veilhcf.hub.utils;

import java.util.*;

public class ServerStatusAPI
{
    public HashMap<String, String> getStatus(final String ip, final int port, final int timeout) {
        final HashMap<String, String> toReturn = new HashMap<>();
        boolean serverOnline = false;
        String consoleErrorMessage = null;
        String motd = "";
        String onlinePlayers = "0";
        String maxPlayers = "0";
        try {
            final String[] result = ServerPinger.pingServer(ip, port, timeout);
            if (result == null) {
                serverOnline = false;
                consoleErrorMessage = "Server not reachable within set timeout";
            }
            else {
                motd = result[0];
                onlinePlayers = result[1];
                maxPlayers = result[2];
                serverOnline = true;
            }
        }
        catch (ServerPinger.PingException e) {
            //Ignore
        }
        toReturn.put("online", serverOnline ? "true" : "false");
        toReturn.put("motd", motd);
        toReturn.put("onlineplayers", onlinePlayers);
        toReturn.put("maxplayers", maxPlayers);
        return toReturn.isEmpty() ? null : toReturn;
    }
}
