package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * ̹���е�Ѫ�飬�࣬���˿��Ի�Ѫ
 * @author ��
 *
 */
public class Blood {
	
	//�뾶
	public  static int W=10;
	public  static int H=10;
	//����
	private int x=500;
	private int y=300;
	//ִ�е��ڼ���
	private int step=0;
	//�ж�Ѫ���Ƿ��ǻ���
	private boolean live=true;
	
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	//���ж��������
	TankClient tc;
	
	//Ѫ���˶��Ĺ켣
	private int[] [] pos= {
			{500,460},{500,450},{500,440},{500,430},{500,420},{500,410},
			{500,400},
			{500,410},{500,420},{500,430},{500,440},{500,450},{500,460},
	};
	
	//���췽��
	public Blood(TankClient tc) {
		this.tc=tc;
	}
	
	//��ͼ
	public void draw(Graphics g) {
		//���ж��Ƿ���ţ�������˲�����ֱ�����
		if(!live) {
			return ;
		}
		
		Color c=g.getColor();
		//������ɫ��С
		g.setColor(Color.magenta);
		g.fillRect(x, y, W, H);
		//�ƶ�
		move();
		g.setColor(c);
	}

	//���չ켣һ�������ƶ�
	private void move() {
		step++;
		//����˶���֮�󣬼���������
		if(step==pos.length) {
			step=0;
		}
		x=pos[step][0];
		y=pos[step][1];
		
	}
	
	//�ж�λ��
	public Rectangle getRec() {
		return new Rectangle(x,y,W,H);
	}
		
	

	
	
}
