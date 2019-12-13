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
	
	//ÿ���ƶ��ľ���
	public static final int XDISTANCE=7;
	public static final int YDISTANCE=7;
	
	//̹�˰뾶
	public  static int W=40;
	public  static int H=40;
	
	//�Ƿ����ƶ���
	private boolean bL=false,bR=false,bU=false,bD=false;
	
	//̹�˵�Ĭ�Ϸ���
	private Direction dir=Direction.STOP;
	
	//��Ͳ�ķ���
	private Direction ptdir=Direction.D;
	
	//̹�˵�����
	private int x;
	private int y;
	
	//̹�˵�����
	private int oldX;
	private int oldY;
	
	//���жԷ�������
	TankClient tc=null;
	
	//����̹�˵ı�ʶ
	private boolean good;
	
	//��¼̹����������
	private boolean live=true;
	
	//�����������
	private static Random r=new Random();
	
	//̹������Ĳ���
	private int stop=r.nextInt(12)+3;
	
	//̹�˵�����ֵ
	private int life=100;
	
	//Ѫ��
	private BooldBar bb=new BooldBar();
	
	//�����࣬��������ͼƬ���ڴ���
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	
	//���ͼƬ��ͼƬ����
	public static Image [] tankImage= null;
	
	//��������ͼƬ��mapӳ��
	public static Map<String ,Image> imgs=new HashMap<String, Image>();
	
	//��̬�����
	static {
		
		//��ʼ��ͼƬ����
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
		
		//���ӳ��
		imgs.put("L", tankImage[0]);
		imgs.put("R", tankImage[1]);
		imgs.put("U", tankImage[2]);
		imgs.put("D", tankImage[3]);
		imgs.put("UL", tankImage[4]);
		imgs.put("UR", tankImage[5]);
		imgs.put("DL", tankImage[6]);
		imgs.put("DR", tankImage[7]);
	};	
	
	

	//������
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
	
	
	//��ͼ
	public void draw(Graphics g) {
		
		//�ж��Ƿ���ţ������Ƴ�����
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		//����ǺõĻ���Ѫ��
		if(good) {
			bb.draw(g);
		}
		
		//̹���ƶ�
		move();
		
		//������Ͳ�ķ��򣬻���̹�˵ķ���
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

	//̹�˵Ŀ���
	public void  fire() {
		
		//������ˣ����Ὺ��
		if(!live) {
			return ;
		}
		
		//���һ���µ��ڵ�
		tc.missiles.add(new Missile(x+W/2-Missile.W/2, y+W/2-Missile.H/2, good,ptdir,this.tc));
	}
	
	//�ƶ�������ڵ�
	public void  fire(Direction dir1) {
		if(!live) {
			return ;
		}
		tc.missiles.add(new Missile(x+W/2-Missile.W/2, y+W/2-Missile.H/2, good,dir1,this.tc));
	}
	
	//�����ڵ�
	public void superFire() {
		if(!live) {
			return ;
		}
		//�����������
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++) {
			fire(dirs[i]);
		}
	}
	
	//̹�˵��ƶ�����
	public void move() {
		//������һ�ε�λ�ã�����װǽʱ������
		oldX=x;
		oldY=y;
		
		//�ж��ƶ�
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
		
		//�ж���Ͳ�ķ���
		if(this.dir !=Direction.STOP) {
			this.ptdir=this.dir;
		}
		
		//�ж�̹�˵�λ�ã�����̹�˳���
		if(x<5) x=5;
		if(y<30) y=30;
		if(x+Tank.W+5>TankClient.W) x= TankClient.W-Tank.W-5;
		if(y+Tank.H+5>TankClient.H) y= TankClient.H-Tank.H-5;
		
		//�����˵�̹�������������
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
	
	//ȷ��̹�˵ķ���
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
	
	//���̰�ѹ����
	public void keyPress(KeyEvent e) {
		
		//�õ�����ֵ
		int key=e.getKeyCode();
		
		//�жϰ���ֵ
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
		
		//ȷ��λ��
		locateDirection();
	}

	//����ɿ������ļ���
	public void keyRelease(KeyEvent e) {
		
		//��ȡ�ɿ�������ֵ
		int key=e.getKeyCode();
		
		//�ж��ɿ�������ֵ
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
		
		//�жϷ���
		locateDirection();
	}
	
	//�����ײ���
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
	
	//���̹��ײǽ������
	public boolean hitWall(Wallet w) {
		if(this.live&&getRec().intersects(w.getRec())) {
			stay();
			return true;
		}
		return false;
	}
	
	//���̹��ײǽ������
	public boolean hitWall(List<Wallet> wallets) {
		for(int i=0;i<wallets.size();i++) {
			if(hitWall(wallets.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	//���̹�˿��Դӱ��̹�����Ϲ�ȥ������
	public boolean hitTank(Tank t) {
		if(this.live&&getRec().intersects(t.getRec())&&isLive()&&this!=t) {
			stay();
			t.stay();
			return true;
		}
		return false;
	}
	
	//���̹�˿��Դӱ��̹�����Ϲ�ȥ������
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
	
	//�ڲ��࣬Ѫ����
	public class BooldBar{
		
		//��ͼ����
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
	
	//ײ��Ѫ����Ѫ��
	public boolean hitBlood(Blood b) {
		if(good&&this.live&&getRec().intersects(b.getRec())&&b.isLive()) {
			//װ����Ѫ����ʧ
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
