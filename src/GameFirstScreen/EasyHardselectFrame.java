package GameFirstScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import easygamescreen.LoginFrame_easy;
import hardgamescreen.LoginFrame_hard;

public class EasyHardselectFrame extends JFrame {

	public EasyHardselectFrame() {
		setTitle("모드 설정");
		setSize(400, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 현재 프레임만 종료

		setLayout(null);
		Font malangFont = new Font("한컴 말랑말랑 Regular", Font.PLAIN, 14);

		// Easy 모드 버튼
		JButton easyButton = new JButton("이지 모드");// 생명 5개, 속도 느림
		easyButton.setBounds(50, 50, 150, 50);
		easyButton.setFont(malangFont);

		// 버튼 테두리 설정 (검은색, 두께 2)
		easyButton.setBorder(new LineBorder(Color.BLACK, 2));
		// 버튼 배경색 설정 (하얀색)
		easyButton.setBackground(Color.WHITE);

		add(easyButton);

		// Hard 모드 버튼
		JButton hardButton = new JButton("하드 모드");// 생명 3개, 속도 빠름
		hardButton.setBounds(200, 50, 150, 50);
		hardButton.setFont(malangFont);

		// 버튼 테두리 설정 (검은색, 두께 2)
		hardButton.setBorder(new LineBorder(Color.BLACK, 2));
		// 버튼 배경색 설정 (하얀색)
		hardButton.setBackground(Color.WHITE);

		add(hardButton);

		// 버튼에 액션 리스너 추가
		easyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 이지모드 선택
				System.out.println("Easy mode selected");
				LoginFrame_easy loginFrame = new LoginFrame_easy();
				setVisible(false); // 로그인 프레임으로 넘어가면 창을 보이지 않게 한다
			}
		});

		hardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Handle hard mode selection
				System.out.println("Hard mode selected");
				LoginFrame_hard loginFrame = new LoginFrame_hard();
				setVisible(false); // 로그인 프레임으로 넘어가면 창을 보이지 않게 한다.
			}
		});

		setLocationRelativeTo(null); // 프레임을 중앙으로 보이게 한다

		// Set the icon for the JFrame
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/icon_su.jpg");// 이모티콘
		setIconImage(img);

		setVisible(true);
	}
}
