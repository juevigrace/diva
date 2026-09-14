package io.github.juevigrace.diva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// TODO: Actually implement this
abstract class DivaViewModel : ViewModel() {
    protected val scope = viewModelScope
}
