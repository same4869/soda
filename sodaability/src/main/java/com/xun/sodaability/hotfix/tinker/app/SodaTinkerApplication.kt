package com.xun.sodaability.hotfix.tinker.app

import com.tencent.tinker.loader.app.TinkerApplication
import com.tencent.tinker.loader.shareutil.ShareConstants

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/19
 */

class SodaTinkerApplication : TinkerApplication(
    ShareConstants.TINKER_ENABLE_ALL,
    "com.xun.sodaability.hotfix.tinker.app.SodaTinkerApplicationLike",
    "com.tencent.tinker.loader.TinkerLoader",
    false
)