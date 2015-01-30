package net.frc2914.services;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;

public class VisionService extends Service {
	int session;
	Image frame;
	
	@Override
	public void update() {
		NIVision.Rect rect = new NIVision.Rect(10,10,100,100);
		NIVision.IMAQdxGrab(session, frame, 1);
		NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
		CameraServer.getInstance().setImage(frame);
	}

	@Override
	public void init() {
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera("cam0",
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
		NIVision.IMAQdxStartAcquisition(session);
	}

}
