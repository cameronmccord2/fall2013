#include "Utils.h"

#include <sstream>

using namespace std;
string& itoa(string& answer, int i) {
    stringstream ss;
    ss << i;// m
    answer = ss.str();
    return answer;
}
