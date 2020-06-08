// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String id)
  {
    try
    {
      client= new ChatClient(host, port,this, id);
    }
    catch(IOException exception)
    {
      System.out.println("Error: Can't setup connection!");
      accept();
    }
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {
      BufferedReader fromConsole =
        new BufferedReader(new InputStreamReader(System.in));
      String message;
      while (true)
      {
        message = fromConsole.readLine();
        if (message.length() != 0 && message.charAt(0) ==  '#'){
            handleCommands(message);
        }
        else if (client != null && client.isConnected()){
            client.handleMessageFromClientUI(message);
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  public void handleCommands(String message){
      message = message.substring(1);
      if (message.toLowerCase().equals("quit")){
          client.quit();
      }
      else if (message.toLowerCase().equals("logoff")){
          try {
              System.out.println("Connection closed");
              client.closeConnection();
          }
          catch(IOException e) {}
      }

      else if (message.toLowerCase().split(" ")[0].equals("sethost")){
          client.setHost(message.toLowerCase().split(" ")[1]);
          System.out.println("Host has been set to: " + client.getHost());
      }

      else if (message.toLowerCase().split(" ")[0].equals("setport")){
          client.setPort(Integer.parseInt(message.toLowerCase().split(" ")[1]));
          System.out.println("Port has been set to: " + client.getPort());
      }

      else if(message.toLowerCase().equals("login")){
          if (client.isConnected()) {
              System.out.println("Error: Already connected");
          }
          else{
              try{
                  client.openConnection();
              }
              catch(IOException e) {
                  System.out.println("Could not log in");
              }
          }
      }

      else if(message.toLowerCase().equals("gethost")){
          System.out.println("Host is currently set to: " + client.getHost());
      }

      else if(message.toLowerCase().equals("getport")){
          System.out.println("Port is currently set to: " + client.getPort());
      }
      else{
          System.out.println("Unknown command.");
      }

  }




  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
      if (message.length()>=12 && message.substring(0,12).equals("SERVER MSG> ")){
          System.out.println(message);
      }
      else{
          System.out.println("> " + message);
      }
  }

  protected void connectionClosed() {
      System.out.println("Connection closed");
      client.quit();
  }

  protected void connectionException(){
      System.out.println("Connection exception");
      client.quit();
  }

  protected void connectionEstablished() {
      System.out.println("Connection established");
	}



  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    String host = "";
    int port = 0;  //The port number
    String id = "";

    try{
        id = args[0];
    }
    catch(ArrayIndexOutOfBoundsException e){}

    try
    {
      port = Integer.parseInt(args[2]);
      host = (String) args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
      port = DEFAULT_PORT;
    }

    ClientConsole chat= new ClientConsole(host, port, id);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
