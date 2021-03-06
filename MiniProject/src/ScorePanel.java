import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;

public class ScorePanel extends JPanel {
	private Score s = new Score();
	
	private int score = s.getScore();
	
	private JLabel scoreText = new JLabel("Score:");
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel userNameText = new JLabel("User Name:");
	private JLabel userName = new JLabel(new UserName().getUserName());
	
	private JLabel policeLabel;
	private ImageIcon policeIcon;
	
	public ScorePanel() {
		scoreLabel = new JLabel("0");

		setBackground(Color.YELLOW);
		setLayout(null);
		
		userNameText.setSize(100, 30);
		userNameText.setLocation(10, 10);
		add(userNameText);
		
		userName.setSize(100, 30);
		userName.setLocation(90, 10);
		add(userName);
		
		scoreText.setSize(50, 30);
		scoreText.setLocation(10, 30);
		add(scoreText);

		scoreLabel.setSize(100, 30);
		scoreLabel.setLocation(70, 30);
		add(scoreLabel);
		
		ImageIcon icon = new ImageIcon("police.png");
		Image img = icon.getImage();
		img = img.getScaledInstance(130, 140, Image.SCALE_SMOOTH);
		policeIcon = new ImageIcon(img);
		
		policeLabel = new JLabel();
		policeLabel.setIcon(policeIcon);
		
		policeLabel.setSize(130, 140);
		policeLabel.setLocation(70, 55);
		
		add(policeLabel);
		
		ScoreThread st = new ScoreThread();
		st.start();
	}
	
	public JLabel getPoliceLabel() {
		return policeLabel;
	}
	
	public void setFont(String fontname, int fontsize) {
		scoreLabel.setFont(new Font(fontname, Font.BOLD, fontsize));
		scoreText.setFont(new Font(fontname, Font.BOLD, fontsize));
		userName.setFont(new Font(fontname, Font.BOLD, fontsize));
		userNameText.setFont(new Font(fontname, Font.BOLD, fontsize));
	}
	
	public void setForeground(int r, int g, int b) {
		Color c = new Color(r, g, b);
		scoreLabel.setForeground(c);
		scoreText.setForeground(c);
		userName.setForeground(c);
		userNameText.setForeground(c);
	}
	
	public void setBackground(int r, int g, int b) {
		Color c = new Color(r, g, b);
		setBackground(c);
	}

	class ScoreThread extends Thread {
		public void run() {
			while (true) {
				scoreLabel.setText(Integer.toString(s.getScore()));
			}
		}
	}
}
