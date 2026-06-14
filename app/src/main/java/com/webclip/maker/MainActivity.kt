package com.webclip.maker

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.webclip.maker.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.etName.setText("我的网页")

        binding.btnCreate.setOnClickListener {
            hideKeyboard()
            createShortcut()
        }

        binding.btnUninstall.setOnClickListener {
            uninstallApp()
        }
    }

    private fun createShortcut() {
        val url = binding.etUrl.text.toString().trim()
        val name = binding.etName.text.toString().trim()

        if (url.isEmpty()) {
            showStatus("请输入网址！", false)
            return
        }

        if (name.isEmpty()) {
            showStatus("请输入快捷方式名称！", false)
            return
        }

        val validUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }

        try {
            URL(validUrl)
        } catch (e: Exception) {
            showStatus("网址格式不正确！", false)
            return
        }

        showStatus("正在生成快捷方式...", null)

        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            showStatus("您的设备不支持添加快捷方式", false)
            return
        }

        try {
            val shortcutIntent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val icon = createShortcutIcon(name)

            val shortcutInfo = ShortcutInfoCompat.Builder(this, "webclip_${System.currentTimeMillis()}")
                .setShortLabel(name)
                .setLongLabel(name)
                .setIcon(IconCompat.createWithBitmap(icon))
                .setIntent(shortcutIntent)
                .build()

            val pinned = ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)

            if (pinned) {
                showStatus("快捷方式已生成！\n请在弹窗中点击【添加】", true)
                handler.postDelayed({
                    showStatus("快捷方式已生成！\n现在可以卸载本工具", true)
                }, 3000)
            } else {
                showStatus("快捷方式生成失败", false)
            }

        } catch (e: Exception) {
            showStatus("错误: ${e.message}", false)
            e.printStackTrace()
        }
    }

    private fun createShortcutIcon(name: String): Bitmap {
        val size = 192
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#2196F3")
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())
        canvas.drawRoundRect(rect, size * 0.2f, size * 0.2f, paint)

        val firstChar = name.firstOrNull()?.toString() ?: "W"
        paint.color = Color.WHITE
        paint.textSize = size * 0.5f
        paint.textAlign = Paint.Align.CENTER
        val x = size / 2f
        val y = size / 2f - (paint.descent() + paint.ascent()) / 2f
        canvas.drawText(firstChar, x, y, paint)

        return bitmap
    }

    private fun uninstallApp() {
        AlertDialog.Builder(this)
            .setTitle("确认卸载")
            .setMessage("确定要卸载 WebClip 快捷方式生成器吗？")
            .setPositiveButton("卸载") { _, _ ->
                val packageUri = Uri.parse("package:$packageName")
                val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
                startActivity(uninstallIntent)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showStatus(message: String, isSuccess: Boolean?) {
        binding.tvStatus.text = message
        binding.tvStatus.visibility = android.view.View.VISIBLE

        when (isSuccess) {
            true -> binding.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
            false -> binding.tvStatus.setTextColor(Color.parseColor("#F44336"))
            null -> binding.tvStatus.setTextColor(Color.parseColor("#2196F3"))
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
