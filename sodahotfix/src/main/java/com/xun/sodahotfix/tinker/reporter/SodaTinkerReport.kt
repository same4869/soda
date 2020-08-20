package com.xun.sodahotfix.tinker.reporter

import com.tencent.tinker.lib.util.TinkerLog
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals
import com.xun.sodahotfix.tinker.utils.Utils

/**
 * @Description: is SampleTinkerReport
 * @Author:         xwang
 * @CreateDate:     2019-11-28
 */

class SodaTinkerReport {

    interface Reporter {
        fun onReport(key: Int)

        fun onReport(message: String)
    }

    fun setReporter(tinkerReporter: Reporter) {
        reporter = tinkerReporter
    }

    companion object {
        private val TAG = "Tinker.SampleTinkerReport"

        // KEY - PV
        val KEY_REQUEST = 0
        val KEY_DOWNLOAD = 1
        val KEY_TRY_APPLY = 2
        val KEY_TRY_APPLY_SUCCESS = 3
        val KEY_APPLIED_START = 4
        val KEY_APPLIED = 5
        val KEY_LOADED = 6
        val KEY_CRASH_FAST_PROTECT = 7
        val KEY_CRASH_CAUSE_XPOSED_DALVIK = 8
        val KEY_CRASH_CAUSE_XPOSED_ART = 9
        val KEY_APPLY_WITH_RETRY = 10

        //Key -- try apply detail
        val KEY_TRY_APPLY_UPGRADE = 70
        val KEY_TRY_APPLY_DISABLE = 71
        val KEY_TRY_APPLY_RUNNING = 72
        val KEY_TRY_APPLY_INSERVICE = 73
        val KEY_TRY_APPLY_NOT_EXIST = 74
        val KEY_TRY_APPLY_GOOGLEPLAY = 75
        val KEY_TRY_APPLY_ROM_SPACE = 76
        val KEY_TRY_APPLY_ALREADY_APPLY = 77
        val KEY_TRY_APPLY_MEMORY_LIMIT = 78
        val KEY_TRY_APPLY_CRASH_LIMIT = 79
        val KEY_TRY_APPLY_CONDITION_NOT_SATISFIED = 80
        val KEY_TRY_APPLY_JIT = 81

        //Key -- apply detail
        val KEY_APPLIED_UPGRADE = 100
        val KEY_APPLIED_UPGRADE_FAIL = 101

        val KEY_APPLIED_EXCEPTION = 120
        val KEY_APPLIED_DEXOPT_OTHER = 121
        val KEY_APPLIED_DEXOPT_EXIST = 122
        val KEY_APPLIED_DEXOPT_FORMAT = 123
        val KEY_APPLIED_INFO_CORRUPTED = 124
        //package check
        val KEY_APPLIED_PACKAGE_CHECK_SIGNATURE = 150
        val KEY_APPLIED_PACKAGE_CHECK_DEX_META = 151
        val KEY_APPLIED_PACKAGE_CHECK_LIB_META = 152
        val KEY_APPLIED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND = 153
        val KEY_APPLIED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND = 154
        val KEY_APPLIED_PACKAGE_CHECK_META_NOT_FOUND = 155
        val KEY_APPLIED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL = 156
        val KEY_APPLIED_PACKAGE_CHECK_RES_META = 157
        val KEY_APPLIED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT = 158

        //version check
        val KEY_APPLIED_VERSION_CHECK = 180
        //extract error
        val KEY_APPLIED_PATCH_FILE_EXTRACT = 181
        val KEY_APPLIED_DEX_EXTRACT = 182
        val KEY_APPLIED_LIB_EXTRACT = 183
        val KEY_APPLIED_RESOURCE_EXTRACT = 184
        //cost time
        val KEY_APPLIED_SUCC_COST_5S_LESS = 200
        val KEY_APPLIED_SUCC_COST_10S_LESS = 201
        val KEY_APPLIED_SUCC_COST_30S_LESS = 202
        val KEY_APPLIED_SUCC_COST_60S_LESS = 203
        val KEY_APPLIED_SUCC_COST_OTHER = 204

        val KEY_APPLIED_FAIL_COST_5S_LESS = 205
        val KEY_APPLIED_FAIL_COST_10S_LESS = 206
        val KEY_APPLIED_FAIL_COST_30S_LESS = 207
        val KEY_APPLIED_FAIL_COST_60S_LESS = 208
        val KEY_APPLIED_FAIL_COST_OTHER = 209


        // KEY -- load detail
        val KEY_LOADED_UNKNOWN_EXCEPTION = 250
        val KEY_LOADED_UNCAUGHT_EXCEPTION = 251
        val KEY_LOADED_EXCEPTION_DEX = 252
        val KEY_LOADED_EXCEPTION_DEX_CHECK = 253
        val KEY_LOADED_EXCEPTION_RESOURCE = 254
        val KEY_LOADED_EXCEPTION_RESOURCE_CHECK = 255


        val KEY_LOADED_MISMATCH_DEX = 300
        val KEY_LOADED_MISMATCH_LIB = 301
        val KEY_LOADED_MISMATCH_RESOURCE = 302
        val KEY_LOADED_MISSING_DEX = 303
        val KEY_LOADED_MISSING_LIB = 304
        val KEY_LOADED_MISSING_PATCH_FILE = 305
        val KEY_LOADED_MISSING_PATCH_INFO = 306
        val KEY_LOADED_MISSING_DEX_OPT = 307
        val KEY_LOADED_MISSING_RES = 308
        val KEY_LOADED_INFO_CORRUPTED = 309

        //load package check
        val KEY_LOADED_PACKAGE_CHECK_SIGNATURE = 350
        val KEY_LOADED_PACKAGE_CHECK_DEX_META = 351
        val KEY_LOADED_PACKAGE_CHECK_LIB_META = 352
        val KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND = 353
        val KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND = 354
        val KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL = 355
        val KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND = 356
        val KEY_LOADED_PACKAGE_CHECK_RES_META = 357
        val KEY_LOADED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT = 358


        val KEY_LOADED_SUCC_COST_500_LESS = 400
        val KEY_LOADED_SUCC_COST_1000_LESS = 401
        val KEY_LOADED_SUCC_COST_3000_LESS = 402
        val KEY_LOADED_SUCC_COST_5000_LESS = 403
        val KEY_LOADED_SUCC_COST_OTHER = 404

        val KEY_LOADED_INTERPRET_GET_INSTRUCTION_SET_ERROR = 450
        val KEY_LOADED_INTERPRET_INTERPRET_COMMAND_ERROR = 451
        val KEY_LOADED_INTERPRET_TYPE_INTERPRET_OK = 452

        private var reporter: Reporter? = null

        fun onTryApply(success: Boolean) {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_TRY_APPLY)

            reporter?.onReport(KEY_TRY_APPLY_UPGRADE)

            if (success) {
                reporter?.onReport(KEY_TRY_APPLY_SUCCESS)
            }
        }

