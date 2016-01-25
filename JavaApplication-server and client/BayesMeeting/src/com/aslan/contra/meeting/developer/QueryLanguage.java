package com.aslan.contra.meeting.developer;

public class QueryLanguage {
	
	public QueryLanguage(){
		
	}
	
	public String isInMeeting(int userID){
		String IsUserInMeeting = "IsUserInMeeting";
		String userIDString = Integer.toString(userID);
		String queryString = IsUserInMeeting + "," + userIDString + "\n";
		return queryString;
	}

}
