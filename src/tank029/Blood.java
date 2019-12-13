package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * 坦克中的血块，类，吃了可以回血
 * @author 程
 *
 */
public class Blood {
	
	//半径
	public  static int W=10;
	public  static int H=10;
	//坐标
	private int x=500;
	private int y=300;
	//执行到第几步
	private int step=0;
	//判断血块是否是活着
	private boolean live=true;
	
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	//持有对象的引用
	TankClient tc;
	
	//血块运动的轨迹
	private int[] [] pos= {
			{500,460},{500,450},{500,440},{500,430},{500,420},{500,410},
			{500,400},
			{500,410},{500,420},{500,430},{500,440},{500,450},{500,460},
	};
	
	//构造方法
	public Blood(TankClient tc) {
		this.tc=tc;
	}
	
	//画图
	public void draw(Graphics g) {
		//先判断是否活着，如果死了不花，直接输出
		if(!live) {
			return ;
		}
		
		Color c=g.getColor();
		//设置颜色大小
		g.setColor(Color.magenta);
		g.fillRect(x, y, W, H);
		//移动
		move();
		g.setColor(c);
	}

	//按照轨迹一步步的移动
	private void move() {
		step++;
		//如果运动完之后，计数器重置
		if(step==pos.length) {
			step=0;
		}
		x=pos[step][0];
		y=pos[step][1];
		
	}
	
	//判断位置
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
		
	

	
	
}
