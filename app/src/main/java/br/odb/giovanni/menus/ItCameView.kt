package br.odb.giovanni.menus

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.DisplayMetrics
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import br.odb.giovanni.R
import br.odb.giovanni.engine.Constants
import br.odb.giovanni.engine.VirtualPad
import br.odb.giovanni.game.Actor
import br.odb.giovanni.game.Level
import br.odb.giovanni.game.LevelFactory.createRandomLevel
import br.odb.giovanni.game.Miner
import br.odb.giovanni.engine.Vec2
import java.util.*
import kotlin.system.exitProcess

class ItCameView(context: Context?, enableSounds: Boolean) : View(context), Runnable,
    VirtualPadClient, OnTouchListener {

    var playing = false

    private var timeSinceAcquiredFocus: Long = 0
    private var drawOnScreenController = false
    private val vPad: VirtualPad
    private val keyMap: BooleanArray
    private val paint: Paint = Paint()
    private val camera: Vec2 = Vec2()
    private val actor: Miner
    override val bitmapOverlay: Bitmap
    private var level: Level? = null

    companion object {
        var playSounds = true
        val viewport = Rect()
    }

    init {
        isFocusable = true
        isClickable = true
        isLongClickable = true
        playSounds = enableSounds
        bitmapOverlay = BitmapFactory.decodeResource(resources, R.drawable.control_brown)
        vPad = VirtualPad(this)
        this.requestFocus()
        this.isFocusableInTouchMode = true
        keyMap = vPad.keyMap
        if (MainMenuActivity.needsReset) {
            level = createRandomLevel(
                br.odb.giovanni.game.Constants.SIZE_X,
                br.odb.giovanni.game.Constants.SIZE_Y, resources, context!!
            )
            MainMenuActivity.needsReset = false
        }
        actor = level!!.miner!!
        setBackgroundColor(Color.BLACK)
        val monitorThread = Thread(this, "main game ticker")
        monitorThread.priority = Thread.MIN_PRIORITY
        monitorThread.start()
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            (context as AppCompatActivity).display!!.getMetrics(displayMetrics)
        } else {
            (context as AppCompatActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        viewport[0, 0, screenWidth] = screenHeight
        setOnTouchListener(this)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        var handled = false

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            keyMap[Constants.KB_UP] = false
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            keyMap[Constants.KB_DOWN] = false
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            keyMap[Constants.KB_LEFT] = false
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            keyMap[Constants.KB_RIGHT] = false
            handled = true
        }

        return handled
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        var handled = false

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            keyMap[Constants.KB_UP] = true
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            keyMap[Constants.KB_DOWN] = true
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            keyMap[Constants.KB_LEFT] = true
            handled = true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            keyMap[Constants.KB_RIGHT] = true
            handled = true
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) exitProcess(0)

        return handled
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val screenWidth = width
        val screenHeight = height
        if (hasFocus) {
            timeSinceAcquiredFocus = 5000
        }
        viewport[0, 0, screenWidth] = screenHeight
    }// This device is a game controller. Store its device ID.

    // Verify that the device has gamepad buttons, control sticks, or both.
    private val gameControllerIds: ArrayList<Int>
        get() {
            val gameControllerDeviceIds = ArrayList<Int>()
            val deviceIds = InputDevice.getDeviceIds()
            for (deviceId in deviceIds) {
                val dev = InputDevice.getDevice(deviceId)
                val sources = dev.sources

                // Verify that the device has gamepad buttons, control sticks, or both.
                if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
                    || (sources and InputDevice.SOURCE_JOYSTICK
                            == InputDevice.SOURCE_JOYSTICK)
                ) {
                    // This device is a game controller. Store its device ID.
                    if (!gameControllerDeviceIds.contains(deviceId)) {
                        gameControllerDeviceIds.add(deviceId)
                    }
                }
            }
            return gameControllerDeviceIds
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(actor) {
            vPad.setBounds(0, 0, width, height)

            level!!.setCurrentCamera(camera)

            if (level != null) {
                level!!.setCurrentCamera(actor.position)
                level!!.draw(canvas, paint)
            }

            if (drawOnScreenController) {
                vPad.draw(canvas)
            }

            drawMap(canvas)

            paint.color = Color.YELLOW
            paint.isFakeBoldText = true

            canvas.drawText(
                "Você derrotou " + level!!.dead
                        + " monstros; Tempo para a detonação: "
                        + level!!.dynamite!!.timeToBlow / 1000 + "s", 0f, (
                        height - 50).toFloat(), paint
            )
            paint.isFakeBoldText = false
        }
        if (timeSinceAcquiredFocus > 0) {
            val text = "Jogo começando em " + timeSinceAcquiredFocus / 1000
            paint.getTextBounds(text, 0, text.length, bounds)
            val prevSize = paint.textSize
            paint.textSize = 30f
            canvas.drawText(text, width / 2.0f - bounds.width(), height / 2.0f, paint)
            paint.textSize = prevSize
        }
    }

    private val bounds = Rect()
    private fun drawMap(canvas: Canvas) {
        var x2: Int
        var y2: Int
        paint.color = Color.YELLOW
        paint.alpha = 128
        for (x in 0 until level!!.width) {
            for (y in 0 until level!!.height) {
                if (!level!!.mayMoveTo(x, y)) {
                    canvas.drawRect(
                        (x * 5).toFloat(),
                        (y * 5).toFloat(),
                        ((x + 1) * 5).toFloat(),
                        ((y + 1) * 5).toFloat(),
                        paint
                    )
                }
            }
        }
        paint.color = Color.BLUE
        paint.alpha = 128
        for (a in level!!.actors) {
            if (a.killed) {
                continue
            }
            x2 = (a.position.x / Constants.BASE_TILE_WIDTH).toInt()
            y2 = (a.position.y / Constants.BASE_TILE_HEIGHT).toInt()
            canvas.drawRect(
                (x2 * 5).toFloat(), (y2 * 5).toFloat(), ((x2 + 1) * 5).toFloat(), (
                        (y2 + 1) * 5).toFloat(), paint
            )
        }
        paint.alpha = 255
    }

    override fun run() {
        var running = true
        while (running) {
            try {
                drawOnScreenController = gameControllerIds.size == 0
                Thread.sleep(Constants.INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                running = false
            }
            if (!playing) {
                continue
            }
            if (timeSinceAcquiredFocus > 0) {
                timeSinceAcquiredFocus -= Constants.INTERVAL
                postInvalidate()
                continue
            }
            handleKeys(keyMap)
            level!!.tick(Constants.INTERVAL)
            if (level!!.gameShouldEnd) {
                val intent = (this.context as ItCameFromTheCaveActivity)
                    .intent
                intent.putExtra("result", if (level!!.miner!!.killed) "failure" else "victory")
                (this.context as ItCameFromTheCaveActivity).setResult(
                    AppCompatActivity.RESULT_OK, intent
                )
                (this.context as ItCameFromTheCaveActivity).finish()
                return
            }
            if (level!!.dynamite!!.killed
                && level!!.dynamite!!.position
                    .isCloseEnoughToConsiderEqualTo(
                        level!!.miner!!.position
                    )
            ) {
                val intent = (this.context as ItCameFromTheCaveActivity)
                    .intent
                intent.putExtra("result", if (level!!.miner!!.killed) "failure" else "victory")
                (this.context as ItCameFromTheCaveActivity).setResult(
                    AppCompatActivity.RESULT_OK, intent
                )
                running = false
                (this.context as ItCameFromTheCaveActivity).finish()
            }
            postInvalidate()
        }
    }

    override fun handleKeys(keymap: BooleanArray?) {
        if (timeSinceAcquiredFocus > 0) {
            return
        }
        synchronized(actor) {
            var handled = false
            var x = 0
            var y = 0
            val currentX = (actor.position.x / Constants.BASE_TILE_WIDTH).toInt()
            val currentY = (actor.position.y / Constants.BASE_TILE_HEIGHT).toInt()
            when {
                keymap!![Constants.KB_UP] -> {
                    y -= (1.25f).toInt()
                    actor.direction = 0
                    handled = true
                }
                keymap[Constants.KB_DOWN] -> {
                    y += (1.25f).toInt()
                    actor.direction = 2
                    handled = true
                }
                keymap[Constants.KB_LEFT] -> {
                    x -= (1.25f).toInt()
                    actor.direction = 3
                    handled = true
                }
                keymap[Constants.KB_RIGHT] -> {
                    actor.direction = 1
                    x += (1.25f).toInt()
                    handled = true
                }
            }
            if (handled) {
                if (level!!.mayMoveTo(currentX + x, currentY + y)) {
                    actor.move(
                        (x * Constants.BASE_TILE_WIDTH).toFloat(), (y
                                * Constants.BASE_TILE_HEIGHT).toFloat()
                    )
                    actor.state = Actor.ActorStates.MOVING
                }
            } else actor.state = Actor.ActorStates.STILL
            if (actor.position.x < 0) actor.position.x = 0f
            if (actor.position.y < 0) actor.position.y = 0f
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return vPad.onTouchEvent(event)
    }
}