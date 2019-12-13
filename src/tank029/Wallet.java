package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Wallet {
	//半径
	public int W;
	public int H;
	//坐标
	private int x;
	private int y;
	//持有对方的引用
	TankClient tc;
	public Wallet(int x, int y, int W,int H,TankClient tc) {
		super();
		this.W=W;
		this.H=H;
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		Color c=g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, W, H);
		
		g.setColor(c);
	}
	
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
	
	
	
	
	
	
}
