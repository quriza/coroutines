package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
//import javax.inject.Inject


class CatsViewModel : ViewModel() {
    private var job: Job? = null


    private var result = MutableLiveData<Result>()
    lateinit var catsService: CatsService

    sealed class Result {
        data class Success(val factAndPicture: FactAndPicture) : Result()
        data class Error(val message: String): Result()
    }

    fun setCatService(service: CatsService) {
        catsService = service
    }

    fun getCatsFact() {
        var picture: Picture? = null
        var fact: Fact? = null

        job = viewModelScope.
        launch(Dispatchers.Main + CoroutineName("v1coroutine")) {
            try {
                coroutineScope {
                    val one = async {
                        fact = catsService.getCatFact()
                    }
                    val two = async {
                        picture = catsService.getCatPicture()
                    }

                    awaitAll(one,two)
                    result.value = Result.Success(FactAndPicture(fact!!,picture!!))
                }
            }  catch (e: java.net.SocketTimeoutException) {
                 result.value = Result.Error("Таймаут")
            }
        }

    }


    fun getObservableData(): MutableLiveData<Result> {
        return result
    }



    override fun onCleared() {
         super.onCleared()
         job?.cancel()
     }
}