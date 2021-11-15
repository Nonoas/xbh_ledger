package indi.nonoas.xbh.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nonoas
 * @date 2021/11/15
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyCrash";
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    @SuppressLint("StaticFieldLeak")
    private static GlobalExceptionHandler instance = new GlobalExceptionHandler();
    private Context mContext;

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个GlobalExceptionHandler实例
     */
    private GlobalExceptionHandler() {
    }

    /**
     * 获取GlobalExceptionHandler实例 ,单例模式
     */
    public static GlobalExceptionHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该GlobalExceptionHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);

        } else {
            SystemClock.sleep(3000);
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        try {
            // 使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将返回.",
                            Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();

            // 保存日志文件
//            saveCrashInfoFile(ex);
            SystemClock.sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

//    /**
//     * 保存错误信息到文件中
//     */
//    private void saveCrashInfoFile(Throwable ex) throws Exception {
//        StringBuilder sb = new StringBuilder();
//        try {
//            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String date = sDateFormat.format(new Date());
//            sb.append("\r\n").append(date).append("\n");
//
//            Writer writer = new StringWriter();
//            PrintWriter printWriter = new PrintWriter(writer);
//            ex.printStackTrace(printWriter);
//            Throwable cause = ex.getCause();
//
//            while (cause != null) {
//                cause.printStackTrace(printWriter);
//                cause = cause.getCause();
//            }
//
//            printWriter.flush();
//            printWriter.close();
//            String result = writer.toString();
//            sb.append(result);
//            writeFile(sb.toString());
//
//        } catch (Exception e) {
//            Log.e(TAG, "an error occurred while writing file...", e);
//            sb.append("an error occurred while writing file...\r\n");
//            writeFile(sb.toString());
//        }
//    }
//
//    private void writeFile(String sb) throws Exception {
//        String time = formatter.format(new Date());
//        String fileName = "crash-" + time + ".txt";
//        if (hasSdcard()) {
//            String path = getGlobalPath();
//            File dir = new File(path);
//            if (!dir.exists())
//                dir.mkdirs();
//
//            FileOutputStream fos = new FileOutputStream(path + fileName, true);
//            fos.write(sb.getBytes());
//            fos.flush();
//            fos.close();
//        }
//    }
//
//    // 异常log保存路径
//    private String getGlobalPath() {
//        return mContext.getExternalFilesDir("").getAbsolutePath() + "/Crash/";
//    }
//
//    // 判断是否存在sd卡
//    private boolean hasSdcard() {
//        return Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED);
//    }
}
