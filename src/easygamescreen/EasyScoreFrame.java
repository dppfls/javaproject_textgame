package easygamescreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

class Score {
	private String id;
	private int score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Score(String id, int score) {
		super();
		this.id = id;
		this.score = score;
	}

}

public class EasyScoreFrame extends JFrame {

	private File file = new File("resource/record/record.txt");
	private Vector<Score> scores = new Vector<>();

	private void recordScore() {
		try {
			// 파일 읽기
			FileReader filereader = new FileReader(file);

			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";

			// 주어진 파일을 한 줄씩 읽어옴
			while ((line = bufReader.readLine()) != null) {
				String arr[] = line.split("/"); // "/"를 기준으로 id와 score가 나뉨
				String id = arr[0];
				String score = arr[1];

				Score scoretmp = new Score(id, Integer.parseInt(score));
				scores.add(scoretmp);
			}

			System.out.println("-----점수------");
			for (int i = 0; i < scores.size(); i++) {
				Score scoretmp = scores.get(i);
				String id = scoretmp.getId();
				int score = scoretmp.getScore();
				for (int j = i + 1; j < scores.size(); j++) {
					Score scoretmpRef = scores.get(j);
					String idRef = scoretmpRef.getId();
					int scoreRef = scoretmpRef.getScore();

					// 중복된 ID에 대하여 최고 점수를 유지하도록 처리
					if (id.equals(idRef)) {
						if (score >= scoreRef) {
							scores.remove(scoretmpRef);
						} else {
							score = scoreRef;
							scoretmp.setScore(score);
							scores.remove(scoretmpRef);
							i--;
						}
					}
				}
			}
			// 최종 점수 목록을 출력
			for (int i = 0; i < scores.size(); i++) {
				System.out.println(i + "\t " + scores.get(i).getId() + "\t\t " + scores.get(i).getScore());
			}
		} catch (Exception e) {
			System.out.println("에러");
		}
	}

	public EasyScoreFrame() {
		recordScore(); // 기존 점수를 읽어옴

		setForeground(Color.CYAN);
		getContentPane().setForeground(Color.CYAN);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/icon_su.jpg");
		setIconImage(img);
		setTitle("점수 기록");
		setSize(290, 448);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("아이디");
		lblNewLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		lblNewLabel.setBounds(41, 10, 98, 28);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("점수");
		lblNewLabel_1.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
		lblNewLabel_1.setBounds(151, 10, 98, 28);
		getContentPane().add(lblNewLabel_1);

		System.out.println("--------------------------------------");

		// 정렬에 사용할 임시 배열 생성
		JLabel[] tmp = new JLabel[scores.size()];
		String[] tmpId = new String[scores.size()];
		int[] tmpScore = new int[scores.size()];

		// 기존 점수를 임시 배열에 복사
		for (int i = 0; i < scores.size() - 1; i++) {
			Score scoretmp = scores.get(i);
			tmpId[i] = scoretmp.getId();
			tmpScore[i] = scoretmp.getScore();
		}

		// 점수를 내림차순으로 정렬
		for (int i = 0; i < scores.size() - 1; i++) {
			int indexMax = i;

			for (int j = i + 1; j < scores.size(); j++) {
				if (tmpScore[j] > tmpScore[i]) {
					indexMax = j;
				}
			}
			// swap
			int tmpInt = tmpScore[indexMax];
			tmpScore[indexMax] = tmpScore[i];
			tmpScore[i] = tmpInt;

			String tmpString = tmpId[indexMax];
			tmpId[indexMax] = tmpId[i];
			tmpId[i] = tmpString;
		}

		// 정렬된 점수를 화면과 콘솔에 출력
		for (int i = 0; i < scores.size(); i++) {
			String id = tmpId[i];
			int score = tmpScore[i];

			JLabel idLabel = new JLabel(id);
			idLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
			idLabel.setBounds(41, 10 + ((i + 1) * 27), 98, 28);
			getContentPane().add(idLabel);

			JLabel scoreLabel = new JLabel(Integer.toString(score));
			scoreLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 20));
			scoreLabel.setBounds(151, 10 + ((i + 1) * 27), 98, 28);
			getContentPane().add(scoreLabel);

			System.out.println(id + "\t:" + score);
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

	}

}
