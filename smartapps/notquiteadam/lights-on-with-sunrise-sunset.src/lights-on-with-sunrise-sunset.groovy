/**
 *  Lights on with Sunrise / Sunset
 *
 *  Copyright 2016 Adam Aiello
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *  
 */
definition(
    name: "Lights on with Sunrise / Sunset",
    namespace: "NotQuiteAdam",
    author: "Adam",
    description: "Light control based on sunrise / sunset -- will add presence modifier as well. ",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("When someone isn't home after sunset...") {
		input "presence1", "capability.presenceSensor", title: "Who?", multiple: true
	}
	section("Turn on a light..."){
		input "switch1", "capability.switch", multiple: true
	}
	section("When returning home, turn light off after...") {
		input "minutes1", "number", required:true
	}
}

def installed() {
	subscribe(presence1, "presence", presenceHandler)
}

def updated() {
	unsubscribe()
	subscribe(presence1, "presence", presenceHandler)
}

def presenceHandler(evt)
{
	def now = new Date()
	def sunTime = getSunriseAndSunset()
	def sunsOut = null
    
	log.debug "nowTime: $now"
	log.debug "riseTime: $sunTime.sunrise"
	log.debug "setTime: $sunTime.sunset"
	log.debug "presenceHandler $evt.name: $evt.value"
    
	def current = presence1.currentValue("presence")
	log.debug current
	
	if ((now > sunTime.sunrise) && (now < sunTime.sunset)){
		sunsOut = 1
		log.debug "Sun is out"
	}
    	else {
    		sunsOut = 0 
    		log.debug "Sun is not out"
    	}
    	
	def presenceValue = presence1.find{it.currentPresence == "not present"}
	log.debug presenceValue
        
	if(presenceValue && (sunsOut == 0)) {
		switch1.on()
		log.debug "It's night time. Someone isn't home. Turning on lights."
	}
	else if(presenceValue && (sunsOut == 1)) {
    	log.debug "It's day time. Someone isn't home. Leaving lights off."
	}
	else {
		switch1.off()
		log.debug "Just turn it off."
	}
}
