#ifndef STATE_H
#define STATE_H

#include <string>

/**
 * The states of the finite state machine defined in an enumerated type.
 */
enum State {Comma, Period, Q_Mark, Left_Paren, Right_Paren, SawColon, Colon_Dash, Multiply, Add, SawAQuote, 
			ProcessingString, PossibleEndOfString, Start, End, Equals, Exclamation, Less, More, Undefined, 
			Comment, PossibleEndOfComment, ProcessingComment, BlockComment, ProcessingBlockComment,
			PossibleEndOfBlockComment, SawPound, ProcessingId, PossibleEndOfId, SchemesS1, SchemesC, SchemesH,
			SchemesE1, SchemesM, SchemesE2, SchemesS2, FactsF, FactsA, FactsC, FactsT, FactsS, RulesR, RulesU, 
			RulesL, RulesE, RulesS, QueriesQ, QueriesU, QueriesE1, QueriesR, QueriesI, QueriesE2, QueriesS,
			PossibleEndOfFacts, PossibleEndOfSchemes, PossibleEndOfRules, PossibleEndOfQueries, Whitespace
           };

    /**
     * Converts a state to a string.
     * 
     * Parameters: state -- the state to be converted to a string.
     * PreCondition: none
     * Postcondition: result = the string representation of the state which
     *                         looks exactly like its State name.
     */
    std::string StateToString(State state);
#endif
