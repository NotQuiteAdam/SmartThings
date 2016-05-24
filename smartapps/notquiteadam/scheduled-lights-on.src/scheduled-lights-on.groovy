/**
 *  Scheduled Lights
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
    name: "Scheduled Lights On",
    namespace: "NotQuiteAdam",
    author: "Adam Aiello",
    description: "Simple daily repeating schedule for lights.",
    category: "Convenience",
    iconUrl: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn@2x.png",
    iconX3Url: "http://cdn.device-icons.smartthings.com/Lighting/light9-icn@3x.png")


preferences {
	section ("Daily time..."){
    	input "theTime", "time", title: "Time to execute every day"
    }
    section ("Turn on these lights"){
    	input "theSwitches", "capability.switch", multiple:true
    }
    section ("Off time..."){
    	input "offTime", "time", title: "Time to execute every day"
    }
}

def installed() {
	initialize()
}

def updated() {
	unsubscribe()
	initialize()
}

def initialize() {
	schedule(theTime, handler)
	schedule(offTime, offHandler)
}

// called every day at the time specified by the user
def handler() {
    theSwitches.on()
}
def offHandler() {
    theSwitches.off()
}