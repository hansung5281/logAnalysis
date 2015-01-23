package LogAnalysis;

import java.util.ArrayList;

public class Print {
	
	void printStateCode(int stateNoURL, int stateSuccess, int stateNoPage){
		System.out.println("상대코드 별 횟수");
		System.out.println("10: "+stateNoURL);
		System.out.println("200: "+stateSuccess);
		System.out.println("404 "+stateNoPage + "\n");
	}
	
	void printFrequentAPIKEY(String frequentAPIKEY){
		System.out.println("최다호출 APIKEY");
		System.out.println(frequentAPIKEY+"\n");
	}
	
	void printFrequentAPIservices(String firstService, String secondService, String thirdService, 
			int firstServiceNumber, int secondServiceNumber, int thirdServiceNumber){
		
		System.out.println("상위 3개의 API ServiceID와 각각의 요청 수");
		
		System.out.println(firstService + " : " + firstServiceNumber);
		System.out.println(secondService + " : " + secondServiceNumber);
		System.out.println(thirdService + " : " + thirdServiceNumber);
	}
}
