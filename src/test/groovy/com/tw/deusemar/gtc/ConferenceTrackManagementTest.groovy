package com.tw.deusemar.gtc;


import com.tw.deusemar.gtc.controller.ConferenceScheduler;
import com.tw.deusemar.gtc.entity.Conference;
import com.tw.deusemar.gtc.util.FileUtil;

import spock.lang.Specification

class ConferenceTrackManagementTest extends Specification {

	def 'Test Conference Track Management Multiple Full Day Events '(){
		given:
		def fileInput = ClassLoader.getSystemClassLoader().getResource( 'input_file' ).path
		def textExpected = ClassLoader.getSystemClassLoader().getResource( 'input_file_expected' ).text
		BufferedReader bf = new BufferedReader(new FileReader(fileInput));
		def Conference conference = new ConferenceScheduler().schedule(bf);
		def boolean isEqual 
		when:
		 isEqual =	FileUtil.contentEquals(conference.toString(),textExpected);
		then:
		 isEqual	
    }

    def 'Test Conference Track Management Multiple Day Less Events'() {
		def String fileInput = ClassLoader.getSystemClassLoader().getResource( 'input_file_less_events' ).path
		def textExpected = ClassLoader.getSystemClassLoader().getResource( 'input_file_less_events_expected' ).text
		BufferedReader bf = new BufferedReader(new FileReader(fileInput));
		def Conference conference = new ConferenceScheduler().schedule(bf);
		def boolean isEqual
		when:
		 isEqual =	FileUtil.contentEquals(conference.toString(),textExpected);
		then:
		 isEqual
    }

    
    def 'Test Conference Track Management Single Day Events'(){
		def String fileInput = ClassLoader.getSystemClassLoader().getResource( 'input_file_single_day_events' ).path
		def textExpected = ClassLoader.getSystemClassLoader().getResource( 'input_file_single_day_events_expected' ).text
		BufferedReader bf = new BufferedReader(new FileReader(fileInput));
		def Conference conference = new ConferenceScheduler().schedule(bf);
		def boolean isEqual
		when:
		 isEqual =	FileUtil.contentEquals(conference.toString(),textExpected);
		then:
		 isEqual
    }

}
