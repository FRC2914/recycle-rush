package net.frc2914.services;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.image.BinaryImage;

public class VisionService extends Service {
	private static final int HUE_UPPER = 35;//35 to 50
	private static final int HUE_LOWER = 50;
	
	int session;
	Image frame;
	Image binaryFrame;
	
	@Override
	public void update() {
		NIVision.IMAQdxGrab(session, frame, 1);
		NIVision.Range hue = new NIVision.Range(HUE_UPPER,HUE_LOWER);
		NIVision.Range saturation = new NIVision.Range(150,255);
		NIVision.Range value = new NIVision.Range(150,255);
		
		NIVision.imaqColorThreshold(binaryFrame, frame, 255,ColorMode.HSV, hue, saturation, value);
		/*
		 * TODO:
		 * contour detection.  select biggest contour.  ignore if too small.
		 * find x-coord of both sides of yellow contour.  draw those lines.
		 * calculate approximately how far we are and the angle 
		 * 
		 * set exposure to manual
		 * Calibrate
		 * Optimize
		 */
		CameraServer.getInstance().setImage(binaryFrame);
	}

	@Override
	public void init() {
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 100);
		session = NIVision.IMAQdxOpenCamera("cam0",
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
	}

}
