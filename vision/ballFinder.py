import cv2
import numpy as np
import math
import socket
import sys
import os
"""
    looks for blobs.  calculates the centroid and the distance from center for the biggest. send over udp.
"""
exposure = 10
height = 240
width = 320
hue_lower = 20
hue_upper = 28
saturation_lower = 0
saturation_upper = 255
value_lower = 0
value_upper = 255

skip_gui = len(sys.argv) >= 2 and sys.argv[1] == "--nogui"
#set up camera
camera = cv2.VideoCapture(0)
if os.name is "nt":#set exposure on windows
    camera.set(cv2.cv.CV_CAP_PROP_EXPOSURE,exposure) #time in milliseconds. 5 gives dark image. 100 gives bright image.
else:#set exposure on linux 
    os.system("v4l2-ctl --set-ctrl gain_automatic=0 --device=0")
camera.set(cv2.cv.CV_CAP_PROP_FRAME_WIDTH,width)
camera.set(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT,height)
print camera.get(3),camera.get(4)

s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)

while(1):
    _,capture = camera.read()
    capture = cv2.flip(capture,1)   
#    Convert image to HSV plane using cvtColor() function
    hsvcapture = cv2.cvtColor(capture,cv2.COLOR_BGR2HSV)   
#    turn it into a binary image representing yellows
    inrangepixels = cv2.inRange(hsvcapture,np.array((hue_lower,saturation_lower,value_lower)),np.array((hue_upper,saturation_upper,value_upper)))#in opencv, HSV is 0-180,0-255,0-255

#    Apply erosion and dilation and erosion again to eliminate noise and fill in gaps
#     dilate = cv2.dilate(inrangepixels,None,iterations = 5)
#     erode = cv2.erode(dilate,None,iterations = 10)
#     dilatedagain = cv2.dilate(erode,None,iterations = 5)   

    inrangepixels = cv2.erode(inrangepixels,None,iterations = 3)
#   find the contours

    tobecontourdetected = inrangepixels.copy()
    contours,hierarchy = cv2.findContours(tobecontourdetected,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
    message = ""
    
    for contour in contours:  
        real_area = cv2.contourArea(contour)
        if real_area > 1500:
            message += str(real_area/10)[:3]+','
            M = cv2.moments(contour) #an image moment is the weighted average of a blob
            cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
            message += str(cx)[:3]
            cv2.circle(capture,(cx,cy),5,(0,0,255),-1)       
    
    message += ';'        
    if(message):
        if len(message)<=8:
            message += " "*(8-len(message))
            print message
            #s.sendto(message,("roborio-2914.local",100))
 
#    show our image during different stages of processing
    if(not skip_gui):
        cv2.imshow('capture',capture) 
        cv2.imshow('erodedbinary',inrangepixels)

    if cv2.waitKey(1) == 27:
        break
    
s.close()    
cv2.destroyAllWindows()
camera.release()
