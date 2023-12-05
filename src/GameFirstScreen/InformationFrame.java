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
		Image img = toolkit.getImage("resource/img/icon_su.jpg");// 이모티콘
		setIconImage(img);
		setTitle("게임 설명");
		setSize(590, 380);
		setContentPane(txtPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기
		setResizable(false); // 함부로 크기를 변경하지 않게
		setVisible(true);
	}

	class TextPanel extends JPanel {
		public void paintComponent(Graphics g) {// 게임설명 간단히
			super.paintComponent(g);
			String[] lines = { "본 게임은 산성비 게임을 JAVA Swing으로 구현했습니다.", "", "<게임 방법>",
					"1. 내려오는 글씨를 키보드로 입력하면 점수를 획득할 수 있다.", "", "2. 글씨를 잘못 입력할 경우 생명이 감소 됩니다. 생명이 모두 줄어들면 게임이 종료됩니다.",
					"", "3. 글씨 색마다 기능이 다릅니다.", "   - 하얀색: (점수:10점) 일반 단어 ", "   - 보라색: (점수:20점) 모든 단어 3초간 정지 ",
					"   - 빨간색: (점수:50점) 모든 단어 삭제 ", "   - 초록색: (점수:10점) 단어 내려오는 속도가 빨라짐 ", "",
					"4. 점수가 100점이 넘게 되면 피버타임으로 점수를 두배로 획득할 수 있습니다. (400점 이후엔 종료)", "",
					"5. 생명아이템을 클릭하면 생명 하나를 얻을 수 있습니다. (랜덤한 시간에 나타나고, 5초 후 사라짐)", "",
					"6. 하드모드에선 20초마다 한 번씩 검은색 화면이 뜨기 때문에 글자를 기억해서 점수를 얻어야 합니다." };

			int y = 40; // 시작 위치
			int lineHeight = 15;// 줄 간격

			for (String line : lines) {
				g.drawString(line, 15, y);
				y += lineHeight;
			}
		}

	}
}
