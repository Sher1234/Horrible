package info.horriblesubs.sher.data.mal.api.model.main.meta

import com.google.gson.annotations.SerializedName

data class Status (
    @SerializedName("total_connections_received") var totalConnectionsReceived: String? = null,
    @SerializedName("requests_this_month") var requestsThisMonth: Int? = null,
    @SerializedName("connected_clients") var connectedClients: String? = null,
    @SerializedName("requests_this_week") var requestsThisWeek: Int? = null,
    @SerializedName("cached_requests") var cachedRequests: Int? = null,
    @SerializedName("requests_today") var requestsToday: Int? = null
)