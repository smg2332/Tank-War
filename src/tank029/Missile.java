package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Missile {
	//每次移动的距离
	public static final int XDISTANCE=15;
	public static final int YDISTANCE=15;
	//半径
	public  static int W=10;
	public  static int H=10;
	//坐标
	private int x;
	private int y;
	//方向
	Direction dir;
	//判断是否活着
	private boolean live=true;
	//持有对方的引用
	TankClient tc;
	//判断子弹的好坏
	private boolean good ;
	
	//返回坦克是否活着
	public boolean isLive() {
		return live;
	}

	//工具类，帮助加载图片
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	
	//存放图片的数组
	public static Image [] missileImage= null;
	
	//把对应图片的字符串对应起来
	public static Map<String ,Image> imgs=new HashMap<String, Image>();
	
	//静态代码块，初始化图片数组
	static {
		//加载图片数组
		missileImage=new Image[] {
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileL.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileR.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileU.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileD.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileLU.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileRU.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileLD.gif")),
			tk.getImage(Missile.class.getClassLoader().getResource("image/missileRD.gif"))
		};
		//吧图片数组添加到map中，是字符串和图片数组对应
		imgs.put("L", missileImage[0]);
		imgs.put("R", missileImage[1]);
		imgs.put("D", missileImage[2]);
		imgs.put("U", missileImage[3]);
		imgs.put("UL", missileImage[4]);
		imgs.put("UR", missileImage[5]);
		imgs.put("DL", missileImage[6]);
		imgs.put("DR", missileImage[7]);
	};	
	
	
	//初始化
	public Missile(int x, int y, Direction dir) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	//初始化
	public Missile(int x, int y,boolean good, Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	
	//画图
	public void draw(Graphics g) {
		//移动子弹
		move();
		
		//画出子弹的图片
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case UL:
			g.drawImage(imgs.get("UL"), x, y, null);
			break;
		case UR:
			g.drawImage(imgs.get("UR"), x, y, null);
			break;
		case DL:
			g.drawImage(imgs.get("DL"), x, y, null);
			break;
		case DR:
			g.drawImage(imgs.get("DR"), x, y, null);
			break;
		
		}
		
	}
	
	//移动子弹
	public void move() {
		//判断子弹是否活着，死了移除子弹，不执行移动方法
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		//判断子弹的移动
		switch(dir) {
		case L:
			x=x-XDISTANCE;
			break;
		case R:
			x=x+XDISTANCE;
			break;
		case U:
			y=y-YDISTANCE;
			break;
		case D:
			y=y+YDISTANCE;
			break;
		case UL:
			x=x-XDISTANCE;
			y=y-YDISTANCE;
			break;
		case UR:
			x=x+XDISTANCE;
			y=y-YDISTANCE;
			break;
		case DL:
			y=y+XDISTANCE;
			x=x-YDISTANCE;
			break;
		case DR:
			y=y+YDISTANCE;
			x=x+XDISTANCE;
			break;
		case STOP:
			break;
		}
		
		//如果子弹出界，则子弹死亡
		if(x<0||y<0||x>TankClient.W||y>TankClient.H) {
			live=false;
			
		}
	}
	
	//判断碰撞问题
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
	
	//子弹撞到一个坦克
	public boolean hitTank(Tank t) {
		//判断子弹是否活着，判断是否碰撞，判断坦克是否活着，判断子弹和坦克和好坏
		if(this.live&&this.getRec().intersects(t.getRec())&&t.isLive()&&this.good!=t.isGood()) {
			//是自己的坦克减血，敌人的坦克死亡
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0) {
					t.setLive(false);
				}
			}else {
				t.setLive(false);
			}
			//子弹击中坦克时发生爆炸
			Explode e=new Explode(x,y,tc);
			//爆炸类里添加爆炸对象
			tc.explodes.add(e);
			//子弹死亡
			this.live=false;
			return true;
		}
		return false;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}

	//判断子弹是否撞击一群坦克
	public boolean hitTank(List <Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				
				return true;
			}
		}
		return false;
	}
	
	//子弹撞墙消失
	public boolean hitWall(Wallet w) {
		//判断子弹是否活着，是否撞击
		if(this.live&&getRec().intersects(w.getRec())) {
			this.live=false;
			return true;
		}
		return false;
	}
	
	//判断子弹是否撞击一堆墙
	public boolean hitWall(List<Wallet> wallets) {
		for(int i=0;i<wallets.size();i++) {
			if(hitWall(wallets.get(i))) {
				return true;
			}
		}
		return false;
	}
}