        fun onTryApplyFail(errorCode: Int) {
            if (reporter == null) {
                return
            }
            when (errorCode) {
                ShareConstants.ERROR_PATCH_NOTEXIST -> reporter?.onReport(KEY_TRY_APPLY_NOT_EXIST)
                ShareConstants.ERROR_PATCH_DISABLE -> reporter?.onReport(KEY_TRY_APPLY_DISABLE)
                ShareConstants.ERROR_PATCH_INSERVICE -> reporter?.onReport(KEY_TRY_APPLY_INSERVICE)
                ShareConstants.ERROR_PATCH_RUNNING -> reporter?.onReport(KEY_TRY_APPLY_RUNNING)
                ShareConstants.ERROR_PATCH_JIT -> reporter?.onReport(KEY_TRY_APPLY_JIT)
                Utils.ERROR_PATCH_ROM_SPACE -> reporter?.onReport(KEY_TRY_APPLY_ROM_SPACE)
                Utils.ERROR_PATCH_GOOGLEPLAY_CHANNEL -> reporter?.onReport(KEY_TRY_APPLY_GOOGLEPLAY)
                ShareConstants.ERROR_PATCH_ALREADY_APPLY -> reporter?.onReport(
                    KEY_TRY_APPLY_ALREADY_APPLY
                )
                Utils.ERROR_PATCH_CRASH_LIMIT -> reporter?.onReport(KEY_TRY_APPLY_CRASH_LIMIT)
                Utils.ERROR_PATCH_MEMORY_LIMIT -> reporter?.onReport(KEY_TRY_APPLY_MEMORY_LIMIT)
                Utils.ERROR_PATCH_CONDITION_NOT_SATISFIED -> reporter?.onReport(
                    KEY_TRY_APPLY_CONDITION_NOT_SATISFIED
                )
            }
        }

