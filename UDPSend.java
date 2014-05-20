import java.net.*;
import java.util.Scanner;
class UDPSend {

	private static int server_port;
	private static InetAddress local;

	public static void main(String[] args){		
		Scanner reader = new Scanner(System.in);
		String[] cmd = {"",""};
		try {
		local = InetAddress.getByName((args.length<1)?"0.0.0.0":args[0]);
		} catch (UnknownHostException e){}
		server_port = (args.length<2)?9875:Integer.valueOf(args[1]);
		System.out.println("Hello. Current conenction is "+local.toString()+" on port "+server_port);
		while (!cmd[0].equals("quit")){
			System.out.print("> ");
			cmd[0] = reader.next();
			cmd[1] = (cmd[0].equals("quit"))?"":reader.nextLine();
			if (cmd[0].equals("send"))
				send(cmd[1]);
			else if (cmd[0].equals("setport")){
				server_port = Integer.valueOf(cmd[1]);
				System.out.println("Current port is now "+server_port);
			}
			else if (cmd[0].equals("setaddr")){
				try {
					local = InetAddress.getByName(cmd[1]);
				} catch (UnknownHostException e){}
				System.out.println("Current connection is now "+local);
			}
				
		}

	}

	private static void send(String msg){
		try {
            DatagramSocket s = new DatagramSocket();
            s.send(new DatagramPacket(msg.getBytes(), msg.length(), local,server_port));
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}