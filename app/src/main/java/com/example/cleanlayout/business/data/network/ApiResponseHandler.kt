package com.example.cleanlayout.business.data.network

import com.example.cleanlayout.business.domain.state.DataState
import com.example.cleanlayout.business.domain.state.MessageType
import com.example.cleanlayout.business.domain.state.Response
import com.example.cleanlayout.business.domain.state.StateEvent
import com.example.cleanlayout.business.domain.state.UIComponentType

abstract class ApiResponseHandler<ViewState, Data>(private val response: ApiResult<Data?>, private val stateEvent: StateEvent?) {
    suspend fun getResult(): DataState<ViewState> {
        return when (response) {
            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\n" + "Reason: ${response.errorMessage}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }
            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\n" + "Reason: ${NetworkErrors.NETWORK_ERROR}",
                        uiComponentType = UIComponentType.Dialog(),
                        messageType = MessageType.Error()
                    ),
                    stateEvent = stateEvent
                )
            }
            is ApiResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\n" + "Reason: ${NetworkErrors.NETWORK_DATA_NULL}",
                            uiComponentType = UIComponentType.Dialog(),
                            messageType = MessageType.Error()
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleSuccess(response.value)
                }
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>
}