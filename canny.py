import numpy as np
import cv2 as cv

img = cv.imread('olho.jpg',0)
blur = cv.GaussianBlur(img,(9,9),0)
edges = cv.Canny(blur, 10, 60)

cv.imshow("img", img)
cv.imshow("blur", blur)
cv.imshow("edges", edges)
cv.waitKey(0)

