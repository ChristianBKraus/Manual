package jupiterpa.manual.domain.model;

public class LEDColor {
	public static final LEDColor Black = new LEDColor(0,0,0);
	public static final LEDColor White = new LEDColor(255,255,255);
	public static final LEDColor Red = new LEDColor(255,0,0);
	public static final LEDColor Green = new LEDColor(0,255,0);
	public static final LEDColor Blue = new LEDColor(0,0,255);
	public static final LEDColor Yellow = new LEDColor(0,255,255);

	
	int red;
	int green;
	int blue;
	
	public LEDColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	@Override
	public String toString() {
		return "Color: " + red + "/" + green + "/" + blue;
	}
	
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
	
}
