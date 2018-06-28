package com.fqxyi.kit.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ShenBF
 * @描述: 多文件下载类（支持单文件下载）
 * @Copyright Copyright (c) 2016
 * @Company 昆山妈妈好网络科技有限公司
 * @date 2017/11/14
 */

public class DownloadUtil {

    private static final String TAG = DownloadUtil.class.getSimpleName();

    private Context context;

    // 线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    // 存储文件夹
    private File parentDir;
    // 存储文件名
    private String fileName;
    // 最大重新总执行次数
    private static final int MAX_RETRY_TIMES = 2;

    // 原始下载链接
    private String sourceLink;
    // 原始下载链接集合
    private List<String> sourceLinks;
    // 失败的链接的索引的集合
    private List<Integer> failureIndexs = new ArrayList<>();

    // 下载状态监听，提供回调
    private DownloadStateListener listener;

    private int finishedCount = 0;

    public interface DownloadStateListener {
        void onStart();

        void onFailure(List<Integer> failureIndexs);

        void onAllSucceed(String fileDownloadDir);

        void onError(IOException e);
    }

    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull String sourceLink, DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.sourceLink = sourceLink;
        this.listener = listener;
    }

    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull String fileName, @NonNull String sourceLink,
                        DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.fileName = fileName;
        this.sourceLink = sourceLink;
        this.listener = listener;
    }

    /**
     * 构造方法
     *
     * @param parentDir   存储文件的目录
     * @param sourceLinks 要下载的文件链接集合
     * @param listener    下载状态监听器
     */
    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull List<String> sourceLinks, DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.sourceLinks = sourceLinks;
        this.listener = listener;
    }

    private void initFile(File parentDir) {
        if (parentDir == null) {
            throw new RuntimeException("存储文件的目录不能为NULL");
        } else {
            if (parentDir.exists()) {
                return;
            }
            if (parentDir.mkdirs()) {
                return;
            }
            throw new RuntimeException("存储文件的目录创建失败，请检查权限是否正常等情况");
        }
    }

    public void update(@NonNull String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public void update(@NonNull String fileName, @NonNull String sourceLink) {
        this.fileName = fileName;
        this.sourceLink = sourceLink;
    }

    public void startDownload() {
        // 线程放入线程池，增加isShutdown()判断
        if (!executorService.isShutdown()) {
            // 开始执行
            if (listener != null) {
                listener.onStart();
            }
            if (getSingleDownload()) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveFile(0, sourceLink);
                    }
                });
            } else {
                for (int i = 0; i < sourceLinks.size(); i++) {
                    final int index = i;
                    final String url = sourceLinks.get(index);
                    if (!TextUtils.isEmpty(url)) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                saveFile(index, url);
                            }
                        });
                    }
                }
            }
        } else {
            LogUtil.e(TAG, "线程已关闭");
        }
    }

    private void saveFile(int index, String url) {
        int retryTimes = 0;
        do {
            if (downloadFile(url)) {
                finishedCount++;
                if (getSingleDownload()) {
                    if (finishedCount == 1) {
                        allFinished();
                    }
                } else {
                    if (finishedCount == sourceLinks.size()) {
                        allFinished();
                    }
                }
                return;
            }
            retryTimes++;
        } while (retryTimes < MAX_RETRY_TIMES);

        failureIndexs.add(index);
        finishedCount++;

        if (getSingleDownload()) {
            if (finishedCount == 1) {
                allFinished();
            }
        } else {
            if (finishedCount == sourceLinks.size()) {
                allFinished();
            }
        }
    }

    private boolean downloadFile(String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            return true;
        }

        File file;
        if (TextUtils.isEmpty(fileName)) {
            file = new File(parentDir, urlString.substring(urlString.lastIndexOf("/") + 1));
        } else {
            file = new File(parentDir, fileName);
        }

        if (urlString.startsWith("//")) {
            urlString = "https:" + urlString;
        }
        if (!urlString.startsWith("http")) {
            // 非网络文件
            urlString = urlString.substring(7);
            // 拷贝文件到parentDir
            copyFile(urlString, file.getAbsolutePath());
            // 更新图库
            updateGallery(urlString, file);
            return true;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            // 更新图库
            updateGallery(urlString, file);
            return true;
        } catch (IOException e) {
            LogUtil.e("Mamahao " + e.getMessage());
            if (listener != null) {
                listener.onError(e);
            }
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e("Mamahao " + e.getMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e("Mamahao " + e.getMessage());
                }
            }
        }
        return false;
    }

    /**
     * 全部完成时调用
     */
    private void allFinished() {
        finishedCount = 0;
        if (failureIndexs.size() == 0) {
            // 全部下载成功
            if (listener != null) {
                listener.onAllSucceed(parentDir.getAbsolutePath());
            }
        } else {
            if (listener != null) {
                listener.onFailure(failureIndexs);
            }
        }
        failureIndexs.clear();
    }

    /**
     * 扫描文件通知相册更新
     */
    private void updateGallery(String url, File file) {
        if ("image".equals(getFileType(url.substring(url.lastIndexOf(".") + 1)))) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }

    /**
     * 拷贝本地文件到新的文件路径中
     * @param oldPath 旧的文件路径
     * @param newPath 新的文件路径
     */
    private boolean copyFile(String oldPath, String newPath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(oldPath);
            os = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            return true;
        } catch (IOException e) {
            LogUtil.e("Mamahao " + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                LogUtil.e("Mamahao " + e.getMessage());
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LogUtil.e("Mamahao " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 文件类型判断工具类
     * 参考自：/libcore/luni/src/main/java/libcore/net/MimeUtils.java
     */
    private String getFileType(String suffix) {
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType.split("/")[0];
        }
        return null;
    }

    /**
     * true：下载单个链接，false：下载链接集合
     */
    private boolean getSingleDownload() {
        return sourceLinks == null && !TextUtils.isEmpty(sourceLink);
    }

}
