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
	//ÿ���ƶ��ľ���
	public static final int XDISTANCE=15;
	public static final int YDISTANCE=15;
	//�뾶
	public  static int W=10;
	public  static int H=10;
	//����
	private int x;
	private int y;
	//����
	Direction dir;
	//�ж��Ƿ����
	private boolean live=true;
	//���жԷ�������
	TankClient tc;
	//�ж��ӵ��ĺû�
	private boolean good ;
	
	//����̹���Ƿ����
	public boolean isLive() {
		return live;
	}

	//�����࣬��������ͼƬ
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	
	//���ͼƬ������
	public static Image [] missileImage= null;
	
	//�Ѷ�ӦͼƬ���ַ�����Ӧ����
	public static Map<String ,Image> imgs=new HashMap<String, Image>();
	
	//��̬����飬��ʼ��ͼƬ����
	static {
		//����ͼƬ����
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
		//��ͼƬ������ӵ�map�У����ַ�����ͼƬ�����Ӧ
		imgs.put("L", missileImage[0]);
		imgs.put("R", missileImage[1]);
		imgs.put("D", missileImage[2]);
		imgs.put("U", missileImage[3]);
		imgs.put("UL", missileImage[4]);
		imgs.put("UR", missileImage[5]);
		imgs.put("DL", missileImage[6]);
		imgs.put("DR", missileImage[7]);
	};	
	
	
	//��ʼ��
	public Missile(int x, int y, Direction dir) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	//��ʼ��
	public Missile(int x, int y,boolean good, Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	
	//��ͼ
	public void draw(Graphics g) {
		//�ƶ��ӵ�
		move();
		
		//�����ӵ���ͼƬ
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
	
	//�ƶ��ӵ�
	public void move() {
		//�ж��ӵ��Ƿ���ţ������Ƴ��ӵ�����ִ���ƶ�����
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		//�ж��ӵ����ƶ�
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
		
		//����ӵ����磬���ӵ�����
		if(x<0||y<0||x>TankClient.W||y>TankClient.H) {
			live=false;
			
		}
	}
	
	//�ж���ײ����
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
	
	//�ӵ�ײ��һ��̹��
	public boolean hitTank(Tank t) {
		//�ж��ӵ��Ƿ���ţ��ж��Ƿ���ײ���ж�̹���Ƿ���ţ��ж��ӵ���̹�˺ͺû�
		if(this.live&&this.getRec().intersects(t.getRec())&&t.isLive()&&this.good!=t.isGood()) {
			//���Լ���̹�˼�Ѫ�����˵�̹������
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0) {
					t.setLive(false);
				}
			}else {
				t.setLive(false);
			}
			//�ӵ�����̹��ʱ������ը
			Explode e=new Explode(x,y,tc);
			//��ը������ӱ�ը����
			tc.explodes.add(e);
			//�ӵ�����
			this.live=false;
			return true;
		}
		return false;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}

	//�ж��ӵ��Ƿ�ײ��һȺ̹��
	public boolean hitTank(List <Tank> tanks) {
		for(int i=0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i))) {
				
				return true;
			}
		}
		return false;
	}
	
	//�ӵ�ײǽ��ʧ
	public boolean hitWall(Wallet w) {
		//�ж��ӵ��Ƿ���ţ��Ƿ�ײ��
		if(this.live&&getRec().intersects(w.getRec())) {
			this.live=false;
			return true;
		}
		return false;
	}
	
	//�ж��ӵ��Ƿ�ײ��һ��ǽ
	public boolean hitWall(List<Wallet> wallets) {
		for(int i=0;i<wallets.size();i++) {
			if(hitWall(wallets.get(i))) {
				return true;
			}
		}
		return false;
	}
}
