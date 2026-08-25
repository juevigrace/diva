package io.github.juevigrace.diva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

abstract class DivaViewModel : ViewModel() {
    protected val scope = viewModelScope
}
