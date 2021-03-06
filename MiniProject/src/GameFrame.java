import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class GameFrame extends JFrame {
	private JFrame frame;
	
	private Clip clip;

	private GamePanel gamePanel = new GamePanel();
	private ScorePanel scorePanel = new ScorePanel();
	private EditPanel editPanel = new EditPanel();

	public GameFrame() {
		super("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(false);
		splitPane();
		
		try {
			clip = AudioSystem.getClip();
			File audioFile = new File("mapmakebgm.wav");
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
		gamePanel.setClip(clip);

		frame = this;

		setJMenuBar(makeMenu());
		add(makeToolBar(), BorderLayout.NORTH);

		setVisible(true);
	}

	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);

		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(500);
		hPane.setEnabled(false); // split bar를 움직일 수 없도록 하기 위해
		hPane.setLeftComponent(gamePanel);

		JSplitPane pPane = new JSplitPane();
		hPane.setRightComponent(pPane);
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setTopComponent(scorePanel);
		pPane.setBottomComponent(editPanel);
		
		gamePanel.setPoliceLabel(scorePanel.getPoliceLabel());
	}

	public JMenuBar makeMenu() {
		JMenuBar mb = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem fileMI[] = new JMenuItem[3];
		String fileMN[] = { "Home", "Word File Open", "Exit" };
		for (int i = 0; i < fileMI.length; i++) {
			fileMI[i] = new JMenuItem(fileMN[i]);
			if (i == 2)
				fileMenu.addSeparator();

			fileMI[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem) e.getSource();
					if (item == fileMI[0]) {
						MainFrame g = new MainFrame();
						
						clip.stop();
						
						setVisible(false);
						dispose();
					} else if (item == fileMI[1]) {
						WordDialog wd = new WordDialog(frame, "Word.txt");
					} else if (item == fileMI[2]) {
						System.exit(0);
					}
				}
			});

			fileMenu.add(fileMI[i]);
		}

		JMenu editMenu = new JMenu("Edit");
		JMenuItem editMI[] = new JMenuItem[3];
		String editMN[] = { "Font", "FG Color", "BG Color" };
		for (int i = 0; i < editMI.length; i++) {
			editMI[i] = new JMenuItem(editMN[i]);
			editMI[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem) e.getSource();
					if (item == editMI[0]) {
						FontDialog fd = new FontDialog(frame, "Set Font");
					} else if (item == editMI[1]) {
						FgColorDialog fcd = new FgColorDialog(frame, "Set Foreground");
					} else if (item == editMI[2]) {
						BgColorDialog bcd = new BgColorDialog(frame, "Set Background");
					}
				}
			});
			editMenu.add(editMI[i]);
		}

		JMenu viewMenu = new JMenu("View");
		JMenuItem viewMI[] = new JMenuItem[2];
		String viewMN[] = { "Help", "Rank" };
		for (int i = 0; i < viewMI.length; i++) {
			viewMI[i] = new JMenuItem(viewMN[i]);
			viewMI[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem item = (JMenuItem) e.getSource();
					if (item == viewMI[0]) {
						Help fd = new Help(frame, "Help");
					} else if (item == viewMI[1]) {
						RankDialog fcd = new RankDialog(frame, "Rank");
					}
				}
			});
			viewMenu.add(viewMI[i]);
		}

		mb.add(fileMenu);
		mb.add(editMenu);
		mb.add(viewMenu);

		return mb;
	}

	class WordDialog extends JDialog {
		public WordDialog(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new BorderLayout());
			setSize(200, 600);

			JLabel titleLabel = new JLabel("word.txt");
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

			Vector<String> word = new WordFile().getWord();

			JList<String> wordList = new JList<String>(word);

			add(titleLabel, BorderLayout.NORTH);
			add(new JScrollPane(wordList));

			setVisible(true);
		}
	}

	class Help extends JDialog {
		private ImageIcon numIcon[] = new ImageIcon[4];
		private JLabel titleLabel;
		private JLabel helplabel[] = new JLabel[4];

		public Help(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			setSize(600, 400);

			ImageIcon icon[] = new ImageIcon[4];

			icon[0] = new ImageIcon("1.jpg");
			icon[1] = new ImageIcon("2.jpg");
			icon[2] = new ImageIcon("3.jpg");
			icon[3] = new ImageIcon("booster.jpg");

			for (int i = 0; i < icon.length; i++) {
				Image img = icon[i].getImage();
				img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
				numIcon[i] = new ImageIcon(img);
			}

			titleLabel = new JLabel("TyPing Game Help");
			titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

			helplabel[0] = new JLabel("과속을 하고 있는 캐릭터들의 옆 글자를 맞춰 캐릭터들을 잡아주세요!", numIcon[0], SwingConstants.CENTER);
			helplabel[1] = new JLabel("20초마다 과속범들의 속도가 빨라집니다! 과속범들을 잡으려고 노력하세요~", numIcon[1], SwingConstants.CENTER);
			helplabel[2] = new JLabel("캐릭터를 잡으면 +10점, 못 잡으면 -10점! 1분동안 최대한 많은 과속범들을 잡아주세요!", numIcon[2],
					SwingConstants.CENTER);
			helplabel[3] = new JLabel("이 아이템을 잡으면 +50점이 추가됩니다!", numIcon[3], SwingConstants.CENTER);

			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(titleLabel);
			for (int i = 0; i < numIcon.length; i++) {
				add(helplabel[i]);
			}
			
			this.getContentPane().setBackground(Color.WHITE);

			setVisible(true);
		}
	}

	class RankDialog extends JDialog {
		private Vector<Integer> scoreRank = new Rank().getScoreRank();
		private Vector<String> nameRank = new Rank().getNameRank();

		public RankDialog(JFrame frame, String title) {
			super(frame, title, true);
			setSize(200, 200);
			setLayout(new FlowLayout(FlowLayout.CENTER, 45, 10));

			if (scoreRank.size() == 1) {
				JLabel label = new JLabel("랭킹에 등록된 사람이 없습니다.");
				add(label);
			} else {
				JLabel list[] = new JLabel[scoreRank.size() - 1];
				for (int i = 0; i < list.length; i++) {
					list[i] = new JLabel((i + 1) + "등 " + nameRank.get(i) + " : " + scoreRank.get(i));
					add(list[i]);
				}
			}
			setVisible(true);
		}
	}

	class FontDialog extends JDialog {
		public FontDialog(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new FlowLayout());
			setSize(300, 100);

			JLabel fontLabel = new JLabel("Font : ");
			JTextField fontTextField = new JTextField("Arial", 20);

			JButton saveBtn = new JButton("Save");
			saveBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GameStart[] gs = gamePanel.getGameStart();
					for (int i = 0; i < gs.length; i++) {
						gs[i].setFont(fontTextField.getText(), 12);
					}
					scorePanel.setFont(fontTextField.getText(), 12);
					editPanel.setFont(fontTextField.getText(), 12);
					setVisible(false);
				}
			});
			saveBtn.setHorizontalAlignment(SwingConstants.CENTER);

			add(fontLabel);
			add(fontTextField);
			add(saveBtn);

			setVisible(true);
		}
	}

	class FgColorDialog extends JDialog {
		public FgColorDialog(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new FlowLayout());
			setSize(200, 180);

			JLabel exLabel = new JLabel("0 ~ 255 숫자를 넣어주세요.");

			JLabel RLabel = new JLabel("R value : ");
			JTextField RTextField = new JTextField("0", 10);
			JLabel GLabel = new JLabel("G value : ");
			JTextField GTextField = new JTextField("0", 10);
			JLabel BLabel = new JLabel("B value : ");
			JTextField BTextField = new JTextField("0", 10);

			JButton saveBtn = new JButton("Save");
			saveBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					GameStart[] gs = gamePanel.getGameStart();
					try {
						scorePanel.setForeground(Integer.parseInt(RTextField.getText()),
								Integer.parseInt(GTextField.getText()), Integer.parseInt(BTextField.getText()));
						editPanel.setForeground(Integer.parseInt(RTextField.getText()),
								Integer.parseInt(GTextField.getText()), Integer.parseInt(BTextField.getText()));
						setVisible(false);
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "0~255범위 내의 숫자만 입력하세요!", "error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, "0~255범위 내의 숫자만 입력하세요!", "error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			exLabel.setHorizontalAlignment(SwingConstants.CENTER);
			saveBtn.setHorizontalAlignment(SwingConstants.CENTER);

			add(exLabel);
			add(RLabel);
			add(RTextField);
			add(GLabel);
			add(GTextField);
			add(BLabel);
			add(BTextField);
			add(saveBtn);

			setVisible(true);
		}
	}

	class BgColorDialog extends JDialog {
		public BgColorDialog(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new FlowLayout());
			setSize(200, 180);

			JLabel exLabel = new JLabel("0 ~ 255 숫자를 넣어주세요.");

			JLabel RLabel = new JLabel("R value : ");
			JTextField RTextField = new JTextField("255", 10);
			JLabel GLabel = new JLabel("G value : ");
			JTextField GTextField = new JTextField("255", 10);
			JLabel BLabel = new JLabel("B value : ");
			JTextField BTextField = new JTextField("0", 10);

			JButton saveBtn = new JButton("Save");
			saveBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						scorePanel.setBackground(Integer.parseInt(RTextField.getText()),
								Integer.parseInt(GTextField.getText()), Integer.parseInt(BTextField.getText()));
						editPanel.setBackground(Integer.parseInt(RTextField.getText()),
								Integer.parseInt(GTextField.getText()), Integer.parseInt(BTextField.getText()));
						setVisible(false);
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "0~255범위 내의 숫자만 입력하세요!", "error",
								JOptionPane.ERROR_MESSAGE);
					} catch (IllegalArgumentException e1) {
						JOptionPane.showMessageDialog(null, "0~255범위 내의 숫자만 입력하세요!", "error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			exLabel.setHorizontalAlignment(SwingConstants.CENTER);
			saveBtn.setHorizontalAlignment(SwingConstants.CENTER);

			add(exLabel);
			add(RLabel);
			add(RTextField);
			add(GLabel);
			add(GTextField);
			add(BLabel);
			add(BTextField);
			add(saveBtn);

			setVisible(true);
		}
	}

	public JToolBar makeToolBar() {
		JToolBar tb = new JToolBar();

		tb.setEnabled(false);

		JButton gameStartBtn = new JButton("Game Start");
		gameStartBtn.addActionListener(gamePanel.new StartButton());
		gameStartBtn.setToolTipText("게임을 시작합니다.");
		tb.add(gameStartBtn);

		return tb;
	}
}