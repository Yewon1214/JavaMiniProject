import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class WordFile {
	private static Vector<String> word = null;
	
	public Vector<String> getWord() {
		if(word == null) {
			openFile();
		}
		return word;
	}
	
	public int getWordCount() {
		return word.size();
	}
	
	public void openFile() {
		Vector<String> character = new Vector<String>();
		word = new Vector<String>();

		try {
			FileReader file = new FileReader("words.txt");
			int c;

			while ((c = file.read()) != -1) {
				if (c == 13 || c == 10)
					character.add(Character.toString('/'));
				else
					character.add(Character.toString((char) c));
			}
		} catch (FileNotFoundException e1) {
			System.out.println("파일을 열 수 없습니다.");
			System.exit(0);
		} catch (IOException e1) {
			System.out.println("입출력 오류");
			System.exit(0);
		}

		Iterator<String> it = character.iterator();
		String str = " ";

		while (it.hasNext())
			str = str.concat(it.next());
		StringTokenizer token = new StringTokenizer(str.trim(), "/");

		while (token.hasMoreTokens())
			word.add(token.nextToken());
	}
}
