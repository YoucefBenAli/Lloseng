import java.io.*;
import client.*;
import common.*;

public class ServerConsole implements ChatIF {

    EchoServer server;

    public ServerConsole(EchoServer server){
        this.server = server;
    }

    public void display(String message)
    {
      System.out.println("SERVER MSG> " + message);
    }

    private void handleCommands(String message){
        message = message.substring(1);
        if (message.toLowerCase().equals("quit")){
            try{
                server.close();
                System.exit(0);
            }
            catch(IOException e) {}
        }
        else if (message.toLowerCase().equals("stop")){
            server.stopListening();
            server.sendToAllClients("SERVER MSG> WARNING - Server has stopped listening for connections.");
        }

        else if (message.toLowerCase().equals("close")){
            try {
                server.close();
            }
            catch(IOException e) {}
        }

        else if (message.toLowerCase().equals("start")){
            try {
                if (server.isListening()){
                    System.out.println("Server already listening for clients");
                }
                else{
                    server.listen();
                }
            }
            catch(IOException e) {}
        }


        else if (message.toLowerCase().split(" ")[0].equals("setport")){
            server.setPort(Integer.parseInt(message.toLowerCase().split(" ")[1]));
            System.out.println("Port has been set to: " + server.getPort());
        }

        else if(message.toLowerCase().equals("getport")){
            System.out.println("Port is currently set to: " + server.getPort());
        }
        else{
            System.out.println("Unknown command.");
        }
    }

    public void input(){
        try
        {
          BufferedReader fromConsole =
            new BufferedReader(new InputStreamReader(System.in));
          String message;
          while (true)
          {
            message = fromConsole.readLine();
            if (message.length() != 0 && message.charAt(0) ==  '#'){
                this.handleCommands(message);
            }
            else{
                this.display(message);
                server.sendToAllClients("SERVER MSG> " + message);

            }
          }
        }
        catch (Exception ex)
        {
          System.out.println
            ("Unexpected error while reading from console!");
        }
    }

}
