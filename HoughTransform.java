package br.ufma.nca.imageprocessing.edgedetectors;

/**
* <p>
* Title:
* </p>
* <p>
* Description:
* </p>
* <p>
* Copyright: Copyright (c) 2005
* </p>
* <p>
* Company:
* </p>
*
* @author not attributable
* @version 1.0
*/

import java.awt.image.BufferedImage;
import java.awt.Point;

import java.util.List;
import java.util.LinkedList;

import br.ufma.nca.imageprocessing.*;

public class HoughTransform {

	private short accumulator[][][] = null;

	private Point pxs_on[] = null;

	private int rmin = 0, rmax = 1;

	private BufferedImage img = null;

	/** limites utilizados para definir os pixels do circulo que ser�o contabilizados no acumulador   */
	private int limSupD = 60;

	private int limInfD = 300;

	private int limSupE = 120;

	private int limInfE = 240;

	/** ---------------------------------------------------- */

	public HoughTransform(BufferedImage img, int rmin, int rmax) {
		this.rmin = rmin;
		this.rmax = rmax;
		this.img  = img;
		//aloca e zera o acumulador
		allocateAccumulator(img.getWidth(), img.getHeight());
		// pega todos os pixels de valor == 255
		pxs_on = Converters.BufferedImageToPoint(img, 0);
	}

	/**
	*
	* @param img Imagem
	* @param rmin Raio minimo
	* @param rmax  Raio m�ximo
	* @param limSupD Limite Superior do intervalo do lado direito da circunferencia do circulo que ser� contabilizado no acumulador
	* @param limInfD Limite Inferior do intervalo do lado direito da circunferencia do circulo que ser� contabilizado no acumulador
	* @param limSupE Limite Superior do intervalo do lado esquerdo da circunferencia do circulo que ser� contabilizado no acumulador
	* @param limInfE Limite Inferior do intervalo do lado esquerdo da circunferencia do circulo que ser� contabilizado no acumulador
	*/
	public HoughTransform(BufferedImage img, int rmin, int rmax, int limSupD, int limInfD, int limSupE, int limInfE) {
		this.rmin = rmin;
		this.rmax = rmax;
		this.img  = img;

		this.limSupD =limSupD;
		this.limInfD = limInfD;
		this.limSupE = limSupE;
		this.limInfE = limInfE;

		//aloca e zera o acumulador
		allocateAccumulator(img.getWidth(), img.getHeight());
		// pega todos os pixels de valor == 255
		pxs_on = Converters.BufferedImageToPoint(img, 0);
	}

	private void allocateAccumulator(int lmax, int cmax) {
		try {
			//aloca
			accumulator = new short[lmax][cmax][];
			for (int lin = 0; lin < lmax; lin++) {
				for (int col = 0; col < cmax; col++) {
					accumulator[lin][col] = new short[rmax - rmin];
				}
			}
			//iniciliza
			for (int raio = 0; raio < (rmax - rmin); raio++) {
				for (int lin = 0; lin < lmax; lin++) {
					for (int col = 0; col < cmax; col++) {
						accumulator[lin][col][raio] = 0;
					}
				}
			}
		} catch (NullPointerException e) {
			System.out
			.println("Hough Transform: Erro na inicializa do acumulador.");
		} catch (Exception ex) {
			System.out
			.println("Hough Transform: Erro na inicializa do acumulador.");
		}
	}

	public int isCirculo( BufferedImage img, Point centro, int raio, float perc ){

				/*int  px_ativos  = 0, x, y;
				int p[] = new int[3];
				Point[] circulo = getCirculo(raio);
				WritableRaster raster =	img.getRaster();
				for (int coord = 0; coord < circulo.length; coord++) {//varia os pixels do circulo calculado

				x = centro.x - circulo[coord].x;
				y = centro.y - circulo[coord].y;

				//valida os dados de entrada
				if ((x >= 0) && (x < img.getWidth()) &
				(y >= 0) && (y < img.getHeight())) {
				if( raster.getPixel(x,y,p)[0] == 255 )
				px_ativos += 1;
			}
		}//end for coord
		double valor = (px_ativos*100)/(8*raio);
		if( valor > perc )
		return (int)valor;*/

		return -1;
	}

