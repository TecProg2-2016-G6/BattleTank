package src;

import java.util.Random;

//Store useful arithmetic data for the game engine such as 
//Cos/Sin look up table, color palette, etc...
public class GameData {

	public static int random[];
	public static int randomIndex;
	public static double sin[];
	public static double cos[];
	public static int colorTable[][];
	public static int screenTable[];
	public static Vector randomVectors[];
	public static int size[][];
	public static int distortion1[];
	public static short distortion2[];
	private static int SIZE_RANDOM_VECTOR = 1000;
	private final static int SCREENWIDHT = 640;
	private final static int SCREENHEIGHT = 480;
	private final static int MAX_RANDOM_VALUE = 100;
	static final double CIRCUMFERENCE = 360;
	static final double HALF_CIRCUMFERENCE = 180;

	public static void makeData() {

		// Make a screen table, so a pixel index can be retrived quickly
		screenTable = new int[SCREENHEIGHT];

		for (int i = 0; i < SCREENHEIGHT; i++) {
			screenTable[i] = SCREENWIDHT * i;
		}

		// Make random number table
		random = new int[SIZE_RANDOM_VECTOR];

		for (int i = 0; i < SIZE_RANDOM_VECTOR; i++) {
			random[i] = (int) (Math.random() * 100);
		}

		random = genereteRandomValuesVector(random, 999);

		// Make sin and cos look up tables
		sin = new double[(int) (CIRCUMFERENCE + 1)];
		cos = new double[(int) (CIRCUMFERENCE + 1)];
		for (int i = 0; i < (CIRCUMFERENCE + 1); i++) {
			sin[i] = Math.sin(Math.PI * i / HALF_CIRCUMFERENCE);
			cos[i] = Math.cos(Math.PI * i / HALF_CIRCUMFERENCE);
		}

		colorTable = makeColorTable();

		// Create randomVectors, they will be used in generating smoke particles
		randomVectors = new Vector[SIZE_RANDOM_VECTOR];
		
		for (int i = 0; i < SIZE_RANDOM_VECTOR; i++) {
			randomVectors[i] = new Vector(Math.random() * 0.016 - 0.008, 0.01, Math.random() * 0.016 - 0.008);
		}

		size = generateSprites();

		distortion1 = new int[128 * 128];
		distortion2 = new short[128 * 128];

		for (int i = 0; i < 128 * 128; i++) {
			distortion2[i] = (short) ((getRandomNumber() * 1.5) - 75);
		}

		for (int j = 0; j < 2; j++) {

			for (int i = 0; i < 128 * 128; i++)
				distortion1[i] = distortion2[i];

			for (int i = 0; i < 128 * 128; i++) {
				int average = 0;

				for (int y = -3; y < 4; y++) {
					for (int x = -3; x < 4; x++) {
						int index = ((i + 128 * y + x) + 128 * 128) % (128 * 128);

						average += distortion1[index];
					}
				}

				distortion2[i] = (short) (average / 49);

			}
		}

		// Generate distortion1 map, it solely used by stealth tank
		distortion1 = new int[128 * 128];
		for (int j = 0; j < 128; j++) {
			for (int k = 0; k < 128; k++)
				distortion1[j * 128
						+ k] = (int) (Math.sin(Math.PI / 32 * k + Math.PI / 8) * 10 * Math.sin(Math.PI / 16 * j));
		}

	}

	public static int getRandomNumber() {
		randomIndex++;
		if (randomIndex >= SIZE_RANDOM_VECTOR)
			randomIndex = 0;
		return random[randomIndex];

	}

	public static Vector getRandomVector() {
		randomIndex++;
		if (randomIndex >= SIZE_RANDOM_VECTOR)
			randomIndex = 0;
		return randomVectors[randomIndex];

	}

	// It frees the data stored when the applet is finished
	public static void destroy() {
		random = null;
		randomVectors = null;
		sin = null;
		cos = null;
		colorTable = null;
		screenTable = null;
	}

	// Generate vector with random values between 0 and 999
	private static int[] genereteRandomValuesVector(int vector[], int vectorSize) {

		Random generator = new Random();

		for (int i = 0; i < vectorSize; i++) {
			vector[i] = generator.nextInt(MAX_RANDOM_VALUE);
		}

		return vector;
	}

