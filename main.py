import cv2
from Hough import HoughTransform

img = cv2.imread('circulo.png')
output=img.copy()
hough = HoughTransform(img, 98, 102, 60, 120, 240, 300)

for (y, x, r, acc) in hough.coord_center:
    x=int(x)
    y=int(y)
    r=int(r)
    cv2.rectangle(output, (x - 1, y - 1), (x + 1, y + 1), (0, 128, 255), -1)
    cv2.circle(output, (x, y), r, (0, 255, 0), 1)

cv2.imshow("imagem", img)
cv2.waitKey(0)

for i in range(hough.rmax-hough.rmin):
    cv2.imshow("Acumulador", hough.accumulator[:,:,i])
    cv2.waitKey(0)

cv2.imshow("output", output)
cv2.waitKey(0)
