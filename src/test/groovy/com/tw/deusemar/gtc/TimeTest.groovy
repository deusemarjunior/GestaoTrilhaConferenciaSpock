package com.tw.deusemar.gtc;

import com.tw.deusemar.gtc.util.Time;

import spock.lang.Specification

class TimeTest extends Specification {

	def 'Time in minutes cannot be greater than 1439 minutes'() {
		when:
			Time.minutesToDisplayTime(12 * 60 + 12 * 60)
		then:
			thrown(IllegalArgumentException)
		
	}
}
