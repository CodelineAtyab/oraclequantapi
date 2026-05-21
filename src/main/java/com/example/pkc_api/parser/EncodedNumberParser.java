package com.example.pkc_api.parser;

import org.springframework.stereotype.Component;

@Component
public class EncodedNumberParser implements NumberParser {

    @Override
    public ParsedNumber parse(String input, int startIndex) {
        if (input == null || startIndex >= input.length()) {
            return new ParsedNumber(0, startIndex);
        }

        char current = input.charAt(startIndex);

        if (isContinuationCharacter(current)) {
            return parseMultiCharacterNumber(input, startIndex);
        }

        int value = getCharacterValue(current);
        return new ParsedNumber(value, startIndex + 1);
    }

    private ParsedNumber parseMultiCharacterNumber(String input, int index) {
        int total = 0;

        while (index < input.length() && isContinuationCharacter(input.charAt(index))) {
            total += 26;
            index++;
        }

        if (index < input.length()) {
            total += getCharacterValue(input.charAt(index));
            index++;
        }

        return new ParsedNumber(total, index);
    }

    private int getCharacterValue(char character) {
        character = Character.toLowerCase(character);

        if (character >= 'a' && character <= 'z') {
            return character - 'a' + 1;
        }

        return 0;
    }

    private boolean isContinuationCharacter(char character) {
        return Character.toLowerCase(character) == 'z';
    }
}
