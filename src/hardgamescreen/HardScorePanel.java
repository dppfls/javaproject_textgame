package hardgamescreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class HardScorePanel extends JPanel {
	private String id;

	private int score = 0;
	private int level = 1;
	private JLabel textLabel = null;
	private JLabel scoreLabel = new JLabel("0", SwingConstants.CENTER); // 기본점수.
	private JLabel levelLabel = new JLabel("Level " + level);
	private JButton startButton = new JButton("시작");
	private JButton resetButton = new JButton("리셋");
	private HardGamePanel gamePanel_hard;
	JLabel lblNewLabel = new JLabel("New label");

	public void setGamePanel(HardGamePanel gamePanel) {
		this.gamePanel_hard = gamePanel;
	}

	// ScorePanel() 생성자
	public HardScorePanel() {
		this.setBackground(new Color(00, 102, 51));

		textLabel = new JLabel("점수 : ");
		textLabel.setForeground(Color.WHITE);
		setLayout(null);

		textLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		add(textLabel);

		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 22));
		scoreLabel.setSize(80, 31);
		scoreLabel.setLocation(139, 32);
		add(scoreLabel); // scorePanel에 붙이기

		levelLabel.setForeground(Color.WHITE);
		levelLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		levelLabel.setSize(117, 45);
		levelLabel.setLocation(75, 250);
		add(levelLabel); // scorePanel에 붙이기

		startButton.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		startButton.setBounds(12, 117, 195, 45);

		// 버튼 배경색 설정 (하얀색)
		startButton.setBackground(Color.WHITE);

		// 버튼 테두리 설정 (검은색, 두께 2)
		startButton.setBorder(new LineBorder(Color.BLACK, 2));

		add(startButton);

		textLabel.setSize(135, 32);
		textLabel.setLocation(12, 32);
		add(textLabel); // scorePanel에 붙이기

		resetButton.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		resetButton.setBounds(12, 182, 195, 45);

		// 버튼 테두리 설정 (검은색, 두께 2)
		resetButton.setBorder(new LineBorder(Color.BLACK, 2));

		// 버튼 배경색 설정 (하얀색)
		resetButton.setBackground(Color.WHITE);

		add(resetButton);
		resetButton.setEnabled(false);

		startButton.addActionListener(new StartAction());
		resetButton.addActionListener(new ResetAction());
	}

	// 점수 증가
	public synchronized void increase(int n) {
		score += n;
		scoreLabel.setText(Integer.toString(score));
	}

	// 점수 감소
	public void decrease(int n) {
		score -= n;
		scoreLabel.setText(Integer.toString(score));
	}

	public synchronized void setLevel(int level) {
		this.level = level;
		levelLabel.setText("Level " + level);
	}

	public synchronized int getScore() {
		return score;
	}

	// 레벨과 점수를 초기화
	public void resetLevelAndPoint() {
		this.score = 0;
		this.level = 1;
		levelLabel.setText("Level " + 1);
		scoreLabel.setText("0");
	}

	// 시작 버튼을 활성화
	public void setEnabledStartButton() {
		System.out.println("startButton enabled");
		startButton.setEnabled(true);
	}

	// 시작 버튼 액션 리스너
	public class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel_hard.startGame();
			startButton.setEnabled(false);
			resetButton.setEnabled(true);
		}
	}

	// 게임 리셋
	private class ResetAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gamePanel_hard.resetGame();
			startButton.setEnabled(true);
			resetButton.setEnabled(false);
		}
	}
}
