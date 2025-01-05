/*
 * Copyright (c) 2024. William E. Kitson.
 */

enum class LastError {
    NoError,
    UsernameDuplicated,
    UsernameEmpty,
    PasswordTooShort,
    PasswordNoUppercases,
    PasswordNoLowercases,
    PasswordNoNumerals,
    UsernameUnregistered,
    PasswordIncorrect,
    RoomAlreadyExists;

    override fun toString(): String {
        return when (this) {
            NoError -> ""
            UsernameEmpty -> "Error: you have not specified a username."
            UsernameDuplicated -> "Error: username duplicated."
            PasswordTooShort -> "Error: password too short."
            PasswordNoUppercases -> "Error: password no uppercases."
            PasswordNoLowercases -> "Error: password no lowercases."
            PasswordNoNumerals -> "Error: password no numerals."
            UsernameUnregistered -> "Error: username unregisted."
            PasswordIncorrect -> "Error: password incorrect."
            RoomAlreadyExists -> "Error: room already exists."
        }
    }
}