package easygamescreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginFrame_easy extends JFrame {// 로그인 프레임
	private JTextField textField;

	public LoginFrame_easy() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("resource/img/icon_su.jpg");
		setIconImage(img);

		setTitle("아이디");
		setSize(233, 131);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기

		textField = new JTextField();
		textField.setBounds(87, 10, 116, 21);
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton registerButton = new JButton("게임 시작");
		registerButton.setFont(new Font("한컴 말랑말랑 Regular", Font.PLAIN, 16));
		registerButton.setBounds(24, 41, 179, 35);
		registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		registerButton.setBackground(Color.WHITE);
		getContentPane().add(registerButton);

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '\n') {
					startGame();
				}
			}
		});

		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});

		textField.requestFocus();

		JLabel idLabel = new JLabel("아이디");
		idLabel.setFont(new Font("한컴 말랑말랑 Regular", Font.BOLD, 12));
		idLabel.setBounds(24, 13, 81, 15);
		getContentPane().add(idLabel);

		setResizable(false);
		setVisible(true);
	}

	private void startGame() {
		String id = textField.getText();
		EasyGameFrame gameFrame = new EasyGameFrame(id);
		setVisible(false);
	}
}
