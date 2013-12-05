//
//  Node.h
//  DatalogProgram
//
//  Created by Cameron McCord on 12/4/13.
//  Copyright (c) 2013 McCord Inc. All rights reserved.
//

#ifndef __DatalogProgram__Node__
#define __DatalogProgram__Node__

#include <iostream>
//#include "Relation.h"
#include "DataRule.h"


class Node{
public:
    Node();
    Node(string identifier);
    ~Node();
    Relation *query;
    DataRule *dataRule;
    string identifier;// Q# where # is the number of the query given in the initial datalog program, or R#
    bool isQuery;
    set<Node> edges;
    void addEdge(Node newEdge);
    vector<string> dependencies;
    bool hasBackwardEdge;
    Node *backwardEdge;
    int postOrderNumber;
    map<string, string> backwardsEdges;// used only by query type
    vector<pair<string, int>> postOrderPairs;
    vector<string> rulesEvaluated;
    vector<string> ruleEvaluationOrder;
private:
};
#endif /* defined(__DatalogProgram__Node__) */
