package com.ivarvisser.cineapp.domain

/**
 * Factory object responsible for creating a 2D grid representation of seats (a list of [SeatRow])
 * from a flat list of [Seat] instances.
 *
 * The factory builds rows from index 0..maxRow and columns from 0..maxCol where `maxRow` and
 * `maxCol` are computed from the provided `allSeats` list. For any missing seat at a (row, col)
 * position a [SeatCell] is created with `seat = null` and `colorGroup = -1`.
 *
 * NOTES:
 * - This object does not persist or mutate seats; it only produces an in-memory layout structure.
 * - The implementation assumes seat row/col indices are zero-based integers.
 * - If `allSeats` is empty, the current implementation will throw because it uses [kotlin.collections.maxOf].
 *
 * See also:
 * - `Seat` - the domain model representing a single seat (expected to have `row`, `col`,
 *   and `virtualCol` properties used by this factory).
 * - `SeatRow` - container for a single row of [SeatCell] instances produced by this factory.
 * - `SeatCell` - container that wraps a nullable [Seat] and a color-group integer used by the UI.
 */
object SeatFactory {

    /**
     * Build a rectangular grid (list of [SeatRow]) containing [SeatCell] elements from a flat list
     * of [Seat] objects.
     *
     * Algorithm (high level):
     * 1. Determine the maximum row and column indices present in `allSeats`.
     * 2. For each row index from 0..maxRow:
     *    a. Filter seats that belong to this row and create a map keyed by column index.
     *    b. For each column index from 0..maxCol:
     *       - If a seat exists at (rowIndex, colIndex) wrap it in a [SeatCell] and set `colorGroup`
     *         to `seat.virtualCol`.
     *       - If no seat exists at that position create a [SeatCell] with `seat = null` and
     *         `colorGroup = -1`.
     *    c. Wrap the row's cells in a [SeatRow] and include it in the result.
     *
     * Complexity:
     * - Time: O(n + R*C) where n = number of seats in `allSeats`, R = maxRow + 1, C = maxCol + 1.
     * - Space: O(R*C) for the returned grid of seat cells.
     *
     * Important behavior details:
     * - The method uses [kotlin.collections.maxOf] which will throw a [NoSuchElementException]
     *   if `allSeats` is empty. Callers should ensure the list is non-empty or filter/guard accordingly.
     * - Missing seats in the rectangular grid are represented as `SeatCell(seat = null, colorGroup = -1)`.
     *
     * @param allSeats Flat list of [Seat] objects from which to build the grid. Seats must expose
     *                 an integer `row` and `col` for positioning and `virtualCol` for color grouping.
     * @return A list of [SeatRow] representing rows (from rowIndex = 0 to maxRow) each containing
     *         a list of [SeatCell] for columns 0..maxCol.
     *
     * @throws NoSuchElementException if `allSeats` is empty (due to use of [maxOf]).
     *
     * Example:
     * ```
     * // Given seats:
     * val seats = listOf(
     *     Seat(id = "A1", row = 0, col = 0, virtualCol = 0, type = SeatType.REGULAR),
     *     Seat(id = "A2", row = 0, col = 1, virtualCol = 1, type = SeatType.REGULAR),
     *     Seat(id = "B1", row = 1, col = 0, virtualCol = 0, type = SeatType.REGULAR)
     * )
     *
     * // buildSeatGrid(seats) will produce 2 rows (row 0 and row 1) and 2 columns (col 0 and col 1).
     * // Position (1,1) will be a SeatCell with seat = null and colorGroup = -1.
     * ```
     */
    fun buildSeatGrid(allSeats: List<Seat>): List<SeatRow> {
        val maxRow = allSeats.maxOf { it.row }
        val maxCol = allSeats.maxOf { it.col }

        return (0..maxRow).map { rowIndex ->

            val seatsByCol = allSeats
                .filter { it.row == rowIndex }
                .associateBy { it.col }

            val cells = (0..maxCol).map { colIndex ->
                val seat = seatsByCol[colIndex]

                SeatCell(
                    seat = seat,
                    colorGroup = seat?.virtualCol ?: -1
                )
            }

            SeatRow(
                rowIndex = rowIndex,
                seats = cells
            )
        }
    }
}