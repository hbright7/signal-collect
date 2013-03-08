/*
 *  @author Philip Stutz
 *  
 *  Copyright 2012 University of Zurich
 *      
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.signalcollect.nodeprovisioning.torque

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.PoisonPill
import akka.actor.actorRef2Scala

class NodeProvisionerActor(numberOfNodes: Int) extends Actor {

  var nodeListRequestor: ActorRef = _

  var nodeControllers = List[ActorRef]()

  def receive = {
    case "GetNodes" =>
      nodeListRequestor = sender
      sendNodesIfReady
    case "NodeReady" =>
      nodeControllers = sender :: nodeControllers
      sendNodesIfReady
  }

  def sendNodesIfReady {
    if (nodeControllers.size == numberOfNodes) {
      nodeListRequestor ! nodeControllers
      self ! PoisonPill
    }
  }
}