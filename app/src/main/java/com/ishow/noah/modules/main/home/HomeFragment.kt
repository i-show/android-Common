package com.ishow.noah.modules.main.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.ishow.common.extensions.fitKeyBoard
import com.ishow.common.extensions.toJSON
import com.ishow.common.manager.CacheManager
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.entries.Token
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.Job


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        val list = mutableListOf<AA>(
            AA(12, false),
            AA(10, true),
            AA(15, false),
            AA(18, false)
        )
        var index = 0
        image1.setImageResource(R.drawable.test_banner)
        image1.setOnClickListener {
            val list2 = list.sorted()
            Log.i("yhy", "list2 = " + list.toJSON())
        }

        image2.setImageResource(R.drawable.test_banner)
        image2.setOnClickListener {
            val token = Token()
            token.accessToken = "11111111111"

            CacheManager.cache("1", token)
        }
        Logger.addLogAdapter(AndroidLogAdapter())

        image3.setImageResource(R.drawable.test_banner)
        image3.setOnClickListener {
            //调用系统文件管理器打开指定路径目录
            //调用系统文件管理器打开指定路径目录
            val file = requireContext().getExternalFilesDir("phone") ?: return@setOnClickListener
            if (!file.exists()) {
                file.mkdirs()
            }
            val context = it.context

            val photoURI: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, photoURI)
            startActivityForResult(intent, 1)
        }

        root.fitKeyBoard(neeShow)
    }


    private class AA(val count: Int, val ok: Boolean) : Comparable<AA> {
        override fun compareTo(other: AA): Int {
            return if (ok == other.ok) {
                this.count.compareTo(other.count)
            } else {
                ok.compareTo(other.ok)
            }
        }

    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, Observer { PrintView.print(it.toString()) })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
            .start()
    }

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}