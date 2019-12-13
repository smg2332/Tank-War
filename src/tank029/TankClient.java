package tank029;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
/**
 *F2重新开始
 *没坦克添加坦克
 * @author mi
 *
 */
public class TankClient extends Frame{
	
	
	//尺寸
	public static final int W=800;
	public static final int H=600;
	
	
	//双缓存
	private Image offScreenImage=null;
	//自己控制的坦克
	Tank tank=new Tank(60, 60,true, Direction.STOP,this);
	
	//敌人的坦克
	List<Tank> tanks=new ArrayList<Tank>();
	//发生的爆炸
	List<Explode> explodes=new ArrayList<Explode>();
	//打出去的子弹
	List <Missile> missiles=new ArrayList<Missile>();
	//创建一个墙
	List<Wallet> wallets=new ArrayList<Wallet>();
	//创建血包
	Blood blood =new Blood(this);
	
	
	
	//方法入口
	public void launch() {
		
		//建立标题
		setTitle("TankWar");
		
		//建立窗口
		setBounds(100,100,W,H);
		
		//设置可见性
		setVisible(true);
		
		//设置背景色
		setBackground(Color.black);
		
		//添加窗口监听
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//设置窗口不可改变
		setResizable(false);
		
		//添加键盘监听
		addKeyListener(new KeyMonitor());
		
		//获取配置文件中的数
		int initTankCount=Integer.parseInt(PropertyM.getProperty("initTankCount"));
		
		//创建敌方坦克
		for(int i=0;i<initTankCount;i++) {
			tanks.add(new Tank(+80*(i+1),450,false,Direction.STOP,this));
		}
		
		//创建墙
		wallets.add(new Wallet(400, 100, 20,100,this));
		wallets.add(new Wallet(100, 400, 100,20,this));
		wallets.add(new Wallet(300, 300, 20,100,this));
		wallets.add(new Wallet(200, 100, 100,20,this));
		wallets.add(new Wallet(600, 100, 20,100,this));
		wallets.add(new Wallet(200, 500, 100,20,this));
		wallets.add(new Wallet(600, 400, 100,20,this));
		wallets.add(new Wallet(200, 400, 20,100,this));
		wallets.add(new Wallet(100, 300, 100,20,this));
		wallets.add(new Wallet(600, 400, 100,20,this));
		wallets.add(new Wallet(500, 500, 20,100,this));
		wallets.add(new Wallet(400, 500, 100,20,this));
		
		
		//刷新线程启动
		new Thread(new PaintThread()).start();
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		//
		Color c=g.getColor();
		
		//画出来墙
		for(int i=0;i<wallets.size();i++) {
			Wallet wallet=wallets.get(i);
			wallet.draw(g);
		}
		
		//获取配置文件中的数据
		int reflushTankCount=Integer.parseInt(PropertyM.getProperty("reflushTankCount"));
		
		//当敌人坦克死完后，重新话坦克
		if(tanks.size()<=0) {
			for(int i=0;i<reflushTankCount;i++) {
				tanks.add(new Tank(+80*(i+1),450,false,Direction.STOP,this));
			}
		}
		
		//画出血包
		blood.draw(g);
		
		//画出自己的坦克
		tank.draw(g);
		
		
		tank.hitWall(wallets);
		
		//判断是否打击到别的坦克
		tank.hitTanks(tanks);
		
		//判断是否撞击血包
		tank.hitBlood(blood);
		
		//画出计数器
		g.setColor(Color.white);
		g.drawString("missiles count"+missiles.size(), 60,40);
		g.drawString("explodes count"+explodes.size(), 60,60);
		g.drawString("tanks count"+tanks.size(), 60,80);
		g.drawString("your life"+tank.getLife(), 60,100);
		
		//画出子弹调用子弹的射击方法
		for(int i=0;i<missiles.size();i++) {
//			if(!m.isLive()) missiles.remove(i);
			Missile m=missiles.get(i);
			m.draw(g);
			m.hitTank(tanks);
			m.hitTank(tank);
			m.hitWall(wallets);
		}
		
		//画出爆炸
		for(int i=0;i<explodes.size();i++) {
			Explode e=explodes.get(i);
			e.draw(g);			
		}
		
		//画出敌方坦克
		for(int i=0;i<tanks.size();i++){
			Tank enmTank=tanks.get(i);
			enmTank.hitWall(wallets);
			enmTank.draw(g);
			enmTank.hitTanks(tanks);
			enmTank.hitTank(tank);
		}
		
		//
		g.setColor(c);
	}
	
	//双缓存结构，避免闪烁
	public void update(Graphics g) {
		if(offScreenImage==null) {
			//创建缓存照片
			offScreenImage=this.createImage(W,H);
		}
		//获取缓存画笔
		Graphics gOffScr=offScreenImage.getGraphics();
		
		Color c=gOffScr.getColor();
		//绘制缓存图片
		gOffScr.setColor(Color.black);
		gOffScr.fillRect(0, 0,W,H);
		gOffScr.setColor(c);
		//调用paint方法
		paint(gOffScr);
		//覆盖缓存
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	
	private class PaintThread implements Runnable{

		public void run() {
			
			//每隔五十秒重新绘制
			while(true) {
				repaint();
				try {
					
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	//添加键盘监听
	public class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			tank.keyPress(e);
			
		}
		public void keyReleased(KeyEvent e) {
			tank.keyRelease( e);
		}
	}
	
	
	
	
	
	public static void main(String[] args) {
		TankClient tc=new TankClient();
		tc.launch();
	}
}








