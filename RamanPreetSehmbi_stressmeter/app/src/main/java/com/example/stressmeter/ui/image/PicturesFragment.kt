package com.example.stressmeter.ui.image

import ImageAdapter
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.R
import com.example.stressmeter.SubmitResult

class PicturesFragment : Fragment() {
    // The way picture fragment works is quit simple,
    // When the user clicks on Loads more picture, I am just incrementing the
    // array of pictures by one [[pic_set1], [pic_set_2]..]
    // and setting the index appropriately.
    companion object {
        lateinit var vibrator: Vibrator;
        lateinit var ringPlayer: MediaPlayer;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val fragmentHomeView = inflater.inflate(R.layout.fragment_home, container, false)
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]
        ringPlayer = MediaPlayer.create(requireActivity(), R.raw.tune);

        val pattern = longArrayOf(0, 1000, 200)
        if(picturesViewModel.VIBRATION)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager?
                vibrator = vibratorManager!!.defaultVibrator
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(pattern, 0);
            }
            ringPlayer.start();
        }
        loadImages(fragmentHomeView)
        updateGridView(fragmentHomeView)
        setOnClickListenerGridView(fragmentHomeView)
        return fragmentHomeView
    }

    private fun updateGridView(fragmentView:View){
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]
        val gridView = fragmentView.findViewById<GridView>(R.id.image_grid_view_id)
        val imageAdapter = ImageAdapter(requireContext(),IMAGES[picturesViewModel.IMAGE_SET])
        gridView.adapter = imageAdapter
    }

    private fun setOnClickListenerGridView(fragmentView:View){
        val gridView = fragmentView.findViewById<GridView>(R.id.image_grid_view_id)
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]
        gridView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            checkTuneAndVibration()
            val submitIntent = Intent(requireActivity(), SubmitResult::class.java)
            submitIntent.putExtra("IMAGE_PAGE_ID", picturesViewModel.IMAGE_SET)
            submitIntent.putExtra("IMAGE_PICTURE_ID", position)
            startActivity(submitIntent)
        })
    }

    public fun checkTuneAndVibration(){
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]
        if(picturesViewModel.VIBRATION == true){
            ringPlayer.stop()
            vibrator.cancel()
            picturesViewModel.VIBRATION = false
        }
    }

    private fun loadImages(fragmentView:View){
        val loadImagesButton = fragmentView.findViewById<Button>(R.id.more_images_id)
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]

        loadImagesButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                checkTuneAndVibration()
                if (picturesViewModel.IMAGE_SET == 2){
                    picturesViewModel.IMAGE_SET = 0
                }
                else {
                    picturesViewModel.IMAGE_SET = picturesViewModel.IMAGE_SET + 1
                }
                updateGridView(fragmentView)
            }
        })
    }


}