        fun onLoadPackageCheckFail(errorCode: Int) {
            if (reporter == null) {
                return
            }
            when (errorCode) {
                ShareConstants.ERROR_PACKAGE_CHECK_SIGNATURE_FAIL -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_SIGNATURE
                )
                ShareConstants.ERROR_PACKAGE_CHECK_DEX_META_CORRUPTED -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_DEX_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_LIB_META_CORRUPTED -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_LIB_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL
                )
                ShareConstants.ERROR_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_RESOURCE_META_CORRUPTED -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_RES_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT -> reporter?.onReport(
                    KEY_LOADED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT
                )
            }
        }

        fun onLoaded(cost: Long) {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_LOADED)

            if (cost < 0L) {
                TinkerLog.e(TAG, "hp_report report load cost failed, invalid cost")
                return
            }

            if (cost <= 500) {
                reporter?.onReport(KEY_LOADED_SUCC_COST_500_LESS)
            } else if (cost <= 1000) {
                reporter?.onReport(KEY_LOADED_SUCC_COST_1000_LESS)
            } else if (cost <= 3000) {
                reporter?.onReport(KEY_LOADED_SUCC_COST_3000_LESS)
            } else if (cost <= 5000) {
                reporter?.onReport(KEY_LOADED_SUCC_COST_5000_LESS)
            } else {
                reporter?.onReport(KEY_LOADED_SUCC_COST_OTHER)
            }
        }

        fun onLoadInfoCorrupted() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_LOADED_INFO_CORRUPTED)
        }

        fun onLoadFileNotFound(fileType: Int) {
            if (reporter == null) {
                return
            }
            when (fileType) {
                ShareConstants.TYPE_DEX_OPT -> reporter?.onReport(KEY_LOADED_MISSING_DEX_OPT)
                ShareConstants.TYPE_DEX -> reporter?.onReport(KEY_LOADED_MISSING_DEX)
                ShareConstants.TYPE_LIBRARY -> reporter?.onReport(KEY_LOADED_MISSING_LIB)
                ShareConstants.TYPE_PATCH_FILE -> reporter?.onReport(KEY_LOADED_MISSING_PATCH_FILE)
                ShareConstants.TYPE_PATCH_INFO -> reporter?.onReport(KEY_LOADED_MISSING_PATCH_INFO)
                ShareConstants.TYPE_RESOURCE -> reporter?.onReport(KEY_LOADED_MISSING_RES)
            }
        }

        fun onLoadInterpretReport(type: Int, e: Throwable) {
            if (reporter == null) {
                return
            }
            when (type) {
                ShareConstants.TYPE_INTERPRET_GET_INSTRUCTION_SET_ERROR -> {
                    reporter?.onReport(KEY_LOADED_INTERPRET_GET_INSTRUCTION_SET_ERROR)
                    reporter?.onReport(
                        "Tinker Exception:interpret occur exception " + Utils.getExceptionCauseString(
                            e
                        )
                    )
                }
                ShareConstants.TYPE_INTERPRET_COMMAND_ERROR -> {
                    reporter?.onReport(KEY_LOADED_INTERPRET_INTERPRET_COMMAND_ERROR)
                    reporter?.onReport(
                        "Tinker Exception:interpret occur exception " + Utils.getExceptionCauseString(
                            e
                        )
                    )
                }
                ShareConstants.TYPE_INTERPRET_OK -> reporter?.onReport(
                    KEY_LOADED_INTERPRET_TYPE_INTERPRET_OK
                )
            }
        }

        fun onLoadFileMisMatch(fileType: Int) {
            if (reporter == null) {
                return
            }
            when (fileType) {
                ShareConstants.TYPE_DEX -> reporter?.onReport(KEY_LOADED_MISMATCH_DEX)
                ShareConstants.TYPE_LIBRARY -> reporter?.onReport(KEY_LOADED_MISMATCH_LIB)
                ShareConstants.TYPE_RESOURCE -> reporter?.onReport(KEY_LOADED_MISMATCH_RESOURCE)
            }
        }

        fun onLoadException(throwable: Throwable, errorCode: Int) {
            if (reporter == null) {
                return
            }
            var isCheckFail = false
            when (errorCode) {
                ShareConstants.ERROR_LOAD_EXCEPTION_DEX -> if (throwable.message?.contains(
                        ShareConstants.CHECK_DEX_INSTALL_FAIL
                    ) == true
                ) {
                    reporter?.onReport(KEY_LOADED_EXCEPTION_DEX_CHECK)
                    isCheckFail = true
                    TinkerLog.e(TAG, "tinker dex check fail:" + throwable.message)
                } else {
                    reporter?.onReport(KEY_LOADED_EXCEPTION_DEX)
                    TinkerLog.e(TAG, "tinker dex reflect fail:" + throwable.message)
                }
                ShareConstants.ERROR_LOAD_EXCEPTION_RESOURCE -> if (throwable.message?.contains(
                        ShareConstants.CHECK_RES_INSTALL_FAIL
                    ) == true
                ) {
                    reporter?.onReport(KEY_LOADED_EXCEPTION_RESOURCE_CHECK)
                    isCheckFail = true
                    TinkerLog.e(TAG, "tinker res check fail:" + throwable.message)
                } else {
                    reporter?.onReport(KEY_LOADED_EXCEPTION_RESOURCE)
                    TinkerLog.e(TAG, "tinker res reflect fail:" + throwable.message)
                }
                ShareConstants.ERROR_LOAD_EXCEPTION_UNCAUGHT -> reporter?.onReport(
                    KEY_LOADED_UNCAUGHT_EXCEPTION
                )
                ShareConstants.ERROR_LOAD_EXCEPTION_UNKNOWN -> reporter?.onReport(
                    KEY_LOADED_UNKNOWN_EXCEPTION
                )
            }
            //reporter exception, for dex check fail, we don't need to report stacktrace
            if (!isCheckFail) {
                reporter?.onReport(
                    "Tinker Exception:load tinker occur exception " + Utils.getExceptionCauseString(
                        throwable
                    )
                )
            }
        }

        fun onApplyPatchServiceStart() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_APPLIED_START)
        }

        fun onApplyDexOptFail(throwable: Throwable) {
            if (reporter == null) {
                return
            }
            if (throwable.message?.contains(ShareConstants.CHECK_DEX_OAT_EXIST_FAIL) == true) {
                reporter?.onReport(KEY_APPLIED_DEXOPT_EXIST)
            } else if (throwable.message?.contains(ShareConstants.CHECK_DEX_OAT_FORMAT_FAIL) == true) {
                reporter?.onReport(KEY_APPLIED_DEXOPT_FORMAT)
            } else {
                reporter?.onReport(KEY_APPLIED_DEXOPT_OTHER)
                reporter?.onReport(
                    "Tinker Exception:apply tinker occur exception " + Utils.getExceptionCauseString(
                        throwable
                    )
                )
            }
        }

        fun onApplyInfoCorrupted() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_APPLIED_INFO_CORRUPTED)
        }

        fun onApplyVersionCheckFail() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_APPLIED_VERSION_CHECK)
        }

        fun onApplyExtractFail(fileType: Int) {
            if (reporter == null) {
                return
            }
            when (fileType) {
                ShareConstants.TYPE_DEX -> reporter?.onReport(KEY_APPLIED_DEX_EXTRACT)
                ShareConstants.TYPE_LIBRARY -> reporter?.onReport(KEY_APPLIED_LIB_EXTRACT)
                ShareConstants.TYPE_PATCH_FILE -> reporter?.onReport(KEY_APPLIED_PATCH_FILE_EXTRACT)
                ShareConstants.TYPE_RESOURCE -> reporter?.onReport(KEY_APPLIED_RESOURCE_EXTRACT)
            }
        }

        fun onApplied(cost: Long, success: Boolean) {
            if (reporter == null) {
                return
            }
            if (success) {
                reporter?.onReport(KEY_APPLIED)
            }

            if (success) {
                reporter?.onReport(KEY_APPLIED_UPGRADE)
            } else {
                reporter?.onReport(KEY_APPLIED_UPGRADE_FAIL)
            }

            TinkerLog.i(TAG, "hp_report report apply cost = %d", cost)

            if (cost < 0L) {
                TinkerLog.e(TAG, "hp_report report apply cost failed, invalid cost")
                return
            }

            if (cost <= 5000) {
                if (success) {
                    reporter?.onReport(KEY_APPLIED_SUCC_COST_5S_LESS)
                } else {
                    reporter?.onReport(KEY_APPLIED_FAIL_COST_5S_LESS)
                }
            } else if (cost <= 10 * 1000) {
                if (success) {
                    reporter?.onReport(KEY_APPLIED_SUCC_COST_10S_LESS)
                } else {
                    reporter?.onReport(KEY_APPLIED_FAIL_COST_10S_LESS)
                }
            } else if (cost <= 30 * 1000) {
                if (success) {
                    reporter?.onReport(KEY_APPLIED_SUCC_COST_30S_LESS)
                } else {
                    reporter?.onReport(KEY_APPLIED_FAIL_COST_30S_LESS)
                }
            } else if (cost <= 60 * 1000) {
                if (success) {
                    reporter?.onReport(KEY_APPLIED_SUCC_COST_60S_LESS)
                } else {
                    reporter?.onReport(KEY_APPLIED_FAIL_COST_60S_LESS)
                }
            } else {
                if (success) {
                    reporter?.onReport(KEY_APPLIED_SUCC_COST_OTHER)
                } else {
                    reporter?.onReport(KEY_APPLIED_FAIL_COST_OTHER)
                }
            }
        }

        fun onApplyPackageCheckFail(errorCode: Int) {
            if (reporter == null) {
                return
            }
            TinkerLog.i(TAG, "hp_report package check failed, error = %d", errorCode)

            when (errorCode) {
                ShareConstants.ERROR_PACKAGE_CHECK_SIGNATURE_FAIL -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_SIGNATURE
                )
                ShareConstants.ERROR_PACKAGE_CHECK_DEX_META_CORRUPTED -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_DEX_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_LIB_META_CORRUPTED -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_LIB_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_PATCH_TINKER_ID_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_APK_TINKER_ID_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_TINKER_ID_NOT_EQUAL
                )
                ShareConstants.ERROR_PACKAGE_CHECK_PACKAGE_META_NOT_FOUND -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_META_NOT_FOUND
                )
                ShareConstants.ERROR_PACKAGE_CHECK_RESOURCE_META_CORRUPTED -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_RES_META
                )
                ShareConstants.ERROR_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT -> reporter?.onReport(
                    KEY_APPLIED_PACKAGE_CHECK_TINKERFLAG_NOT_SUPPORT
                )
            }
        }

        fun onApplyCrash(throwable: Throwable) {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_APPLIED_EXCEPTION)
            reporter?.onReport(
                "Tinker Exception:apply tinker occur exception " + Utils.getExceptionCauseString(
                    throwable
                )
            )
        }

        fun onFastCrashProtect() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_CRASH_FAST_PROTECT)
        }

        fun onXposedCrash() {
            if (reporter == null) {
                return
            }
            if (ShareTinkerInternals.isVmArt()) {
                reporter?.onReport(KEY_CRASH_CAUSE_XPOSED_ART)
            } else {
                reporter?.onReport(KEY_CRASH_CAUSE_XPOSED_DALVIK)
            }
        }

        fun onReportRetryPatch() {
            if (reporter == null) {
                return
            }
            reporter?.onReport(KEY_APPLY_WITH_RETRY)
        }
    }

}