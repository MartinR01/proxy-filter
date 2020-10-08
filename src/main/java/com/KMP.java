package com;

/**
 * objects of the KMP class represent one individual
 * configuration of KMP sequence-search algorithm.
 * KMP algorithm doesn't require backtracking,
 * making it ideal for stream scanning.
 *
 * Supports continuous search for a sequence across several buffers
 * (including overlapping).
 */
public class KMP {
    /** Sequence the object will search for */
    private final char[] sequence;
    /** Longest prefix that equals suffix (on a substring) */
    private final int[] error;

    /** Counts matched characters */
    private int matched = 0;

    /**
     * Pre-calculates needed internal variables of the KMP algorithm
     * @param sequence text sequence this instance will be used to search for
     */
    public KMP(char[] sequence){
        this.sequence = sequence;
        this.error = new int[sequence.length];

        int j = 0;
        for (int i = 1; i < sequence.length; i++){
            while(j > 0 && sequence[j] != sequence[i]){
                j = error[j-1];
            }
            if (sequence[j] == sequence[i]){
                j++;
            }
            error[i] = j;
        }
    }

    public KMP(String sequence){
        this(sequence.toCharArray());
    }

    /**
     * Search the buffer for configured sequence.
     * Allows to limit the actual search scope.
     * Keeps track of result across several searches
     * @param text buffer to search in.
     * @param maxLength scope of search can be limited if the whole buffer is not used
     * @return index of last character of the found sequence or -1 if not found.
     */
    public int search(byte[] text, int maxLength){
        for(int i = 0; i < maxLength; i++) {
            if (text[i] == sequence[matched]) {
                matched++;
                if (matched == sequence.length) {
                    return i;
                }
            } else {
                // set to value according to error field
                matched = (matched > 0) ? error[matched - 1] : 0;
            }
        }
        return -1;
    }

    /**
     * Resets internal status of the algorithm so as to restore to the newly created state.
     */
    public void reset(){
        this.matched = 0;
    }
}
