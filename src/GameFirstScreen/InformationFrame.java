package GameFirstScreen;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class InformationFrame extends JFrame {
	private TextPanel txtPanel = new TextPanel();

	public InformationFrame() {// 게임설명 폼 
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/favcion_bugi.png");// 이모티콘
		setIconImage(img);
		setTitle("게임 설명");
		setSize(288, 142);
		setContentPane(txtPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기
		setResizable(false); // 함부로 크기를 변경하지 않게.
		setVisible(true);
	}

	class TextPanel extends JPanel {
		public void paintComponent(Graphics g) {// 게임설명간단히
			super.paintComponent(g);
			g.drawString("With JAVA Swing", 15, 30);
			g.drawString("A+을 자바팀", 65, 50);

		}
	}
}