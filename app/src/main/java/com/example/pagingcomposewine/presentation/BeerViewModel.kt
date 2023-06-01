package com.example.pagingcomposewine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.pagingcomposewine.data.local.Beer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BeerViewModel @Inject constructor(
    pager: Pager<Int, Beer>
): ViewModel() {

    val beerPagingFlow = pager
        .flow
        .cachedIn(viewModelScope)
}