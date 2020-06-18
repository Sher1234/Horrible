package info.horriblesubs.sher.data.mal.api.model.enums

class IncompatibleEnumException : Exception {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor() : super()
}