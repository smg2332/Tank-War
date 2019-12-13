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
 *F2���¿�ʼ
 *û̹�����̹��
 * @author mi
 *
 */
public class TankClient extends Frame{
	
	
	//�ߴ�
	public static final int W=800;
	public static final int H=600;
	
	
	//˫����
	private Image offScreenImage=null;
	//�Լ����Ƶ�̹��
	Tank tank=new Tank(60, 60,true, Direction.STOP,this);
	
	//���˵�̹��
	List<Tank> tanks=new ArrayList<Tank>();
	//�����ı�ը
	List<Explode> explodes=new ArrayList<Explode>();
	//���ȥ���ӵ�
	List <Missile> missiles=new ArrayList<Missile>();
	//����һ��ǽ
	List<Wallet> wallets=new ArrayList<Wallet>();
	//����Ѫ��
	Blood blood =new Blood(this);
	
	
	
	//�������
	public void launch() {
		
		//��������
		setTitle("TankWar");
		
		//��������
		setBounds(100,100,W,H);
		
		//���ÿɼ���
		setVisible(true);
		
		//���ñ���ɫ
		setBackground(Color.black);
		
		//��Ӵ��ڼ���
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//���ô��ڲ��ɸı�
		setResizable(false);
		
		//��Ӽ��̼���
		addKeyListener(new KeyMonitor());
		
		//��ȡ�����ļ��е���
		int initTankCount=Integer.parseInt(PropertyM.getProperty("initTankCount"));
		
		//�����з�̹��
		for(int i=0;i<initTankCount;i++) {
			tanks.add(new Tank(+80*(i+1),450,false,Direction.STOP,this));
		}
		
		//����ǽ
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
		
		
		//ˢ���߳�����
		new Thread(new PaintThread()).start();
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		//
		Color c=g.getColor();
		
		//������ǽ
		for(int i=0;i<wallets.size();i++) {
			Wallet wallet=wallets.get(i);
			wallet.draw(g);
		}
		
		//��ȡ�����ļ��е�����
		int reflushTankCount=Integer.parseInt(PropertyM.getProperty("reflushTankCount"));
		
		//������̹����������»�̹��
		if(tanks.size()<=0) {
			for(int i=0;i<reflushTankCount;i++) {
				tanks.add(new Tank(+80*(i+1),450,false,Direction.STOP,this));
			}
		}
		
		//����Ѫ��
		blood.draw(g);
		
		//�����Լ���̹��
		tank.draw(g);
		
		
		tank.hitWall(wallets);
		
		//�ж��Ƿ��������̹��
		tank.hitTanks(tanks);
		
		//�ж��Ƿ�ײ��Ѫ��
		tank.hitBlood(blood);
		
		//����������
		g.setColor(Color.white);
		g.drawString("missiles count"+missiles.size(), 60,40);
		g.drawString("explodes count"+explodes.size(), 60,60);
		g.drawString("tanks count"+tanks.size(), 60,80);
		g.drawString("your life"+tank.getLife(), 60,100);
		
		//�����ӵ������ӵ����������
		for(int i=0;i<missiles.size();i++) {
//			if(!m.isLive()) missiles.remove(i);
			Missile m=missiles.get(i);
			m.draw(g);
			m.hitTank(tanks);
			m.hitTank(tank);
			m.hitWall(wallets);
		}
		
		//������ը
		for(int i=0;i<explodes.size();i++) {
			Explode e=explodes.get(i);
			e.draw(g);			
		}
		
		//�����з�̹��
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
	
	//˫����ṹ��������˸
	public void update(Graphics g) {
		if(offScreenImage==null) {
			//����������Ƭ
			offScreenImage=this.createImage(W,H);
		}
		//��ȡ���滭��
		Graphics gOffScr=offScreenImage.getGraphics();
		
		Color c=gOffScr.getColor();
		//���ƻ���ͼƬ
		gOffScr.setColor(Color.black);
		gOffScr.fillRect(0, 0,W,H);
		gOffScr.setColor(c);
		//����paint����
		paint(gOffScr);
		//���ǻ���
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	
	private class PaintThread implements Runnable{

		public void run() {
			
			//ÿ����ʮ�����»���
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
	
	//��Ӽ��̼���
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








