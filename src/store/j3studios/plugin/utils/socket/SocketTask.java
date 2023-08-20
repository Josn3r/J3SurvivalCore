package store.j3studios.plugin.utils.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import store.j3studios.plugin.SCore;

public class SocketTask implements Runnable {
    
    private Socket socket;
    private Scanner scanner;
    private PrintWriter out;
	
    public SocketTask (Socket socket) {
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
        }
        System.out.println(socket.toString());
    }
	
    @Override
    public void run() {
        while(SocketServer.compute && socket.isConnected()) {
            if (scanner.hasNext()) {
                String message = scanner.next();
                if (message.isEmpty()) continue;
                
                final JsonObject json;
                try {
                    JsonElement jse = new JsonParser().parse(message);
                    if (jse.isJsonNull() || !jse.isJsonObject()){
                        SCore.debug("Received bad data from: " + socket.getInetAddress().toString());
                        continue;
                    }
                    json = jse.getAsJsonObject();
                } catch (JsonSyntaxException e) {
                	SCore.debug("Received bad data from: " + socket.getInetAddress().toString());
                        continue;
                }
                
                if (!json.has("type")) continue;

                switch (json.get("type").getAsString()) {
                    case "UPDATE" -> {
                        if (!json.has("server_name")) break;
                        if (!json.has("max_players")) break;
                        if (!json.has("current_players")) break;
                    }
                }
            } else {
                try {
                    socket.close();
                    SCore.debug("Socket closed: " + socket.toString());
                } catch (IOException e) {
                }
                Thread.currentThread().interrupt();
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    };

    public PrintWriter getOut() {
        return out;
    }
	
}
