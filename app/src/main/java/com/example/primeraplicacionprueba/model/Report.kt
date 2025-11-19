package com.example.primeraplicacionprueba.model

import com.google.firebase.Timestamp

enum class ReportStatus {
    PENDING,
    RESOLVED,
    DISMISSED
}

enum class ReportReason {
    INAPPROPRIATE_CONTENT,
    WRONG_INFORMATION,
    DUPLICATE,
    SPAM,
    CLOSED_PERMANENTLY,
    OTHER
}

data class Report(
    var id: String = "",
    val placeId: String = "",
    val reporterId: String = "",
    val reason: ReportReason = ReportReason.OTHER,
    val description: String = "",
    val status: ReportStatus = ReportStatus.PENDING,
    val createdDate: Timestamp = Timestamp.now(),
    val resolvedDate: Timestamp? = null,
    val resolvedBy: String? = null
)
