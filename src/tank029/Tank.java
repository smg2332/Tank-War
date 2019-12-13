package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Tank {
	
	//每次移动的距离
	public static final int XDISTANCE=7;
	public static final int YDISTANCE=7;
	
	//坦克半径
	public  static int W=40;
	public  static int H=40;
	
	//是否按下移动键
	private boolean bL=false,bR=false,bU=false,bD=false;
	
	//坦克的默认方向
	private Direction dir=Direction.STOP;
	
	//炮筒的方向
	private Direction ptdir=Direction.D;
	
	//坦克的坐标
	private int x;
	private int y;
	
	//坦克的坐标
	private int oldX;
	private int oldY;
	
	//持有对方的引用
	TankClient tc=null;
	
	//敌我坦克的标识
	private boolean good;
	
	//记录坦克生死的量
	private boolean live=true;
	
	//随机数产生器
	private static Random r=new Random();
	
	//坦克随机的步数
	private int stop=r.nextInt(12)+3;
	
	//坦克的生命值
	private int life=100;
	
	//血条
	private BooldBar bb=new BooldBar();
	
	//工具类，帮助加载图片到内存中
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	
	//存放图片的图片数组
	public static Image [] tankImage= null;
	
	//帮助区分图片的map映射
	public static Map<String ,Image> imgs=new HashMap<String, Image>();
	
	//静态代码块
	static {
		
		//初始化图片数组
		tankImage=new Image[] {
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankLU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankRU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankLD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("image/tankRD.gif"))
		};
		
		//添加映射
		imgs.put("L", tankImage[0]);
		imgs.put("R", tankImage[1]);
		imgs.put("U", tankImage[2]);
		imgs.put("D", tankImage[3]);
		imgs.put("UL", tankImage[4]);
		imgs.put("UR", tankImage[5]);
		imgs.put("DL", tankImage[6]);
		imgs.put("DR", tankImage[7]);
	};	
	
	

	//构造器
	public Tank(int x,int y ,boolean good) {
		this.x=x;
		this.y=y;
		this.oldX=x;
		this.oldY=y;
		this.good=good;
	}

	public Tank(int x,int y,boolean good,TankClient tc) {
		this(x,y,good);
		this.tc=tc;
	}
	
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc) {
		this(x,y,good,tc);
		this.dir=dir;
	}
	
	
	//画图
	public void draw(Graphics g) {
		
		//判断是否活着，死了移除数组
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		//如果是好的画出血条
		if(good) {
			bb.draw(g);
		}
		
		//坦克移动
		move();
		
		//根据炮筒的方向，画出坦克的方向
		switch(ptdir) {
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
		case STOP:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		}
		
	}

	//坦克的开炮
	public void  fire() {
		
		//如果死了，不会开炮
		if(!live) {
			return ;
		}
		
		//添加一发新的炮弹
		tc.missiles.add(new Missile(x+W/2-Missile.W/2, y+W/2-Missile.H/2, good,ptdir,this.tc));
	}
	
	//制定方向的炮弹
	public void  fire(Direction dir1) {
		if(!live) {
			return ;
		}
		tc.missiles.add(new Missile(x+W/2-Missile.W/2, y+W/2-Missile.H/2, good,dir1,this.tc));
	}
	
	//超级炮弹
	public void superFire() {
		if(!live) {
			return ;
		}
		//向各个方向发炮
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++) {
			fire(dirs[i]);
		}
	}
	
	//坦克的移动方法
	public void move() {
		//保存上一次的位置，方便装墙时，返回
		oldX=x;
		oldY=y;
		
		//判断移动
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
		
		//判断炮筒的方向
		if(this.dir !=Direction.STOP) {
			this.ptdir=this.dir;
		}
		
		//判断坦克的位置，不让坦克出界
		if(x<5) x=5;
		if(y<30) y=30;
		if(x+Tank.W+5>TankClient.W) x= TankClient.W-Tank.W-5;
		if(y+Tank.H+5>TankClient.H) y= TankClient.H-Tank.H-5;
		
		//给敌人的坦克添加随机开火键
		if(!good) {
			Direction[] dirs=Direction.values();
			if(stop==0) {
				stop=r.nextInt(12)+3;
				int rn=r.nextInt(dirs.length);
				dir=dirs[rn];
			}
			if(r.nextInt(30)>25) {
				fire();
			}
			stop--;
		}
	}
	
	//确定坦克的方向
	public void locateDirection() {
		if(bL && !bR && !bU && !bD) dir=Direction.L;
		else if(!bL && bR && !bU && !bD) dir=Direction.R;
		else if(!bL && !bR && bU && !bD) dir=Direction.U;
		else if(!bL && !bR && !bU && bD) dir=Direction.D;
		
		else if(bL && !bR && bU && !bD) dir=Direction.UL;
		else if(bL && !bR && !bU && bD) dir=Direction.DL;
		else if(!bL && bR && bU && !bD) dir=Direction.UR;
		else if(!bL && bR && !bU && bD) dir=Direction.DR;
		
		else if(!bL && !bR && !bU && !bD) dir=Direction.STOP;
	}
	
	//键盘按压监听
	public void keyPress(KeyEvent e) {
		
		//得到按键值
		int key=e.getKeyCode();
		
		//判断按键值
		switch(key) {
		case KeyEvent.VK_D:
			if(!live) {
				this.live=true;
				life=100;
			}
			break;
		case KeyEvent.VK_LEFT:
			bL=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
		
		}
		
		//确定位置
		locateDirection();
	}

	//添加松开按键的监听
	public void keyRelease(KeyEvent e) {
		
		//获取松开按键的值
		int key=e.getKeyCode();
		
		//判断松开按键的值
		switch(key) {
		case KeyEvent.VK_LEFT:
			bL=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		}
		
		//判断方向
		locateDirection();
	}
	
	//添加碰撞检测
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
	
	//解决坦克撞墙的问题
	public boolean hitWall(Wallet w) {
		if(this.live&&getRec().intersects(w.getRec())) {
			stay();
			return true;
		}
		return false;
	}
	
	//解决坦克撞墙的问题
	public boolean hitWall(List<Wallet> wallets) {
		for(int i=0;i<wallets.size();i++) {
			if(hitWall(wallets.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	//解决坦克可以从别的坦克身上过去的问题
	public boolean hitTank(Tank t) {
		if(this.live&&getRec().intersects(t.getRec())&&isLive()&&this!=t) {
			stay();
			t.stay();
			return true;
		}
		return false;
	}
	
	//解决坦克可以从别的坦克身上过去的问题
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public void stay(){
		x=oldX;
		y=oldY;
		this.stop=0;
	}
	
	//内部类，血条类
	public class BooldBar{
		
		//画图方法
		public void draw(Graphics g) {
			Color c=g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, W, 10);
			g.setColor(Color.red);
			int l=W*life/100;
			g.fillRect(x, y-10, l, 10);
			
			
			g.setColor(c);
		}
	}
	
	//撞到血条加血，
	public boolean hitBlood(Blood b) {
		if(good&&this.live&&getRec().intersects(b.getRec())&&b.isLive()) {
			//装到后血条消失
			b.setLive(false);
			life=100;
			return true;
		}
		return false;
	}
	
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}
	
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

}
