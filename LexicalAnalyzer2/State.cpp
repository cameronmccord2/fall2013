#include "State.h"

using namespace std;

string StateToString(State tokenType){
    string result = "";
    switch(tokenType){
        case Comma:                      result = "Comma"; break;
        case Period:                     result = "Period"; break;
        case SawColon:                   result = "SawColon"; break;
        case Colon_Dash:                 result = "Colon_Dash"; break;
        case SawAQuote:                  result = "SawAQuote"; break;
        case ProcessingString:           result = "ProcessingString"; break;
        case PossibleEndOfString:        result = "PossibleEndOfString"; break;
        case Start:                      result = "Start"; break;
        case End:                        result = "End"; break;
    }
    return result;
};
