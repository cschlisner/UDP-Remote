import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.awt.Robot;
import java.awt.event.*;
import java.awt.*;
class UDPServer
{
   private static int mouseX=0, mouseY=0;
   public static void main(String args[]) throws Exception
      {
          DatagramSocket serverSocket = new DatagramSocket(9875);
          byte[] receiveData = new byte[1024];
          byte[] sendData = new byte[1024];
          while(true){      
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String message = new String(receivePacket.getData(),0,receivePacket.getLength());
            InetAddress clientIP = receivePacket.getAddress();
            System.out.println("RECEIVED: " + message);
            if (message != null){
               try {
                  Robot robot = new Robot();
                  if (message.length()<=3){
                    if (!message.equals("DEL")) write(robot, message);
                    else{
                      robot.keyPress(KeyEvent.VK_BACK_SPACE);
                      robot.keyRelease(KeyEvent.VK_BACK_SPACE); 
                    } 
                  }
                  else { 
                    if (message.equals("RESTARTCASTSERVER"))
                      restartCastServer(robot);                                                    
                    else if (message.equals("RIGHTCLICKD"))
                      robot.mousePress(InputEvent.BUTTON3_MASK);
                    else if (message.equals("RIGHTCLICKU"))
                      robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    else if (message.equals("LEFTCLICKD"))
                      robot.mousePress(InputEvent.BUTTON1_MASK);
                    else if (message.equals("LEFTCLICKU"))
                      robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    else {   
                      PointerInfo a = MouseInfo.getPointerInfo();
                      Point b = a.getLocation();
                      int x = (int) b.getX();
                      int y = (int) b.getY();
                      parseCords(String.valueOf(message.subSequence(7, message.length())));
                      robot.mouseMove(x + mouseX, y + mouseY);
                    }
                  }
               } catch(Exception e){
                  System.out.println("FUCKFUCKFUCK CAN'T HANDLE IT");
               }
            }
          }
      }

      private static void write(Robot bot, String s) {
        for (int i=0; i<s.length(); ++i){
          int keyCode = KeyEvent.getExtendedKeyCodeForChar(s.charAt(i));
          bot.keyPress(keyCode);
          bot.keyRelease(keyCode);
        }
        

        // byte[] bytes = s.getBytes();
        // for (byte b : bytes)
        // {
        //     int code = b;
        //     // keycode only handles [A-Z] (which is ASCII decimal [65-90])
        //     if (code > 96 && code < 123) code = code - 32;
        //     bot.delay(40);
        //     bot.keyPress(code);
        //     bot.keyRelease(code);
        // }
      }

      // Recieved format for mouse coordinates: MOUSE: xxxx yyyy
      private static void parseCords(String s){     
          int marker = 0;
          for (int i=0; i<s.length(); ++i){
            if (String.valueOf(s.charAt(i)).equals(" ")){
              marker = i;
            }
          }
          mouseX = Integer.parseInt(s.subSequence(0, marker).toString());
          mouseY = Integer.parseInt(s.subSequence(marker+1, s.length()).toString()); 
      }

      private static void restartCastServer(Robot robot){
          robot.keyPress(KeyEvent.VK_SHIFT);
          robot.keyPress(KeyEvent.VK_CONTROL);
          robot.keyPress(KeyEvent.VK_C);
          robot.keyRelease(KeyEvent.VK_SHIFT);
          robot.keyRelease(KeyEvent.VK_CONTROL);
          robot.keyRelease(KeyEvent.VK_C);
          try {
            Thread.sleep(1000);
          } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
          robot.keyPress(KeyEvent.VK_UP);
          robot.keyRelease(KeyEvent.VK_UP);
          robot.keyPress(KeyEvent.VK_ENTER);
          robot.keyRelease(KeyEvent.VK_ENTER);
      }
}