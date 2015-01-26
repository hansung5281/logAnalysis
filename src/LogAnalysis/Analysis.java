package LogAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Analysis {
	BufferedReader inputStream;
	int stateNoURL=0,
		stateSuccess=0,
		stateNoPage=0;
	
	String frequentAPIKEY;
	
	List<String> apikeyList = new ArrayList<String>();
	List<Integer> apikeyCountList = new ArrayList<Integer>();
	
	List<String> apiServiceList = new ArrayList<String>();
	List<Integer> apiServiceCountList = new ArrayList<Integer>();
	
	List<String> webUsageList = new ArrayList<String>();
	List<Integer> webUsageCountList = new ArrayList<Integer>();

	
	String currentPeakTime="";
	int currentPeakTimeCount=0;
	
	String currentComparePeakTime="";
	int currentComparePeakTimeCount=0;
	
	
	
	Print print = new Print();
	
	public Analysis(BufferedReader inputStream){
		this.inputStream = inputStream;
	}
	
	void dataAnalysis() throws IOException{
		readDataLine();
		printResult();
	}
	
	void readDataLine() throws IOException{
		while(true) {
			String dataLine = inputStream.readLine();
            if (dataLine==null) {
            	break;}
            
            stateCodeAnalysis(dataLine);
            frequentAPIKEYanalysis(dataLine);
            apiServiceAnalysis(dataLine);
            peakTimeZoneAnalysis(dataLine);
            webUsageAnalysis(dataLine);
            
		}
		inputStream.close();
	}
	
	void stateCodeAnalysis(String dataLine) throws IOException{
		String stateCode = selectData(dataLine, 0);
		countStateCode(stateCode, dataLine);
	}
	
	void frequentAPIKEYanalysis(String dataLine){	
		final int apikeyLength = 4;
		String stateCode = selectData(dataLine, 0);
		
		//상태코드가 10이 아닐떄만
		if(!(stateCode.equals("10"))){
			String url = selectData(dataLine, 1);
			
			int startAPIKEYindex = url.indexOf("=") + 1,
				endAPIKEYindex = startAPIKEYindex + apikeyLength;
			
			String apikey = url.substring(startAPIKEYindex,endAPIKEYindex);
			countAPIKEY(apikey);
		}
	}
	
	void apiServiceAnalysis(String dataLine){
		final int basicUrlLength = 28;
		String stateCode = selectData(dataLine, 0);
		
		if(!(stateCode.equals("404"))){
			String url = selectData(dataLine, 1);
			
			int startApiServiceIndex = basicUrlLength,
				endApiserviceIndex = url.indexOf("?");

			String apiService = url.substring(startApiServiceIndex,endApiserviceIndex);
			countAPIservice(apiService);
		}
		
	}
	
	void peakTimeZoneAnalysis(String dataLine){
		String accessTime = selectData(dataLine, 3);
		
		if(currentPeakTimeCount==0){
			currentPeakTime = accessTime;
			currentComparePeakTime = accessTime;
			currentPeakTimeCount++;
			currentComparePeakTimeCount++;
			return ;
			
		}
		
		if(currentComparePeakTime.equals(accessTime)){
			if(currentPeakTimeCount < currentComparePeakTimeCount){
				currentPeakTimeCount = currentComparePeakTimeCount;
			} 
			currentComparePeakTimeCount++;
		}
		else{
			if(currentPeakTimeCount < currentComparePeakTimeCount){
				currentPeakTimeCount = currentComparePeakTimeCount;
				currentPeakTime = currentComparePeakTime;
			}
			currentComparePeakTime = accessTime;
			currentComparePeakTimeCount=1;
		}
			
	}
	
	void webUsageAnalysis(String dataLine){
		String webType = selectData(dataLine, 2);
		countWebUsage(webType);
	}
	
	void countStateCode(String stateCode, String dataLine){
		if(stateCode.equals("10")){
			stateNoURL++;
		}
		else if(stateCode.equals("200")){
			stateSuccess++;
		}
		else{ //404
			stateNoPage++;
		}
	}
	
	void countAPIKEY(String apikey){
		boolean equalAPIKEY = false;
		
		if(apikeyList.isEmpty()){
			apikeyList.add(apikey);
			apikeyCountList.add(1);
		}
		else{
			for (String APIKEY : apikeyList) {
				if(APIKEY.equals(apikey)){
					equalAPIKEY = true;
					int apikeyCountIndex = apikeyList.indexOf(APIKEY);
					apikeyCountList.set(apikeyCountIndex, (apikeyCountList.get(apikeyCountIndex)+1));
				}
			}
			if(equalAPIKEY==false){
				apikeyList.add(apikey);
				apikeyCountList.add(1);
			}

		}
	}

	void countAPIservice(String apiService){
		boolean equalAPIService = false;
		
		if(apiServiceList.isEmpty()){
			apiServiceList.add(apiService);
			apiServiceCountList.add(1);
		}
		else{
			for (String APIservice : apiServiceList) {
				if(APIservice.equals(apiService)){
					equalAPIService = true;
					int apiServiceCountIndex = apiServiceList.indexOf(APIservice);
					
					apiServiceCountList.set(apiServiceCountIndex, (apiServiceCountList.get(apiServiceCountIndex)+1));
				}
			}
			if(equalAPIService==false){
				apiServiceList.add(apiService);
				apiServiceCountList.add(1);
			}

		}
	}
	
	void chooseFrequentAPIKEY(){
		int frequentCount= apikeyCountList.get(0);
		
		for (Integer count : apikeyCountList) {
			if(count> frequentCount){
				frequentCount = count;
			}
		}
		
		int frequentIndex = apikeyCountList.indexOf(frequentCount);
		frequentAPIKEY = apikeyList.get(frequentIndex);
		print.printFrequentAPIKEY(apikeyList.get(frequentIndex));
	}
	
	void chooseFrequentAPIservices(){
		int arrayIndex = 0;
		int apiServiceCountArray[] = new int[6]; 
		for (int apiServiceCount : apiServiceCountList) {
			apiServiceCountArray[arrayIndex] = apiServiceCount; 
			arrayIndex++;
		}
		compareFrequentAPIservices(apiServiceCountArray);
	}
	
	void compareFrequentAPIservices(int apiServiceCountArray[] ){
		int tmp,
			compareNumber= apiServiceCountArray.length-1;
		while(compareNumber>0){
			for(int i=0; i< compareNumber; i++){
				if(apiServiceCountArray[i] > apiServiceCountArray[i+1]){
					tmp = apiServiceCountArray[i];
					apiServiceCountArray[i] = apiServiceCountArray[i+1];
					apiServiceCountArray[i+1] = tmp;
					}
				}
				compareNumber--;
			}
		String firstService = apiServiceList.get(apiServiceCountList.indexOf(apiServiceCountArray[5])),
			   secondService = apiServiceList.get(apiServiceCountList.indexOf(apiServiceCountArray[4])),
			   thirdService = apiServiceList.get(apiServiceCountList.indexOf(apiServiceCountArray[3]));
		
		int firstServiceNumber = apiServiceCountArray[5],
			secondServiceNumber = apiServiceCountArray[4],
			thirdServiceNumber = apiServiceCountArray[3];
		
		print.printFrequentAPIservices(firstService, secondService, thirdService, 
				firstServiceNumber, secondServiceNumber, thirdServiceNumber);
		
	}
	
	void choosePeakTimeZone(){
		print.printPeakTimeZone(currentPeakTime);
	}
	
	
	void countWebUsage(String webType){
		boolean equalWebType = false;
		
		if(webUsageList.isEmpty()){
			webUsageList.add(webType);
			webUsageCountList.add(1);
		}
		else{
			for (String WebType : webUsageList) {
				if(WebType.equals(webType)){
					equalWebType = true;
					int webUsageTypeCountIndex = webUsageList.indexOf(WebType);
					
					webUsageCountList.set(webUsageTypeCountIndex, (webUsageCountList.get(webUsageTypeCountIndex)+1));
				}
			}
			if(equalWebType==false){
				webUsageList.add(webType);
				webUsageCountList.add(1);
			}

		}
	}
	
	void webUsageRateAnalysis(){
		int totalWebUsage = 0,
			listSize = webUsageCountList.size();
		
		float[] usageRate = new float[listSize];
		
		for (Integer webUsage : webUsageCountList) {
			totalWebUsage = totalWebUsage + webUsage;  
		}
		for(int i=0; i<listSize; i++){
			usageRate[i] = ( (float)(webUsageCountList.get(i) * 100) / (float)totalWebUsage) ;
		}
		
		print.printWebUsageRate(webUsageList, usageRate, listSize);
		
	}
	
	String selectData(String dataLine, int areaNumber){
		
		int startIndex=0, 
			endIndex = dataLine.length();
		
		String resultData;
		
			for(int i=0; i<areaNumber; i++){
				startIndex = ( (dataLine.substring(startIndex,endIndex)).indexOf("]") + 1 ) + startIndex;
			}
			endIndex = startIndex + (dataLine.substring(startIndex,endIndex)).indexOf("]");
			
			startIndex = startIndex+1; 
			resultData = dataLine.substring(startIndex,endIndex);
			
			return resultData;
	}
	
	
	void printResult(){
		chooseFrequentAPIKEY();
		print.printStateCode(stateNoURL, stateSuccess, stateNoPage);
		chooseFrequentAPIservices();
		choosePeakTimeZone();
		webUsageRateAnalysis();
	}
}
