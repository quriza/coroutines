package otus.homework.coroutines

import com.google.gson.annotations.SerializedName

//{"file":"https:\/\/purr.objects-us-east-1.dream.io\/i\/20160826_092643.jpg"}
data class Picture (
    @field:SerializedName("file") val file: String
)