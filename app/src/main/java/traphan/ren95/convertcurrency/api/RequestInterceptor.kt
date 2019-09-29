package traphan.ren95.convertcurrency.api

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        var request = original.newBuilder().build()
        var response = chain.proceed(request)
        return response
    }
}