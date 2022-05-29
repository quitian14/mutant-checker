package org.quitian.mutantchecker.services;

import org.quitian.mutantchecker.exceptions.BadDNASequenceException;
import org.quitian.mutantchecker.model.entities.DNAValidation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Component to validate a dna sequence.
 */
@Component
public class DNAValidator {

    /**
     * Validate length and allowed characters. Verify the NxN restriction
     * @param list list of adn sequences
     * @throws BadDNASequenceException when the validation fails.
     */
    public void validateLengthAndLetter(String[] list) throws BadDNASequenceException {
        int length = list.length;
        Pattern pat = Pattern.compile("[ATGC]+");

        for (int i = 0; i < length; i++) {
            String value = list[i];

            if (value.length() != length) {
                throw new BadDNASequenceException("La cantidad de letras en la posicion " + (i + 1) + " con valor " + value + " no concuerda con la cantidad de registros en el arreglo que es " + length);
            }

            Matcher mat = pat.matcher(value);

            if (!mat.matches()) {
                throw new BadDNASequenceException("La cadena ingresada " + value + " debe contener solo estas letras A, T, G, C mayusculas");
            }
        }
    }

    /**
     * Transform the array of sequences to a matrix for processing
     * @param list list of adn sequences.
     * @return matrix of characters
     */
    public String[][] convertStringToMatrix(String[] list) {
        int length = list.length;
        String[][] adn = new String[length][length];

        for (int i = 0; i < length; i++) {
            String value = list[i];
            char[] valueArray = value.toCharArray();

            for (int j = 0; j < valueArray.length; j++) {
                adn[i][j] = valueArray[j] + "";
            }
        }

        return adn;
    }

    /**
     * Extract the vertical matrix character´s sequences
     * @param list matrix of characters.
     * @return list of sequences.
     */
    public String[] getVertical(String[][] list) {
        int length = list.length;
        String[] vertical = new String[length];

        for (String[] strings : list) {
            for (int j = 0; j < strings.length; j++) {
                String value = strings[j];
                vertical[j] = vertical[j] != null ? vertical[j] + value : value;
            }
        }

        return vertical;
    }

    /**
     * Extract the diagonal right matrix character´s sequences
     * @param list matrix of characters.
     * @return list of sequences.
     */
    public String[] getDiagonalRight(String[][] list) {
        int length = list.length;
        String[] horizontal = new String[length + (length - 1)];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < list[i].length; j++) {
                String value = list[i][j];

                if (i == j) {
                    horizontal[i - j] = horizontal[i - j] == null ? value : horizontal[i - j] + value;
                }

                if (j > i) {
                    horizontal[j - i] = horizontal[j - i] == null ? value : horizontal[j - i] + value;
                }

                if (i > j) {
                    int index = ((length - 1) - j) + i;
                    horizontal[index] = horizontal[index] == null ? value : horizontal[index] + value;
                }
            }
        }

        return horizontal;
    }

    /**
     * Extract the diagonal left matrix character´s sequences
     * @param list matrix of characters.
     * @return list of sequences.
     */
    public String[] getDiagonalLeft(String[][] list) {
        int length = list.length;
        String[] horizontal = new String[length + (length - 1)];

        for (int i = 0; i < length; i++) {
            for (int j = (list[i].length - 1); j >= 0; j--) {
                String value = list[i][j];

                if ((i + j) == (length - 1)) {
                    int index = (i + j) - (length - 1);
                    horizontal[index] = horizontal[index] == null ? value : horizontal[index] + value;
                }

                if ((i + j) < (length - 1)) {
                    int index = (length - 1) - (j + i);
                    horizontal[index] = horizontal[index] == null ? value : horizontal[index] + value;
                }

                if ((i + j) > (length - 1)) {
                    int index = j + i;
                    horizontal[index] = horizontal[index] == null ? value : horizontal[index] + value;
                }
            }
        }

        return horizontal;
    }

    /**
     * When getDiagonalLeft, getDiagonalRight, getVertical extract the adn sequences
     * this method verify the 4 same continous pattern.
     * @param dna matrix of characters.
     * @return list of sequences.
     */
    public boolean validateRepeatedCharacters(String[] dna) throws BadDNASequenceException {
        String[] adnLetters = {"AAAA", "TTTT", "CCCC", "GGGG"};

        for (String value : dna) {
            if (Arrays.stream(adnLetters).anyMatch(value::contains)) return true;
        }

        return false;
    }

    /**
     * DNA orchestrate all the process
     *
     * @param dna list of dna strings.
     * @return a validation.
     */
    public DNAValidation validate(String[] dna) throws BadDNASequenceException {

        // validation initial
        validateLengthAndLetter(dna);

        // validate horizontal
        boolean isMutant = validateRepeatedCharacters(dna);

        if (!isMutant) {

            String[][] adnMatrix = convertStringToMatrix(dna);

            isMutant = validateRepeatedCharacters(getVertical(adnMatrix));

            if (!isMutant) {
                isMutant = validateRepeatedCharacters(getDiagonalRight(adnMatrix));
            }

            if (!isMutant) {
                isMutant = validateRepeatedCharacters(getDiagonalLeft(adnMatrix));
            }
        }

        return new DNAValidation(Arrays.toString(dna), isMutant);
    }
}  

