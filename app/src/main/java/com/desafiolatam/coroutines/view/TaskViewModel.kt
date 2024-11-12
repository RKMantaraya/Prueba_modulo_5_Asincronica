package com.desafiolatam.coroutines.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desafiolatam.coroutines.data.TaskEntity
import com.desafiolatam.coroutines.repository.TaskRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepositoryImp
) : ViewModel() {

    private val _data: MutableStateFlow<List<TaskEntity>> = MutableStateFlow(emptyList())
    val taskListStateFlow: StateFlow<List<TaskEntity>> = _data.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTasks().collectLatest {
                _data.value = it
            }
        }
    }

    fun addTask(title:String, description:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(TaskEntity(0, title, description, false))
        }

    }

    fun deleteTask(task:TaskEntity){
        viewModelScope.launch ( Dispatchers.IO){
            repository.deleteTask(task)
        }
    }

    fun completeTask(task: TaskEntity, isCompleted:Boolean){
        viewModelScope.launch(Dispatchers.IO){
            repository.isTaskCompleted(task, isCompleted)
        }
    }
}
