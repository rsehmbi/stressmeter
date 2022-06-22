import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.stressmeter.R

public class ImageAdapter(var applicationContext: Context, var pictures: IntArray): BaseAdapter(){
    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return pictures.size;
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(index: Int, viewAdapter: View?, viewGroup: ViewGroup?): View {
        layoutInflater = LayoutInflater.from(applicationContext)
        val viewInflater = layoutInflater.inflate(R.layout.image_grid_view,null, true)
        val icon: ImageView = viewInflater.findViewById(R.id.icon)
        icon.setImageResource(pictures[index]); // set logo images
        return viewInflater;
    }

}