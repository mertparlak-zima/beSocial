package app.zimablue.besocial.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.zimablue.besocial.databinding.RecyclerRowBinding
import app.zimablue.besocial.model.Post
import com.squareup.picasso.Picasso

class FlowFragmentRecyclerAdapter(private val postList: ArrayList<Post>): RecyclerView.Adapter<FlowFragmentRecyclerAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.binding.tvUserEmail.text = postList[position].email
        holder.binding.tvComment.text = postList[position].comment

        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.ivPostImage)

    }

}