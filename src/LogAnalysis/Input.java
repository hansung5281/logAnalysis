package LogAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Input {
	
	void inputFile() throws IOException{
		FileReader frd = new FileReader("input.log");
		BufferedReader inputStream = new BufferedReader(new FileReader("input.log"));
        
		Analysis analysis = new Analysis(inputStream);
		analysis.dataAnalysis();;
		
	}
}
