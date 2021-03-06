import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class Rank {
	private static Vector<String> nameRank = null;
	private static Vector<Integer> scoreRank = null;
	
	public Vector<String> getNameRank() {
		if(nameRank == null) {
			openRanking();
		}
		return nameRank;
	}
	
	public Vector<Integer> getScoreRank() {
		if(scoreRank == null) {
			openRanking();
		}
		return scoreRank;
	}
	
	public void openRanking() {
		nameRank = new Vector<String>();
		scoreRank = new Vector<Integer>();
		
		try {
			Scanner sc = new Scanner(new FileReader("ranking.txt"));
			while(sc.hasNext()) {
				String name = sc.nextLine(); //이름 한줄
				String score = sc.nextLine(); //점수 한줄 씩 스캔
				
				int s = Integer.parseInt(score);
				
				nameRank.add(name);
				scoreRank.add(s);
			}
		} catch (FileNotFoundException e1) {
			System.out.println("파일을 열 수 없습니다.");
			System.exit(0);
		} catch (IOException e1) {
			System.out.println("입출력 오류");
			System.exit(0);
		}
		
		scoreRank.add(-1); //append를 하기 위한 잡 공간
		nameRank.add("tmp"); //append를 하기 위한 잡 공간
	}
	
	public void writeRanking() {
		try {
			FileWriter fout = new FileWriter("ranking.txt");

			for (int i = 0; i < scoreRank.size() - 1; i++) {
				fout.write(nameRank.get(i));
				fout.write("\r\n", 0, 2);
				fout.write(Integer.toString(scoreRank.get(i)));
				fout.write("\r\n", 0, 2);
			}
			fout.close();
		} catch (IOException e1) {
			System.out.println("입출력 오류");
			System.exit(0);
		}
	}
}
