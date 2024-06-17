package com.example.caesarzonapplication.ui.components

import androidx.compose.runtime.Composable
import java.util.LinkedList

@Composable
fun SearchHistoryList (search: String){
    val _searchList: MutableList<String> = LinkedList()
    var searchList: List<String> = _searchList
    if(!_searchList.contains(search))
        _searchList.add(search)
    if(_searchList.size>5)
        _searchList.removeFirst()
}