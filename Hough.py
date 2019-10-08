import numpy as np
import cv2

class HoughTransform:

    def __init__(self, img, rmin, rmax, limSupD=None, limSupE=None, limInfE=None, limInfD=None):
        self.img = img
        self.rmin = rmin
        self.rmax = rmax
        self.limSupD = limSupD
        self.limSupE = limSupE
        self.limInfE = limInfE
        self.limInfD = limInfD
        self.width = img.shape[0]
        self.height = img.shape[1]
        self.accumulator=np.zeros((self.width,self.height,(self.rmax-self.rmin)))
        self.pxs_on = self.BufferedImageToPoint(self.img, 0)

    def BufferedImageToPoint(self, img, valor):
        pontos = []
        for  lin in range(0, img.shape[0]):
            for col in range(0, img.shape[1]):
                if(img[lin][col][0] > valor):
                    pontos.append([lin, col])

        return pontos

    #def getCirculo

    #def applyMethod

    #def getPeak
        #Index_Min?

    #localizaLimbo?
    #ordenaHough?


img = cv2.imread('edges_screenshot_07.10.2019.png')
hough = HoughTransform(img, 100, 150)
