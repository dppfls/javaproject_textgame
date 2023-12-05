package hardgamescreen;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

public class HardGameFrame extends JFrame {
	private String id = "";

	public static Clip clip;

	private JMenuItem exitItem = new JMenuItem("게임 종료");
	private JMenuItem audioStartItem = new JMenuItem("배경음악 키기");
	private JMenuItem audioStopItem = new JMenuItem("배경음악 끄기");
	private JMenuItem audioControlItem = new JMenuItem("음량 조절");
	private JMenuItem scoreItem = new JMenuItem("점수 기록");

	private HardScorePanel scorePanel = new HardScorePanel();
	private EditPanel editPanel = new EditPanel();
	private HardGamePanel hardgamePanel = new HardGamePanel(scorePanel, editPanel);
	private HardMediatorPanel mediatorPanel;

	public HardGameFrame(String id) {
		mediatorPanel = new HardMediatorPanel();
		mediatorPanel.setGamePanelEasy(hardgamePanel);
		mediatorPanel.setScorePanel(scorePanel);
		hardgamePanel.setScorePanel(scorePanel);
		scorePanel.setGamePanel(mediatorPanel.getGamePanel_easy());

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/icon_su.jpg");// 이모티콘
		setIconImage(img);

		this.id = id;
		editPanel.setId(id);
		setTitle("수룡이 타이핑 게임");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 이 창만 꺼지기
		setSize(800, 600);

		splitPane(); // JSplitPane을 생성하여 content pane에 CENTER에 부착
		makeMenu();
		setLocationRelativeTo(null); // 프레임을 화면의 정 가운데에 위치시킴
		setResizable(false); // 프레임의 크기를 사용자가 함부러 변경할 수 없음
		setVisible(true);
	}

	// hard게임 패널
	public HardGamePanel getGamePanel() {
		return hardgamePanel;
	}

	// ContentPane을 SplitPanne으로 나눔
	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		// ContentPane()은 BorderLayout이 기본
		getContentPane().add(hPane, BorderLayout.CENTER);

		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(550);

		// 못 움직이게 고정 - 이건 모든 컴포넌트에 다 적용됨
		hPane.setEnabled(false);

		// 왼쪽에 게임패널 넣기
		hPane.setLeftComponent(hardgamePanel);

		// 왼쪽에 붙일 패널과 오른족에 붙일 패널을 생성
		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setEnabled(false);

		// EditPanel붙이기
		pPane.setTopComponent(editPanel);
		// ScorePanel붙이기
		pPane.setBottomComponent(scorePanel);

		// 오른쪽에 pPane 넣기
		hPane.setRightComponent(pPane);
	}

	// 메뉴바
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		// 게임 관련 메뉴
		JMenu gameMenu = new JMenu("게임");
		gameMenu.add(exitItem);

		// 소리 관련 메뉴
		JMenu soundMenu = new JMenu("설정");
		soundMenu.add(audioStartItem);
		soundMenu.add(audioStopItem);
		soundMenu.add(audioControlItem);

		// 점수 관련 메뉴
		JMenu scoreMenu = new JMenu("점수");
		scoreMenu.add(scoreItem);

		menuBar.add(gameMenu);
		menuBar.add(soundMenu);
		menuBar.add(scoreMenu);

		// 액션리스너
		exitItem.addActionListener(new ExitAction());

		audioStopItem.addActionListener(new audioStopAction());
		audioStartItem.addActionListener(new audioStartAction());
		audioControlItem.addActionListener(new audioAction());

		scoreItem.addActionListener(new ScoreAction());

		audioStartItem.setEnabled(false);
	}

	// 액션 리스너 작성
	// 게임 종료
	private class ExitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	private class audioStopAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clip.stop();
			audioStopItem.setEnabled(false);
			audioStartItem.setEnabled(true);
		}
	}

	private class audioStartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			clip.loop(clip.LOOP_CONTINUOUSLY);
			audioStartItem.setEnabled(false);
			audioStopItem.setEnabled(true);
		}
	}

	private class audioAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ControlSoundFrame controlSoundFrame = new ControlSoundFrame();
		}
	}

	// 점수 정보
	private class ScoreAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			HardScoreFrame scoreFrame = new HardScoreFrame();// 점수 프레임 열리게
		}
	}
}
