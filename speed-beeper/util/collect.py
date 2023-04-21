import datetime
import time
import os
import cv2

x0 = 100
x1 = 500
y0 = 150
y1 = 350
scale = 0.3


today=time.strftime('%Y-%m-%d')
clock=time.strftime('%H-%M-%S')
collectionDir = "./" + str(today) + "_" + str(clock)
print("Collecting to folder " + collectionDir) 
os.system("mkdir "+collectionDir)

cap = cv2.VideoCapture(2)

# Check if the webcam is opened correctly
if not cap.isOpened():
    raise IOError("Cannot open webcam")

count = 0
while True:
    ret, frame = cap.read()
    frame = frame[y0:y1, x0:x1]
    frame = cv2.resize(frame, None, fx=scale, fy=scale, interpolation=cv2.INTER_AREA)
    file = f"{count:03d}.jpg"
    count += 1
    filepath = collectionDir + "/" + file
    print("Saving "+ filepath)
    cv2.imwrite(filepath, frame)
    cv2.imshow('Input', frame)

    c = cv2.waitKey(1)
    if c == 27:
        break

    time.sleep(.5)

cap.release()
cv2.destroyAllWindows()

