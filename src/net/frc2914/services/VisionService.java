package net.frc2914.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class VisionService extends Service {
	private DatagramSocket visionSocket;
	private boolean recieving;
	private int bufferSize = 8;
	private int objectOffset;
	private int objectHeight;
	private boolean isTrackingObject;
	@Override
	public void update() {		
		if(!recieving){
			byte[] buffer = new byte[bufferSize];
			DatagramPacket recievedPacket = new DatagramPacket(buffer, buffer.length);
			recieving = true;
			try {
				visionSocket.receive(recievedPacket);
				String recievedData = new String(recievedPacket.getData(),0, recievedPacket.getLength());
				recievedData = recievedData.substring(0, recievedData.lastIndexOf(";")-1);
				if(recievedData.length() > 0){
					String[] data = recievedData.split(",");
					objectOffset = Integer.parseInt(data[0]);
					objectHeight = Integer.parseInt(data[1]);
					isTrackingObject = true;
				}else{
					isTrackingObject = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				recieving = false;
			}
		}
	}

	@Override
	public void init() {
		try {
			visionSocket = new DatagramSocket(100);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
	
	public int getObjectHeight(){
		return objectHeight;
	}
	public int getObjectOffset(){
		return objectOffset;
	}
	public boolean isTrackingObject(){
		return isTrackingObject;
	}

}