	// Modifica��o despreza o fator utilizado na divis�o da circunferencia em peda�os
	private Point[] getCirculo(int raio, float perc, float fator) {
		List circulo = new LinkedList();
		int max_div   = (int)fator*raio;//numero de vezes que o circulo vai ser dividido
		//teste max_div
		max_div = 1;
		double angulo = 0.0;   //angulo em radianos referente a cada divisao do
		int Xc = 0, Yc = 0, Xant = 0, Yant = 0;

		// Varre o circulo completo circulo
		//for (int passo = 0; passo < max_div; passo++) {
		for (int passo = 0; passo < 360; passo++) {
			// teste - pegando apenas as bordas da direita e da esquerda do circulo
			if ((passo > 0 && passo < limSupD) || (passo > limInfD && passo < 360) ||
			(passo > limSupE && passo < limInfE)) {
				//angulo = (2 * Math.PI * passo) / max_div; //angulo em radianos
				angulo = passo;
				angulo = Math.toRadians(angulo);
				Xc = Math.round((float)(raio*perc*Math.cos(angulo))); //coordenada x
				// do centro
				Yc = Math.round((float)(raio*perc*Math.sin(angulo))); //coordenada y
				// do centro
				if( passo == 0 || ( (Xc != Xant) || (Yc != Yant) )){
					Xant = Xc;  Yant = Yc;
					circulo.add((Object)(new Point(Xc, Yc)));
				}

			}

		}//end for
		return Converters.ListToPoint(circulo);
	}

	//	 Modifica��o despreza o fator utilizado na divis�o da circunferencia em peda�os
	private Point[] getCirculoBrilho(int raio, float perc, float fator) {
		List circulo = new LinkedList();
		int max_div   = (int)fator*raio;//numero de vezes que o circulo vai ser dividido
		//teste max_div
		max_div = 1;
		double angulo = 0.0;   //angulo em radianos referente a cada divisao do

		int Xc = 0, Yc = 0, Xant = 0, Yant = 0;
		// Varre o circulo completo circulo
		for (int passo = 0; passo < 360; passo++) {
			//angulo = (2 * Math.PI * passo) / max_div; //angulo em radianos
			angulo = Math.toRadians(passo);
			Xc = Math.round((float)(raio*perc*Math.cos(angulo))); //coordenada x
			// do centro
			Yc = Math.round((float)(raio*perc*Math.sin(angulo))); //coordenada y
			// do centro
			if( passo == 0 || ( (Xc != Xant) || (Yc != Yant) )){
				Xant = Xc;  Yant = Yc;
				circulo.add((Object)(new Point(Xc, Yc)));
			}
		}//end for
		return Converters.ListToPoint(circulo);
	}

	/**
	* @param qtd
	* @param perc
	* @return
	*/
	public int[][] applyMethod(int qtd, float perc, float fator) {

		if (Math.abs(rmax - rmin) > 70)
		throw new IllegalArgumentException(
		"A diferen�a entre os raios deve ser de no m�ximo 70");
		if( img == null )
		throw new IllegalArgumentException("Imagem inv�lida");
		int h_x = 0, h_y  = 0; //coordenadas no espa�o de hough
		//comeca a votacao, ou seja, a acumulacao de votos
		for (int raio = 0; raio < (rmax - rmin); raio++) {
			//constroi o circulo modelo
			Point circulo[] = getCirculo((raio + rmin),perc, fator);
			//Point circulo2[] = getCirculo((raio + rmin),0.995f);
			//para cada pixel da borda da imagem calcular um circulo de raio
			// (raio+rmin) para determinar todos os possiveis centros de
			//circulos ao redor desse pixel
			for (int np = 0; np < pxs_on.length; np++) {//varia todos os pixels da borda
				for (int coord = 0; coord < circulo.length; coord++) {//varia os pixels do circulo calculado
					//calcula os possiveis centros de circuferencia ao redor do
					//pixel pxs_on[np]
					h_x = (pxs_on[np].x + circulo[coord].x); //coordenada no espaco de hough
					//para x
					h_y = (pxs_on[np].y + circulo[coord].y); //coordenada no espaco de hough
					//para y
					//valida os dados de entrada
					if ((h_x >= 0) && (h_x < img.getWidth()) &
					(h_y >= 0) && (h_y < img.getHeight())) {
						accumulator[h_x][h_y][raio] += 1;
					}
				}//end for coord
			}//end for np
		}//end for raio
		return getPeak(qtd);
	}

