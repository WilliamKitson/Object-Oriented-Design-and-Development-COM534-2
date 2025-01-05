/*
 * Copyright (c) 2024. William E. Kitson.
 */

class PasswordAuthenticator {
    fun valid(password: String): Boolean{
        if (!length(password)){
            return false
        }

        if (!uppercase(password)){
            return false
        }

        if (!lowercase(password)){
            return false
        }

        if (!numerals(password)){
            return false
        }

        return true
    }

    fun length(passwordIn: String): Boolean {
        return passwordIn.length >= 20
    }

    fun uppercase(passwordIn: String): Boolean{
        return passwordContains(passwordIn, "QWERTYUIOPASDFGHJKLZXCVBNM")
    }

    fun lowercase(passwordIn: String): Boolean{
        return passwordContains(passwordIn, "qwertyuiopasdfghjklzxcvbnm")
    }

    fun numerals(passwordIn: String): Boolean{
        return passwordContains(passwordIn, "1234567890")
    }

    private fun passwordContains(passwordIn: String, containsIn: String): Boolean{
        var matches = 0

        for (i in passwordIn) {
            matches += countMatches(i, containsIn)
        }

        return matches > 0
    }

    private fun countMatches(passwordCharacterIn: Char, containsIn: String): Int{
        var matches = 0

        for (i in containsIn) {
            matches += getMatch(passwordCharacterIn, i)
        }

        return matches
    }

    private fun getMatch(passwordCharIn: Char, charactersCharIn: Char): Int{
        if (passwordCharIn == charactersCharIn){
            return 1
        }

        return 0
    }

    fun getError(password: String): LastError {
        if(!length(password)){
            return LastError.PasswordTooShort
        }

        if(!uppercase(password)){
            return LastError.PasswordNoUppercases
        }

        if(!lowercase(password)){
            return LastError.PasswordNoLowercases
        }

        if(!numerals(password)){
            return LastError.PasswordNoNumerals
        }

        return LastError.NoError
    }
}