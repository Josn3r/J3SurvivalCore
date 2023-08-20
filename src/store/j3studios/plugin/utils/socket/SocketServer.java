package store.j3studios.plugin.utils.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import store.j3studios.plugin.SCore;

public class SocketServer {
    
    public static Thread acceptThread;
    private int task;
    
    public static boolean compute = true;
    private final ServerSocket serverSocket;
    
    public SocketServer (ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void start() {    	
    	compute = true;
        task = Bukkit.getScheduler().runTaskAsynchronously(SCore.get(), ()-> {
            while (compute){
                try {
                    Socket socket = serverSocket.accept();
                    Bukkit.getScheduler().runTaskAsynchronously(SCore.get(), new SocketTask(socket));
                } catch (IOException e) {
                }
            }
        }).getTaskId();
    }
        
    public void closeServerSocket() {
    	compute = false;
        Bukkit.getScheduler().cancelTask(task);    	
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {         
        }
     }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
        
    
}