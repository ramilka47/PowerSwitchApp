package ru.power.selector.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UseCase<Request, Response> {

    suspend fun execute(request: Request, scope : CoroutineScope) : Response
}

suspend fun <Request, Response> UseCase<Request, Response>.execute(request: Request) = withContext(Dispatchers.IO){
    execute(request, this)
}

suspend fun UseCase<Unit, Unit>.execute() = execute(Unit)