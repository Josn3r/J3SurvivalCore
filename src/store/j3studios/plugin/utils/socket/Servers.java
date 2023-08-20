package store.j3studios.plugin.utils.socket;

import java.util.HashMap;

public class Servers {
    
    private final HashMap<String, SocketTask> socketByServer = new HashMap<>();
    private static Servers ins;
    
    private Servers() {
        ins = this;
    }
    
    public static Servers get(){
        return ins == null ? new Servers() : ins;
    }
    
    public void registerServerSocket (String client, SocketTask task) {
        if (socketByServer.containsKey(client)) {
            socketByServer.replace(client, task);
            return;
        }
        socketByServer.put(client, task);
    }
    
    public static SocketTask getSocketByServer (String client) {
        return get().socketByServer.getOrDefault(client, null);
    }
    
    public HashMap<String, SocketTask> getSocketByServer() {
        return socketByServer;
    }
    
}
