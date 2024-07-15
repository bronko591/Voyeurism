package com.example.voyeurism.reposetorys

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.voyeurism.models.ChaturbateModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class Chaturbate {
    private companion object {
        const val urlSufix:String = "page="
        const val urlDetails:String = "https://chaturbate.com/"
        const val jsonUrl:String = "https://chaturbate.com/affiliates/api/onlinerooms/?format=json&wm=P3YWn"
    }

    fun parseJson(context: Context, genderSelection:String):MutableList<ChaturbateModel> {
        val listModels:MutableList<ChaturbateModel> = mutableListOf()
        val queue = Volley.newRequestQueue(context)
        val request = JsonArrayRequest(Request.Method.GET, jsonUrl, null, { response ->
            try {
                for (i in 0 until response.length()) {
                    val respObj = response.getJSONObject(i)
                    if (respObj.getString("gender") == genderSelection ){
                        val gender:String = respObj.getString("gender")
                        val name:String = respObj.getString("username")
                        val image:String = respObj.getString("image_url")
                        val location:String = respObj.getString("location")
                        val hd:String = respObj.getString("is_hd")
                        val age:String = respObj.getString("age")
                        val description:String = respObj.getString("room_subject")
                        val views:String = respObj.getString("num_users")
                        val link = "https://chaturbate.com/$name/"
                        listModels.add(ChaturbateModel(name, image, hd, age, gender, description, views, link))
                        // Log("CAM SEX!","$name" + "$image" + "$hd" + "$age" + "$gender" + "$description" + "$location" + "$views" + "$link")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("CAM SEX! ", e.printStackTrace().toString())
            }
        }, { error ->
            Toast.makeText(context, "Fail to get response", Toast.LENGTH_SHORT).show()
            Log.d("CAM SEX! ", error.toString())
        })
        queue.add(request)
        return listModels
    }

    fun parseImage(url:String):String {
        var imageString = ""
        val html:String = jsoupToHtml(url)
        val reg = "<meta property=\"og:image\" content=\"([^\"\"]*)\" />".toRegex()
        reg.findAll(html).forEach {
            imageString = it.groupValues[0]
        }
        return imageString
    }

    fun parseVideo(url:String):String{
        val html:String = jsoupToHtml(url)
        val rexx = """http(s)?://([\w+?\.\w+])+([a-zA-Z0-9\~\!\@\#\$\%\^\&\*\(\)_\-\=\+\\\/\?\.\:\;\'\,]*)?.m3u8""".toRegex()
        val finds = rexx.findAll(html)
        finds.forEach {
            var rep: String = it.groupValues[0]
            rep = rep.replace("\\u0027", "'").replace("\\u0022", "\"").replace("\\u002D", "-").replace("\\u005C", "\"").replace("u003D", "=")
            //  Log.d("", rep)
            return rep
        }
        return ""
    }

    fun parseCams(url:String, pageCount:Int):MutableList<ChaturbateModel>{
        val listData = mutableListOf<ChaturbateModel>()
        val doc: Document = jsoupToDocument(url + urlSufix + pageCount)
        val tagName = doc.tagName("li")
        for (tag in tagName.getElementsByClass("room_list_room")){
            val name:String = tag.getElementsByClass("no_select").attr("href").replace("/", "")
            val image:String = tag.getElementsByClass("png").attr("src")
            val age:String = isINT(tag.getElementsByClass("age").text())
            val label:String = tag.getElementsByClass("thumbnail_label").text()
            val views:String = tag.getElementsByClass("cams").text()
            val descriptions:String = tag.getElementsByClass("subject").text()
            val link:String = "https://chaturbate.com" + tag.getElementsByClass("no_select").attr("href")
            //listData.add(ChaturbateModel(name,image,label,age,descriptions,views,link))
            Log.d("", name)
        }
        return listData
    }

    private fun isINT(value:String):String{
        val check = "[0-9]+".toRegex()
        return if(value.matches(check)){ value } else{""}
    }

    private fun jsoupToHtml(Url:String): String{
        var html:String? = null
        try{
            html = Jsoup.connect(Url).get().html()
        } catch (e: Exception) {
            Log.e("Voyeurism ", "Error parsing " + e.message, e)
        }
        return html!!
    }

    private fun jsoupToDocument(Url:String): Document {
        var doc: Document? = Document(Url)
        try{
            doc = Jsoup.connect(Url).get()
        } catch (e: Exception) {
            Log.e("Voyeurism ", "Error parsing " + e.message, e)
        }
        return doc!!
    }

}