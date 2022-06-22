package com.example.stressmeter.ui.image

import androidx.lifecycle.ViewModel

class PicturesViewModel : ViewModel() {
    // Handles the vibration and image set, doesn't change the picture
    // set when the screen is rotated
    var IMAGE_SET = 0
    var VIBRATION = true;
}