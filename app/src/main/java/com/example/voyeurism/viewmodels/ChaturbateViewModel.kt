package com.example.voyeurism.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyeurism.models.ChaturbateModel
import com.example.voyeurism.reposetorys.Chaturbate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChaturbateViewModel : ViewModel() {
    private var items: MutableLiveData<MutableList<ChaturbateModel>> = MutableLiveData()

    private val repo = Chaturbate()

    fun fetchData(context: Context, genderSelection: String): MutableLiveData<MutableList<ChaturbateModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            items.postValue(repo.parseJson(context, genderSelection))
        }
        return items
    }
}