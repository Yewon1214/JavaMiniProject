import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private Clip clip;
	public MainFrame() {
		setTitle("타이핑 게임 메인화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(false);

		try {
			clip = AudioSystem.getClip();
			File audioFile = new File("startbgm.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			clip.open(audioStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);

		setContentPane(new MainPanel());

		setVisible(true);
	}

	class MainPanel extends JPanel {
		ImageIcon icon = new ImageIcon("main.png");
		Image img = icon.getImage();

		public MainPanel() {
			setLayout(null);

			JButton startbtn = new JButton("START");
			JLabel username = new JLabel("User Name");
			JTextField usernametf = new JTextField(5);

			startbtn.setLocation(344, 500);
			startbtn.setSize(100, 30);
			startbtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!usernametf.getText().equals("")) {
						new UserName().setUserName(usernametf.getText());
						
						clip.stop();

						GameFrame g = new GameFrame();
						setVisible(false);
						dispose();
					}
				}
			});

			username.setForeground(Color.white);
			username.setLocation(354, 360);
			username.setSize(100, 30);
			username.setFont(new Font("Arial", Font.BOLD, 15));

			usernametf.setLocation(344, 400);
			usernametf.setSize(100, 30);

			add(startbtn);
			add(username);
			add(usernametf);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
}
