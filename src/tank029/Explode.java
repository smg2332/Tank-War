package tank029;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


/**
 * 坦克里面的爆炸类
 * 当子弹打到坦克时，会发生爆炸，创建爆炸对象
 * @author 程
 *
 */
public class Explode {
	//爆炸对象的坐标
	private int x;
	
	private int y;
	//判断爆炸对象是否活着
	private boolean live=true;
	//判断爆炸进行到第几步
	private int stop=0;
	//持有对象的引用
	private TankClient tc;
	//判断爆炸是否初始化
	private static boolean init=false;
	//工具类，帮助加载图片到内存之中
	private static Toolkit tk=java.awt.Toolkit.getDefaultToolkit();
	//静态代码块，加载爆炸的图片
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
	
	//构造方法
	public Explode(int x, int y, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	//画图方法
	public void draw(Graphics g) {
		//判断是否初始化，如果没有先初始化一边，解决因为异步而导致第一次爆炸不显示问题
		if(!init) {
			for(int i=0;i<imgs.length;i++) {
				g.drawImage(imgs[i], -100, -100, null);
			}
		}
		
		//判断坦克是否还活着，如果没有移除坦克对象，不执行画图方法
		if(!live){
			tc.explodes.remove(this);
			return ;
		}
		
		//判断执行到第几步，一边结束时刷新次数
		if(stop==imgs.length) {
			stop=0;
			live=false;
			return ;
		}
		
		//画出爆炸图片
		g.drawImage(imgs[stop], x, y, null);
		
		//步骤加加
		stop++;
		
	}
	
	
	
	
}
