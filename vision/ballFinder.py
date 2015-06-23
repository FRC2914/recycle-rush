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
hue_lower = 15
hue_upper = 30
saturation_lower = 0
saturation_upper = 255
value_lower = 0
value_upper = 255

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
#    remove single pixels
    inrangepixels = cv2.erode(inrangepixels,None,iterations = 3)
#   find the contours
    tobecontourdetected = inrangepixels.copy()
    contours,hierarchy = cv2.findContours(tobecontourdetected,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
    
    message = ""   
    
    if len(contours)>0:
        #find biggest contour
        tote_sizes = []
        for tote in contours:
            tote_sizes.append(cv2.contourArea(tote))
        biggest_tote_index = 0
        for i in range(1,len(tote_sizes)):
            if tote_sizes[i]>tote_sizes[biggest_tote_index]:
                biggest_tote_index = i    
        biggest_tote=contours[biggest_tote_index]
        biggest_tote_size=tote_sizes[biggest_tote_index]      
        
        #if biggest tote is big enough
        if biggest_tote_size > 1500:
            #find highest and lowest point
            highest_contour_y=biggest_tote[0][0][1]
            lowest_contour_y=biggest_tote[0][0][1]
            for coord in biggest_tote:
                if coord[0][1]<highest_contour_y:
                    highest_contour_y = coord[0][1]
                if coord[0][1]>lowest_contour_y:
                    lowest_contour_y = coord[0][1]
            
            #find centroid
            tote_centroid = cv2.moments(biggest_tote)        
            cx,cy = int(tote_centroid['m10']/tote_centroid['m00']), int(tote_centroid['m01']/tote_centroid['m00'])
            
            #message is "<x-coord>,<height>;"
            message += str(cx)[:3]+','
            message += str(abs(highest_contour_y-lowest_contour_y))[:3]    
            cv2.line(capture,(0,highest_contour_y),(320,highest_contour_y),(0,0,255),5)
            cv2.line(capture,(0,lowest_contour_y),(320,lowest_contour_y),(0,0,255),5) 
        
        message += ';'
        if len(message)<=8:
            message += " "*(8-len(message))
            if os.name is "nt":#print on windows, send on linux
                print message
            else:
                s.sendto(message,("roborio-2914.local",2914))       
 
#    show our image during different stages of processing
    if os.name is "nt":#only show on windows
        cv2.imshow('capture',capture) 
        cv2.imshow('erodedbinary',inrangepixels)

    if cv2.waitKey(1) == 27:
        break
    
s.close()    
cv2.destroyAllWindows()
camera.release()
