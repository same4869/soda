package com.xun.soda.download

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xun.soda.R
import com.xun.sodaability.comm.onClick
import com.xun.sodaability.download.DownLoadManager
import com.xun.sodaability.download.core.DownloadListener
import com.xun.sodalibrary.log.SodaLog
import kotlinx.android.synthetic.main.activity_soda_download.*

class SodaDownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soda_download)

        mSodaDownloadBtn1.onClick {
            DownLoadManager().download("https://m.bbs.mihoyo.com/app/mihoyobbs_1.6.0_bh3.apk",
                "mihoyobbs1.6.0.apk",
                false,
                object : DownloadListener {
                    override fun onStartDownload() {
                        SodaLog.d("download onStartDownload")
                    }

                    override fun onProgress(progress: Int) {
                        SodaLog.d("download onProgress : $progress")
                    }

                    override fun onFinishDownload(url: String, path: String) {
                        SodaLog.d("download onFinishDownload")
                    }

                    override fun onFail(url: String, errorInfo: String) {
                        SodaLog.d("download onFail")
                    }

                })
        }

        val mDownLoadManager2 = DownLoadManager()

        mSodaDownloadBtn2.onClick {
            mDownLoadManager2.download("https://ip3442164186.out.azhimalayanvh.com/fs08/2020/07/16/9/109_5c2680731c2868e21f675c9a953cc862.apk?yingid=wdj_web&fname=地铁跑酷&productid=2011&pos=wdj_web/detail_normal_dl/0&appid=172353&packageid=200912955&apprd=172353&iconUrl=http://android-artworks.25pp.com/fs08/2020/08/18/0/109_446dc92a21152a268ddfe8271b8678bb_con.png&pkg=com.kiloo.subwaysurf&did=49fba8c804d86ee579cd4ec23ed8695e&vcode=30900&md5=26a96d23035ee17f471a49c6c1dfbf13&ali_redirect_domain=alissl.ucdl.pp.uc.cn&ali_redirect_ex_ftag=e9e34805dd06b973f09a244d3cf8d378c41437b00722b2ba&ali_redirect_ex_tmining_ts=1597922252&ali_redirect_ex_tmining_expire=3600&ali_redirect_ex_hot=100",
                "ditiepaoku.apk",
                true,
                object : DownloadListener {
                    override fun onStartDownload() {
                        SodaLog.d("download onStartDownload")
                    }

                    override fun onProgress(progress: Int) {
                        SodaLog.d("download onProgress : $progress")
//                        SodaLog.log(object : SodaLogConfig() {
//                            override fun stackTraceDepth(): Int {
//                                return 5
//                            }
//                        }, contents = "download onProgress : $progress")
                    }

                    override fun onFinishDownload(url: String, path: String) {
                        SodaLog.d("download onFinishDownload")
                    }

                    override fun onFail(url: String, errorInfo: String) {
                        SodaLog.d("download onFail")
                    }

                })
        }

        mSodaDownloadBtn21.onClick {
            mDownLoadManager2.stopDownload()
        }

        mSodaDownloadBtn22.onClick {
            mDownLoadManager2.continueDownload()
        }
    }
}