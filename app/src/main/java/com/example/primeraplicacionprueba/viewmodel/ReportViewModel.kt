package com.example.primeraplicacionprueba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.primeraplicacionprueba.R
import com.example.primeraplicacionprueba.model.Report
import com.example.primeraplicacionprueba.model.ReportStatus
import com.example.primeraplicacionprueba.utils.RequestResult
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val app: Application = getApplication()
    private val db = FirebaseFirestore.getInstance()

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    private val _reportResult = MutableStateFlow<RequestResult?>(null)
    val reportResult: StateFlow<RequestResult?> = _reportResult

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("reports").get().await()
                val reportsList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Report::class.java)?.apply {
                        id = doc.id
                    }
                }
                _reports.value = reportsList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun create(report: Report) {
        viewModelScope.launch {
            _reportResult.value = RequestResult.Loading
            _reportResult.value = runCatching { createFirebase(report) }
                .fold(
                    onSuccess = {
                        RequestResult.Success(message = app.getString(R.string.msg_report_created))
                    },
                    onFailure = {
                        RequestResult.Failure(errorMessage = it.message ?: app.getString(R.string.error_creating_report))
                    }
                )
        }
    }

    private suspend fun createFirebase(report: Report): Report {
        val docRef = db.collection("reports").document()
        val reportWithId = report.copy(id = docRef.id)
        docRef.set(reportWithId).await()
        loadReports() // Reload after creating
        return reportWithId
    }

    fun resolveReport(reportId: String, adminId: String) {
        viewModelScope.launch {
            try {
                db.collection("reports")
                    .document(reportId)
                    .update(
                        mapOf(
                            "status" to ReportStatus.RESOLVED.name,
                            "resolvedDate" to Timestamp.now(),
                            "resolvedBy" to adminId
                        )
                    ).await()
                loadReports() // Reload after resolving
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dismissReport(reportId: String, adminId: String) {
        viewModelScope.launch {
            try {
                db.collection("reports")
                    .document(reportId)
                    .update(
                        mapOf(
                            "status" to ReportStatus.DISMISSED.name,
                            "resolvedDate" to Timestamp.now(),
                            "resolvedBy" to adminId
                        )
                    ).await()
                loadReports() // Reload after dismissing
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPendingReportsCount(): Int {
        return _reports.value.count { it.status == ReportStatus.PENDING }
    }

    fun getResolvedReportsCount(): Int {
        return _reports.value.count { it.status == ReportStatus.RESOLVED }
    }

    fun getDismissedReportsCount(): Int {
        return _reports.value.count { it.status == ReportStatus.DISMISSED }
    }

    fun getReportsHandledByAdmin(adminId: String): Int {
        return _reports.value.count {
            it.resolvedBy == adminId && (it.status == ReportStatus.RESOLVED || it.status == ReportStatus.DISMISSED)
        }
    }

    fun getProblematicPlacesCount(): Int {
        // Count places that have 2 or more pending reports
        val placeReportCounts = _reports.value
            .filter { it.status == ReportStatus.PENDING }
            .groupBy { it.placeId }
            .mapValues { it.value.size }

        return placeReportCounts.count { it.value >= 2 }
    }

    fun clearReportResult() {
        _reportResult.value = null
    }
}
