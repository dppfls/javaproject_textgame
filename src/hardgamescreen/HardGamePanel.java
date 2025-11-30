package hardgamescreen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class HardGamePanel extends JPanel {
	private boolean showRectangle = false;
	// Audio 관련 클립
	private Clip effectSoundClip; // 정답
	private Clip effectSoundClip2; // 오답
	private Clip gameOverClip; // 게임오버
	private Clip lifeMinusClip; // 생명 감소

	private JTextField input = new JTextField(25);
	private JLabel timeLabel = new JLabel("Time: 02:00");
	private ImageIcon gameBackGround = new ImageIcon("resource/img/HardGameBackground.jpg");

	private Vector<TextObject> textArr = new Vector<TextObject>(50);
	private HardScorePanel scorePanel = null;
	private EditPanel editPanel = null;
	private TextSource textSource = new TextSource(); // 단어 생성
	private int number = 0;

	private int life = 3; // hard 초기 라이프

	private Vector<LifeIcon> lifeIcons = new Vector<LifeIcon>();
	private Vector<LifePanel> lifePanels = new Vector<LifePanel>();

	private Timer itemTimer;// 타이머
	private ImageIcon itemIcon = new ImageIcon("resource/img/item.png"); // 생명 아이템 사진

	public int pixel = 10;
	public int speed = 15;
	public int gameLevel = 1;
	private JLabel feverLabel = new JLabel("Fever Time!");// 피버타임 글씨

	// 스레드 재시작 하기 위해서 벡터를 사용.
	private int threadCount = 0;
	private Vector<RainThread> rainThreads = new Vector<RainThread>();
	private Vector<TimeThread> timeThreads = new Vector<TimeThread>();
	private Vector<AudioThread> audioThreads = new Vector<AudioThread>();
	private int threadStateFlag = 2;
	private AudioThread audioThread = new AudioThread();
	private EndGameFrame endGameFrame = new EndGameFrame();

	public void setScorePanel(HardScorePanel scorePanel) {
		this.scorePanel = scorePanel;
		System.out.println(scorePanel.getName() + " ScorePanel 만들어짐." + scorePanel.getScore());
	}

	private void saveRecord() {
		int scoreTmp = rainThreads.get(threadCount).getScore();
		String idTmp = editPanel.getId();

		System.out.println(scoreTmp + " " + idTmp);
		endGameFrame = new EndGameFrame(scoreTmp, idTmp);
	}

	private void makeThreads() {
		RainThread rainThread = new RainThread();
		TimeThread timeThread = new TimeThread();
		AudioThread audioThread = new AudioThread();

		rainThreads.add(rainThread);
		timeThreads.add(timeThread);
		audioThreads.add(audioThread);
	}

	private RainThread getRainThread() {
		return rainThreads.get(threadCount);
	}

	public TimeThread getTimeThread() {
		return timeThreads.get(threadCount);
	}

	private AudioThread getAudioThread() {
		return audioThreads.get(threadCount);
	}

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		g.drawImage(gameBackGround.getImage(), 0, 0, d.width, d.height, null);

		if (showRectangle) {
			// 블랙아웃을 위한 검은색 사각형 그리기
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

	}

	private String setTime(int sec) {// 시간설정
		int min;
		min = sec / 60;
		sec = sec % 60;
		min = min % 60;
		return "Time:" + min + ":" + sec;
	}

	class RainThread extends Thread { // 스레드 상속
		private int score = 0;

		public int getScore() {
			return score;
		}

		synchronized public void run() {
			try {
				while (true) {
					// game over
					if (Thread.interrupted()) {
						break;
					}
					if (life <= 0) {// 생명이 0이되면 게임종료
						System.out.println("Game over");
						resetGame();// 리셋
						break;
					}
					score = scorePanel.getScore();
					if (score > 100 && score <= 200) {
						setLevel(2);
					} else if (score > 200 && score <= 300) {
						setLevel(3);
					} else if (score > 300 && score <= 400) {
						setLevel(4);
					} else if (score > 400 && score <= 500) {
						setLevel(5);
					} else if (score > 500 && score <= 600) {
						setLevel(6);
					} else if (score > 600 && score <= 700) {
						setLevel(7);
					} else if (score > 700 && score <= 800) {
						setLevel(8);
					} else if (score > 800 && score <= 900) {
						setLevel(9);
					} else if (score > 900 && score <= 1000) {
						setLevel(10);
					}

					setText();
					changePosition();
					HardGamePanel.this.repaint();

					if (threadStateFlag >= 200) {
						Thread.sleep(3000); // 3초 정지
						threadStateFlag = 0;
					} else if (threadStateFlag < 0) {
						Thread.sleep(500); // 0.5초 정지
					} else {
						threadStateFlag = 0;
						Thread.sleep(1000); // 1초 정지
					}
					System.out.println("ability protocol :" + threadStateFlag);
					threadStateFlag++;
				}
			} catch (InterruptedException e) {
				System.out.println("Thread stop");
				e.printStackTrace();
			}
		}
	}

	// 블랙아웃을 위한 검은색 사각형이 나타나게 하는 메서드
	public void showRectangle() {
		showRectangle = true;
		repaint();

		// 3초 후에 검은색 사각형 숨기기
		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showRectangle = false;
				repaint();
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	// TextOBject를 보이게 하는 메서드
	public void showTextObjects() {
		// TextObject를 나타내는 코드
		for (int i = 0; i < textArr.size(); i++) {
			TextObject tmp = (TextObject) textArr.get(i);
			tmp.setVisible(true);
		}
		repaint();
	}

	// TextOBject를 숨기는 메서드
	public void hideTextObjects() {
		for (int i = 0; i < textArr.size(); i++) {
			TextObject tmp = (TextObject) textArr.get(i);
			tmp.setVisible(false);
		}
		repaint();
		// 3초 후에 다시 나타내기
		Timer showTimer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTextObjects();
			}
		});
		showTimer.setRepeats(false);
		showTimer.start();
	}

	class TimeThread extends Thread {
		private int time = 120; // 게임 시간 2분

		public int getTime() {
			return time;
		}

		public void resetTime() {
			time = 120; // 초기값으로 설정 또는 원하는 값으로 변경
		}

		synchronized public void run() {
			while (true) {
				try {
					if (Thread.interrupted()) {
						break;
					} else if (time <= 0) {
						System.out.println("Game over");
						resetGame(); // 리셋
						break;
					}

					time--;
					timeLabel.setText(setTime(time));
					if (time % 20 == 0) { // 20초마다 한 번씩 블랙아웃이 일어남
						showBlackRectangle();
						hideTextObjects();
					}

					Thread.sleep(1000);

				} catch (InterruptedException e) {
					timeLabel.setText(setTime(0));
					System.out.println("Kill TimeThread");
					e.printStackTrace();
					interrupt();
					break;
				}
			}
		}

		public void showBlackRectangle() {
			showRectangle();
		}

	}

	private void setLevel(int level) {// 레벨이 올라갈수록 속도가 빨라짐
		switch (level) {
		case 1:
			scorePanel.setLevel(1);
			this.speed = 15;
			break;
		case 2:
			scorePanel.setLevel(2);
			this.speed = 17;
			break;
		case 3:
			scorePanel.setLevel(3);
			this.speed = 20;
			break;
		case 4:
			scorePanel.setLevel(4);
			this.speed = 25;
			break;
		case 5:
			scorePanel.setLevel(5);
			this.speed = 30;
			break;
		case 6:
			scorePanel.setLevel(6);
			this.speed = 32;
			break;
		case 7:
			scorePanel.setLevel(7);
			this.speed = 34;
			break;
		case 8:
			scorePanel.setLevel(8);
			this.speed = 36;
			break;
		case 9:
			scorePanel.setLevel(9);
			this.speed = 40;
			break;
		case 10:
			scorePanel.setLevel(10);
			this.speed = 45;
			break;
		}
	}

	private void setText() {
		// System.out.println("SetText");
		String newWord = textSource.get();
		TextObject textTmp = new TextObject(newWord, number);
		// System.out.println(textTmp.getText() + " 단어 출력.");

		for (int i = 0; i < textArr.size(); i++) {
			TextObject tmp = (TextObject) textArr.get(i);
			tmp.setSize(100, 30);

			// 색 설정
			switch (tmp.getAbility()) {
			case 0:
				tmp.setForeground(Color.WHITE);
				break;
			case 1:
				tmp.setForeground(new Color(137, 7, 255));// 보라색
				break;
			case 2:
				tmp.setForeground(new Color(238, 49, 53));// 빨간색
				break;
			case 3:
				tmp.setForeground(new Color(15, 169, 93));// 초록색
				break;
			}
			tmp.setLocation(tmp.getX(), tmp.getY());
			add(tmp);
		}
		textArr.add(textTmp);
		number++;
	}

	public void resetGame() {
		scorePanel.setEnabledStartButton();

		if (itemTimer != null && itemTimer.isRunning()) {
			itemTimer.stop();
		}
		// 아이템 제거
		Component[] components = getComponents();
		for (Component component : components) {
			if (component instanceof JLabel) {
				JLabel label = (JLabel) component;
				if (label.getIcon() == itemIcon) {
					remove(label);
				}
			}
		}

		// life객체 지우기
		LifePanel lifePanel = lifePanels.get(threadCount);
		lifePanel.removeAll();
		remove(lifePanel);
		number = 0;
		life = 3;
		pixel = 10;
		speed = 15;
		gameLevel = 1;
		for (int i = 0; i < textArr.size(); i++) {
			TextObject textTmp = (TextObject) textArr.get(i);
			textTmp.setText("");
		}
		getRainThread().interrupt();
		getTimeThread().interrupt(); // 중단된 스레드에 대해 interrupt 호출
		try {
			getTimeThread().join(); // 스레드의 종료를 기다림
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getTimeThread().resetTime();
		getAudioThread().interrupt();

		scorePanel.resetLevelAndPoint();
		textArr.removeAllElements();
		HardGameFrame.clip.stop();
		loadGameOverEffectAudio();
		saveRecord();
		threadCount++;
	}

	public void startGame() {

		LifePanel lifePanel = new LifePanel(); // 생명 패널
		lifePanels.add(lifePanel);
		add(lifePanel);

		makeThreads();
		getTimeThread().start();
		getRainThread().start();
		System.out.println((threadCount + 1) + "번째 시도 시작.(threadMake)");
		itemTimer.start();

		// 배경음악 시작
		loadAudio("resource/sound/bgm.wav");
		getAudioThread().start();
		feverLabel.setVisible(false);
	}

	private synchronized void changePosition() {
		for (int i = 0; i < textArr.size(); i++) {
			TextObject textTmp = (TextObject) textArr.get(i);
			int y = textTmp.getY();

			// 생명 감소
			if (y > 500) {
				// 입력을 못한 상황
				textArr.remove(i);
				textTmp.setText("");
				HardGamePanel.this.repaint();

				LifeIcon icon = (LifeIcon) lifeIcons.get(life - 1);
				icon.setText("");
				lifeIcons.remove(icon);

				System.out.println("life 감소.");
				life--;
				loadLifeEffectAudio();
			}
			textTmp.setY(y + speed);
		}
	}

	// 생명 아이콘
	class LifeIcon extends JLabel {
		private ImageIcon lifeIcon = new ImageIcon("resource/img/life.png");// 생명 이미지
		private Image lifeImg = lifeIcon.getImage();

		public void paintComponent(Graphics g) {
			setOpaque(false);
			super.paintComponent(g);
			g.drawImage(lifeImg, 0, 0, this.getWidth(), this.getHeight(), null);
		}

		public LifeIcon() {
			this.setSize(40, 30);
			this.setForeground(new Color(136, 194, 234));
			this.setText("000");
			System.out.println("그림 아이콘그림");
		}
	}

	class LifePanel extends JPanel {

		public void makeIcon() {
			JLabel tmp = new JLabel();
			tmp.setSize(40, 30);
		}

		public LifePanel() {
			this.setBounds(162, 0, 400, 30);
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			this.setBackground(new Color(255, 0, 0, 0)); // 투명

			for (int i = 0; i < life; i++) {// 생명 수 만큼 그림을 그림
				LifeIcon lifeIcon = new LifeIcon();
				lifeIcons.add(lifeIcon);
				add(lifeIcon);
			}

		}

	}

	// 정답 맞출때 효과
	private void loadAnswerEffectAudio() {
		try {
			effectSoundClip = AudioSystem.getClip();
			File soundFile = new File("resource/sound/correctEffect.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			effectSoundClip.open(audioStream);
			effectSoundClip.start();
			System.out.println("맞춤");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 틀릴때 효과
	private void loadWrongAnswerEffectAudio() {
		try {
			effectSoundClip2 = AudioSystem.getClip();
			File soundFile = new File("resource/sound/wrongAnswerEffect.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			effectSoundClip2.open(audioStream);
			effectSoundClip2.start();
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 생명 감소 효과
	private void loadLifeEffectAudio() {
		try {
			lifeMinusClip = AudioSystem.getClip();
			File soundFile = new File("resource/sound/lifeEffect.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			lifeMinusClip.open(audioStream);
			lifeMinusClip.start();
			// System.out.println("라이프 감소 사운드");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 게임오버 효과
	private void loadGameOverEffectAudio() {
		try {
			gameOverClip = AudioSystem.getClip();
			File soundFile = new File("resource/sound/gameOverEffect.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			gameOverClip.open(audioStream);
			gameOverClip.start();
			// System.out.println("게임오버 사운드");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 배경 오디오 올려놓기
	private void loadAudio(String pathName) {
		try {
			HardGameFrame.clip = AudioSystem.getClip();
			File soundFile = new File(pathName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			HardGameFrame.clip.open(audioStream);
			// System.out.println("배경음악 start");
		} catch (Exception e) {
			System.out.println("오디오 에러");
		}
	}

	// 배경 오디오 관련 쓰레드
	class AudioThread extends Thread {
		@Override
		synchronized public void run() {
			try {
				HardGameFrame.clip.loop(Clip.LOOP_CONTINUOUSLY); // 무한재생
			} catch (Exception e) {
				timeLabel.setText(setTime(0));
				System.out.println("Kill AudioThread");
				e.printStackTrace();
			}
		}
	}

	public HardGamePanel() {

	}

	// 생명아이템이 나타나고 5초 후 사라지게 하는 메서드
	private void spawnItem() {
		int x = (int) (Math.random() * getWidth());
		int y = (int) (Math.random() * getHeight());

		ImageIcon scaledIcon = new ImageIcon(itemIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

		JLabel itemLabel = new JLabel(scaledIcon);
		itemLabel.setSize(40, 40);
		itemLabel.setLocation(x, y);
		itemLabel.addMouseListener(new ItemClickListener());
		add(itemLabel);
		repaint();

		// 5초 후에 아이템 제거를 위한 타이머 설정
		Timer removeItemTimer = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(itemLabel);
				repaint();
			}
		});
		removeItemTimer.setRepeats(false); // 한 번만 실행하도록 설정
		removeItemTimer.start();
		itemTimer.restart();
	}

	// 생명아이템 액션리스너
	private class ItemClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			remove((Component) e.getSource());
			life++;
			updateLifePanel();
		}
	}

	private void updateLifePanel() {
		LifeIcon lifeIcon = new LifeIcon();
		lifeIcons.add(lifeIcon);

		LifePanel lifePanel = lifePanels.get(threadCount);
		lifePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // 레이아웃 설정

		lifePanel.add(lifeIcon);
		lifePanel.revalidate(); // 레이아웃 업데이트 확인
		repaint();
	}

	// 랜덤한 시간을 반환하는 메서드
	private int getRandomSpawnTime() {
		Random random = new Random();
		// 30초에서 60초 사이에 랜덤한 값 설정
		int minDelay = 30000; // 30초
		int maxDelay = 60000; // 60초
		return random.nextInt(maxDelay - minDelay + 1) + minDelay;
	}

	public HardGamePanel(HardScorePanel scorePanel, EditPanel editPanel) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;

		setLayout(null);

		input.setBounds(216, 481, 130, 25);// 텍스트 입력 상자 위치,크기 설정
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 색상 및 굵기
		add(input);
		input.setColumns(10);
		timeLabel.setFont(new Font("한글 말랑말랑 BOLD", Font.BOLD, 15));
		timeLabel.setSize(127, 24);
		timeLabel.setLocation(0, 0);
		timeLabel.setForeground(Color.WHITE);
		add(timeLabel);

		feverLabel.setFont(new Font("Eras Bold ITC", Font.PLAIN, 15));
		feverLabel.setSize(150, 24);// 타임라벨 바로 밑에
		feverLabel.setLocation(0, 30);
		feverLabel.setForeground(Color.RED);
		feverLabel.setVisible(false);
		add(feverLabel);

		// 생명아이템 관련 타이머
		itemTimer = new Timer(getRandomSpawnTime(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (life > 0) {
					spawnItem();
					// 새로운 랜덤한 시간으로 타이머를 재설정
					itemTimer.setInitialDelay(getRandomSpawnTime());
					itemTimer.restart();
				}
			}
		});
		itemTimer.start();

		input.addActionListener(new ActionListener() {
			int flag = 0;

			public void actionPerformed(ActionEvent e) {
				JTextField t = (JTextField) e.getSource();
				String inWord = t.getText();
				boolean correctAnswer = false; // 정답 여부를 나타내는 플래그
				boolean feverTimeActive = false;// 피버타임 플래그
				for (int i = 0; i < textArr.size(); i++) {
					TextObject textTmp = (TextObject) textArr.get(i);

					while (scorePanel.getScore() > 100 && !feverTimeActive) {
						feverLabel.setVisible(true);
						feverTimeActive = true; // 피버 타임 시작

						if (scorePanel.getScore() > 400) {
							// 특정 조건에서 피버 타임 종료
							feverLabel.setVisible(false);
							feverTimeActive = false;
							System.out.println("Fever time deactivated due to score >= 400!");
							break;
						}
					}

					if (textTmp.getText().equals(inWord)) {// 정답 맞췄을때
						loadAnswerEffectAudio(); // 정답 맞추면 싸운드
						Dimension d = editPanel.getSize();
						editPanel.setCharImage(editPanel.getGraphics(), d, true);
						switch (textTmp.getAbility()) {
						case 0: // white 흰색 점수+10
							if (feverTimeActive) {
								scorePanel.increase(20); // 피버타임 때 점수 2배
							} else {
								scorePanel.increase(10);
							}
							textTmp.setText("");
							textArr.remove(i); // 지우기
							repaint();
							t.setText("");

							break;

						case 1: // 보라색
							threadStateFlag = 200; // 3초간 정지, 점수+20
							if (feverTimeActive) {
								scorePanel.increase(40);// 피버타임 때 점수 2배
							} else {
								scorePanel.increase(20);
							}
							textTmp.setText("");
							textArr.remove(i); // 지우기
							repaint();
							t.setText("");

							break;

						case 2: // 빨간색
							if (feverTimeActive) {
								scorePanel.increase(100); // 피버타임 때 점수 2배
							} else {
								scorePanel.increase(50);
							}
							textTmp.setText("");
							// 모든 내용 지우기.
							for (int j = 0; j < textArr.size(); j++) {
								TextObject tmp = (TextObject) textArr.get(j);
								tmp.setText("");
							}
							t.setText("");
							textArr.removeAllElements();
							repaint();

							break;

						case 3: // 초록색
							threadStateFlag = -10; // 5초간 빨라짐.
							if (feverTimeActive) {
								scorePanel.increase(20); /// 피버타임 때 점수 2배
							} else {
								scorePanel.increase(10);
							}
							textTmp.setText("");
							textArr.remove(i); // 지우기
							repaint();
							t.setText("");

							break;
						}
						flag = -1; // 정답이 있었다.
						break;
					}
					t.setText(""); // 맞추든 틀리든 지워준다.
				}
				// 틀린경우
				if (flag == 0) { // flag가 그냥 나온 경우 -> 틀렸다.
					loadWrongAnswerEffectAudio();
					Dimension d = editPanel.getSize();
					editPanel.setCharImage(editPanel.getGraphics(), d, false);

					HardGamePanel.this.repaint();
					LifeIcon icon = (LifeIcon) lifeIcons.get(life - 1);
					icon.setText("");
					lifeIcons.remove(icon);
					System.out.println("life 감소.");
					life--;
					loadLifeEffectAudio();
				}
				t.setText("");
				flag = 0;// 다시 0으로
			}
		});
	}
}