	/**
	* @param qtd
	* @param perc
	* @return
	*/
	public int[][] applyMethodBrilho(int qtd, float perc, float fator) {

		if (Math.abs(rmax - rmin) > 70)
		throw new IllegalArgumentException(
		"A diferen�a entre os raios deve ser de no m�ximo 70");
		if( img == null )
		throw new IllegalArgumentException("Imagem inv�lida");
		int h_x = 0, h_y  = 0; //coordenadas no espa�o de hough
		//comeca a votacao, ou seja, a acumulacao de votos
		for (int raio = 0; raio < (rmax - rmin); raio++) {
			//constroi o circulo modelo
			Point circulo[] = getCirculoBrilho((raio + rmin),perc, fator);
			//para cada pixel da borda da imagem calcular um circulo de raio
			// (raio+rmin) para determinar todos os possiveis centros de
			//circulos ao redor desse pixel
			for (int np = 0; np < pxs_on.length; np++) {//varia todos os pixels da borda
				for (int coord = 0; coord < circulo.length; coord++) {//varia os pixels do circulo calculado
					//calcula os possiveis centros de circuferencia ao redor do
					//pixel pxs_on[np]
					h_x = (pxs_on[np].x + circulo[coord].x); //coordenada no espaco de hough
					//para x
					h_y = (pxs_on[np].y + circulo[coord].y); //coordenada no espaco de hough
					//para y
					//valida os dados de entrada
					if ((h_x >= 0) && (h_x < img.getWidth()) &
					(h_y >= 0) && (h_y < img.getHeight())) {
						accumulator[h_x][h_y][raio] += 1;
					}
				}//end for coord
			}//end for np
		}//end for raio
		return getPeak(qtd);
	}

	private int[][] getPeak(int qtd) {
		int coord_center[][] = new int[qtd][4];
		int index = 0;
		int cont = 0;
		boolean trocou = true;
		for (int raio = 0; raio < (rmax - rmin); raio++) {
			for (int lin = 0; lin < accumulator.length; lin++) {
				for (int col = 0; col < accumulator[0].length; col++) {
					if (cont < coord_center.length) {
						coord_center[cont][0] = lin;
						coord_center[cont][1] = col;
						coord_center[cont][2] = raio + rmin;
						coord_center[cont][3] = accumulator[lin][col][raio];
						cont++;
					} else {
						if (trocou) {
							index = Index_Min(coord_center);
							trocou = false;
						}
						if (accumulator[lin][col][raio] > coord_center[index][3]) {
							coord_center[index][0] = lin;
							coord_center[index][1] = col;
							coord_center[index][2] = raio + rmin;
							coord_center[index][3] = accumulator[lin][col][raio];
							trocou = true;
						}
					}
				}
			}
		}
		//System.out.println("teste: "+(rmax-rmin)+" "+cont+" "+index+" " );
		return coord_center;
	}

