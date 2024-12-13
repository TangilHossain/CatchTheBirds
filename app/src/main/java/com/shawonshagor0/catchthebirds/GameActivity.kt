package com.shawonshagor0.catchthebirds

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.shawonshagor0.catchthebirds.databinding.ActivityGameBinding
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityGameBinding
    lateinit var imgBtnDuckOne: ImageButton
    lateinit var imgBtnDuckTwo: ImageButton
    lateinit var imgBtnDuckThree: ImageButton
    lateinit var imgBtnDuckFour: ImageButton
    lateinit var imgBtnDuckFive: ImageButton
    lateinit var tvScore: TextView
    lateinit var tvLife: TextView
    var duckMap = mutableMapOf<Int, Boolean>()
    var duckClicked = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        imgBtnDuckOne = findViewById(R.id.imgbtnduckone)
        imgBtnDuckTwo = findViewById(R.id.imgbtnducktwo)
        imgBtnDuckThree = findViewById(R.id.imgbtnduckthree)
        imgBtnDuckFour = findViewById(R.id.imgbtnduckfour)
        imgBtnDuckFive = findViewById(R.id.imgbtnduckfive)
        tvScore = findViewById(R.id.tvScore)
        tvLife = findViewById(R.id.tvLife)
        imgBtnDuckTwo.translationX = -500f
        imgBtnDuckThree.translationX = -500f
        imgBtnDuckFour.translationX = -500f
        imgBtnDuckFive.translationX = -500f

        duckMap[R.id.imgbtnduckone] = false
        duckMap[R.id.imgbtnducktwo] = false
        duckMap[R.id.imgbtnduckthree] = false
        duckMap[R.id.imgbtnduckfour] = false
        duckMap[R.id.imgbtnduckfive] = false

        initiateDuck(1)
        if(Random.nextInt(0,2) == 1){
            initiateDuck(1)
        }

    }


    private fun initiateDuck(Count: Int) {
        var duckCount = Count
        for (key in duckMap) {
            if (key.value == false) {
                startDuck(key.key)
                duckCount -= 1
            }
            if(duckCount == 0){
                break
            }
        }
    }

    fun startDuck(id: Int){
        val duck = findViewById<ImageButton>(id)
        duck.visibility = View.VISIBLE
        duckMap[id] = true
        Glide.with(this).load(R.drawable.duckflying).into(duck)

        //BLOCK FOR A DUCK START
        duck.translationY = Random.nextInt(-500,501).toFloat()
        incomingAnimation(id)
    }

    fun incomingAnimation(id: Int){

        val duck = findViewById<ImageButton>(id)
        duck.visibility = View.VISIBLE

        val incomingDirection = Random.nextInt(0,2).toInt()
        lateinit var xAnimation: ObjectAnimator

        if(incomingDirection == 1) {
            duck.translationX = -500f
            xAnimation = ObjectAnimator.ofFloat(duck, "translationX", 1100f)
        }
        else{
            duck.rotationY = 180f
            duck.translationX = 1500f
            xAnimation = ObjectAnimator.ofFloat(duck, "translationX", -500f)
        }

        xAnimation.duration = 3000
        xAnimation.interpolator = AccelerateInterpolator()
        //MAKE ELEVATION
        val yAnimation = ObjectAnimator.ofFloat(duck, "translationY", Random.nextInt(-500, 501).toFloat())
        yAnimation.duration = Random.nextInt(2000, 3001).toLong() // 500 milliseconds
        yAnimation.interpolator = AccelerateDecelerateInterpolator() // Or other interpolators

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(xAnimation, yAnimation)
        animatorSet.start()


        animatorSet.doOnEnd {
            if(duckClicked[id] == false){
                if(tvLife.text.length == 5){
                    tvLife.setText("❤❤❤❤")
                }else if(tvLife.text.length == 4){
                    tvLife.setText("❤❤❤")
                }
                else if(tvLife.text.length == 3){
                    tvLife.setText("❤❤")
                }
                else if(tvLife.text.length == 2){
                    tvLife.setText("❤")
                }else if(tvLife.text.length == 1){
                    Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            else{
                duckClicked[id] = false
            }
            duckMap[id] = false

            initiateDuck(Random.nextInt(1,2))


        }
        duck.setOnClickListener {
            duckClicked[id] = true
            tvScore.setText((tvScore.text.toString().toInt() + 1).toString())
            Toast.makeText(this, resources.getResourceEntryName(id).toString(), Toast.LENGTH_SHORT).show()
            duckFall(id)
        }

    }


    fun duckFall(id: Int){
        val duck = findViewById<ImageButton>(id)
        //animate the duck to fall
        val spinDirection = Random.nextInt(-1,2).toInt()
        var duckFallAnimator = ObjectAnimator.ofFloat(duck, View.Y,2200f)
        duckFallAnimator.interpolator = AccelerateInterpolator()
        duckFallAnimator.duration = 1000

        var duckRotateAnimator = ObjectAnimator.ofFloat(duck, View.ROTATION, 150f*spinDirection)
        duckRotateAnimator.interpolator = DecelerateInterpolator()
        duckRotateAnimator.duration = 1000
        val fallRotate = AnimatorSet()
        fallRotate.playTogether(duckFallAnimator, duckRotateAnimator)
        fallRotate.start()
        fallRotate.doOnEnd {
            duck.rotation = 360f-(150f*spinDirection)
            duckMap[id] = false
            duck.visibility = View.GONE
        }
    }

}