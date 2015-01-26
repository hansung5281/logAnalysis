package LogAnalysis;

import java.util.List;

public class Print {
	
	void printStateCode(int stateNoURL, int stateSuccess, int stateNoPage){
		System.out.println("상대코드 별 횟수");
		System.out.println("10: "+stateNoURL);
		System.out.println("200: "+stateSuccess);
		System.out.println("404 "+stateNoPage);
		System.out.println();
	}
	
	void printFrequentAPIKEY(String frequentAPIKEY){
		System.out.println("최다호출 APIKEY");
		System.out.println(frequentAPIKEY);
		System.out.println();
	}
	
	void printFrequentAPIservices(String firstService, String secondService, String thirdService, 
			int firstServiceNumber, int secondServiceNumber, int thirdServiceNumber){
		
		System.out.println("상위 3개의 API ServiceID와 각각의 요청 수");
		
		System.out.println(firstService + " : " + firstServiceNumber);
		System.out.println(secondService + " : " + secondServiceNumber);
		System.out.println(thirdService + " : " + thirdServiceNumber);
		System.out.println();
	}
	
	void printPeakTimeZone(String peakTimeZone){
		System.out.println("피크 시간대");
		System.out.println(peakTimeZone);
		System.out.println();
	}
	
	void printWebUsageRate(List webUsageList, float[] usageRate, int listSize){
		System.out.println("웹 브라우저 별 사용비율");
		for(int i=0; i<listSize; i++){
			System.out.println(webUsageList.get(i)+": " + usageRate[i] + "%");
		}
			
	}
}
