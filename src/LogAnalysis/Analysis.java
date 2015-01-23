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
            
		}
		inputStream.close();
	}
	
	void stateCodeAnalysis(String dataLine) throws IOException{
            int stateCodeIndex = dataLine.indexOf(']');
            String stateCode = dataLine.substring(1,stateCodeIndex);
            countStateCode(stateCode, dataLine);
	}
	
	void frequentAPIKEYanalysis(String dataLine){
		final int apikeyLength = 4;
		int startAPIKEYindex = dataLine.indexOf("apikey="),
			endAPIKEYindex = startAPIKEYindex+7;
		String apikey = dataLine.substring(endAPIKEYindex,endAPIKEYindex+apikeyLength);
	
		countAPIKEY(apikey);
	}
	
	void stateAPIservice(String dataLine){
		final int UrlLength = 28;
		int startAPIserviceIndex = dataLine.indexOf("http") + UrlLength,
			endAPIserviceIndex = dataLine.indexOf("?");
		String apiService = dataLine.substring((startAPIserviceIndex),(endAPIserviceIndex));
		
		countAPIservice(apiService);
	}
	
	void countStateCode(String stateCode, String dataLine){
		if(stateCode.equals("10")){
			stateNoURL++;
			stateAPIservice(dataLine);
		}
		else if(stateCode.equals("200")){
			stateSuccess++;
			frequentAPIKEYanalysis(dataLine);
			stateAPIservice(dataLine);
		}
		else{ //404
			stateNoPage++;
			frequentAPIKEYanalysis(dataLine);
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
	
	void choiceFrequentAPIKEY(){
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
	
	void choiceFrequentAPIservices(){
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
	

	void printResult(){
		choiceFrequentAPIKEY();
		print.printStateCode(stateNoURL, stateSuccess, stateNoPage);
		choiceFrequentAPIservices();

	}
}
