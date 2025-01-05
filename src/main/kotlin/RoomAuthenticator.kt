class RoomAuthenticator {
    fun authenticate(room: Room, rooms: MutableList<Room>): Boolean {
        if (room.number.isEmpty()) {
            return false
        }

        if (room.building.isEmpty()) {
            return false
        }

        if (room.compType.isEmpty()) {
            return false
        }

        if (roomExists(room, rooms)) {
            return false
        }

        return true
    }

    private fun roomExists(room: Room, rooms: MutableList<Room>): Boolean {
        for (i in rooms) {
            if (roomMatches(i, room)) {
                return true
            }
        }

        return false
    }

    private fun roomMatches(first: Room, second: Room): Boolean {
        if (first.number != second.number) {
            return false
        }

        if (first.building != second.building) {
            return false
        }

        return true
    }

    fun getLastError(room: Room, rooms: MutableList<Room>): LastError {
        if (room.number.isEmpty()) {
            return LastError.RoomNumberEmpty
        }

        if (room.building.isEmpty()) {
            return LastError.RoomBuildingEmpty
        }

        if (room.compType.isEmpty()) {
            return LastError.RoomComputerTypeEmpty
        }

        if (roomExists(room, rooms)) {
            return LastError.RoomAlreadyExists
        }

        return LastError.NoError
    }
}