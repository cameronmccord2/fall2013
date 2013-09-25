//
//  DomainList.cpp
//  DatalogProgram
//
//  Created by Taylor McCord on 9/24/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#include "DomainList.h"

DomainList::DomainList(){
    this->elements = new std::set<std::string>();
}

DomainList::~DomainList(){
    delete this->elements;
}

void DomainList::setDomainElement(std::string element){
    this->elements->insert(element);
}

int DomainList::getCount(){
    return (int)this->elements->size();
}

std::string DomainList::toString(){
    std::string finalString = "";
    for(std::set<std::string>::iterator it=this->elements->begin(); it!= this->elements->end(); ++it){
        finalString += "  '";
        finalString += *it;
        finalString += "'";
        if (it != this->elements->end()) {
            finalString += "\n";
        }
    }
//    for (size_t i = 0; i < this->elements->size(); i++) {
//        finalString += "\t'";
//        finalString += this->elements->at(i);
//        finalString += "'";
//        if (i != this->elements->size() - 1) {
//            finalString += "\n";
//        }
//    }
    return finalString;
}