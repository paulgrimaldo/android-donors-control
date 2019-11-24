package com.grimaldo.apps.controldonantes.register

import com.grimaldo.apps.controldonantes.domain.User
import java.util.*

class UserEvent(source: Any, val target: User) : EventObject(source) {
}