	/*
	 * Make color palette. The main color palette has 32768 (15bits) different
	 * colors with 64 different intensity levels. The default intensity is at
	 * level 31 .
	 */
	private static int[][] makeColorTable() {

		colorTable = new int[64][32768];

		int[][] colorTableTemp = new int[32768][64];

		double r, g, b, dr, dg, db;
		int r_, g_, b_;

		for (int i = 0; i < 32768; i++) {
			r = (double) ((i & 31744) >> 10) * 8;
			g = (double) ((i & 992) >> 5) * 8;
			b = (double) ((i & 31)) * 8;

			dr = r * 0.9 / 32;
			dg = g * 0.9 / 32;
			db = b * 0.9 / 32;

			// calculated the intensity from lvl 0 ~ 31
			for (int j = 0; j < 32; j++) {
				r_ = (int) (r - dr * j);
				g_ = (int) (g - dg * j);
				b_ = (int) (b - db * j);
				colorTableTemp[i][31 - j] = b_ + (g_ << 8) + (r_ << 16);
			}

			dr = r * 0.7 / 32;
			dg = g * 0.7 / 32;
			db = b * 0.7 / 32;

			// calculated the intensity from lvl 32 ~ 63
			for (int j = 1; j <= 32; j++) {
				r_ = (int) (r + dr * j);
				g_ = (int) (g + dg * j);
				b_ = (int) (b + db * j);
				if (r_ > 245)
					r_ = 245;
				if (g_ > 245)
					g_ = 245;
				if (b_ > 245)
					b_ = 245;
				colorTableTemp[i][31 + j] = b_ + (g_ << 8) + (r_ << 16);
			}
		}

		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 32768; j++)
				colorTable[i][j] = colorTableTemp[j][i];
		}

		// free memory used by creating color table
		colorTableTemp = null;

		return colorTable;
	}

	// Generate sprites for particles with different size
	private static int[][] generateSprites() {
		size = new int[9][];
		size[0] = new int[] { 0, -1, -SCREENWIDHT };
		size[1] = new int[] { -(SCREENWIDHT + 1), 0, -1, -SCREENWIDHT };
		size[2] = new int[] { 1, 0, -1, -SCREENWIDHT, SCREENWIDHT };
		size[3] = new int[] { -(SCREENWIDHT + 1), -(SCREENWIDHT - 1), 1, 0, -1, -SCREENWIDHT, SCREENWIDHT };
		size[4] = new int[] { -(SCREENWIDHT + 1), -(SCREENWIDHT - 1), 1, 0, -1, -SCREENWIDHT, SCREENWIDHT,
				(SCREENWIDHT - 1), (SCREENWIDHT + 1) };
		size[5] = new int[] { -(SCREENWIDHT * 2), -((SCREENWIDHT * 2) + 1), -(SCREENWIDHT + 2), -(SCREENWIDHT + 1),
				-SCREENWIDHT, -(SCREENWIDHT - 1), -2, -1, 0, 1, SCREENWIDHT - 1, SCREENWIDHT };
		size[6] = new int[] { -((SCREENWIDHT * 2) + 1), -((SCREENWIDHT * 2) - 1), -(SCREENWIDHT + 2),
				-(SCREENWIDHT - 2), SCREENWIDHT - 2, SCREENWIDHT + 2, ((SCREENWIDHT * 2) - 1), ((SCREENWIDHT * 2) + 1),
				-(SCREENWIDHT * 2), -(SCREENWIDHT + 1), -2, -(SCREENWIDHT - 1), 1, 2, 0, -1, -SCREENWIDHT, SCREENWIDHT,
				(SCREENWIDHT - 1), SCREENWIDHT + 1, (SCREENWIDHT * 2) };
		size[7] = new int[] { -((SCREENWIDHT * 2) - 2), -((SCREENWIDHT * 2) + 2), ((SCREENWIDHT * 2) + 2),
				((SCREENWIDHT * 2) - 2), -(SCREENHEIGHT * 4), (SCREENHEIGHT * 4), -3, 3, -((SCREENWIDHT * 2) + 1),
				-((SCREENWIDHT * 2) - 1), -(SCREENWIDHT + 2), -(SCREENWIDHT - 2), SCREENWIDHT - 2, SCREENWIDHT + 2,
				((SCREENWIDHT * 2) - 1), ((SCREENWIDHT * 2) + 1), -(SCREENWIDHT * 2), -(SCREENWIDHT + 1), -2,
				-(SCREENWIDHT - 1), 1, 2, 0, -1, -SCREENWIDHT, SCREENWIDHT, SCREENWIDHT - 1, SCREENWIDHT + 1,
				(SCREENWIDHT * 2) };
		size[8] = new int[] { 0 };
		
		return size;
	}
}
