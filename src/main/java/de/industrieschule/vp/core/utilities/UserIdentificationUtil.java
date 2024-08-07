package de.industrieschule.vp.core.utilities;

public class UserIdentificationUtil {

    /**
     * Retrieves the requested user ID based on the provided input string.
     *
     * <p>If the input string is "@me", the method returns the ID of the current user.
     * Otherwise, it attempts to parse and return the user ID from the input string.</p>
     *
     * @param input The input string representing a user ID or the "@me" keyword.
     * @param selfUserId The ID of the current user.
     * @return The requested user ID. If "@me" is provided as the input, it returns the selfUserId.
     * @throws NullPointerException If the input string is null.
     * @throws NumberFormatException If the input string cannot be parsed as an integer.
     */
    public static int getRequestedUserId(String input, int selfUserId){
        if(input == null){
            throw new NullPointerException("Input cannot be null");
        }

        if("@me".equals(input)){
            return selfUserId;
        }

        return Integer.parseInt(input);
    }
}
