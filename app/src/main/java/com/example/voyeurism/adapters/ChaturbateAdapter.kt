package com.example.voyeurism.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.voyeurism.R
import com.example.voyeurism.models.ChaturbateModel
import com.example.voyeurism.reposetorys.Chaturbate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChaturbateAdapter(private var context: Context) : RecyclerView.Adapter<ChaturbateAdapter.ViewHolder>() {
    private var items = mutableListOf<ChaturbateModel>()
    lateinit var listener : WorkoutClickListener

    interface WorkoutClickListener {
        fun onWorkoutClicked(hashTag: String)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        private var repo = Chaturbate()
        val modelName: TextView = itemView.findViewById(R.id.tv_name)
        val modelImage: ImageView = itemView.findViewById(R.id.iw_image)
        val modelAge: TextView = itemView.findViewById(R.id.tv_age)
        //  val modelLabel: TextView = itemView.findViewById(R.id.tv_label)
        val modelGender: ImageView = itemView.findViewById(R.id.iw_gender)
        val modelHd: TextView = itemView.findViewById(R.id.is_hd)
        val favoriteCheck: AppCompatCheckBox = itemView.findViewById(R.id.favorite_check)
        val modelDescription: TextView = itemView.findViewById(R.id.tv_description)
        val modelViews: TextView = itemView.findViewById(R.id.tv_views)
        val modelLink: TextView = itemView.findViewById(R.id.tv_link)
        init{itemView.setOnClickListener(this)}
        override fun onClick(v: View?) {
            if (this.modelLink.text.contains("chaturbate")) {
                repo = Chaturbate()
                var videoLink:String
                var imageLink:String
                scope.launch {
                    videoLink = repo.parseVideo(modelLink.text.toString()).toString()
                    imageLink = repo.parseImage(modelLink.text.toString())
                    withContext(Dispatchers.Main) {
                    //    val intent = Intent(v!!.context, DetailsActivity::class.java)
                  //      intent.putExtra("title", modelName.text)
                 //       intent.putExtra("modelLink", modelLink.text)
                //        intent.putExtra("videoLink", videoLink)
                //        intent.putExtra("imageLink",imageLink)
                        Toast.makeText(itemView.context,"Open " + modelName.text, Toast.LENGTH_SHORT).show()
               //         v.context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        context = parent.context
        // dBHelper = DBHelper(parent.context)
        // favoritesList = dBHelper.readAllUsers().toMutableList()
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chaturbate, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //   holder.itemView.favorite_check.isChecked = favoritesList.containsAll(dBHelper.readUser(items[position].Name).toMutableList())
        val img: List<String> = items[position].Image.split("?")
        //   if (!items.contains(Model(items[position].Name,items[position].Image, items[position].Label, items[position].Age, items[position].Description, items[position].Views, items[position].Link))) {
        //   } else {
        holder.modelName.text = items[position].Name
        Glide.with(holder.itemView)
            .load(items[position].Image)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.chaturbate_nopicture)
            .dontTransform()
            .into(holder.modelImage)
        Log.d("CAM SEX! ", img[0])
        if(items[position].hdLabel == "") {
            holder.modelHd.visibility = View.GONE
        } else {
            holder.modelHd.visibility = View.VISIBLE
            // holder.modelHd.text = items[position].hdLabel
        }
        //holder.modelDescription.text = items[position].Description  -> UNTENDRUNTER IMPLAMENTIERT!
        val ss = SpannableString(items[position].Description)
        val words: List<String> = ss.split(" ")
        for (word: String in words) {
            if (word.startsWith("#")) {
                val clickableSpan: ClickableSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        //----->>>> listener.onWorkoutClicked(word)
                        //textView.setOnClickListener{}
                    }
                }
                ss.setSpan(
                    clickableSpan,
                    ss.indexOf(word),
                    ss.indexOf(word) + word.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        holder.modelDescription.apply {
            text = ss
            movementMethod = LinkMovementMethod.getInstance()
        }
        holder.modelViews.text = items[position].Views
        //Age Parse only Numbers! //
        val check = "[0-9]+".toRegex()
        if(items[position].Age.matches(check)) {
            holder.modelAge.text = items[position].Age
        } else { holder.modelAge.text = "" }
        // holder.modelAge.text = items[position].Age
        // Label Parse Color fix //
        //       if (items[position].Label.contains("CHATURBATING")) {
        //           holder.modelLabel.setTextColor(Color.parseColor("#FFFFFF"))
        //           holder.modelLabel.text = items[position].Label.trimMargin().trimStart()
        //           holder.modelLabel.setBackgroundColor(Color.parseColor("#71B404"))
        //       }else if(items[position].Label.contains("BEWORBEN")){
        //           holder.modelLabel.text = items[position].Label.trimMargin().trimStart()
        //           holder.modelLabel.setTextColor(Color.parseColor("Color.BLACK"))
        //           holder.modelLabel.setBackgroundColor(Color.parseColor("#ED7E53"))
        //       }else if(items[position].Label.contains("NEU")){
        //           holder.modelLabel.setTextColor(Color.parseColor("#FFFFFF"))
        //           holder.modelLabel.text = items[position].Label.trimMargin().trimStart()
        //           holder.modelLabel.setBackgroundColor(Color.parseColor("#71B404"))
        //       }else{
        //           holder.modelLabel.setTextColor(Color.parseColor("#FFFFFF"))
        //           holder.modelLabel.text = items[position].Label.trimMargin().trimStart()
        //           holder.modelLabel.setBackgroundColor(Color.parseColor("#2472B4"))
        //       }
        //    holder.modelLabel.text = items[position].Label
        holder.modelLink.text = items[position].Link
        // --->
        Glide.with(holder.itemView)
            .load(items[position].Gender)
            // .placeholder(R.drawable.progress_animation)
            // .error(R.drawable.chaturbate_nopicture)
            .dontTransform()
            .into(holder.modelGender)
        if (holder.modelGender === { "f" }) { holder.modelGender }
        val gender:String = items[position].Gender
        val model = ChaturbateModel(
            items[position].Name,
            img[0].trimStart('?'),
            items[position].hdLabel,
            items[position].Age,
            items[position].Gender,
            items[position].Description,
            items[position].Views,
            items[position].Link
        )
        holder.favoriteCheck.setOnClickListener {
            if (holder.favoriteCheck.isChecked) {
                // dBHelper.insertUser(model)
                // favoritesList.add(model)
                writeToast(holder.modelName.text.toString() + " saved in Favorites!")
            } else {
                // favoritesList.remove(model)
                //  dBHelper.deleteUser(model.Name)
                writeToast(holder.modelName.text.toString() + " removed from Favorites!")
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun getModels():MutableList<ChaturbateModel> { return items }

    @SuppressLint("NotifyDataSetChanged")
    fun add(model:ChaturbateModel) {
        items.add(model)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMore(data: MutableList<ChaturbateModel>) {
        items.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAll() {
        items.removeAll { true }
        notifyDataSetChanged()
    }

    private fun writeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}