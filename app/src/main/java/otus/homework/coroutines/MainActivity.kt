package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    //lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()
    val catsViewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        catsViewModel.setCatService(diContainer.service)

        catsViewModel.getCatsFact()
        view.findViewById<Button>(R.id.button).setOnClickListener{
            catsViewModel.getCatsFact()
        }

        //view.presenter = catsPresenter
        //catsPresenter.attachView(view)
        //catsPresenter.onInitComplete()

        val catsObserver = Observer<CatsViewModel.Result> { result ->
            // Update the UI, in this case, a TextView.
            if (result is CatsViewModel.Result.Success) {
                view.populate(FactAndPicture(result.factAndPicture.fact, result.factAndPicture.picture))
            } else if (result is CatsViewModel.Result.Error){
                view.showError(result.message)


            }

        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        catsViewModel.getObservableData().observe(this, catsObserver)


    }




    override fun onStop() {

        if (isFinishing) {
          //  catsPresenter.detachView()
        }
        super.onStop()
    }
}

