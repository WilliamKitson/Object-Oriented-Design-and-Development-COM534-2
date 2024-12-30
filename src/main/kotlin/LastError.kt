/*
 * Copyright (c) 2024. William E. Kitson.
 */

package domain

enum class LastError {
    NoError,
    UsernameDuplicated,
    PasswordTooShort,
    PasswordNoUppercases,
    PasswordNoLowercases,
    PasswordNoNumerals,
    UsernameUnregistered;

    override fun toString(): String {
        return when (this) {
            NoError -> ""
            UsernameDuplicated -> "Error: username duplicated."
            PasswordTooShort -> "Error: password too short."
            PasswordNoUppercases -> "Error: password no uppercases."
            PasswordNoLowercases -> "Error: password no lowercases."
            PasswordNoNumerals -> "Error: password no numerals."
            UsernameUnregistered -> "Error: username unregisted."
        }
    }
}