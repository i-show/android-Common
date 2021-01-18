package com.ishow.noah.modules.sample.detail.utils.db.room

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.download.db.DownloadDB
import com.ishow.common.utils.download.db.DownloadData
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleRoomBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-03-31.
 */
class SampleRoomFragment : AppBindFragment<FSampleRoomBinding, SampleRoomViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_room

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintView.init(binding.printView)
        val db = DownloadDB.get(view.context)
        val downloadDao = db.getDownloadDao()
        binding.get.setOnClickListener {
            PrintView.print("yhy", downloadDao.getData()?.toJSON())
        }

        binding.update.setOnClickListener {
            val data = DownloadData()
            data.id = 1
            data.url = "www.baidu.com"
            data.downloadLength = 500L
            downloadDao.update(data)
        }

        binding.insert.setOnClickListener {
            val data = DownloadData()
            data.id = 1
            data.url = "www.baidu.com"
            data.downloadLength = 100L
            downloadDao.insert(data)
        }

        binding.delete.setOnClickListener {
            val data = DownloadData()
            data.id = 1
        }
    }
}