package net.frc2914.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class VisionService extends Service {
	private DatagramSocket visionSocket;
	private boolean receiving;
	private int bufferSize = 8;
	private int objectOffset;
	private int objectHeight;
	private boolean isTrackingObject;
	private final int notTrackingTolerance = 10;
	private int notTrackingCounter = 0;
	@Override
	public void update() {
		if (!receiving) {
			byte[] buffer = new byte[bufferSize];
			DatagramPacket recievedPacket = new DatagramPacket(buffer,
					buffer.length);
			receiving = true;
			try {
				visionSocket.receive(recievedPacket);
				String receivedData = new String(recievedPacket.getData(), 0,
						recievedPacket.getLength());
				receivedData = receivedData.substring(0,
						receivedData.lastIndexOf(";"));
				System.out.println("received: " + receivedData);
				if (receivedData.length() > 0) {
					notTrackingCounter = 0;
					String[] data = receivedData.split(",");
					objectOffset = Integer.parseInt(data[0]);
					objectHeight = Integer.parseInt(data[1]);
					isTrackingObject = true;
				} else {
					if(notTrackingCounter++ > notTrackingTolerance){
						isTrackingObject = false;						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				receiving = false;
			}
		}
	}

	@Override
	public void init() {
		try {
			visionSocket = new DatagramSocket(2914);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public int getObjectHeight() {
		return objectHeight;
	}

	public int getObjectOffset() {
		return objectOffset;
	}

	public boolean isTrackingObject() {
		return isTrackingObject;
	}

}
