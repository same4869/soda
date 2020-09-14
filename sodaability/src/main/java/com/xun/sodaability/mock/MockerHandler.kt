package com.xun.sodaability.mock

import com.xun.sodaability.mock.annotation.MOCK
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * Created by javalong on 2018/7/8.
 */
class MockerHandler<T>(internal var retrofit: Retrofit, internal var api: T) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {
        val isExist = method.isAnnotationPresent(MOCK::class.java)
        if (isExist) {
            val mock = method.getAnnotation(MOCK::class.java)
            if (!mock.enable) {
                return method.invoke(api, args)
            } else {
                if (mock.value.startsWith("http")) {
                    //如果是http的 就尝试自己去请求,就自己修改下url 然后请求
                    preLoadServiceMethod(method, mock.value)
                    if (args == null) {
                        return method.invoke(api)
                    } else {
                        when (args.size) {
                            1 -> return method.invoke(api, args[0])
                            2 -> return method.invoke(api, args[0], args[1])
                            3 -> return method.invoke(api, args[0], args[1], args[2])
                            4 -> return method.invoke(api, args[0], args[1], args[2], args[3])
                            5 -> return method.invoke(
                                api,
                                args[0],
                                args[1],
                                args[2],
                                args[3],
                                args[4]
                            )
                            6 -> return method.invoke(
                                api,
                                args[0],
                                args[1],
                                args[2],
                                args[3],
                                args[4],
                                args[5]
                            )
                            7 -> return method.invoke(
                                api,
                                args[0],
                                args[1],
                                args[2],
                                args[3],
                                args[4],
                                args[5],
                                args[6]
                            )
                            8 -> return method.invoke(
                                api,
                                args[0],
                                args[1],
                                args[2],
                                args[3],
                                args[4],
                                args[5],
                                args[6],
                                args[7]
                            )
                        }
                        return method.invoke(api, args)
                    }
                } else {
                    //认为是在assets中
                    val response = readAssets(mock.value)
                    val responseObj = retrofit.nextResponseBodyConverter<Any>(
                        null,
                        getReturnTye(method),
                        method.annotations
                    )
                        .convert(
                            ResponseBody.create(
                                "application/json".toMediaTypeOrNull(),
                                response
                            )
                        )
                    return (retrofit.nextCallAdapter(
                        null,
                        method.genericReturnType,
                        method.annotations
                    ) as CallAdapter<Any, Any>).adapt(
                        MockerCall(
                            responseObj
                        )
                    )
                }
            }
        } else {
            //如果method有mock注解，就处理下，如果没有，就直接调用后返回
            if (args == null) {
                return method.invoke(api)
            } else {
                when (args.size) {
                    1 -> return method.invoke(api, args[0])
                    2 -> return method.invoke(api, args[0], args[1])
                    3 -> return method.invoke(api, args[0], args[1], args[2])
                    4 -> return method.invoke(api, args[0], args[1], args[2], args[3])
                    5 -> return method.invoke(api, args[0], args[1], args[2], args[3], args[4])
                    6 -> return method.invoke(
                        api,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5]
                    )
                    7 -> return method.invoke(
                        api,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5],
                        args[6]
                    )
                    8 -> return method.invoke(
                        api,
                        args[0],
                        args[1],
                        args[2],
                        args[3],
                        args[4],
                        args[5],
                        args[6],
                        args[7]
                    )

                }
                return method.invoke(api, args)
            }
        }
    }

    private fun getReturnTye(method: Method): Type {
        return (method.getGenericReturnType() as ParameterizedType).getActualTypeArguments()[0]
    }

    private fun preLoadServiceMethod(method: Method, relativeUrl: String) {
        try {
            val m =
                Retrofit::class.java!!.getDeclaredMethod("loadServiceMethod", Method::class.java)
            m.isAccessible = true
            val serviceMethod = m.invoke(retrofit, method)
            val field = serviceMethod.javaClass.getDeclaredField("relativeUrl")
            field.isAccessible = true
            field.set(serviceMethod, relativeUrl)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun readAssets(fileName: String): String {
        try {
            val `is` = this.javaClass.getResourceAsStream("/assets/${fileName}");
            val size = `is`!!.available()
            // Read the entire asset into a local byte buffer.
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            // Convert the buffer into a string.
            // Finally stick the string into the text view.
            return String(buffer, Charset.forName("utf-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "读取错误，请检查文件名"
    }

}