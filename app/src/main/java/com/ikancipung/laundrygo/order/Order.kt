package com.ikancipung.laundrygo.order

data class Order(
    val id: String? = null, // Menambahkan ID
    val LaundryName: String = "",
    val isCompleted: Boolean = false,
    val time: String = "",
    val date: String = "",
    val category: Int = 0,
    val process: Int = 0,
    val processTimes: List<Pair<String, String>> = emptyList()
) {
    // Konstruktor kosong diperlukan untuk Firebase atau serialisasi lainnya
    constructor() : this(null, "", false, "", "", 0, 0, emptyList())
}