/*
 * Copyright (c) 2024. William E. Kitson.
 */

enum class LastError {
    NoError,
    UsernameDuplicated,
    UsernameEmpty,
    PasswordEmpty,
    PasswordTooShort,
    PasswordNoUppercases,
    PasswordNoLowercases,
    PasswordNoNumerals,
    UsernameUnregistered,
    PasswordIncorrect,
    RoomAlreadyExists,
    RoomNumberEmpty;

    override fun toString(): String {
        return when (this) {
            NoError -> ""
            UsernameDuplicated -> "Error: username duplicated."
            UsernameEmpty -> "Error: you have not specified a username."
            PasswordEmpty -> "Error: you have not specified a password."
            PasswordTooShort -> "Error: password too short."
            PasswordNoUppercases -> "Error: password no uppercases."
            PasswordNoLowercases -> "Error: password no lowercases."
            PasswordNoNumerals -> "Error: password no numerals."
            UsernameUnregistered -> "Error: username unregisted."
            PasswordIncorrect -> "Error: password incorrect."
            RoomAlreadyExists -> "Error: room already exists."
            RoomNumberEmpty -> "Error: you have not specified a room number."
        }
    }
}