	public void localizaLimbo(int[][] hough, int perc, int width){

		int [] maiorD = new int[perc];
		int [] maiorE = new int[perc];
		int [] RaioD = new int[perc];
		int [] RaioE = new int[perc];
		int [] od_x = new int[perc];
		int [] od_y = new int[perc];
		int [] oe_x = new int[perc];
		int [] oe_y = new int[perc];
		int count_d = 0;
		int count_e = 0;

		for (int x = 0; x < perc; x++){
			if (hough[x][0] < width/2){
				maiorD[count_d] = hough[x][3];
				od_x[count_d] = hough[x][0];
				od_y[count_d] = hough[x][1];
				RaioD[count_d++] = hough[x][2];
			}
			else {
				maiorE[count_e] = hough[x][3];
				oe_x[count_e] = hough[x][0];
				oe_y[count_e] = hough[x][1];
				RaioE[count_e++] = hough[x][2];
			}
		}

		// Ordena vetor de acumula��o do Olho Direito
		for (int i = 0; i < perc; i++) {
			int copyNumber = maiorD[i];
			int pos_I = od_x[i];
			int pos_J = od_y[i];
			int j = i;
			while (j > 0 && copyNumber < maiorD[j-1]) {
				maiorD[j] = maiorD[j-1];
				od_x[j] = od_x[j-1];
				od_y[j] = od_y[j-1];
				j--;
			}
			maiorD[j] = copyNumber;
			od_x[j] = pos_I;
			od_y[j] = pos_J;
		}

		//		 Ordena vetor de acumula��o do Olho Esquerdo
		for (int i = 0; i < perc; i++) {
			int copyNumber = maiorE[i];
			int pos_I = oe_x[i];
			int pos_J = oe_y[i];
			int j = i;
			while (j > 0 && copyNumber < maiorE[j-1]) {
				maiorE[j] = maiorE[j-1];
				oe_x[j] = oe_x[j-1];
				oe_y[j] = oe_y[j-1];
				j--;
			}
			maiorE[j] = copyNumber;
			oe_x[j] = pos_I;
			oe_y[j] = pos_J;
		}

		System.out.println();
		for (int i = perc-1; i >=0; i--) {
			System.out.print( maiorD[i]+" ");
		}
		System.out.println();
		for (int i = perc-1; i >=0; i--) {
			System.out.print( maiorE[i]+" ");
		}

	}

	public int[][] ordenaHough(int hough[][]){

		//Insertion Sort
		for (int i = 0; i < hough.length; i++) {
			int copyNumber = hough[i][3];
			int x = hough[i][0];
			int y = hough[i][1];
			int raio = hough[i][2];
			int j = i;
			while (j > 0 && copyNumber < hough[j-1][3]) {
				hough[j][3] = hough[j-1][3];
				hough[j][0] = hough[j-1][0];
				hough[j][1] = hough[j-1][1];
				hough[j][2] = hough[j-1][2];
				j--;
			}
			hough[j][3] = copyNumber;
			hough[j][0] = x;
			hough[j][1] = y;
			hough[j][2] = raio;
		}

		return hough;
	}

	//retorna o maximo valor de uma matriz
	private int Index_Min(int[][] m) {
		float min = 100000000;
		int index = 0;
		try {
			for (int lin = 0; lin < m.length; lin++) {
				if (min > m[lin][3]) {
					min = m[lin][3];
					index = lin;
				}
			}
		} catch (NullPointerException e) {
			System.out
			.println("Matriz sem mem�ria na funcao Index_min classe Hough Transform");
		}
		return index;
	}

	public short[][][] getAccumalator() {
		return accumulator;
	}

	private Point[] getPeaks(int max_peaks) {
		return null;
	}

	/**
	* @return Returns the rmax.
	*/
	public int getRmax() {
		return rmax;
	}
	/**
	* @param rmax The rmax to set.
	*/
	public void setRmax(int rmax) {
		this.rmax = rmax;
	}
	/**
	* @return Returns the rmin.
	*/
	public int getRmin() {
		return rmin;
	}
	/**
	* @param rmin The rmin to set.
	*/
	public void setRmin(int rmin) {
		this.rmin = rmin;
	}
}
