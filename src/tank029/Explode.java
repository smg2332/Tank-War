package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


/**
 * ̹������ı�ը��
 * ���ӵ���̹��ʱ���ᷢ����ը��������ը����
 * @author ��
 *
 */
public class Explode {
	//��ը���������
	private int x;
	
	private int y;
	//�жϱ�ը�����Ƿ����
	private boolean live=true;
	//�жϱ�ը���е��ڼ���
	private int stop=0;
	//���ж��������
	private TankClient tc;
	//�жϱ�ը�Ƿ��ʼ��
	private static boolean init=false;
	//�����࣬��������ͼƬ���ڴ�֮��
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	//��̬����飬���ر�ը��ͼƬ
	public static Image [] imgs= {
			tk.getImage(Explode.class.getClassLoader().getResource("image/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("image/10.gif")),
	};
	
	//���췽��
	public Explode(int x, int y, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	//��ͼ����
	public void draw(Graphics g) {
		//�ж��Ƿ��ʼ�������û���ȳ�ʼ��һ�ߣ������Ϊ�첽�����µ�һ�α�ը����ʾ����
		if(!init) {
			for(int i=0;i<imgs.length;i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
		}
		
		//�ж�̹���Ƿ񻹻��ţ����û���Ƴ�̹�˶��󣬲�ִ�л�ͼ����
		if(!live){
			tc.explodes.remove(this);
			return ;
		}
		
		//�ж�ִ�е��ڼ�����һ�߽���ʱˢ�´���
		if(stop==imgs.length) {
			stop=0;
			live=false;
			return ;
		}
		
		//������ըͼƬ
		g.drawImage(imgs[stop], x, y, null);
		
		//����Ӽ�
		stop++;
		
	}
	
	
	
	
}
