//
//  Node.cpp
//  DatalogProgram
//
//  Created by Cameron McCord on 12/4/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "Node.h"

Node::Node(){
    
}

Node::Node(string identifier){
    this->identifier = identifier;
    this->postOrderNumber = 0;
    this->rulesEvaluated = vector<string>();
    this->alreadyVisited = false;
}

Node::~Node(){
    
}

void Node::addEdge(Node newEdge){
    
}